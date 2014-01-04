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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class QuantifiedParticipant extends Participant {

	private final static Logger log = Logger.getLogger(QuantifiedParticipant.class);

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		taxon = record.getInt(DBTAXON);
		substrate = record.getInt(DBSUBSTRATE);
		anatomy = record.getInt(DBANATOMY);
		quantification = record.getString(DBQUANTIFICATION);
		label = record.getString(DBLABEL);
		generated_id = record.getString(DBGENERATEDID);
		publication_taxon = record.getString(DBPUBLICATIONTAXON);
		publication_anatomy = record.getString(DBPUBLICATIONANATOMY);
		publication_substrate = record.getString(DBPUBLICATIONSUBSTRATE);
		if (taxon != 0){
			if (record.getString(DBTAXONSOURCEID) != null){
				this.set_taxonIRI(record.getString(DBTAXONSOURCEID));
			}
			else if (record.getString(DBTAXONGENERATEDID) != null){
				this.set_taxonIRI(record.getString(DBTAXONGENERATEDID));
			}
			else{
				final String msg = String.format(BADTAXONIRI, id, taxon);
				throw new IllegalStateException(msg);
			}
		}
		if (anatomy != 0){
			if (record.getString(DBANATOMYSOURCEID) != null){
				this.set_anatomyIRI(record.getString(DBANATOMYSOURCEID));
			}
			else if (record.getString(DBANATOMYGENERATEDID) != null){
				this.set_anatomyIRI(record.getString(DBANATOMYGENERATEDID));
			}
			else{
				final String msg = String.format(BADANATOMYIRI, id, anatomy);
				throw new IllegalStateException(msg);
			}
		}
		if (substrate != 0){
			if (record.getString(DBSUBSTRATESOURCEID) != null){
				this.set_substrateIRI(record.getString(DBSUBSTRATESOURCEID));
			}
			else if (record.getString(DBSUBSTRATEGENERATEDID) != null){
				this.set_substrateIRI(record.getString(DBSUBSTRATEGENERATEDID));
			}
			else{
				final String msg = String.format(BADSUBSTRATEIRI, id, substrate);
				throw new IllegalStateException(msg);
			}
		}
	}

	@Override
	public OWLObject generateOWL(Owlbuilder builder) {
		if (taxon != 0){
			if (get_taxonIRI() != null){
				processParticipantTaxon(builder,IRI.create(get_taxonIRI()));
			}
			else {
				final String msg = String.format("No taxon IRI available; id = %s",id);
				throw new IllegalStateException(msg);
			}
		}
		if (anatomy != 0){
			if (get_anatomyIRI() != null){
				processParticipantAnatomy(builder,IRI.create(get_anatomyIRI()));
			}
			else{
				final String msg = String.format("No anatomy IRI available; id = %s",id);
				throw new IllegalStateException(msg);
			}
		}
		if (substrate != 0){
			if (get_anatomyIRI() != null){
				processParticipantSubstrate(builder,IRI.create(get_substrateIRI()));
			}
			else{
				final String msg = String.format("No substrate IRI available; id = %s",id);
				throw new IllegalStateException(msg);
			}
		}

		return null;
	}
	
	void processParticipantTaxon(Owlbuilder builder,IRI taxonIRI){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getReasoner();
		boolean taxon_duplicate = target.containsClassInSignature(taxonIRI);
		if (!taxon_duplicate){
			boolean taxon_exists = merged.containsClassInSignature(taxonIRI);
			if (taxon_exists){
				log.info("Found class in signature of merged ontology for: " + taxonIRI);
				OWLClass taxonClass = factory.getOWLClass(taxonIRI);
				final NodeSet<OWLClass> taxonParents = reasoner.getSuperClasses(taxonClass, false);
				log.info("Node count = " + taxonParents.getNodes().size());
				Set<OWLClass>parentList =  taxonParents.getFlattened();
				log.info("Flattened parent count = " + parentList.size());
				parentList.add(taxonClass);
				for (OWLClass taxon : parentList){
					super.processTaxon(builder,taxon);
				}
			}
			else{
				log.info("Did not find class in signature of merged ontology for: " + get_taxonIRI());
			}
		}
	}
	
	
	void processParticipantAnatomy(Owlbuilder builder, IRI anatomyIRI){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getReasoner();

	}

	
	void processParticipantSubstrate(Owlbuilder builder, IRI substrateIRI){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getReasoner();

	}
	
	
}
