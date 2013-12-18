package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

public class QuantifiedParticipant extends Participant {

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
	}

	@Override
	public OWLObject generateOWL(OWLOntology o, OWLDataFactory factory,
			IRIManager iriManager) {
		// TODO Auto-generated method stub
		return null;
	}

}
