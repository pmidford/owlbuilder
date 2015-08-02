package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.Map;

import org.arachb.arachadmin.TaxonBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

public class Taxon implements NamedGeneratingEntity,TaxonomicEntity{

	private final TaxonBean bean;

	public Taxon(TaxonBean b){
		bean = b;
	}

	@Override
	public boolean isTaxon(){
		return true;
	}


	public String getParentRefId(){
		return bean.getParentRefId();
	}


	public String getName() {
		return bean.getName();
	}

	final private static Map<String,OWLObject> defaultElementMap = new HashMap<String,OWLObject>();

	@Override
	public OWLObject generateOWL(Owlbuilder b, Map<String, OWLObject> elements) throws Exception {
		OWLDataFactory factory = b.getDataFactory();
		IRI classIRI = IRI.create(bean.getIRIString());
		OWLClass newClass = factory.getOWLClass(classIRI);
		elements.put(bean.getIRIString(), newClass);
		return newClass;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws Exception {
		OWLObject result = generateOWL(builder, defaultElementMap);
		defaultElementMap.clear();
		return result;
	}

}
