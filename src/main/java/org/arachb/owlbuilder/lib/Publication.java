package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.Map;

import org.arachb.arachadmin.PublicationBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Publication implements GeneratingEntity {
	
	
	private final PublicationBean bean;

	private String generatedLabel;

	public Publication(PublicationBean b){
		bean = b;
        generatedLabel = generateLabel();
	}



	public boolean hasGeneratedLabel(){
		return (generatedLabel != null);
	}
	
	public String getGeneratedLabel(){
		return generatedLabel;
	}
	
    //generate a label; this should gradually get smarter
	private String generateLabel(){
		StringBuilder b = new StringBuilder(100);
        b.append(bean.getAuthorList());
        b.append(' ');
        b.append(bean.getPublicationYear());
		return b.toString();
	}

	final private static Map<String,OWLObject> defaultElementMap = new HashMap<String,OWLObject>();

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws Exception {
		OWLObject result = generateOWL(builder, defaultElementMap);
		defaultElementMap.clear();
		return result;
	}


	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements)
			throws Exception {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass pubAboutInvestigationClass = 
				factory.getOWLClass(Vocabulary.pubAboutInvestigation);
		IRI publication_id = IRI.create(bean.checkIRIString(builder.getIRIManager()));
		assert(publication_id != null);
		OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
		OWLClassAssertionAxiom classAssertion = 
				factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
		manager.addAxiom(builder.getTarget(), classAssertion);
		if (hasGeneratedLabel()){
			OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
					factory.getOWLLiteral(getGeneratedLabel()));
			OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(publication_id, labelAnno);
			// Add the axiom to the ontology
			manager.addAxiom(builder.getTarget(),ax);
		}
		final String iComment = "Individual from publication owlgeneration, id = " + bean.getId();
		OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getRDFSComment(), 
				                                             factory.getOWLLiteral(iComment));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(publication_id, commentAnno);
		manager.addAxiom(builder.getTarget(),ax);

		return pub_ind;
	}

}
