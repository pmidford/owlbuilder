package org.arachb.owlbuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.arachadmin.*;
import org.arachb.owlbuilder.lib.Config;
import org.arachb.owlbuilder.lib.Vocabulary;
import org.obolibrary.robot.ExtractOperation;
import org.obolibrary.robot.IOHelper;
import org.obolibrary.robot.MergeOperation;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.*;
//import owltools.graph.OWLGraphWrapper;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class OwlBuilder {

    //private final Config config;
    public static final Logger log = Logger.getLogger(OwlBuilder.class);

    private final OWLOntologyManager manager;
    public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
    private OWLOntology target;
    //private final OWLGraphWrapper testWrapper;

    /* Map from support ontology to its declared domain */
    private final Map<OWLOntology, String>supportOntologies = new HashMap<>();
    private OWLOntology mergedSources = null;

    private OWLReasoner postReasoner;


    private final DBSource dbSource = new DBSource();
    private Config config;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    EntityManager em = entityManagerFactory.createEntityManager();


    public OwlBuilder() throws Exception {
        config = new Config("");
        manager = OWLManager.createOWLOntologyManager();
        target = manager.createOntology(IRI.create(targetIRI));
        final OWLDocumentFormat format = manager.getOntologyFormat(target);
        if (format == null) {
            throw new RuntimeException(String.format("Couldn't retrieve format from %s or %s", target, manager));
        } else {
            format.asPrefixOWLOntologyFormat().setPrefix("doi", "http://dx.doi.org/");
        }
    }

    public static void main(String[] args) {
        OwlBuilder builder;
        // Configure log4j
        PropertyConfigurator.configure(OwlBuilder.class.getClassLoader().getResource("log4j.properties"));
        log.info("Trying to start");
        try {
            builder = new OwlBuilder();
            builder.process();
            //builder.shutdown();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void process() throws Exception {

        IOHelper ioHelper = new IOHelper();
        mergeDomainOntologies(ioHelper);
        target = ioHelper.loadOntology("merged.owl");


        // Classify the ontology.
        try {
            processDatabase();
            log.info("After processing database, target is = " + target);


            System.out.println(Runtime.getRuntime().freeMemory() +
                    " \t \t " + Runtime.getRuntime().totalMemory() +
                    " \t \t " + Runtime.getRuntime().maxMemory());
            log.info("starting post reasoning on target");
            log.info("Creating postReasoner");
            //OWLReasonerFactory elkFactory = new ElkReasonerFactory();
            //postReasoner = elkFactory.createReasoner(target);
            OWLReasonerFactory postFactory = new org.semanticweb.HermiT.ReasonerFactory();
            postReasoner = postFactory.createReasoner(target);
            log.info("Precomputing hierarchy");
            postReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
            log.info("Finished precomputing hierarchy");
            log.info("After precomputing hierarchy, target is = " + target);

			// To generate an inferred ontology we use implementations of
			// inferred axiom generators
            List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<>();
            gens.add(new InferredSubClassAxiomGenerator());
            gens.add(new InferredEquivalentClassAxiomGenerator());
            gens.add(new InferredClassAssertionAxiomGenerator());


            log.info("Processing Inferred ontology");
            InferredOntologyGenerator iog = new InferredOntologyGenerator(postReasoner, gens);
            iog.fillOntology(manager.getOWLDataFactory(), target);
            log.info("flushing reasoner");
			postReasoner.flush();
//

            log.info("Saving ontology");

            log.info("After adding inferred axioms, target is = " + target);


            ioHelper.saveOntology(target, "test.owl");

			generateHTML();
			log.info("Shutting down reasoner");
//
        }
        catch(Exception e){
            log.error(e);
           e.printStackTrace();
        }
        finally{
            if (postReasoner != null){
                postReasoner.dispose();
            }
            else{
                log.info("No post reasoner found at shutdown");
            }
        }

    em.getTransaction().begin();
    List<Taxon> result = em.createQuery( "SELECT t FROM Taxon t", Taxon.class ).getResultList();
    for ( Taxon taxon : result ) {
        System.out.println( "Taxon: " + taxon.getName() + " : " + taxon.getAuthor() );
    }
    em.getTransaction().commit();
    em.close();
    }

    private void mergeDomainOntologies(IOHelper ioHelper) throws Exception {
        log.info("Loading ontologies");
        OWLOntology merged = loadImportedOntologies();
        ArrayList<OWLOntology> buildList = new ArrayList<>();
        buildList.add(target);
        buildList.add(merged);
        target = MergeOperation.merge(buildList, true, false);

        initializeMisc();

        //IRI outputIRI = ioHelper.createIRI("http://arachb.org/arachb/arachb.owl");  //not sure where this is used
        ioHelper.saveOntology(merged, "merged.owl");
    }

    /**
     * This loads the support ontologies specified in the source_ontologies table
     * @throws Exception
     */
    OWLOntology loadImportedOntologies() throws Exception{
        final Map<String, String> sourceMap = dbSource.loadImportSourceMap(em);
        for (String sourceIRIStr : sourceMap.keySet()) {
            String domain = sourceMap.get(sourceIRIStr);
            OWLOntology supOnt = loadAndFilterSupportOntology(domain, sourceIRIStr);
            System.out.print("Loaded support Ontology: " + supOnt);
            supportOntologies.put(supOnt, domain);
        }
        OWLOntology merged = mergeSupportOntologies(supportOntologies.keySet());
        log.info("Loaded " + merged.getClassesInSignature().size() + " classes.");
        log.info("Loaded " + merged.getAxiomCount() + " axioms");
        System.out.println("Free Memory \t Total Memory \t Max Memory");
        System.out.println(Runtime.getRuntime().freeMemory() +
                " \t \t " + Runtime.getRuntime().totalMemory() +
                " \t \t " + Runtime.getRuntime().maxMemory());
        return merged;
    }

    /**
     * @param ontSet Set of OWL ontolgies - collected of reduced imported ontologies.
     * @return OWLOntolgy - merged axioms from reduced ontologies in ontSet
     */
    OWLOntology mergeSupportOntologies(Set<OWLOntology> ontSet) {
        ArrayList<OWLOntology> ontList = new ArrayList<>(ontSet);
        return MergeOperation.merge(ontList, true,false);
    }

    void processDatabase() throws Exception{
        log.info("Processing Publications");
        Map<String,OWLObject> publicationIndividuals = processPublications(em);
//		log.info("Processing Narratives");
//		Map<String,OWLObject> narrativeIndividuals = processNarratives();
//		log.info("Processing Claims");
//		processClaims(narrativeIndividuals);

    }

    Map<String, OWLObject> processPublications(EntityManager em) throws Exception{
        Map<String,OWLObject> results = new HashMap<>();

        final List<Publication> publications = dbSource.getPublications(em);
        System.out.println("Loaded " + publications.size() + " publications");
        for (Publication p : publications) {
            p.generateOWL(this, results);  //no return value, OWLObject added to results map
        }
        System.out.println("Generated " + results.size() + " new objects");
        return results;
    }

    /*
      New approach to pruning ontologies.
     */

    //TODO how to handle multiple ontologies for one domain
    /**
     *
     * @param domain String specifying the domain (anatomy, taxonomy, behavior, etc.) of the ontology(ies) to load
     * @param sourcePath String specifying an IRI for an ontology
     * @return OWLOntology
     */
    OWLOntology loadAndFilterSupportOntology(String domain, String sourcePath) throws SQLException, IOException, OWLOntologyCreationException {
        IOHelper ioHelper = new IOHelper();
        final Set<String> usedDomainIds = dbSource.getTermIdsByDomain(domain);
        Set<IRI> domainIRIs = ioHelper.createIRIs(usedDomainIds);
        final IRI iri = IRI.create(sourcePath);
        String cachePath = makeCachePath(iri);

        OWLOntology sourceOnt = ioHelper.loadOntology(cachePath);
        IRI outputIRI = ioHelper.createIRI("http://arachb.org/" + domain + ".owl");


        System.out.println("SourceOnt: " + sourceOnt);
        System.out.println("Extracting from " + domainIRIs.size() + " terms");
        // see https://robot.obolibrary.org/extract.html
        Map<String,String> options = ExtractOperation.getDefaultOptions();
        if ("phenotype".equals(domain)) {
            options.put("intermediates", "none");  // "none" works, minimal does not
        }
        else{
            System.out.println("Domain: " + domain + "intermediates=minimal");
            options.put("intermediates", "minimal");
        }
        options.put("individuals", "definitions"); // "definitions" works
        OWLOntology filteredOnt = ExtractOperation.extract(sourceOnt, domainIRIs, outputIRI, ModuleType.BOT, options);
        System.out.print("FilteredOnt: " + filteredOnt);

        final String dumpFile = domain + "_Ontology.owl";
        ioHelper.saveOntology(filteredOnt, dumpFile);
        log.info("Finished loadAndFilterOntology: " + domain);
        return filteredOnt;
    }

    static final IRI[] MISC_PROPERTIES = {
            Vocabulary.partOfProperty,
            Vocabulary.denotesProperty,
            Vocabulary.realizedInProperty,
            Vocabulary.hasParticipantProperty,
            Vocabulary.hasActiveParticipantProperty,
            Vocabulary.determinesProperty,
            Vocabulary.hasMaterialContributionProperty,
            Vocabulary.mereotopologicallyRelatedToProperty,
            Vocabulary.overlapsProperty,
            //Vocabulary.hasPartProperty,  //suppressed (see Vocabulary.java)
            Vocabulary.participatesInProperty,
            Vocabulary.activelyParticipatesInProperty
    };

    static final IRI[] IAO_TERMS = {
            Vocabulary.informationContentEntity,
            Vocabulary.pubAboutInvestigation,
            Vocabulary.textualEntity,
    };

    static final IRI[] ARACHB_TERMS = {
            Vocabulary.narrative_IRI,
            Vocabulary.behavior_phenotype_IRI
    };

    static final IRI[] BFO_TERMS = {
            Vocabulary.dispositionIRI,
            Vocabulary.processIRI
    };


    //somewhere we should assert that 'part_of' is SAMEAS 'part of' from BFO for any OBO-based
    //ontology (e.g., SPD) that defines part_of on their own

    static final IRI [] PARTOFEQUIVALENTS = {
            IRI.create("http://purl.obolibrary.org/obo/spd#part_of")
    };

    private void initializeMisc(){
        final OWLDataFactory factory = manager.getOWLDataFactory();
        for (IRI mTerm : IAO_TERMS){
            String termName = Vocabulary.iaoImports.get(mTerm);
            //pull stuff from database for this
            if (termName != null) {
                addLabel(mTerm, termName);
            }
            else {
                log.warn("Term " + mTerm.toString() + " has no label");
            }
            OWLClass mClass = factory.getOWLClass(mTerm);
            initializeMiscTermAndParents(mClass);
        }
        log.info("About to initialize arachb terms: " + Arrays.toString(ARACHB_TERMS));
        for (IRI mTerm : ARACHB_TERMS){
            String termName = Vocabulary.arachbTerms.get(mTerm);
            //pull stuff from database for this
            OWLClass mClass = factory.getOWLClass(mTerm);
            addLabel(mTerm, termName);
            initializeMiscTermAndParents(mClass);
            log.info("Initialized arachb term: " + mClass + " with label: " + termName);
        }
        log.info("About to initialize bfo terms: " + Arrays.toString(BFO_TERMS));
        for (IRI mTerm : BFO_TERMS){
            String termName = Vocabulary.bfoImports.get(mTerm);
            //pull stuff from database for this
            OWLClass mClass = factory.getOWLClass(mTerm);
            addLabel(mTerm, termName);
            initializeMiscTermAndParents(mClass);
            log.info("Initialized arachb term: " + mClass + " with label: " + termName);
        }

        List<Property> properties = dbSource.getProperties();
        Map<String, Property> propertiesHash = new HashMap<>();
        // A table of all the properties just read, to support parent initialization
        for (Property p: properties){
            propertiesHash.put(p.getRef_id(), p);
        }
        for (Property p: properties){
            log.info("Initializing Property " + p.getRef_id());
            IRI pIRI = IRI.create(p.getRef_id());
            if (Arrays.asList(MISC_PROPERTIES).contains(pIRI)) {
                OWLObjectProperty mprop = factory.getOWLObjectProperty(pIRI);
                initializeMiscObjPropertyAndParents(mprop, propertiesHash);
            } else if (Arrays.asList(PARTOFEQUIVALENTS).contains(pIRI)) {
                //factory.getOWLEquivalentObjectPropertiesAxiom(partofEquiv, Vocabulary.partOfProperty);
            }
        }
    }

    public void initializeMiscTermAndParents(OWLClass term){
        assert term != null;
        initializeMiscTerm(term);
        log.info("initializing terms for: " + term.toStringID());
        //final NodeSet<OWLClass> termParents = postReasoner.getSuperClasses(term, false);
        //for (OWLClass c : termParents.getFlattened()){
        //    log.debug("examining parent term: " + c.toStringID());
        //    initializeMiscTerm(c);
        //}
    }

    public void initializeMiscTerm(OWLClass term){
        assert target != null;
        Set<OWLClassAxiom> taxonAxioms = target.getAxioms(term,
                org.semanticweb.owlapi.model.parameters.Imports.INCLUDED);
        if (taxonAxioms.isEmpty()){   //TODO add subclass relation to owl:Thing?
            log.warn("No Axioms for term " + term.getIRI());
        }
        else{
            manager.addAxioms(target, taxonAxioms);
        }
        Set<OWLAnnotationAssertionAxiom> termAnnotations =
                target.getAnnotationAssertionAxioms(term.getIRI());
        log.debug("Checking for label for " + term.getIRI());
        for (OWLAnnotationAssertionAxiom a : termAnnotations){
            if (a.getAnnotation().getProperty().isLabel()){
                log.debug("Found label for " + term.getIRI());
                manager.addAxiom(target, a);
            }
        }
    }

    public void initializeMiscObjPropertyAndParents(OWLObjectProperty prop, Map<String, Property> p_hash){
        initializeMiscObjProperty(prop, p_hash);
        //final NodeSet<OWLObjectPropertyExpression> propParents = postReasoner.getSuperObjectProperties(prop,true);
        //for (OWLObjectPropertyExpression c : propParents.getFlattened()){
        //    initializeMiscObjProperty(c, p_hash);
        //}
    }

    public void initializeMiscObjProperty(OWLObjectPropertyExpression prope, Map<String, Property> p_hash){
        final OWLDataFactory factory = getDataFactory();
        Set<OWLObjectPropertyAxiom> propertyAxioms =  target.getAxioms(prope,org.semanticweb.owlapi.model.parameters.Imports.INCLUDED);
        if (propertyAxioms.isEmpty()){
            for (OWLObjectProperty p : prope.getObjectPropertiesInSignature()){
                final String iriStr = p.getIRI().toString();
                if (p_hash.containsKey(iriStr)){
                    String p_label = p_hash.get(iriStr).getLabel();
                    OWLAnnotationProperty labelProperty = factory.getRDFSLabel();
                    OWLAnnotationValue labelValue = factory.getOWLLiteral(p_label);
                    OWLAnnotation labelAnnotation = factory.getOWLAnnotation(labelProperty, labelValue);
                    OWLAnnotationAxiom labelAxiom = factory.getOWLAnnotationAssertionAxiom(p.getIRI(), labelAnnotation);
                    manager.addAxiom(target,labelAxiom);
                }
            }
        }
        else{
            log.debug("Adding axioms for " + prope);
            manager.addAxioms(target, propertyAxioms);
        }
        //log.info("Annotations");
        if (prope instanceof OWLObjectProperty){
            OWLObjectProperty prop = (OWLObjectProperty) prope;

            Set<OWLAnnotationAssertionAxiom> termAnnotations =
                    target.getAnnotationAssertionAxioms(prop.getIRI());
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
     * @param ind individual
     */
    public void initializeMiscIndividual(OWLIndividual ind){
        //TODO implement
    }

    public Map<String, Narrative> getNarrativeTableForPublication(Publication p){
        List<Narrative> narratives = dbSource.getNarrativesForPublication(p.getRef_id(), em);
        Map<String, Narrative> result = new HashMap<>();
        for (Narrative n : narratives){
            result.put(n.getRef_id(), n);
        }
        return result;
    }

    public Map<String, BehaviorPhenotype> getBehaviorPhenotypeTableForPublication(Publication p){
        List<BehaviorPhenotype> phenotypes = dbSource.getBehaviorPhenotypesForPublication(p.getRef_id(), em);
        Map<String, BehaviorPhenotype> result = new HashMap<>();
        for (BehaviorPhenotype bp : phenotypes){
            result.put(bp.getRef_id(), bp);
        }
        return result;
    }

    public Map<Integer, Event> getEventTableForNarrative(Narrative n) {
        List<Event> events = dbSource.getEventsForNarrative(n.getRef_id(), em);
        Map<Integer, Event> result = new HashMap<>();
        for (Event e : events) {
            result.put(e.get_local_id(), e);
        }
        return result;
    }
    private String makeCachePath(IRI iri) {
        final String sourcePath = iri.toURI().getPath();
        final String [] pathParts = sourcePath.split("/");
        final String cachePath = config.getCacheDir() + "/" + pathParts[pathParts.length-1];
        log.info("Cachepath is " + cachePath);
        return cachePath;
    }

    /* Should be writing HTML pages for the ontology */
    private void generateHTML(){
        log.info("Generating HTML pages");
    }


    public OWLDataFactory getDataFactory(){
        return manager.getOWLDataFactory();
    }

    private final Set<OWLAxiom> axiom_set = new HashSet<>();

    private void queueAxiom(OWLAxiom a){
        axiom_set.add(a);
    }

    public void addAxioms(){
        manager.addAxioms(target, axiom_set);
        axiom_set.clear();
    }

    public void addClassAssertionAxiom(OWLClassExpression ce, OWLIndividual ind){
        OWLDataFactory df = manager.getOWLDataFactory();
        queueAxiom(df.getOWLClassAssertionAxiom(ce, ind));
    }

    public void addComment(IRI iri, String commentStr){
        OWLDataFactory df = manager.getOWLDataFactory();
        addRDFAnnotation(iri, df.getRDFSComment(), commentStr);
    }

    public void addLabel(IRI iri, String label){
        OWLDataFactory df = manager.getOWLDataFactory();
        addRDFAnnotation(iri,df.getRDFSLabel(), label);
    }

    private void addRDFAnnotation(IRI iri, OWLAnnotationProperty prop, String text){
        OWLDataFactory df = manager.getOWLDataFactory();
        OWLAnnotation anno = df.getOWLAnnotation(prop, df.getOWLLiteral(text));
        queueAxiom(df.getOWLAnnotationAssertionAxiom(iri,anno));
    }

    public void addOWLAnnotationToIndividual(OWLIndividual ind, IRI annoIRI, String annotation) {
        OWLDataFactory df = manager.getOWLDataFactory();
        OWLAnnotationProperty annoProp = df.getOWLAnnotationProperty(annoIRI);
        OWLAnnotation anno = df.getOWLAnnotation(annoProp, df.getOWLLiteral(annotation, "en"));
        queueAxiom(df.getOWLAnnotationAssertionAxiom(ind.asOWLNamedIndividual().getIRI(), anno));
    }
    public void addOWLObjectPropertyAssertion(OWLObjectProperty rel, OWLIndividual ind1, OWLIndividual ind2) {
        OWLDataFactory df = manager.getOWLDataFactory();
        queueAxiom(df.getOWLObjectPropertyAssertionAxiom(rel, ind1, ind2));
    }
}
