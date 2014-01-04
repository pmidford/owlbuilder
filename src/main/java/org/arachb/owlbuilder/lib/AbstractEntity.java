package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public interface AbstractEntity {

	public int get_id();

	void fill(AbstractResults record) throws Exception;
	
	public OWLObject generateOWL(Owlbuilder b) throws Exception;

}
