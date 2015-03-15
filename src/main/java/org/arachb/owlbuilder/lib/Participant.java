package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IRIManager;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.PElementBean;
import org.arachb.arachadmin.ParticipantBean;
import org.arachb.arachadmin.PropertyBean;
import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Participant implements GeneratingEntity{



	final static String BADTAXQuantifiedParticipant =
			"Term without IRI referenced as participant taxon: participant QuantifiedParticipantxon id = %s";
	final static String BADANATOMYIRI =
			"Term without IRQuantifiedParticipantd as participant anatomy: participant id = %s; anatomy id = %s";
	final static String BADSUBSTRATEIRI =
			"Term without IRI referenced as participant substrate; participant id = %s; substrate id = %s";



	private final static String INDIVIDUALQUANTIFIER = "INDIVIDUAL";
	private final static String SOMEQUANTIFIER = "SOME";

	private static Logger log = Logger.getLogger(Participant.class);
	private final ParticipantBean bean;

	public Participant(ParticipantBean b){
		bean = b;

	}

	/**
	 * Utility for making Participant Sets from sets of beans
	 */
	public static Set<Participant> wrapSet(Set<ParticipantBean> bset){
		final Set<Participant>result = new HashSet<Participant>();
		for(ParticipantBean b : bset){
			result.add(new Participant(b));
		}
		return result;
	}
	
	@Override	
	public OWLObject generateOWL(Owlbuilder builder) throws SQLException{
		final Map<Integer, OWLObject> owlElements = new HashMap<Integer, OWLObject>();
		PElementBean headBean = bean.getElementBean(bean.getHeadElement());
		PropertyBean propBean= bean.getParticipationBean();
		OWLObject headObject = traverseElements(builder, owlElements, headBean, propBean);
		return headObject;  //TODO something else is needed

	}
	
	private OWLObject traverseElements(Owlbuilder builder, 
			Map<Integer, OWLObject> owlElements, 
			PElementBean headElement,
			PropertyBean headProperty){
		if (!owlElements.containsKey(headElement.getId())){
			Set<Integer>children = headElement.getChildren();
			OWLObject o = generateElementOWL(headElement,headProperty,builder);
			owlElements.put(headElement.getId(), o);
			for (Integer child : children) {
				log.info("child = " + child);
				PElementBean childBean = headElement.getChildElement(child);
				PropertyBean childProperty = headElement.getChildProperty(child);
				OWLObject childObject = traverseElements(builder,owlElements,childBean,childProperty);
			}
		}
		return owlElements.get(headElement.getId());
	}

	
	private OWLObject generateElementOWL(PElementBean pe, PropertyBean pb, Owlbuilder builder){
		final OWLDataFactory factory = builder.getDataFactory();
		IRI propertyIRI = IRI.create(pb.getSourceId());
		OWLObjectProperty elementProperty = factory.getOWLObjectProperty(propertyIRI);
		if (pe.getTerm() != null){
			TermBean tb = pe.getTerm();
			IRI termIRI;
			try {
				termIRI = IRI.create(tb.checkIRIString(builder.getIRIManager()));
				OWLClass termClass = factory.getOWLClass(termIRI);
				return termClass;  //TODO need to say something about this (using the property?)
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (pe.getIndividual() != null){
			IndividualBean ib = pe.getIndividual();
			IRI individualIRI;
			try {
				individualIRI = IRI.create(ib.checkIRIString(builder.getIRIManager()));
				OWLIndividual individualExpression = factory.getOWLNamedIndividual(individualIRI);
				return individualExpression;   //TODO need to say something about this (using the property?)
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			throw new IllegalStateException("Element " + pe.getId() + " is neither term nor individual");
		}
		return null;
	}

	OWLObject generateOWLForClass(Owlbuilder builder) {
		final OWLDataFactory factory = builder.getDataFactory();
		if (getSubstrate() != 0){
			if (getAnatomyIri() != null){
				processParticipantSubstrateForClass(builder,IRI.create(getSubstrateIri()));
			}
			else{
				final String msg = String.format("No substrate IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		OWLClass taxonClass = null;
		if (getTaxon() != 0){
			if (getTaxonIri() != null){
				taxonClass = processParticipantTaxonForClass(builder,IRI.create(getTaxonIri()));
			}
			else {
				final String msg = String.format("No taxon IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		if (getAnatomy() != 0){
			if (getAnatomyIri() != null){
				OWLClass anatomyClass = processParticipantAnatomyForClass(builder,IRI.create(getAnatomyIri()));
				if (taxonClass != null){
					OWLObjectProperty partOf = factory.getOWLObjectProperty(Vocabulary.partOfProperty);
					OWLClassExpression partOfSomeTaxon = factory.getOWLObjectSomeValuesFrom(partOf,
							taxonClass);
					OWLClassExpression anatomyOfTaxon =
							factory.getOWLObjectIntersectionOf(anatomyClass,partOfSomeTaxon);
					return anatomyOfTaxon;
				}
			}
			else{
				final String msg = String.format("No anatomy IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		return taxonClass;
	}

	/**
	 * This adds assertions for a participant when the participant is an
	 * individual.  An individual is most likely an anatomical part of an
	 * individual instance of a taxon, but it might be the entire individual 
	 * taxon instance or an instance of a substrate class.  
	 *  
	 */
	OWLObject generateOWLForIndividual(Owlbuilder builder) throws SQLException {
		IRIManager iriManager = builder.getIRIManager();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLIndividual ind = factory.getOWLNamedIndividual(IRI.create(getIriString()));
		log.info("individual is " + ind);
		//anatomy specified
		if (bean.getAnatomy() != 0)
			generateOWLforAnatomy(builder, ind);
		// No anatomy specified, just a taxon
		OWLOntology target = builder.getTarget();
		OWLOntologyManager manager = builder.getOntologyManager();
		if (bean.getTaxon() != 0){
			if (bean.getTaxonIri() != null){
				OWLClass taxon = processParticipantTaxonForIndividual(builder,IRI.create(bean.getTaxonIri()));
				OWLClassAssertionAxiom taxonAssertion = factory.getOWLClassAssertionAxiom(taxon,ind);
				manager.addAxiom(target, taxonAssertion);
				return ind;
			}
			else {
				final String msg = String.format("No taxon IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		if (bean.getSubstrate() != 0){
			if (bean.getAnatomyIri() != null){
				processParticipantSubstrateForIndividual(builder,IRI.create(bean.getSubstrateIri()));
			}
			else{
				final String msg = String.format("No substrate IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}

		return ind;
	}

	
	/**
	 * 
	 * @param builder
	 * @param iri
	 */
	void processParticipantSubstrateForIndividual(Owlbuilder builder, IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		boolean substrate_duplicate = target.containsClassInSignature(iri);
		if (!substrate_duplicate){
			boolean substrate_exists = merged.containsClassInSignature(iri);
			if (substrate_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass substrateClass = factory.getOWLClass(iri);
				processSubstrate(builder,substrateClass);
			}
		}
	}

	
	/**
	 * @param builder
	 * @param factory
	 * @param target
	 * @param manager
	 * @param partofProperty
	 * @param ind
	 */
	private void generateOWLforAnatomy(Owlbuilder builder, final OWLIndividual ind) {
		final OWLDataFactory factory = builder.getDataFactory();
		OWLOntology target = builder.getTarget();
		OWLOntologyManager manager = builder.getOntologyManager();
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(Vocabulary.partOfProperty);
		{
			if (bean.getAnatomyIri() != null){
				final OWLClass anatomyClass =processParticipantAnatomy(builder,IRI.create(bean.getAnatomyIri()));
				log.info("anatomy is " + anatomyClass);
				// and the taxon (anatomy w/o taxon should be flagged as a curation error
				if (bean.getTaxon() != 0){
					if (bean.getTaxonIri() != null){
						// This will require some more attention - curation should be able to
						// label the organisms because different parts of the same organism or
						// the same part will be mentioned multiple times - this is why arachb
						// uses individuals in the first place
						log.info("taxon is " + bean.getTaxonIri());
						OWLIndividual organism = factory.getOWLAnonymousIndividual();
						OWLClass taxon = processParticipantTaxon(builder,IRI.create(bean.getTaxonIri()));
						OWLClassAssertionAxiom taxonAssertion = factory.getOWLClassAssertionAxiom(taxon,organism);
						log.warn("assert " + organism + " is " + taxon);
						manager.addAxiom(target, taxonAssertion);
						OWLObjectPropertyAssertionAxiom partofAssertion = 
								factory.getOWLObjectPropertyAssertionAxiom(partofProperty, organism, ind);
						log.warn("assert " + organism + " part of " + ind);
						manager.addAxiom(target, partofAssertion);
                        OWLClassAssertionAxiom anatomyAssertion = factory.getOWLClassAssertionAxiom(anatomyClass, ind);
						log.warn("assert " + ind + " is " + anatomyClass);
                        manager.addAxiom(target, anatomyAssertion);
					}
					else {
						final String msg = String.format("No taxon IRI available; id = %s",getId());
						throw new IllegalStateException(msg);
					}				
				}
				else {
		             final String msg = String.format("No taxon specified; id = %s", getId());
		             throw new IllegalStateException(msg);
				}
			}
			else{
				final String msg = String.format("No anatomy IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
	}
	
	/**
	 * 
	 * @param builder
	 * @param iri
	 * @return
	 */
	OWLClass processParticipantTaxon(Owlbuilder builder,IRI iri){
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		OWLOntology target = builder.getTarget();
		boolean taxon_duplicate = target.containsClassInSignature(iri);
		if (!taxon_duplicate){
			if (merged.containsClassInSignature(iri))  // taxon in merged (so from NCBI)
				return processNCBITaxon(builder, iri);
			else
				return processNonNCBITaxon(builder, iri);
		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass;   // may not be right
		}
	}



	/**
	 * 
	 * @param builder
	 * @param iri
	 * @return class for anatomy
	 */
	OWLClass processParticipantAnatomy(Owlbuilder builder, IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		boolean anatomy_duplicate = target.containsClassInSignature(iri);
		if (!anatomy_duplicate){
			boolean anatomy_exists = merged.containsClassInSignature(iri);
			if (anatomy_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass anatomyClass = factory.getOWLClass(iri);
				processAnatomyForIndividual(builder,anatomyClass);
				return anatomyClass;
			}
			else{
				log.info("Did not find class in signature of merged ontology for: " + bean.getTaxonIri());
				return null;
			}
		}
		else{
			OWLClass anatomyClass = factory.getOWLClass(iri);
			return anatomyClass;   // may not be right
		}
	}


	OWLClass processParticipantTaxonForClass(Owlbuilder builder,IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getPreReasoner();
		boolean taxon_duplicate = target.containsClassInSignature(iri);
		if (!taxon_duplicate){
			boolean taxon_exists = merged.containsClassInSignature(iri);
			if (taxon_exists){
				log.info("Found class in signature of merged ontology for: " + getTaxonIri());
				OWLClass taxonClass = factory.getOWLClass(iri);
				final NodeSet<OWLClass> taxonParents = reasoner.getSuperClasses(taxonClass, false);
				log.info("Node count = " + taxonParents.getNodes().size());
				Set<OWLClass>parentList = taxonParents.getFlattened();
				log.info("Flattened parent count = " + parentList.size());
				parentList.add(taxonClass);
				for (OWLClass taxon : parentList){
					participantProcessTaxon(builder,taxon);
				}
				return taxonClass;
			}
			else{
				log.info("Did not find taxon class in signature of merged ontology for: " + getTaxonIri());
				final IRI taxonIri = IRI.create(getTaxonIri());
				final Map<IRI,Taxon> nonNCBITaxa = builder.getNonNCBITaxa();
				final OWLOntologyManager manager = builder.getOntologyManager();
				Taxon t = nonNCBITaxa.get(taxonIri);
				if (t == null){
					log.info("Taxon IRI not found in declared non-NCBI taxa");
					throw new IllegalStateException("Taxon IRI not found in declared non-NCBI taxa");
				}
				final OWLClass taxonClass = factory.getOWLClass(iri);
				if (t.getParentSourceId() != null){
					IRI parentIri = IRI.create(t.getParentSourceId());
					OWLClass parentClass = factory.getOWLClass(parentIri);
					log.info("Parent IRI is " + parentIri.toString());
					OWLAxiom sc_ax = factory.getOWLSubClassOfAxiom(taxonClass, parentClass);
					manager.addAxiom(target, sc_ax);
				}
				else{
					log.info("failed to find IRI of parent of " + getTaxonIri());
				}
				if (t.getName() != null){
					OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
							factory.getOWLLiteral(t.getName()));
					OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(iri, labelAnno);
					// Add the axiom to the ontology
					manager.addAxiom(target,ax);
				}
				return taxonClass;
			}
		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass; // may not be right
		}
	}

	
	void participantProcessTaxon(Owlbuilder builder,OWLClass taxon){
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){ //add appropriate when figured out
			log.info("Need to add taxon: " + taxon.getIRI());
			//log.info("Defining Axioms");
			manager.addAxioms(extracted, merged.getAxioms(taxon));
			//log.info("Annotations");
			Set<OWLAnnotationAssertionAxiom> taxonAnnotations = merged.getAnnotationAssertionAxioms(taxon.getIRI());
			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
				//log.info(" Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
	}



	/**
	 * 
	 * @param builder
	 * @param iri
	 * @return
	 */
	OWLClass processParticipantTaxonForIndividual(Owlbuilder builder,IRI iri){
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		OWLOntology target = builder.getTarget();
		boolean taxon_duplicate = target.containsClassInSignature(iri);
		if (!taxon_duplicate){
			if (merged.containsClassInSignature(iri))  // taxon in merged (so from NCBI)
				return processNCBITaxon(builder, iri);
			else
				return processNonNCBITaxon(builder, iri);
		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass;   // may not be right
		}
	}

	/**
	 * 
	 * @param builder
	 * @param iri
	 * @return
	 */
	private OWLClass processNCBITaxon(Owlbuilder builder, IRI iri) {
		log.info("Found class in signature of merged ontology for: " + iri);
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getPreReasoner();
		OWLClass taxonClass = factory.getOWLClass(iri);
		final NodeSet<OWLClass> taxonParents = reasoner.getSuperClasses(taxonClass, false);
		log.info("Node count = " + taxonParents.getNodes().size());
		Set<OWLClass>parentList =  taxonParents.getFlattened();
		log.info("Flattened parent count = " + parentList.size());
		parentList.add(taxonClass);
		for (OWLClass taxon : parentList){
			processTaxon(builder,taxon);
		}
		return taxonClass;
	}


	void processTaxon(Owlbuilder builder,OWLClass taxon){
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){  //add appropriate when figured out
			log.info("Need to add taxon: " + taxon.getIRI());
			//log.info("Defining Axioms");
			manager.addAxioms(extracted, merged.getAxioms(taxon));
			//log.info("Annotations");
			Set<OWLAnnotationAssertionAxiom> taxonAnnotations = merged.getAnnotationAssertionAxioms(taxon.getIRI());
			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
				//log.info("   Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
	}


	
	/**
	 * @param builder
	 * @param iri
	 * @return
	 */
	private OWLClass processNonNCBITaxon(Owlbuilder builder, IRI iri) {
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLOntology target = builder.getTarget();
		log.info("Did not find taxon class in signature of merged ontology for: " + bean.getTaxonIri());
		final IRI taxonIri = IRI.create(bean.getTaxonIri());
		final Map<IRI, Taxon> nonNCBITaxa = builder.getNonNCBITaxa();
		final OWLOntologyManager manager = builder.getOntologyManager();
		final Taxon t = nonNCBITaxa.get(taxonIri);
		if (t == null){
			log.info("Taxon IRI not found in declared non-NCBI taxa");
			throw new IllegalStateException("Taxon IRI not found in declared non-NCBI taxa");
		}
		final OWLClass taxonClass = factory.getOWLClass(iri);
		if (t.getParentSourceId() != null){
			IRI parentIri = IRI.create(t.getParentSourceId());
			OWLClass parentClass = factory.getOWLClass(parentIri);
			log.info("Parent IRI is " + parentIri.toString());
			OWLAxiom sc_ax = factory.getOWLSubClassOfAxiom(taxonClass, parentClass);
			manager.addAxiom(target, sc_ax);
		}
		else{
			log.info("failed to find IRI of parent of " + bean.getTaxonIri());
		}
		if (t.getName() != null){
			OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
					factory.getOWLLiteral(t.getName()));
			OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(iri, labelAnno);
			// Add the axiom to the ontology
			manager.addAxiom(target,ax);
		}
		return taxonClass;

	}

	OWLClass processParticipantAnatomyForClass(Owlbuilder builder, IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		boolean anatomy_duplicate = target.containsClassInSignature(iri);
		if (!anatomy_duplicate){
			boolean anatomy_exists = merged.containsClassInSignature(iri);
			if (anatomy_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass anatomyClass = factory.getOWLClass(iri);
				participantProcessAnatomy(builder,anatomyClass);
				return anatomyClass;
			}
			else{
				log.info("Did not find class in signature of merged ontology for: " + getAnatomyIri());
				return null;
			}
		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass; // may not be right
		}
	}
	
	public void participantProcessAnatomy(Owlbuilder builder, OWLClass anatomyClass) {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){
			log.info("Need to add anatomy: " + anatomyClass.getIRI());
			Set<OWLClassAxiom> anatAxioms = merged.getAxioms(anatomyClass);
			manager.addAxioms(extracted, anatAxioms);
			Set<OWLAnnotationAssertionAxiom> anatAnnotations =
					merged.getAnnotationAssertionAxioms(anatomyClass.getIRI());
			for (OWLAnnotationAssertionAxiom a : anatAnnotations){
				//log.info(" Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
		builder.initializeMiscTermAndParents(anatomyClass);

	}



	public void processAnatomyForIndividual(Owlbuilder builder, OWLClass anatomyClass) {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){
			log.info("Need to add anatomy: " + anatomyClass.getIRI());
			Set<OWLClassAxiom> anatAxioms = merged.getAxioms(anatomyClass);
			manager.addAxioms(extracted, anatAxioms);
			Set<OWLAnnotationAssertionAxiom> anatAnnotations = 
					merged.getAnnotationAssertionAxioms(anatomyClass.getIRI());
			for (OWLAnnotationAssertionAxiom a : anatAnnotations){
				//log.info("   Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
		builder.initializeMiscTermAndParents(anatomyClass);

	}


	void processParticipantSubstrateForClass(Owlbuilder builder, IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		boolean substrate_duplicate = target.containsClassInSignature(iri);
		if (!substrate_duplicate){
			boolean substrate_exists = merged.containsClassInSignature(iri);
			if (substrate_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass substrateClass = factory.getOWLClass(iri);
				processSubstrate(builder,substrateClass);
			}
		}

	}

	public void processSubstrate(Owlbuilder builder, OWLClass substrateClass) {
		builder.initializeMiscTermAndParents(substrateClass);	
	}	


	
	public int getId(){
		return bean.getId();
	}

	public int getTaxon(){
		return bean.getTaxon();
	}

	public int getSubstrate(){
		return bean.getSubstrate();
	}

	public int getAnatomy(){
		return bean.getAnatomy();
	}

	public String getQuantification(){
		return bean.getQuantification();
	}

	public String getPublicationTaxon(){
		return bean.getPublicationTaxon();
	}

	public String getLabel(){
		return bean.getLabel();
	}

	public String getPublicationAnatomy(){
		return bean.getPublicationAnatomy();
	}

	public String getPublicationSubstrate(){
		return bean.getPublicationSubstrate();
	}

	public String getTaxonIri(){
		return bean.getTaxonIri();
	}

	public String getSubstrateIri(){
		return bean.getSubstrateIri();
	}

	public String getAnatomyIri(){
		return bean.getAnatomyIri();
	}

	
	public String getGeneratedId(){
		return bean.getGeneratedId();
	}

	public void setGeneratedId(String s){
		bean.setGeneratedId(s);
	}

	public String getIriString(){
		if (getGeneratedId() == null){
			throw new IllegalStateException("Individual has neither assigned nor generated id");
		}
		return getGeneratedId();
	}

	
	public void loadElements(AbstractConnection c) throws Exception{
		bean.loadElements(c);
	}
	
	
	public void resolveElements(AbstractConnection c) throws Exception{
		bean.resolveElements(c);
	}


}
