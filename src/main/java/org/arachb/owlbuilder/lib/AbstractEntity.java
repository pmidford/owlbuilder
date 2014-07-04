package org.arachb.owlbuilder.lib;


import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public interface AbstractEntity {

	/**
	 * 
	 * @return internal id - unique across all database records of a particular AE subtype
	 */
	public int getId();

	
	/**
	 * Creates OWLAPI object and assertions that logically define it; assertions are
	 * added to the target ontology retrieved from the owlbuilder parameter
	 * @param b allows access to global ontology, factory, reasoner, etc. resources
	 * @return OWLAPI object corresponding to an OWL representation of the Entity
	 * @throws Exception
	 */
	public OWLObject generateOWL(Owlbuilder b) throws Exception;

}
