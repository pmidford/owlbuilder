package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class IndividualParticipant extends Participant implements AbstractNamedEntity{
	
	private final static Logger log = Logger.getLogger(IndividualParticipant.class);
		
	
	@Override
	/**
	 * This adds assertions for a participant when the participant is an
	 * individual.  An individual is most likely an anatomical part of an
	 * individual instance of a taxon, but it might be the entire individual 
	 * taxon instance or an instance of a substrate class.  
	 *  
	 */
	public OWLObject generateOWL(Owlbuilder builder) throws SQLException {
		IRIManager iriManager = builder.getIRIManager();
		iriManager.validateIRI(this);
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLIndividual ind = factory.getOWLNamedIndividual(IRI.create(getIriString()));
		log.info("individual is " + ind);
		//anatomy specified
		if (getAnatomy() != 0)
			generateOWLforAnatomy(builder, ind);
		// No anatomy specified, just a taxon
		OWLOntology target = builder.getTarget();
		OWLOntologyManager manager = builder.getOntologyManager();
		if (getTaxon() != 0){
			if (getTaxonIri() != null){
				OWLClass taxon = processParticipantTaxon(builder,IRI.create(getTaxonIri()));
		        OWLClassAssertionAxiom taxonAssertion = factory.getOWLClassAssertionAxiom(taxon,ind);
	        	manager.addAxiom(target, taxonAssertion);
	        	return ind;
			}
			else {
				final String msg = String.format("No taxon IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		if (getSubstrate() != 0){
			if (getAnatomyIri() != null){
				processParticipantSubstrate(builder,IRI.create(getSubstrateIri()));
			}
			else{
				final String msg = String.format("No substrate IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}

		return ind;
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
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		{
			if (getAnatomyIri() != null){
				final OWLClass anatomyClass =processParticipantAnatomy(builder,IRI.create(getAnatomyIri()));
				log.info("anatomy is " + anatomyClass);
				// and the taxon (anatomy w/o taxon should be flagged as a curation error
				if (getTaxon() != 0){
					if (getTaxonIri() != null){
						// This will require some more attention - curation should be able to
						// label the organisms because different parts of the same organism or
						// the same part will be mentioned multiple times - this is why arachb
						// uses individuals in the first place
						log.info("taxon is " + getTaxonIri());
						OWLIndividual organism = factory.getOWLAnonymousIndividual();
						OWLClass taxon = processParticipantTaxon(builder,IRI.create(getTaxonIri()));
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
			super.processTaxon(builder,taxon);
		}
		return taxonClass;
	}


	/**
	 * @param builder
	 * @param iri
	 * @return
	 */
	private OWLClass processNonNCBITaxon(Owlbuilder builder, IRI iri) {
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLOntology target = builder.getTarget();
		log.info("Did not find taxon class in signature of merged ontology for: " + getTaxonIri());
		final IRI taxonIri = IRI.create(getTaxonIri());
		final Map<IRI,Taxon> nonNCBITaxa = builder.getNonNCBITaxa();
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
				super.processAnatomy(builder,anatomyClass);
				return anatomyClass;
			}
			else{
				log.info("Did not find class in signature of merged ontology for: " + getTaxonIri());
				return null;
			}
		}
		else{
			OWLClass anatomyClass = factory.getOWLClass(iri);
			return anatomyClass;   // may not be right
		}
	}

	/**
	 * 
	 * @param builder
	 * @param iri
	 */
	void processParticipantSubstrate(Owlbuilder builder, IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		boolean substrate_duplicate = target.containsClassInSignature(iri);
		if (!substrate_duplicate){
			boolean substrate_exists = merged.containsClassInSignature(iri);
			if (substrate_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass substrateClass = factory.getOWLClass(iri);
				super.processSubstrate(builder,substrateClass);
			}
		}
	}
	

	@Override
	public String getIriString(){
		if (getGeneratedId() == null){
			throw new IllegalStateException("Individual has neither assigned nor generated id");
		}
		return getGeneratedId();
	}
	
	@Override
	public String checkIriString() {
		return getGeneratedId();
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException{
		c.updateIndividualParticipant(this);
	}

}
