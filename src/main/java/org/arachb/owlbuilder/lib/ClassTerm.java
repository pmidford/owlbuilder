package org.arachb.owlbuilder.lib;

import java.util.Map;

import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

public class ClassTerm implements NamedGeneratingEntity,TaxonomicEntity {

	private TermBean bean;
	//TODO figure out how to set this meaningfully
	private boolean isTaxon = false;
	
	public ClassTerm(TermBean tb){
		bean = tb;
	}
	
	@Override
	public boolean isTaxon(){
		return isTaxon;
	}
	
	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception {
		OWLDataFactory factory = b.getDataFactory();
		IRI classIRI = IRI.create(bean.getIRIString());
		return factory.getOWLClass(classIRI);
	}

	@Override
	public OWLObject generateOWL(Owlbuilder b, Map<String, OWLObject> elements) throws Exception {
		OWLObject newClass = generateOWL(b);
		elements.put(bean.getIRIString(), newClass);
		return newClass;
	}

}
