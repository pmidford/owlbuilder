package org.arachb.owlbuilder.lib;

import java.util.Map;

import org.arachb.arachadmin.PropertyBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class PropertyTerm implements NamedGeneratingEntity {

	private final PropertyBean bean;
	
	public PropertyTerm(PropertyBean pb){
		bean = pb;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder b, Map<String, OWLObject> elements) throws Exception {
		IRI propertyIRI = IRI.create(bean.getSourceId());
		OWLObjectProperty elementProperty = b.getDataFactory().getOWLObjectProperty(propertyIRI);
		elements.put(bean.getSourceId(), elementProperty);
		return elementProperty;
	}

	
	/**
	 * Doesn't call any generateOWL methods, so don't bother creating name table
	 * 
	 */
	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception{
		IRI propertyIRI = IRI.create(bean.getSourceId());
		return b.getDataFactory().getOWLObjectProperty(propertyIRI);
	}

	
	public String getSourceId() {
		return bean.getSourceId();
	}


}
