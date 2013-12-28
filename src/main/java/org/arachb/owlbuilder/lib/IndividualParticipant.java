package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class IndividualParticipant extends Participant implements AbstractNamedEntity{

	static final private String ROWUPDATE = "UPDATE participant " +
	"SET generated_id = ? WHERE id = ?";
	
	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		taxon = record.getInt("taxon");
		substrate = record.getInt("substrate");
		anatomy = record.getInt("anatomy");
		quantification = record.getString("quantification");
		label = record.getString("label");
		generated_id = record.getString("generated_id");
		publication_taxon = record.getString("publication_taxon");
		publication_anatomy = record.getString("publication_anatomy");
		publication_substrate = record.getString("publication_substrate");
		if (taxon != 0){
			String taxonSourceID = record.getString("taxon.source_id");
			String taxonGenID = record.getString("taxon.generated_id");
			if (taxonSourceID != null){
				this.set_taxonIRI(taxonSourceID);
			}
			else if (taxonGenID != null){
				this.set_taxonIRI(taxonGenID);
			}
			else{
				throw new IllegalStateException(BADTAXONIRI + taxon);
			}
		}
		if (anatomy != 0){
			String anatomySourceID = record.getString("anatomy.source_id");
			String anatomyGenID = record.getString("anatomy.generated_id");
			if (anatomySourceID != null){
				this.set_anatomyIRI(anatomySourceID);
			}
			else if (anatomyGenID != null){
				this.set_anatomyIRI(anatomyGenID);
			}
			else{
				throw new IllegalStateException(BADANATOMYIRI + anatomy);
			}
		}
		if (substrate != 0){
			String substrateSourceID = record.getString("substrate.source_id");
			String substrateGenID = record.getString("substrate.generated_id");
			if (substrateSourceID != null){
				this.set_substrateIRI(substrateSourceID);
			}
			else if (substrateGenID != null){
				this.set_substrateIRI(substrateGenID);
			}
			else{
				throw new IllegalStateException(BADSUBSTRATEIRI + substrate);
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
	public OWLObject generateOWL(OWLOntology o, OWLOntologyManager manager, IRIManager iriManager) {
		final OWLDataFactory factory = manager.getOWLDataFactory();
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		IRI individual_id = IRI.create(getIRI_String());
		OWLIndividual part = factory.getOWLNamedIndividual(individual_id);
		// TODO Auto-generated method stub
		return null;
	}

	private OWLObject generateAnatomyOWL(OWLOntology o, OWLOntologyManager manager, IRIManager iriManager){
		OWLObject taxonObject = generateTaxonOWL(o,manager,iriManager);
		return null;
	}
	
	private OWLObject generateTaxonOWL(OWLOntology o, OWLOntologyManager manager, IRIManager iriManager){
		return null;
	}
	
	private OWLObject generateSubstrateOWL(OWLOntology o, OWLOntologyManager manager, IRIManager iriManager){
		return null;
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
