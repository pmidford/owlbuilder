package org.arachb.owlbuilder.lib;

import java.util.Map;

import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public interface GeneratingEntity {

	
	/**
	 * Creates OWLAPI object and assertions that logically define it; assertions are
	 * added to the target ontology retrieved from the owlbuilder parameter
	 * @param b allows access to global ontology, factory, reasoner, etc. resources
	 * @param elements table of indexed elements that may be used to avoid regenerating
	 * @return OWLAPI object corresponding to an OWL representation of the Entity
	 * @throws Exception
	 */

	public OWLObject generateOWL(Owlbuilder b, Map<String, OWLObject> elements) throws Exception;


	/**
	 * Version that doesn't accept table
	 * Implementations should treat as if empty table were passed in
	 * @param b allows access to global ontology, factory, reasoner, etc. resources
	 * @return OWLAPI object corresponding to an OWL representation of the Entity
	 * @throws Exception
	 */
	public OWLObject generateOWL(Owlbuilder b) throws Exception;

}
