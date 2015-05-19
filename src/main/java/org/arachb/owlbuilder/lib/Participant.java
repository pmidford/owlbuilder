package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.PElementBean;
import org.arachb.arachadmin.ParticipantBean;
import org.arachb.arachadmin.PropertyBean;
import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

public class Participant implements GeneratingEntity{



	final static String BADTAXQuantifiedParticipant =
			"Term without IRI referenced as participant taxon: participant QuantifiedParticipantxon id = %s";
	final static String BADANATOMYIRI =
			"Term without IRQuantifiedParticipantd as participant anatomy: participant id = %s; anatomy id = %s";
	final static String BADSUBSTRATEIRI =
			"Term without IRI referenced as participant substrate; participant id = %s; substrate id = %s";




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
		final Map<String, OWLObject> owlElements = new HashMap<String, OWLObject>();
		PElementBean headBean = bean.getElementBean(bean.getHeadElement());
		PropertyBean propBean= bean.getParticipationBean();
		OWLObject headObject = generateElementOWL(headBean,builder, owlElements);
		Set <Integer>children = headBean.getChildren();
		switch (children.size()){
		case 0:
			return generateNoDependentOWL(builder, propBean, headObject);
		case 1:
			Integer childIndex = children.iterator().next();
			PElementBean childBean = bean.getElementBean(childIndex);
			OWLObject childObject = generateRestrictionClass(builder, owlElements, childBean);
			return generateDependentOWL(builder, propBean, headObject, childObject);
		default:
			throw new RuntimeException("Didn't expect " + children.size() + " children");
		}
	}

	/**
	 * @param factory
	 * @param propBean
	 * @param headObject
	 * @param childObject
	 * @return
	 */
	private OWLObject generateDependentOWL(Owlbuilder builder,
			PropertyBean propBean, OWLObject headObject, OWLObject childObject) {
		final OWLDataFactory factory = builder.getDataFactory();
		IRI propertyIRI = IRI.create(propBean.getSourceId());
		OWLObjectProperty elementProperty = factory.getOWLObjectProperty(propertyIRI);
		if (headObject instanceof OWLClassExpression){
			final OWLClassExpression headClass = (OWLClassExpression)headObject;
			if (childObject != null){
				if (childObject instanceof OWLClassExpression){
					final OWLClassExpression childClass = (OWLClassExpression)childObject;
					OWLClassExpression intersect = 
							factory.getOWLObjectIntersectionOf(headClass,childClass);
					OWLClassExpression propertyRestriction = 
							factory.getOWLObjectSomeValuesFrom(elementProperty,intersect);
					log.info("Generated Property restriction(2): " + propertyRestriction);					
					return propertyRestriction;
				}
				else if (childObject instanceof OWLIndividual){
					log.info("Individual child of class");
				}
				else {
					throw new RuntimeException("child is neither a class expression or individual: " + childObject);
				}
			}
			OWLClassExpression propertyRestriction = 
					factory.getOWLObjectSomeValuesFrom(elementProperty,headClass);
			log.info("Generated Property restriction: " + propertyRestriction);
			return propertyRestriction;
		}
		else if (headObject instanceof OWLIndividual){
			log.info("Generated Individual reference: " + headObject);
			final OWLIndividual headIndividual = (OWLIndividual)headObject;
			if (childObject != null){
				if (childObject instanceof OWLIndividual){
					OWLIndividual childIndividual = (OWLIndividual)childObject;
			        OWLObjectPropertyAssertionAxiom assertion = 
			        		factory.getOWLObjectPropertyAssertionAxiom(elementProperty, headIndividual, childIndividual);
			        // Finally, add the axiom to our ontology and save
			        AddAxiom addAxiomChange = new AddAxiom(builder.getTarget(), assertion);
			        builder.getOntologyManager().applyChange(addAxiomChange);
				}
				else {  //child is class expression?
					log.info("class child of individual");
				}
				
			}
			return headObject; //TODO finish implementing individual case
		}
		else {
			throw new RuntimeException("Bad head object in participant: " + headObject);
		}
	}

	
	/**
	 * @param builder
	 * @param propBean
	 * @param headObject
	 * @return
	 */
	private OWLObject generateNoDependentOWL(Owlbuilder builder,
			PropertyBean propBean, OWLObject headObject) {
		final OWLDataFactory factory = builder.getDataFactory();
		IRI propertyIRI = IRI.create(propBean.getSourceId());
		OWLObjectProperty elementProperty = factory.getOWLObjectProperty(propertyIRI);
		if (headObject instanceof OWLClassExpression){
			final OWLClassExpression headClass = (OWLClassExpression)headObject;
			OWLClassExpression propertyRestriction = 
					factory.getOWLObjectSomeValuesFrom(elementProperty,headClass); 
			log.info("Generated Property restriction: " + propertyRestriction);
			return propertyRestriction;
		}
		else if (headObject instanceof OWLIndividual){
			log.info("Generated Individual reference: " + headObject);
			return headObject; //TODO finish implementing individual case  
		}
		else {
			throw new RuntimeException("Bad head object in participant: " + headObject);
		}
	}

	
	/**
	 * @param builder
	 * @param owlElements
	 * @param factory
	 * @param childBean
	 * @return
	 */
	private OWLObject generateRestrictionClass(Owlbuilder builder,
											   final Map<String, OWLObject> owlElements,
											   PElementBean peb) {
		final OWLDataFactory factory = builder.getDataFactory();
		Integer parentIndex = peb.getSingletonParent();
		PropertyBean childProp = peb.getParentProperty(parentIndex);
		IRI childPropIRI = IRI.create(childProp.getSourceId());
		OWLObjectProperty childProperty = factory.getOWLObjectProperty(childPropIRI);
		OWLObject childElement = generateElementOWL(peb,builder, owlElements);
		Set <Integer>children = peb.getChildren();
		if (children.size() >0){
			log.info("children size: " + children.size());
		}
		if (childElement instanceof OWLClassExpression){
			final OWLClassExpression childClass = (OWLClassExpression)childElement;
			OWLClassExpression childPropertyRestriction = 
					factory.getOWLObjectSomeValuesFrom(childProperty, childClass);
			log.info("Generated (child) Property restriction: " + childPropertyRestriction);
			return childPropertyRestriction;
		}
		else if (childElement instanceof OWLIndividual){
			return childElement;
		}
		else {
			throw new RuntimeException("Bad child element: " + childElement);
		}
	}
	
	
	final static String GENERATEOWLBADCHILD = 
			"Child passed to generateElementOWL is neither ClassExpression or Individual";
	
	public OWLObject generateElementOWL(PElementBean pe, Owlbuilder builder, Map<String, OWLObject> elements){
		if (pe.getTerm() != null){
			return generateTermOWL(pe.getTerm(), builder, elements);
		}
		else if (pe.getIndividual() != null){
			return generateIndividualOWL(pe.getIndividual(), builder, elements);
		}
		else {
			throw new RuntimeException(GENERATEOWLBADCHILD + pe);
		}
	}
		
		
	public OWLClassExpression generateTermOWL(TermBean tb, Owlbuilder builder, Map<String, OWLObject> elements){
		final OWLDataFactory factory = builder.getDataFactory();
		IRI termIRI;
		try {
			String termString = tb.checkIRIString(builder.getIRIManager());
			if (elements.containsKey(termString)){
				return (OWLClassExpression)elements.get(termString);
			}
			termIRI = IRI.create(termString);
			log.info("Creating OWL class: " + termIRI);
			OWLClass termClass = factory.getOWLClass(termIRI);
			builder.initializeMiscTermAndParents(termClass);
			elements.put(termString, termClass);
			return termClass;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	public OWLIndividual generateIndividualOWL(IndividualBean ib, Owlbuilder builder, Map<String, OWLObject> elements){
		final OWLDataFactory factory = builder.getDataFactory();
		IRI individualIRI;
		try {
			String indString = ib.checkIRIString(builder.getIRIManager());
			if (elements.containsKey(indString)){
				return (OWLIndividual)elements.get(indString);
			}
			individualIRI = IRI.create(indString);
			OWLIndividual namedIndividual = factory.getOWLNamedIndividual(individualIRI);
			int termId = ib.getTerm();
			final String label = ib.getLabel();
			if (label != null){
				OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
																   factory.getOWLLiteral(label));
				OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(individualIRI, labelAnno);
				// Add the axiom to the ontology
				builder.getOntologyManager().addAxiom(builder.getTarget(),ax);
			}
			builder.initializeMiscIndividual(namedIndividual);
			elements.put(indString, namedIndividual);
			return namedIndividual;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
//	/**
//	 * 
//	 * @param builder
//	 * @param iri
//	 */
//	void processParticipantSubstrateForIndividual(Owlbuilder builder, IRI iri){
//		final OWLOntology target = builder.getTarget();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		boolean substrate_duplicate = target.containsClassInSignature(iri);
//		if (!substrate_duplicate){
//			boolean substrate_exists = merged.containsClassInSignature(iri);
//			if (substrate_exists){
//				log.info("Found class in signature of merged ontology for: " + iri);
//				OWLClass substrateClass = factory.getOWLClass(iri);
//				processSubstrate(builder,substrateClass);
//			}
//		}
//	}
//
//	
//	/**
//	 * @param builder
//	 * @param factory
//	 * @param target
//	 * @param manager
//	 * @param partofProperty
//	 * @param ind
//	 */
//	private void generateOWLforAnatomy(Owlbuilder builder, final OWLIndividual ind) {
//		final OWLDataFactory factory = builder.getDataFactory();
//		OWLOntology target = builder.getTarget();
//		OWLOntologyManager manager = builder.getOntologyManager();
//		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(Vocabulary.partOfProperty);
//		{
//			if (bean.getAnatomyIri() != null){
//				final OWLClass anatomyClass =processParticipantAnatomy(builder,IRI.create(bean.getAnatomyIri()));
//				log.info("anatomy is " + anatomyClass);
//				// and the taxon (anatomy w/o taxon should be flagged as a curation error
//				if (bean.getTaxon() != 0){
//					if (bean.getTaxonIri() != null){
//						// This will require some more attention - curation should be able to
//						// label the organisms because different parts of the same organism or
//						// the same part will be mentioned multiple times - this is why arachb
//						// uses individuals in the first place
//						log.info("taxon is " + bean.getTaxonIri());
//						OWLIndividual organism = factory.getOWLAnonymousIndividual();
//						OWLClass taxon = processParticipantTaxon(builder,IRI.create(bean.getTaxonIri()));
//						OWLClassAssertionAxiom taxonAssertion = factory.getOWLClassAssertionAxiom(taxon,organism);
//						log.warn("assert " + organism + " is " + taxon);
//						manager.addAxiom(target, taxonAssertion);
//						OWLObjectPropertyAssertionAxiom partofAssertion = 
//								factory.getOWLObjectPropertyAssertionAxiom(partofProperty, organism, ind);
//						log.warn("assert " + organism + " part of " + ind);
//						manager.addAxiom(target, partofAssertion);
//                        OWLClassAssertionAxiom anatomyAssertion = factory.getOWLClassAssertionAxiom(anatomyClass, ind);
//						log.warn("assert " + ind + " is " + anatomyClass);
//                        manager.addAxiom(target, anatomyAssertion);
//					}
//					else {
//						final String msg = String.format("No taxon IRI available; id = %s",getId());
//						throw new IllegalStateException(msg);
//					}				
//				}
//				else {
//		             final String msg = String.format("No taxon specified; id = %s", getId());
//		             throw new IllegalStateException(msg);
//				}
//			}
//			else{
//				final String msg = String.format("No anatomy IRI available; id = %s",getId());
//				throw new IllegalStateException(msg);
//			}
//		}
//	}
//	
//	/**
//	 * 
//	 * @param builder
//	 * @param iri
//	 * @return
//	 */
//	OWLClass processParticipantTaxon(Owlbuilder builder,IRI iri){
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		OWLOntology target = builder.getTarget();
//		boolean taxon_duplicate = target.containsClassInSignature(iri);
//		if (!taxon_duplicate){
//			if (merged.containsClassInSignature(iri))  // taxon in merged (so from NCBI)
//				return processNCBITaxon(builder, iri);
//			else
//				return processNonNCBITaxon(builder, iri);
//		}
//		else{
//			OWLClass taxonClass = factory.getOWLClass(iri);
//			return taxonClass;   // may not be right
//		}
//	}
//
//
//
//	/**
//	 * 
//	 * @param builder
//	 * @param iri
//	 * @return class for anatomy
//	 */
//	OWLClass processParticipantAnatomy(Owlbuilder builder, IRI iri){
//		final OWLOntology target = builder.getTarget();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		boolean anatomy_duplicate = target.containsClassInSignature(iri);
//		if (!anatomy_duplicate){
//			boolean anatomy_exists = merged.containsClassInSignature(iri);
//			if (anatomy_exists){
//				log.info("Found class in signature of merged ontology for: " + iri);
//				OWLClass anatomyClass = factory.getOWLClass(iri);
//				processAnatomyForIndividual(builder,anatomyClass);
//				return anatomyClass;
//			}
//			else{
//				log.info("Did not find class in signature of merged ontology for: " + bean.getTaxonIri());
//				return null;
//			}
//		}
//		else{
//			OWLClass anatomyClass = factory.getOWLClass(iri);
//			return anatomyClass;   // may not be right
//		}
//	}
//
//
//	OWLClass processParticipantTaxonForClass(Owlbuilder builder,IRI iri){
//		final OWLOntology target = builder.getTarget();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		final OWLReasoner reasoner = builder.getPreReasoner();
//		boolean taxon_duplicate = target.containsClassInSignature(iri);
//		if (!taxon_duplicate){
//			boolean taxon_exists = merged.containsClassInSignature(iri);
//			if (taxon_exists){
//				log.info("Found class in signature of merged ontology for: " + getTaxonIri());
//				OWLClass taxonClass = factory.getOWLClass(iri);
//				final NodeSet<OWLClass> taxonParents = reasoner.getSuperClasses(taxonClass, false);
//				log.info("Node count = " + taxonParents.getNodes().size());
//				Set<OWLClass>parentList = taxonParents.getFlattened();
//				log.info("Flattened parent count = " + parentList.size());
//				parentList.add(taxonClass);
//				for (OWLClass taxon : parentList){
//					participantProcessTaxon(builder,taxon);
//				}
//				return taxonClass;
//			}
//			else{
//				log.info("Did not find taxon class in signature of merged ontology for: " + getTaxonIri());
//				final IRI taxonIri = IRI.create(getTaxonIri());
//				final Map<IRI,Taxon> nonNCBITaxa = builder.getNonNCBITaxa();
//				final OWLOntologyManager manager = builder.getOntologyManager();
//				Taxon t = nonNCBITaxa.get(taxonIri);
//				if (t == null){
//					log.info("Taxon IRI not found in declared non-NCBI taxa");
//					throw new IllegalStateException("Taxon IRI not found in declared non-NCBI taxa");
//				}
//				final OWLClass taxonClass = factory.getOWLClass(iri);
//				if (t.getParentSourceId() != null){
//					IRI parentIri = IRI.create(t.getParentSourceId());
//					OWLClass parentClass = factory.getOWLClass(parentIri);
//					log.info("Parent IRI is " + parentIri.toString());
//					OWLAxiom sc_ax = factory.getOWLSubClassOfAxiom(taxonClass, parentClass);
//					manager.addAxiom(target, sc_ax);
//				}
//				else{
//					log.info("failed to find IRI of parent of " + getTaxonIri());
//				}
//				if (t.getName() != null){
//					OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
//							factory.getOWLLiteral(t.getName()));
//					OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(iri, labelAnno);
//					// Add the axiom to the ontology
//					manager.addAxiom(target,ax);
//				}
//				return taxonClass;
//			}
//		}
//		else{
//			OWLClass taxonClass = factory.getOWLClass(iri);
//			return taxonClass; // may not be right
//		}
//	}
//
//	
//	void participantProcessTaxon(Owlbuilder builder,OWLClass taxon){
//		final OWLOntologyManager manager = builder.getOntologyManager();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLOntology extracted = builder.getTarget();
//		if (true){ //add appropriate when figured out
//			log.info("Need to add taxon: " + taxon.getIRI());
//			//log.info("Defining Axioms");
//			manager.addAxioms(extracted, merged.getAxioms(taxon));
//			//log.info("Annotations");
//			Set<OWLAnnotationAssertionAxiom> taxonAnnotations = merged.getAnnotationAssertionAxioms(taxon.getIRI());
//			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
//				//log.info(" Annotation Axiom: " + a.toString());
//				if (a.getAnnotation().getProperty().isLabel()){
//					log.info("Label is " + a.getAnnotation().getValue().toString());
//					manager.addAxiom(extracted, a);
//				}
//			}
//		}
//	}
//
//
//
//	/**
//	 * 
//	 * @param builder
//	 * @param iri
//	 * @return
//	 */
//	OWLClass processParticipantTaxonForIndividual(Owlbuilder builder,IRI iri){
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		OWLOntology target = builder.getTarget();
//		boolean taxon_duplicate = target.containsClassInSignature(iri);
//		if (!taxon_duplicate){
//			if (merged.containsClassInSignature(iri))  // taxon in merged (so from NCBI)
//				return processNCBITaxon(builder, iri);
//			else
//				return processNonNCBITaxon(builder, iri);
//		}
//		else{
//			OWLClass taxonClass = factory.getOWLClass(iri);
//			return taxonClass;   // may not be right
//		}
//	}
//
//	/**
//	 * 
//	 * @param builder
//	 * @param iri
//	 * @return
//	 */
//	private OWLClass processNCBITaxon(Owlbuilder builder, IRI iri) {
//		log.info("Found class in signature of merged ontology for: " + iri);
//		final OWLDataFactory factory = builder.getDataFactory();
//		final OWLReasoner reasoner = builder.getPreReasoner();
//		OWLClass taxonClass = factory.getOWLClass(iri);
//		final NodeSet<OWLClass> taxonParents = reasoner.getSuperClasses(taxonClass, false);
//		log.info("Node count = " + taxonParents.getNodes().size());
//		Set<OWLClass>parentList =  taxonParents.getFlattened();
//		log.info("Flattened parent count = " + parentList.size());
//		parentList.add(taxonClass);
//		for (OWLClass taxon : parentList){
//			processTaxon(builder,taxon);
//		}
//		return taxonClass;
//	}
//
//
//	void processTaxon(Owlbuilder builder,OWLClass taxon){
//		final OWLOntologyManager manager = builder.getOntologyManager();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLOntology extracted = builder.getTarget();
//		if (true){  //add appropriate when figured out
//			log.info("Need to add taxon: " + taxon.getIRI());
//			//log.info("Defining Axioms");
//			manager.addAxioms(extracted, merged.getAxioms(taxon));
//			//log.info("Annotations");
//			Set<OWLAnnotationAssertionAxiom> taxonAnnotations = merged.getAnnotationAssertionAxioms(taxon.getIRI());
//			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
//				//log.info("   Annotation Axiom: " + a.toString());
//				if (a.getAnnotation().getProperty().isLabel()){
//					log.info("Label is " + a.getAnnotation().getValue().toString());
//					manager.addAxiom(extracted, a);
//				}
//			}
//		}
//	}
//
//
//	
//	/**
//	 * @param builder
//	 * @param iri
//	 * @return
//	 */
//	private OWLClass processNonNCBITaxon(Owlbuilder builder, IRI iri) {
//		final OWLDataFactory factory = builder.getDataFactory();
//		final OWLOntology target = builder.getTarget();
//		log.info("Did not find taxon class in signature of merged ontology for: " + bean.getTaxonIri());
//		final IRI taxonIri = IRI.create(bean.getTaxonIri());
//		final Map<IRI, Taxon> nonNCBITaxa = builder.getNonNCBITaxa();
//		final OWLOntologyManager manager = builder.getOntologyManager();
//		final Taxon t = nonNCBITaxa.get(taxonIri);
//		if (t == null){
//			log.info("Taxon IRI not found in declared non-NCBI taxa");
//			throw new IllegalStateException("Taxon IRI not found in declared non-NCBI taxa");
//		}
//		final OWLClass taxonClass = factory.getOWLClass(iri);
//		if (t.getParentSourceId() != null){
//			IRI parentIri = IRI.create(t.getParentSourceId());
//			OWLClass parentClass = factory.getOWLClass(parentIri);
//			log.info("Parent IRI is " + parentIri.toString());
//			OWLAxiom sc_ax = factory.getOWLSubClassOfAxiom(taxonClass, parentClass);
//			manager.addAxiom(target, sc_ax);
//		}
//		else{
//			log.info("failed to find IRI of parent of " + bean.getTaxonIri());
//		}
//		if (t.getName() != null){
//			OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
//					factory.getOWLLiteral(t.getName()));
//			OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(iri, labelAnno);
//			// Add the axiom to the ontology
//			manager.addAxiom(target,ax);
//		}
//		return taxonClass;
//
//	}
//
//	OWLClass processParticipantAnatomyForClass(Owlbuilder builder, IRI iri){
//		final OWLOntology target = builder.getTarget();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		boolean anatomy_duplicate = target.containsClassInSignature(iri);
//		if (!anatomy_duplicate){
//			boolean anatomy_exists = merged.containsClassInSignature(iri);
//			if (anatomy_exists){
//				log.info("Found class in signature of merged ontology for: " + iri);
//				OWLClass anatomyClass = factory.getOWLClass(iri);
//				participantProcessAnatomy(builder,anatomyClass);
//				return anatomyClass;
//			}
//			else{
//				log.info("Did not find class in signature of merged ontology for: " + getAnatomyIri());
//				return null;
//			}
//		}
//		else{
//			OWLClass taxonClass = factory.getOWLClass(iri);
//			return taxonClass; // may not be right
//		}
//	}
//	
//	public void participantProcessAnatomy(Owlbuilder builder, OWLClass anatomyClass) {
//		final OWLOntologyManager manager = builder.getOntologyManager();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLOntology extracted = builder.getTarget();
//		if (true){
//			log.info("Need to add anatomy: " + anatomyClass.getIRI());
//			Set<OWLClassAxiom> anatAxioms = merged.getAxioms(anatomyClass);
//			manager.addAxioms(extracted, anatAxioms);
//			Set<OWLAnnotationAssertionAxiom> anatAnnotations =
//					merged.getAnnotationAssertionAxioms(anatomyClass.getIRI());
//			for (OWLAnnotationAssertionAxiom a : anatAnnotations){
//				//log.info(" Annotation Axiom: " + a.toString());
//				if (a.getAnnotation().getProperty().isLabel()){
//					log.info("Label is " + a.getAnnotation().getValue().toString());
//					manager.addAxiom(extracted, a);
//				}
//			}
//		}
//		builder.initializeMiscTermAndParents(anatomyClass);
//
//	}
//
//
//
//	public void processAnatomyForIndividual(Owlbuilder builder, OWLClass anatomyClass) {
//		final OWLOntologyManager manager = builder.getOntologyManager();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLOntology extracted = builder.getTarget();
//		if (true){
//			log.info("Need to add anatomy: " + anatomyClass.getIRI());
//			Set<OWLClassAxiom> anatAxioms = merged.getAxioms(anatomyClass);
//			manager.addAxioms(extracted, anatAxioms);
//			Set<OWLAnnotationAssertionAxiom> anatAnnotations = 
//					merged.getAnnotationAssertionAxioms(anatomyClass.getIRI());
//			for (OWLAnnotationAssertionAxiom a : anatAnnotations){
//				//log.info("   Annotation Axiom: " + a.toString());
//				if (a.getAnnotation().getProperty().isLabel()){
//					log.info("Label is " + a.getAnnotation().getValue().toString());
//					manager.addAxiom(extracted, a);
//				}
//			}
//		}
//		builder.initializeMiscTermAndParents(anatomyClass);
//
//	}
//
//
//	void processParticipantSubstrateForClass(Owlbuilder builder, IRI iri){
//		final OWLOntology target = builder.getTarget();
//		final OWLOntology merged = builder.getMergedSources();
//		final OWLDataFactory factory = builder.getDataFactory();
//		boolean substrate_duplicate = target.containsClassInSignature(iri);
//		if (!substrate_duplicate){
//			boolean substrate_exists = merged.containsClassInSignature(iri);
//			if (substrate_exists){
//				log.info("Found class in signature of merged ontology for: " + iri);
//				OWLClass substrateClass = factory.getOWLClass(iri);
//				processSubstrate(builder,substrateClass);
//			}
//		}
//
//	}
//
//	public void processSubstrate(Owlbuilder builder, OWLClass substrateClass) {
//		builder.initializeMiscTermAndParents(substrateClass);	
//	}	
//

	
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
