package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.owlbuilder.lib.AbstractConnection;
import org.arachb.owlbuilder.lib.Claim;
import org.arachb.owlbuilder.lib.Config;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.IRIManager;
import org.arachb.owlbuilder.lib.Taxon;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import owltools.graph.OWLGraphWrapper;

public class Owlbuilder{

	
	//private OWLOntologyManager manager;
	//private OWLDataFactory factory;
	private final IRIManager iriManager;
	private final AbstractConnection connection;
	private final OWLReasonerFactory rfactory;
	private OWLOntology target;
	private OWLReasoner preReasoner;
	private OWLReasoner postReasoner;
	private final Map<IRI,Taxon>nonNCBITaxa = new HashMap<IRI,Taxon>();
	
	private final Map<String,OWLOntology>supportOntologies = new HashMap<String,OWLOntology>();
	private OWLOntology mergedSources = null;
	
	private final OWLGraphWrapper testWrapper;
		
	
	public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
	public static final String taxonomyTarget = "http://arachb.org/imports/NCBI_import.owl";

	private static String temporaryOutput = "test.owl";
	private static String temporaryTaxonomyReport = "taxonomy.html";
	
	private final Config config;
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	public static void main(String[] args) throws Exception {
		Owlbuilder builder = null;
		// Configure log4j
		PropertyConfigurator.configure(Owlbuilder.class.getClassLoader().getResource("log4j.properties"));
		log.info("Trying to start");
		builder = new Owlbuilder();
		builder.process();
		builder.shutdown();
	}
	
	Owlbuilder() throws Exception{
		config = new Config("");
		if (DBConnection.testConnection()){
			connection = DBConnection.getDBConnection();
		}
		else{
			connection = DBConnection.getMockConnection();
		}
		testWrapper = new OWLGraphWrapper(targetIRI);
		rfactory = new ElkReasonerFactory();
		iriManager = new IRIManager(connection);
		final OWLOntologyFormat format = testWrapper.getManager().getOntologyFormat(testWrapper.getSourceOntology());
		format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
	}

	void process() throws Exception{	
		target = testWrapper.getSourceOntology();
		log.info("Loading ontologies");
		loadImportedOntologies();
		mergedSources = mergeImportedOntologies();
		loadCuratorAddedTerms(mergedSources);
		log.info("Initializing reasoner");
		preReasoner = rfactory.createReasoner(mergedSources);
		log.info("Finished reasoner initialization");
        // Classify the ontology.
		try{
			preReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			log.info("Finished precomputing class hierarchy");
			//target = manager.createOntology(IRI.create(targetIRI));
			log.info("copying relevant taxonony to target ontology");
			initializeTaxonomy();
			log.info("copying misc support terms to target ontology");
			initializeMisc();
			processDatabase();
			preReasoner.flush();
			postReasoner = rfactory.createReasoner(target);
			postReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

	        // To generate an inferred ontology we use implementations of
	        // inferred axiom generators
	        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
	        gens.add(new InferredSubClassAxiomGenerator());
	        gens.add(new InferredEquivalentClassAxiomGenerator());
	        gens.add(new InferredClassAssertionAxiomGenerator());

	        InferredOntologyGenerator iog = new InferredOntologyGenerator(postReasoner,
	                        gens);
	        iog.fillOntology(testWrapper.getManager(),target);
	        
	        saveOntology();
			generateHTML();
			log.info("Shutting down reasoner");
		}
		catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		finally{
			preReasoner.dispose();
			postReasoner.dispose();
		}
	}
	
	void shutdown() throws Exception{
        log.info("Done");
		connection.close();
	}

	void processDatabase() throws Exception{
		log.info("Processing Claims");
		processClaims();
	}
	
