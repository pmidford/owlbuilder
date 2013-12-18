package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

public interface AbstractEntity {

	
	public int get_id();

	void fill(AbstractResults record) throws SQLException;
	
	public OWLObject generateOWL(OWLOntology o, OWLDataFactory factory, IRIManager iriManager);
}
