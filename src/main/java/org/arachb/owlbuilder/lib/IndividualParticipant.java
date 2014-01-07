package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
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

	static final private String ROWUPDATE = "UPDATE participant " +
	"SET generated_id = ? WHERE id = ?";
	
	private final static Logger log = Logger.getLogger(IndividualParticipant.class);

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
		
	public String getUpdateStatement(){
		return IndividualParticipant.ROWUPDATE;
	}
	
	public String get_generated_id(){
		return generated_id;
	}
	
	public String get_available_id(){
		return get_generated_id();
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

	
	void processParticipantTaxon(Owlbuilder builder,IRI iri){
		final OWLOntology target = builder.getTarget();
		final OWLOntology merged = builder.getMergedSources();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLReasoner reasoner = builder.getReasoner();
		boolean taxon_duplicate = target.containsClassInSignature(iri);
		if (!taxon_duplicate){
			boolean taxon_exists = merged.containsClassInSignature(iri);
			if (taxon_exists){
				log.info("Found class in signature of merged ontology for: " + iri);
				OWLClass taxonClass = factory.getOWLClass(iri);
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

	void processParticipantAnatomy(Owlbuilder builder, IRI iri){
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
			}
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
	

	
	@Override
	public void setGeneratedID(String id) {
		generated_id = id;
	}

	@Override
	public String getIRI_String() {
		return get_generated_id();
	}
	

}