	/**
	 * This loads the support ontologies specified in the source_ontologies table
	 * @throws Exception
	 */
	void loadImportedOntologies() throws Exception{
		final Map <String,String> sourceMap = connection.loadImportSourceMap();
		final Map <String,String> namesForLoading = connection.loadOntologyNamesForLoading();
		final OWLOntologyManager manager = testWrapper.getManager();
		for (String source : sourceMap.keySet()){
			log.info(String.format("Loading %s from %s", namesForLoading.get(source), source));
			IRI iri = IRI.create(source);
			String sourcePath = iri.toURI().getPath();
			String [] pathParts = sourcePath.split("/");
			String cachePath = "file:/" + config.getCacheDir() + "/" + pathParts[pathParts.length-1];
            log.info("Cachepath is " + cachePath);
			manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(cachePath)));
			log.info("Added IRIMapper");
			OWLOntology ont = manager.loadOntology(iri);
			log.info("Loaded ontology: " + ont);
            supportOntologies.put(source, ont);
		}
	}
	
	
	OWLOntology mergeImportedOntologies() throws Exception{
		log.info("Starting ontology merge");
		final OWLOntologyManager manager = testWrapper.getManager();
		OWLOntologyMerger merger = new OWLOntologyMerger(manager);
		IRI mergedOntologyIRI = IRI.create("http://www.semanticweb.com/mymergedont");
        OWLOntology merged = merger.createMergedOntology(manager, mergedOntologyIRI);
        log.info("Finished ontology merge; merged support ontologies axiom count = " + merged.getAxiomCount());
		return merged;
	}
	
	
	
	//when to get iri's for terms contained in participants?
	
	void processClaims() throws Exception{
		final Set<Claim> Claims = connection.getClaims();
		log.info("In ProcessClaims");
		for (Claim a : Claims){
			iriManager.validateIRI(a);
			OWLObject owlClaim = a.generateOWL(this);
 		}		
	}
	
	private void initializeTaxonomy(){
		OWLClass arachnidaClass = testWrapper.getDataFactory().getOWLClass(IRIManager.arachnidaTaxon);
		final NodeSet<OWLClass> arachnidaParents = preReasoner.getSuperClasses(arachnidaClass, false);
		final NodeSet<OWLClass> arachnidaChildren = preReasoner.getSubClasses(arachnidaClass, false);
		initializeTaxon(arachnidaClass);
		for (OWLClass c : arachnidaParents.getFlattened()){
			initializeTaxon(c);
		}
		for (OWLClass c : arachnidaChildren.getFlattened()){
			initializeTaxon(c);
		}
	}
	
	private final static List<String> filterLabels = new ArrayList<String>();
	static{
		filterLabels.add("sp. BOLD:");
		filterLabels.add("JXZ-2013");
		filterLabels.add("PS-2009");
		filterLabels.add("WOCS-2009");
		filterLabels.add("LB-2013");
		filterLabels.add("MCZDNA");
		filterLabels.add("DNA10");
	}
	
	private void initializeTaxon(OWLClass taxon){
		final OWLOntologyManager manager = testWrapper.getManager();
		final Set<OWLAnnotationAssertionAxiom> taxonAnnotations = 
				mergedSources.getAnnotationAssertionAxioms(taxon.getIRI());
		boolean skipTaxon = false;
		for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
			if (a.getAnnotation().getProperty().isLabel()){
				String labelStr = a.getAnnotation().getValue().toString();
				for (String filter : filterLabels){
					if (labelStr.contains(filter)){
						skipTaxon = true;
					}
				}
			}
		}
		if (!skipTaxon){
			Set<OWLClassAxiom> taxonAxioms =  mergedSources.getAxioms(taxon);
			manager.addAxioms(target, taxonAxioms);
			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
				if (a.getAnnotation().getProperty().isLabel()){
					manager.addAxiom(target, a);
					log.info("Taxon label is: " + a.getAnnotation().getValue().toString());
				}
			}
		}
	}

	
	void generateOntologyProperties(){
		
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
		final OWLDataFactory factory = testWrapper.getDataFactory();
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
		final NodeSet<OWLClass> termParents = preReasoner.getSuperClasses(term, false);
		for (OWLClass c : termParents.getFlattened()){
			initializeMiscTerm(c);
		}

	}
	
	public void initializeMiscTerm(OWLClass term){
		final OWLOntologyManager manager = testWrapper.getManager();
		Set<OWLClassAxiom> taxonAxioms =  mergedSources.getAxioms(term);
		if (taxonAxioms.isEmpty()){
			log.error("No Axioms for term " + term);
		}
		else{
			manager.addAxioms(target, taxonAxioms);
		}
		Set<OWLAnnotationAssertionAxiom> termAnnotations = 
				mergedSources.getAnnotationAssertionAxioms(term.getIRI());
		for (OWLAnnotationAssertionAxiom a : termAnnotations){
			if (a.getAnnotation().getProperty().isLabel()){
				manager.addAxiom(target, a);
			}
		}

	}
	public void initializeMiscObjPropertyAndParents(OWLObjectProperty prop){
		initializeMiscObjProperty(prop);
		//ELK doesn't support a hierarchy among Object properties
		//final NodeSet<OWLObjectPropertyExpression> propParents = reasoner.getSuperObjectProperties(prop,false);
		//for (OWLObjectPropertyExpression c : propParents.getFlattened()){
		//	initializeMiscObjProperty(c);
		//}

	}
	
	public void initializeMiscObjProperty(OWLObjectPropertyExpression prope){
		final OWLOntologyManager manager = testWrapper.getManager();
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
	
	private void saveOntology() throws OWLOntologyStorageException{
		log.info("Saving ontology");			
		File f = new File(temporaryOutput);
		testWrapper.getManager().saveOntology(target, IRI.create(f.toURI()));		
	}
	

	
	private void generateHTML(){
		log.info("Generating HTML pages");
	}
	
	/**
	 * @return the ontology containing extracted terms and axioms (not reasoned over)
	 */
	public OWLOntology getTarget(){
		return target;
	}
	
	
	public OWLOntologyManager getOntologyManager(){
		return testWrapper.getManager();
	}
	
	
	public OWLDataFactory getDataFactory(){
		return testWrapper.getDataFactory();
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
	
	public OWLReasoner getPreReasoner(){
		return preReasoner;
	}
	
	public OWLReasoner getPostReasoner(){
		return postReasoner;
	}

	public void loadCuratorAddedTerms(OWLOntology mergedSources) throws SQLException{
		final OWLOntologyManager manager = testWrapper.getManager();
		final OWLDataFactory factory = testWrapper.getDataFactory();
		final Set<Taxon> taxa = connection.getTaxa();
		for (Taxon t : taxa){
			final IRI iri = IRI.create(t.getIriString());
			nonNCBITaxa.put(iri, t);

		}

	}
	
	public Map<IRI,Taxon> getNonNCBITaxa(){
		return nonNCBITaxa;
	}
	
		
}
