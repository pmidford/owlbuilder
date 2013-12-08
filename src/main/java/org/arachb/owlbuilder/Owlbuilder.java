package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.owlbuilder.lib.AbstractConnection;
import org.arachb.owlbuilder.lib.Assertion;
import org.arachb.owlbuilder.lib.Config;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.IRIManager;
import org.arachb.owlbuilder.lib.Mireot;
import org.arachb.owlbuilder.lib.Participant;
import org.arachb.owlbuilder.lib.Publication;
import org.arachb.owlbuilder.lib.Taxon;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

public class Owlbuilder {

	
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private final IRIManager iriManager;
	private final AbstractConnection connection;
	private final Mireot mireot;
	
	private Map<String,OWLOntology>supportOntologies;
	
		
	
	public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
	public static final String taxonomyTarget = "http://arachb.org/imports/NCBI_import.owl";

	private static String temporaryOutput = "test.owl";
	private static String temporaryTaxonomyReport = "taxonomy.html";
	
	private final Config config;
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	public static void main( String[] args ) throws Exception {
		Owlbuilder builder = null;
		//try{
			// Configure log4j
			PropertyConfigurator.configure(Owlbuilder.class.getClassLoader().getResource("log4j.properties"));
			log.info("Trying to start");
			builder = new Owlbuilder();
			builder.process();
		//}
		//finally{
			builder.shutdown();
		//}
	}
	
	Owlbuilder() throws Exception{
		config = new Config("");
		if (DBConnection.testConnection()){
			connection = DBConnection.getDBConnection();
		}
		else{
			connection = DBConnection.getMockConnection();
		}
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		iriManager = new IRIManager(connection);
		mireot = new Mireot();
		mireot.setImportDir(config.getImportDir());
		mireot.setMireotDir(config.getMireotDir());
	}

