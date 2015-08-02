package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 * @author Peter E. Midford
 */

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.IRIManager;
import org.arachb.arachadmin.PropertyBean;
import org.arachb.arachadmin.TaxonBean;
import org.arachb.owlbuilder.lib.Claim;
import org.arachb.owlbuilder.lib.Config;
import org.arachb.owlbuilder.lib.Participant;
import org.arachb.owlbuilder.lib.Taxon;
import org.arachb.owlbuilder.lib.Vocabulary;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
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
	@SuppressWarnings("unused")
	private final OWLReasonerFactory rfactory;
	private final OWLReasonerFactory elkFactory;
	private OWLOntology target;
	private OWLReasoner preReasoner;
	private OWLReasoner postReasoner;
	private final Map<IRI,Taxon>nonNCBITaxa = new HashMap<IRI,Taxon>();

	private final Set<OWLAxiom> axiomset = new HashSet<OWLAxiom>();

	private final Map<String,OWLOntology>supportOntologies = new HashMap<String,OWLOntology>();
	private OWLOntology mergedSources = null;

	private final OWLGraphWrapper testWrapper;


	public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
	public static final String taxonomyTarget = "http://arachb.org/imports/NCBI_import.owl";

	private static String temporaryOutput = "test.owl";

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

	public Owlbuilder() throws Exception{
		config = new Config("");
		if (DBConnection.testConnection()){
			connection = DBConnection.getDBConnection();
		}
		else{
			connection = DBConnection.getMockConnection();
		}
		testWrapper = new OWLGraphWrapper(targetIRI);
		rfactory = new Reasoner.ReasonerFactory();
		elkFactory = new ElkReasonerFactory();
		iriManager = connection.getIRIManager();
		final OWLDocumentFormat format = testWrapper.getManager().getOntologyFormat(testWrapper.getSourceOntology());
		format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
	}

	public void setUpForTesting() throws Exception{
		mergedSources = testWrapper.getSourceOntology();
		preReasoner = elkFactory.createReasoner(mergedSources);
		target = testWrapper.getSourceOntology();
	}


	void process() throws Exception{
		target = testWrapper.getSourceOntology();
		log.info("Loading ontologies");
		loadImportedOntologies();
		mergedSources = mergeImportedOntologies();
		loadCuratorAddedTerms(mergedSources);
		//log.info("Saving ontology");
		//File f = new File("merged.owl");
		//testWrapper.getManager().saveOntology(mergedSources, IRI.create(f.toURI()));

		log.info("Initializing reasoner");

        OWLReasonerFactory rf = new StructuralReasonerFactory();
        preReasoner = rf.createReasoner(mergedSources);
		//preReasoner = elkFactory.createReasoner(mergedSources);
		log.info("Finished reasoner initialization");
        // Classify the ontology.
		try{
			preReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			log.info("Finished precomputing class hierarchy");
			//target = manager.createOntology(IRI.create(targetIRI));
			log.info("Before copying taxonomy, target class count = " +target.getClassesInSignature().size());
			log.info("copying relevant taxonony to target ontology");
			initializeTaxonomy(target);
			log.info("After copying taxonomy, target class count = " +target.getClassesInSignature().size());
			log.info("copying misc support terms to target ontology");
			log.info("Before adding misc classes, target class count = " +target.getClassesInSignature().size());
			initializeMisc();
			log.info("After adding misc classes, target class count = " +target.getClassesInSignature().size());
			log.info("Before processing database, target class count = " +target.getClassesInSignature().size());
			processDatabase();
			log.info("After processing database, target class count = " +target.getClassesInSignature().size());

			//log.info("Saving ontology");
			//File f = new File("checkpoint.owl");
			//testWrapper.getManager().saveOntology(target, IRI.create(f.toURI()));
			if (true){
				//preReasoner.flush();
				log.info("disposing preReasoner");
				preReasoner.dispose();
				mergedSources = null;  // hopefully GC will notice this
				log.info("starting post reasoning on target");
				log.info("Creating postReasoner");
				postReasoner = elkFactory.createReasoner(target);
				log.info("Precomputing hierarchy");
				postReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

				// To generate an inferred ontology we use implementations of
				// inferred axiom generators
				List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
				gens.add(new InferredSubClassAxiomGenerator());
				gens.add(new InferredEquivalentClassAxiomGenerator());
				gens.add(new InferredClassAssertionAxiomGenerator());

				InferredOntologyGenerator iog = new InferredOntologyGenerator(postReasoner,
						gens);
				iog.fillOntology(testWrapper.getDataFactory(),target);
				log.info("flushing reasoner");
				postReasoner.flush();

				saveOntology();
				generateHTML();
				log.info("Shutting down reasoner");
			}
		}
		catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}
		finally{
			preReasoner.dispose();
			if (postReasoner != null){
				postReasoner.dispose();
			}
			else{
				log.info("No post reasoner found at shutdown");
			}
		}
	}

	void shutdown() throws Exception{
        log.info("Done");
		connection.close();
	}

	void processDatabase() throws Exception{
		log.info("Processing Claims");
		processClaims();
		connection.close();
	}

	/**
	 * This loads the support ontologies specified in the source_ontologies table
	 * @throws Exception
	 */
	void loadImportedOntologies() throws Exception{
		Map<String, String> sourceMap;
		sourceMap = connection.loadImportSourceMap();
		final Map <String,String> namesForLoading = connection.loadOntologyNamesForLoading();
		final OWLOntologyManager manager = testWrapper.getManager();
		for (String source : sourceMap.keySet()){
			log.info(String.format("Loading %s from %s", namesForLoading.get(source), source));
			IRI iri = IRI.create(source);
			String sourcePath = iri.toURI().getPath();
			String [] pathParts = sourcePath.split("/");
			String cachePath = "file:/" + config.getCacheDir() + "/" + pathParts[pathParts.length-1];
			log.info("Cachepath is " + cachePath);
			manager.getIRIMappers().add(new SimpleIRIMapper(iri, IRI.create(cachePath)));
			log.info("Added IRIMapper");
			try{
				OWLOntology ont = manager.loadOntology(iri);
				log.info("Loaded " + ont.getAxiomCount() + " axioms from " + cachePath);
				supportOntologies.put(source, ont);
			}
			catch (Exception e){
				e.printStackTrace();
			}
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



	void processClaims() throws Exception{
		final Set<Claim> claims = Claim.wrapSet(connection.getClaimTable());
		log.info("In ProcessClaims");
		for (Claim cl : claims){
			final Set<Participant> participants =
					Participant.wrapSet(connection.getParticipantTable(cl.getId()));
			// load participant elements
			for (Participant p : participants){
				p.loadElements(connection);
				p.resolveElements();
			}
			//IRI's for individual participants are generated by their generateOWL method
			cl.generateOWL(this);
 		}
	}

	private void initializeTaxonomy(OWLOntology targetontology){
		OWLClass arachnidaClass = testWrapper.getDataFactory().getOWLClass(Vocabulary.arachnidaTaxon);
		final NodeSet<OWLClass> arachnidaParents = preReasoner.getSuperClasses(arachnidaClass, false);
		final NodeSet<OWLClass> arachnidaChildren = preReasoner.getSubClasses(arachnidaClass, false);
		initializeTaxon(arachnidaClass,targetontology);
		for (OWLClass c : arachnidaParents.getFlattened()){
			initializeTaxon(c,targetontology);
		}
		for (OWLClass c : arachnidaChildren.getFlattened()){
			initializeTaxon(c,targetontology);
		}
	}

	//TODO: consider moving this to a configuration file
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

	private void initializeTaxon(OWLClass taxon, OWLOntology targetontology){
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
			Set<OWLClassAxiom> taxonAxioms =  mergedSources.getAxioms(taxon,
																	  org.semanticweb.owlapi.model.parameters.Imports.INCLUDED);
			manager.addAxioms(targetontology, taxonAxioms);
			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
				if (a.getAnnotation().getProperty().isLabel()){
					manager.addAxiom(targetontology, a);
					log.debug("Taxon label is: " + a.getAnnotation().getValue().toString());
				}
			}
		}
	}

	/**
	 * Should be about generating metadata from target ontology (version, etc.)
	 */
	void generateOntologyProperties(){

	}

	static final IRI[] MISC_PROPERTIES = {
		Vocabulary.partOfProperty,
		Vocabulary.denotesProperty,
		Vocabulary.hasParticipantProperty,
		Vocabulary.hasParticipantAtSomeTimeProperty,
		Vocabulary.hasActiveParticipantProperty,
		Vocabulary.determinesProperty,
		Vocabulary.hasMaterialContributionProperty,
		Vocabulary.mereotopologicallyRelatedToProperty,
		Vocabulary.overlapsProperty,
		Vocabulary.hasPartProperty,
		Vocabulary.participatesInProperty,
		Vocabulary.activelyParticipatesInProperty
	};

	static final IRI[] MISC_TERMS = {
		Vocabulary.informationContentEntity,
		Vocabulary.pubAboutInvestigation,
		Vocabulary.textualEntity
	};


	//somewhere we should assert that 'part_of' is SAMEAS 'part of' from BFO for any OBO-based
	//ontology (e.g., SPD) that defines part_of on their own

	static final IRI [] PARTOFEQUIVALENTS = {
		IRI.create("http://purl.obolibrary.org/obo/spd#part_of")
	};

	private void initializeMisc(){
		final OWLDataFactory factory = testWrapper.getDataFactory();
		for (IRI mTerm : MISC_TERMS){
			//pull stuff from database for this
			OWLClass mClass = factory.getOWLClass(mTerm);
			initializeMiscTermAndParents(mClass);
		}
		for (IRI mProp : MISC_PROPERTIES){
			try {
				connection.getPropertyFromSourceId(mProp.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OWLObjectProperty mprop = factory.getOWLObjectProperty(mProp);
			initializeMiscObjPropertyAndParents(mprop);
		}
		for (IRI partofEquiv : PARTOFEQUIVALENTS){
			//factory.getOWLEquivalentObjectPropertiesAxiom(partofEquiv, Vocabulary.partOfProperty);
		}
	}

	public void initializeMiscTermAndParents(OWLClass term){
		assert term != null;
		initializeMiscTerm(term);
		log.info("initializing terms for: " + term.toStringID());
		final NodeSet<OWLClass> termParents = preReasoner.getSuperClasses(term, false);
		for (OWLClass c : termParents.getFlattened()){
			log.debug("examining parent term: " + c.toStringID());
			initializeMiscTerm(c);
		}

	}

	public void initializeMiscTerm(OWLClass term){
		final OWLOntologyManager manager = testWrapper.getManager();
		assert mergedSources != null;
		Set<OWLClassAxiom> taxonAxioms = mergedSources.getAxioms(term,
																 org.semanticweb.owlapi.model.parameters.Imports.INCLUDED);
		if (taxonAxioms.isEmpty()){   //TODO add subclass relation to owl:Thing?
			//log.error("No Axioms for term " + term);
		}
		else{
			manager.addAxioms(target, taxonAxioms);
		}
		Set<OWLAnnotationAssertionAxiom> termAnnotations =
				mergedSources.getAnnotationAssertionAxioms(term.getIRI());
		log.debug("Checking for label for " + term.getIRI().toString());
		for (OWLAnnotationAssertionAxiom a : termAnnotations){
			if (a.getAnnotation().getProperty().isLabel()){
				log.debug("Found label for " + term.getIRI().toString());
				manager.addAxiom(target, a);
			}
		}

	}
	public void initializeMiscObjPropertyAndParents(OWLObjectProperty prop){
		log.debug("initializing property for: " + prop.toStringID());
		initializeMiscObjProperty(prop);
		final NodeSet<OWLObjectPropertyExpression> propParents = preReasoner.getSuperObjectProperties(prop,false);
		for (OWLObjectPropertyExpression c : propParents.getFlattened()){
			initializeMiscObjProperty(c);
		}
	}

	public void initializeMiscObjProperty(OWLObjectPropertyExpression prope){
		final OWLOntologyManager manager = testWrapper.getManager();
		final OWLDataFactory factory = getDataFactory();
		Set<OWLObjectPropertyAxiom> propertyAxioms =  mergedSources.getAxioms(prope,org.semanticweb.owlapi.model.parameters.Imports.INCLUDED);
		if (propertyAxioms.isEmpty()){
			for (OWLObjectProperty p : prope.getObjectPropertiesInSignature()){
				if (PropertyBean.isSourceIdCached(p.getIRI().toString())){
					PropertyBean pb = PropertyBean.getSourceIdCached(p.getIRI().toString());
					OWLAnnotationProperty labelProperty = factory.getRDFSLabel();
					OWLAnnotationValue labelValue = factory.getOWLLiteral(pb.getLabel());
					OWLAnnotation labelAnnotation = factory.getOWLAnnotation(labelProperty, labelValue);
					OWLAnnotationAxiom labelAxiom = factory.getOWLAnnotationAssertionAxiom(p.getIRI(), labelAnnotation);
					manager.addAxiom(target,labelAxiom);
				}
				else {
					log.warn("Couldn't find propertybean for IRI " + p.getIRI().toString());
				}

			}
		}
		else{
			log.debug("Adding axioms for " + prope.toString());
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

	/**
	 * This should assert misc properties for individuals (e.g., a rdf:label)
	 * @param ind
	 */
	public void initializeMiscIndividual(OWLIndividual ind){
		//TODO implement
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
		final Set<TaxonBean> taxa = connection.getTaxonTable();
		final Set<OWLAxiom> taxonAxioms = new HashSet<OWLAxiom>();
		final OWLDataFactory factory = getDataFactory();
		for (TaxonBean tb : taxa){
			Taxon t = new Taxon(tb);
			final IRI taxonIRI = IRI.create(tb.getIRIString());
			OWLClass taxonClass = getDataFactory().getOWLClass(taxonIRI);
			String parentString = tb.getParentRefId();
			if (parentString == null){
				log.error("No parent specified for curator added taxon: " + tb.getId());
			}
			else{
				final IRI parentIRI = IRI.create(parentString);
				OWLClass parentClass = factory.getOWLClass(parentIRI);
				taxonAxioms.add(factory.getOWLSubClassOfAxiom(taxonClass, parentClass));
				nonNCBITaxa.put(taxonIRI, t);
				if (tb.getName() != null){
					OWLAnnotation labelAnno =
							factory.getOWLAnnotation(factory.getRDFSLabel(),
									                 factory.getOWLLiteral(t.getName()));
					taxonAxioms.add(factory.getOWLAnnotationAssertionAxiom(taxonIRI, labelAnno));
				}
			}
		}
		this.getOntologyManager().addAxioms(mergedSources, taxonAxioms);
	}

	public Map<IRI, Taxon> getNonNCBITaxa() {
		return nonNCBITaxa;
	}


	/* Utilities - some of these maybe belong in the testwrapper */

	private void queueAxiom(OWLAxiom a){
		axiomset.add(a);
	}

	public void addAxioms(){
		testWrapper.getManager().addAxioms(testWrapper.getSourceOntology(), axiomset);
		axiomset.clear();
	}


	public void addComment(String iriStr, String commentStr){
		OWLDataFactory df = testWrapper.getDataFactory();
		OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSComment(),
                                                        df.getOWLLiteral(commentStr));
		queueAxiom(df.getOWLAnnotationAssertionAxiom(IRI.create(iriStr),commentAnno));
	}


}
