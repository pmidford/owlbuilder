package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class QuantifiedParticipant extends Participant {

	private final static Logger log = Logger.getLogger(QuantifiedParticipant.class);


	@Override
	public OWLObject generateOWL(Owlbuilder builder) {
		final OWLDataFactory factory = builder.getDataFactory();
		if (getSubstrate() != 0){
			if (getAnatomyIri() != null){
				processParticipantSubstrate(builder,IRI.create(getSubstrateIri()));
			}
			else{
				final String msg = String.format("No substrate IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		OWLClass taxonClass = null;
		if (getTaxon() != 0){
			if (getTaxonIri() != null){
				taxonClass = processParticipantTaxon(builder,IRI.create(getTaxonIri()));
			}
			else {
				final String msg = String.format("No taxon IRI available; id = %s",getId());
				throw new IllegalStateException(msg);
			}
		}
		if (getAnatomy() != 0){
			if (getAnatomyIri() != null){
				OWLClass anatomyClass = processParticipantAnatomy(builder,IRI.create(getAnatomyIri()));
				if (taxonClass != null){
					  OWLObjectProperty partOf = factory.getOWLObjectProperty(IRIManager.partOfProperty);
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
	
	OWLClass processParticipantTaxon(Owlbuilder builder,IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getReasoner();
		boolean taxon_duplicate = target.containsClassInSignature(iri);
		if (!taxon_duplicate){
			boolean taxon_exists = merged.containsClassInSignature(iri);
			if (taxon_exists){
				log.info("Found class in signature of merged ontology for: " + getTaxonIri());
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
			else{
				log.info("Did not find class in signature of merged ontology for: " + getTaxonIri());
				return null;
			}
		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass;   // may not be right
		}
	}
	
	
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
				log.info("Did not find class in signature of merged ontology for: " + getAnatomyIri());
				return null;
			}

		}
		else{
			OWLClass taxonClass = factory.getOWLClass(iri);
			return taxonClass;   // may not be right
		}

	}

	
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
	
	
}
