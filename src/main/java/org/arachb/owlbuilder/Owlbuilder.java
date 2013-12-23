package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.owlbuilder.lib.AbstractConnection;
import org.arachb.owlbuilder.lib.Assertion;
import org.arachb.owlbuilder.lib.Config;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.IRIManager;
import org.arachb.owlbuilder.lib.IndividualParticipant;
import org.arachb.owlbuilder.lib.Mireot;
import org.arachb.owlbuilder.lib.Participant;
import org.arachb.owlbuilder.lib.Publication;
import org.arachb.owlbuilder.lib.QuantifiedParticipant;
import org.arachb.owlbuilder.lib.Term;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class Owlbuilder {

	
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private final IRIManager iriManager;
	private final AbstractConnection connection;
	private final Mireot mireot;
	
	private final Map<String,OWLOntology>supportOntologies = new HashMap<String,OWLOntology>();
	
		
	
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
        log.info("Generating HTML pages");
        log.info("Done");
	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase(OWLOntology ontology) throws Exception{
		log.info("Processing publications");
		processPublications(ontology);
		log.info("Processing assertions");
		processAssertions(ontology);
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
			IRI pubID = IRI.create(pub.getIRI_String());
			OWLIndividual pub_ind = factory.getOWLNamedIndividual(pubID);
			OWLClassAssertionAxiom classAssertion = 
					factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
			manager.addAxiom(ontology, classAssertion);
		}
	}
	
	//when to get iri's for terms contained in participants?
	
	void processAssertions(OWLOntology ontology) throws Exception{
		final OWLClass textualEntityClass = factory.getOWLClass(IRIManager.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(IRIManager.denotesProperty);
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		OWLClass behaviorProcess = factory.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/NBO_0000313")); 
		final Set<Assertion> assertions = connection.getAssertions();
		for (Assertion a : assertions){
			iriManager.validateIRI(a);
			final Participant primary = connection.getPrimaryParticipant(a);
			OWLObject owlPrimary = primary.generateOWL(ontology,manager,iriManager);
			final Set<Participant> otherParticipants = connection.getParticipants(a);
			for (Participant p : otherParticipants){
				OWLObject owlRep = p.generateOWL(ontology,manager,iriManager);
			}
			//find the behavior id
			
			//find the publication id
			Publication p = connection.getPublication(a.get_publication());
			IRI publication_id = IRI.create(p.getIRI_String());
			//Complete hack - denotes some behavior process
			OWLClassExpression denotesExpr = 
 			   factory.getOWLObjectSomeValuesFrom(denotesProp,behaviorProcess); 
			OWLClassExpression intersectExpr =
					factory.getOWLObjectIntersectionOf(textualEntityClass,denotesExpr);
			OWLIndividual assert_ind = factory.getOWLNamedIndividual(IRI.create(a.getIRI_String()));
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
	
	OWLObject addIndividualParticipant(IndividualParticipant p, OWLOntology o) throws SQLException{
		//TODO check for existing arachb id, don't generate new one
		OWLClassExpression headClass = null;
		if (p.get_taxon() != 0){
			Term taxon_term = connection.getTerm(p.get_taxon());
			taxon_term.get_id();
		}
			OWLIndividual assert_ind = factory.getOWLNamedIndividual(IRI.create(p.getIRI_String()));
		return null;
	}
	
	OWLObject addQuantifiedParticipant(QuantifiedParticipant p, OWLOntology o) throws SQLException{
		//OWLPropertyExpression prop = factory.getOWLObjectSomeValuesFrom(arg0, arg1);  //placeholder
		//OWLClassExpression assert_some_expr = 
		//	factory.getOWLObjectSomeValuesFrom(prop, null); // arg1);
		return null;
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
