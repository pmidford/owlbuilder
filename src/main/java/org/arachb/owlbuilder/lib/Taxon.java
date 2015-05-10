package org.arachb.owlbuilder.lib;

import org.arachb.arachadmin.TaxonBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public class Taxon implements GeneratingEntity{

	private final TaxonBean bean;

	public Taxon(TaxonBean b){
		bean = b;
	}


	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	public String getParentSourceId(){
		return bean.getParentSourceId();
	}


	public String getName() {
		return bean.getName();
	}

}