	void process() throws Exception{		
		log.info("Loading ontologies");
		loadImportedOntologies(connection,config);
		OWLOntology ontology = manager.createOntology(IRI.create(targetIRI));
		OWLOntologyFormat format = manager.getOntologyFormat(ontology);
		format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
		processDatabase(ontology);
		log.info("Saving ontology");
		File f = new File(temporaryOutput);
		manager.saveOntology(ontology, IRI.create(f.toURI()));

	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase(OWLOntology ontology) throws Exception{
		log.info("Processing publications");
		processPublications(ontology);
		log.info("Processing assertions");
		processAssertions(ontology);
		log.info("Processing taxonomy");
		processTaxonomy(ontology);
	}
	
	/**
	 * This loads the support ontologies specified in the source_ontologies table
	 * @param connection2
	 * @param config - retrieve location of support ontology cache
	 * @throws Exception
	 */
	void loadImportedOntologies(AbstractConnection connection, 
		  	                    Config config) throws Exception{
		final Map <String,String> sourceMap = connection.loadImportSourceMap();
		final Map <String,String> namesForLoading = connection.loadOntologyNamesForLoading();
		for (String source : sourceMap.keySet()){
			String name = namesForLoading.get(source);
			String msg = String.format("Loading %s from %s", name,source);
			log.info(msg);
			IRI iri = IRI.create(source);
			String sourcePath = iri.toURI().getPath();
			String [] pathParts = sourcePath.split("/");
			String cachePath = "file:/" + config.getCacheDir() + "/" + pathParts[pathParts.length-1];
            log.info("Cachepath is " + cachePath);
			manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(cachePath)));
			OWLOntology ont = manager.loadOntology(iri);
			log.info("Loaded ontology: " + ont);
            supportOntologies.put(source, ont);
		}
	}
	
	void processPublications(OWLOntology ontology) throws Exception{
		final OWLClass pubAboutInvestigationClass = factory.getOWLClass(IRIManager.pubAboutInvestigation);
		final Set<Publication> pubs = connection.getPublications();
		for (Publication pub : pubs){
			IRI pubID;
			if(pub.get_doi() != null && pub.get_doi() != ""){
				IRI clean = cleanupDOI(pub.get_doi());
				pubID = clean;
				if (pub.get_generated_id() != null && pub.get_generated_id() != ""){
				//TODO check for existing arachb id - generate owl:sameas
				}
			}
			else{ //generate arachb IRI (maybe temporary)
				pubID = iriManager.getARACHB_IRI();
				pub.set_generated_id(pubID.toString());
				connection.updatePublication(pub);
			}
			OWLIndividual pub_ind = factory.getOWLNamedIndividual(pubID);
			OWLClassAssertionAxiom classAssertion = 
					factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
			manager.addAxiom(ontology, classAssertion);
		}
	}
	
	void processAssertions(OWLOntology ontology) throws Exception{
		final OWLClass textualEntityClass = factory.getOWLClass(IRIManager.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(IRIManager.denotesProperty);
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		OWLClass behaviorProcess = factory.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/NBO_0000313")); 
		final Set<Assertion> assertions = connection.getAssertions();
		final HashMap<IRI,String> nboTermMap = new HashMap<IRI,String>();
		final HashMap<IRI,String> missingBehaviorMap = new HashMap<IRI,String>();
		final HashMap<IRI,String> spdTermMap = new HashMap<IRI,String>();
		final HashMap<IRI,String> missingAnatomyMap = new HashMap<IRI,String>();
		for (Assertion a : assertions){
			//TODO check for existing arachb id, don't generate new one
			if (a.get_generated_id() == null || a.get_generated_id() == ""){
				final IRI assertID = iriManager.getARACHB_IRI();
				a.set_generated_id(assertID.toString());
				connection.updateAssertion(a);
			}
			final Participant primary = connection.getPrimaryParticipant(a);
			final Set<Participant> participants = connection.getParticipants(a);
			for (Participant p : participants){
				
			}
			//find the taxon id
			IRI taxon_id;
			final Taxon t  = connection.getTaxon(a.get_taxon());
			if (t != null){
				taxon_id = IRI.create(t.get_available_id());
			}
			else {
				taxon_id = null;
			}
			//find the behavior id
			
			//find the publication id
			IRI publication_id;
			Publication p = connection.getPublication(a.get_publication());
			if (p != null){
				if(p.get_doi() != null && p.get_doi() != ""){
					IRI clean = cleanupDOI(p.get_doi());
					publication_id = clean;
				}
				else
					publication_id = IRI.create(p.get_available_id());
			}
			else {
				publication_id = null;
			}
			//Complete hack - denotes some behavior process
			OWLClassExpression denotesExpr = 
 			   factory.getOWLObjectSomeValuesFrom(denotesProp,behaviorProcess); 
			OWLClassExpression intersectExpr =
					factory.getOWLObjectIntersectionOf(textualEntityClass,denotesExpr);
			OWLIndividual assert_ind = factory.getOWLNamedIndividual(IRI.create(a.get_generated_id()));
			OWLClassAssertionAxiom textClassAssertion = 
					factory.getOWLClassAssertionAxiom(intersectExpr, assert_ind); 
			manager.addAxiom(ontology, textClassAssertion);
			if (publication_id != null){
				OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
				OWLObjectPropertyAssertionAxiom partofAssertion = 
						factory.getOWLObjectPropertyAssertionAxiom(partofProperty, assert_ind, pub_ind);
				manager.addAxiom(ontology, partofAssertion);
			}
 		}		
	}
	
	
	
	void processTaxonomy(OWLOntology ontology) throws Exception{
		final Set<Taxon> taxa = connection.getTaxa();
		final HashMap<IRI,String> ncbiTaxonMap = new HashMap<IRI,String>();
		final HashMap<IRI,String> missingTaxonMap = new HashMap<IRI,String>();
		for (Taxon t : taxa){
			IRI taxonID;
			if (t.get_ncbi_id() == null){
				taxonID = iriManager.getARACHB_IRI();
				t.set_generated_id(taxonID.toString());
				connection.updateTaxon(t);
				missingTaxonMap.put(taxonID,t.get_name());
			}
			else {
				IRI ncbiIRI = iriManager.getNCBI_IRI(t.get_ncbi_id());
				ncbiTaxonMap.put(ncbiIRI, t.get_name());
			}
		}
		
		generateTaxonMiriotReport(ncbiTaxonMap);
		generateMissingTaxaPage();
		
	}
	
	private void generateTaxonMiriotReport(HashMap<IRI,String> taxonomyMap) throws Exception{
		mireot.setSourceTerms(taxonomyMap);
		mireot.setSourceOntology("NCBITaxon");
		mireot.setTargetOntology(IRI.create(taxonomyTarget));
		mireot.setTop("http://purl.obolibrary.org/obo/NCBITaxon_6893", "Araneae");
		mireot.generateRequest();
		
	}
	
	private void generateMissingTaxaPage(){
		
	}
	
	/**
	 * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
	 * @param doi
	 * @return IRI using using doi prefix
	 * @throws Exception either MalformedURL or Encoding exceptions can be thrown
	 */
	private IRI cleanupDOI(String doi) throws Exception{
		URL raw = new URL(doi);
		String cleanpath = URLEncoder.encode(raw.getPath().substring(1),"UTF-8");
		if (log.isDebugEnabled()){
			log.debug("raw is " + raw);
		}
		if (log.isDebugEnabled()){
			log.debug("clean path is " + cleanpath);
		}
		return IRI.create("http://dx.doi.org/",cleanpath);
	}
	
	OWLOntologyManager getOntologyManager(){
		return manager;
	}
	
	OWLDataFactory getDataFactory(){
		return factory;
	}
	
	AbstractConnection getConnection(){
		return connection;
	}
	
}
