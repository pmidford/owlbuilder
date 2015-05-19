package org.arachb.owlbuilder.lib;

import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public class Individual implements GeneratingEntity{

	private final IndividualBean bean;
	
	
	public Individual(IndividualBean ib){
		bean = ib;
	}
	
	public void resolveNarrative(AbstractConnection c){
		
	}
	
	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception{
		return null;
	}

	/* methods to expose bean fields */
	
}
