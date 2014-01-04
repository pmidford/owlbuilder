package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
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
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
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
import org.semanticweb.owlapi.model.OWLOntologySetProvider;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class Owlbuilder{

	
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private final IRIManager iriManager;
	private final AbstractConnection connection;
	private final OWLReasonerFactory rfactory;
	private OWLOntology target;
	private OWLReasoner reasoner;
	private final Mireot mireot;
	
	private final Map<String,OWLOntology>supportOntologies = new HashMap<String,OWLOntology>();
	private OWLOntology mergedSources = null;
		
	
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
		//rfactory = new StructuralReasonerFactory();  //change when reasoner is upgraded
		rfactory = new ElkReasonerFactory();
		iriManager = new IRIManager(connection);
		mireot = new Mireot();
		mireot.setImportDir(config.getImportDir());
		mireot.setMireotDir(config.getMireotDir());
	}

	void process() throws Exception{		
		log.info("Loading ontologies");
		loadImportedOntologies();
		mergedSources = mergeImportedOntologies();
		log.info("Initializing reasoner");



		reasoner = rfactory.createReasoner(mergedSources);
		log.info("Finished reasoner initialization");
        // Classify the ontology.
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        log.info("Finished precomputing class hierarchy");
        target = manager.createOntology(IRI.create(targetIRI));
		OWLOntologyFormat format = manager.getOntologyFormat(target);
		format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
		processDatabase();
		reasoner.flush();
		log.info("Saving ontology");
		File f = new File(temporaryOutput);
		manager.saveOntology(target, IRI.create(f.toURI()));
        log.info("Generating HTML pages");
        log.info("Done");
	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase() throws Exception{
		log.info("Processing publications");
		processPublications();
		log.info("Processing assertions");
		processAssertions();
	}
	
	/**
	 * This loads the support ontologies specified in the source_ontologies table
	 * @throws Exception
	 */
	void loadImportedOntologies() throws Exception{
		final Map <String,String> sourceMap = connection.loadImportSourceMap();
		final Map <String,String> namesForLoading = connection.loadOntologyNamesForLoading();
		for (String source : sourceMap.keySet()){
			log.info(String.format("Loading %s from %s", namesForLoading.get(source), source));
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
	
	
	OWLOntology mergeImportedOntologies() throws Exception{
		log.info("Starting ontology merge");
		OWLOntologyMerger merger = new OWLOntologyMerger(manager);
		IRI mergedOntologyIRI = IRI.create("http://www.semanticweb.com/mymergedont");
        OWLOntology merged = merger.createMergedOntology(manager, mergedOntologyIRI);
        log.info("Finished ontology merge; merged support ontologies axiom count = " + merged.getAxiomCount());
		return merged;
	}
	
	
	
	void processPublications() throws Exception{
		final OWLClass pubAboutInvestigationClass = factory.getOWLClass(IRIManager.pubAboutInvestigation);
		final Set<Publication> pubs = connection.getPublications();
		for (Publication pub : pubs){
			IRI pubID = IRI.create(pub.getIRI_String());
			OWLIndividual pub_ind = factory.getOWLNamedIndividual(pubID);
			OWLClassAssertionAxiom classAssertion = 
					factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
			manager.addAxiom(target, classAssertion);
		}
	}
	
	//when to get iri's for terms contained in participants?
	
	void processAssertions() throws Exception{
		final OWLClass textualEntityClass = factory.getOWLClass(IRIManager.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(IRIManager.denotesProperty);
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		OWLClass behaviorProcess = factory.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/NBO_0000313")); 
		final Set<Assertion> assertions = connection.getAssertions();
		for (Assertion a : assertions){
			iriManager.validateIRI(a);
			OWLObject owlAssertion = a.generateOWL(this);
			//find the behavior id
			
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
	
	
	public OWLOntology getTarget(){
		return target;
	}
	
	public OWLOntologyManager getOntologyManager(){
		return manager;
	}
	
	
	public OWLDataFactory getDataFactory(){
		return factory;
	}
	
	public IRIManager getIRIManager(){
		return iriManager;
	}
	
	public AbstractConnection getConnection(){
		return connection;
	}
	
	public OWLOntology getMergedSources(){
		return mergedSources;
	}

		
	
}
