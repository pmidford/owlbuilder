package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
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
import org.arachb.owlbuilder.lib.Publication;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
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
	private final Map<String,IRI>nonNCBITaxa = new HashMap<String,IRI>();
	
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
		rfactory = new ElkReasonerFactory();
		iriManager = new IRIManager(connection);
	}

	void process() throws Exception{		
		log.info("Loading ontologies");
		loadImportedOntologies();
		mergedSources = mergeImportedOntologies();
		log.info("Initializing reasoner");
		reasoner = rfactory.createReasoner(mergedSources);
		log.info("Finished reasoner initialization");
        // Classify the ontology.
		try{
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			log.info("Finished precomputing class hierarchy");
			target = manager.createOntology(IRI.create(targetIRI));
			OWLOntologyFormat format = manager.getOntologyFormat(target);
			format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
			log.info("copying relevant taxonony to target ontology");
			initializeTaxonomy();
			log.info("copying misc support terms to target ontology");
			initializeMisc();
			processDatabase();
			reasoner.flush();
			log.info("Saving ontology");
			File f = new File(temporaryOutput);
			manager.saveOntology(target, IRI.create(f.toURI()));
			log.info("Generating HTML pages");
			generateHTML();
			log.info("Shutting down reasoner");
		}
		finally{
			reasoner.dispose();
		}
	}
	
	void shutdown() throws Exception{
        log.info("Done");
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
			iriManager.validateIRI(pub);
			IRI pubID = IRI.create(pub.getIriString());
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
		final Set<Assertion> assertions = connection.getAssertions();
		for (Assertion a : assertions){
			iriManager.validateIRI(a);
			OWLObject owlAssertion = a.generateOWL(this);
			//find the behavior id
			
 		}		
	}
	
	private void initializeTaxonomy(){
		OWLClass arachnidaClass = factory.getOWLClass(IRIManager.arachnidaTaxon);
		final NodeSet<OWLClass> arachnidaParents = reasoner.getSuperClasses(arachnidaClass, false);
		final NodeSet<OWLClass> arachnidaChildren = reasoner.getSubClasses(arachnidaClass, false);
		initializeTaxon(arachnidaClass);
		for (OWLClass c : arachnidaParents.getFlattened()){
			initializeTaxon(c);
		}
		for (OWLClass c : arachnidaChildren.getFlattened()){
			initializeTaxon(c);
		}
	}
	
	private void initializeTaxon(OWLClass taxon){
		Set<OWLClassAxiom> taxonAxioms =  mergedSources.getAxioms(taxon);
		manager.addAxioms(target, taxonAxioms);
		//log.info("Annotations");
		Set<OWLAnnotationAssertionAxiom> taxonAnnotations = 
				mergedSources.getAnnotationAssertionAxioms(taxon.getIRI());
		for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
			//log.info("   Annotation Axiom: " + a.toString());
			if (a.getAnnotation().getProperty().isLabel()){
				manager.addAxiom(target, a);
			}
		}
	}

	
	static final IRI[] MISC_PROPERTIES = {
		IRIManager.partOfProperty,
		IRIManager.denotesProperty,
		IRIManager.hasParticipantProperty
	};
	
	static final IRI[] MISC_TERMS = {
		IRIManager.informationContentEntity,
		IRIManager.pubAboutInvestigation,
		IRIManager.textualEntity
	};
	
	private void initializeMisc(){
		for (IRI mTerm : MISC_TERMS){
			OWLClass mClass = factory.getOWLClass(mTerm);
			initializeMiscTermAndParents(mClass);
		}
		for (IRI mProp : MISC_PROPERTIES){
			OWLObjectProperty mprop = factory.getOWLObjectProperty(mProp);
			initializeMiscObjPropertyAndParents(mprop);
		}
	}
	
	public void initializeMiscTermAndParents(OWLClass term){
		initializeMiscTerm(term);
		final NodeSet<OWLClass> termParents = reasoner.getSuperClasses(term, false);
		for (OWLClass c : termParents.getFlattened()){
			initializeMiscTerm(c);
		}

	}
	
	public void initializeMiscTerm(OWLClass term){
		Set<OWLClassAxiom> taxonAxioms =  mergedSources.getAxioms(term);
		if (taxonAxioms.isEmpty()){
			log.error("No Axioms for term " + term);
		}
		else{
			manager.addAxioms(target, taxonAxioms);
		}
		//log.info("Annotations");
		Set<OWLAnnotationAssertionAxiom> termAnnotations = 
				mergedSources.getAnnotationAssertionAxioms(term.getIRI());
		for (OWLAnnotationAssertionAxiom a : termAnnotations){
			//log.info("   Annotation Axiom: " + a.toString());
			if (a.getAnnotation().getProperty().isLabel()){
				manager.addAxiom(target, a);
			}
		}

	}
	public void initializeMiscObjPropertyAndParents(OWLObjectProperty prop){
		initializeMiscObjProperty(prop);
		//final NodeSet<OWLObjectPropertyExpression> propParents = reasoner.getSuperObjectProperties(prop,false);
		//for (OWLObjectPropertyExpression c : propParents.getFlattened()){
		//	initializeMiscObjProperty(c);
		//}

	}
	
	public void initializeMiscObjProperty(OWLObjectPropertyExpression prope){
		Set<OWLObjectPropertyAxiom> propertyAxioms =  mergedSources.getAxioms(prope);
		if (propertyAxioms.isEmpty()){
			log.error("No Axioms for object property " + prope);
		}
		else{
			manager.addAxioms(target, propertyAxioms);
		}
		//log.info("Annotations");
		if (prope instanceof OWLObjectProperty){
			OWLObjectProperty prop = (OWLObjectProperty) prope;
		
			Set<OWLAnnotationAssertionAxiom> termAnnotations = 
					mergedSources.getAnnotationAssertionAxioms(prop.getIRI());
			for (OWLAnnotationAssertionAxiom a : termAnnotations){
				//log.info("   Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					manager.addAxiom(target, a);
				}
			}
		}
	}
	
	private void generateHTML(){
		
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
	
	public OWLReasoner getReasoner(){
		return reasoner;
	}

	public void addNonNCBITaxon(IRI taxonIRI,String label){
		nonNCBITaxa.put(label, taxonIRI);
	}
	
}
