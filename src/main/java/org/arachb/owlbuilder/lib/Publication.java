package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

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



	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws SQLException {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass pubAboutInvestigationClass = 
				factory.getOWLClass(Vocabulary.pubAboutInvestigation);
		String ref_id = get_ref_id(builder);
		final IRI publication_id = IRI.create(ref_id);
		log.info("Publication_id is " + publication_id + " uriset is " + bean.getuidset());
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
		return pub_ind;
	}
	

	private String get_ref_id(Owlbuilder b) throws Exception{
		String ref_id = bean.checkIRIString(b.getIRIManager());
		if (ref_id.startsWith("http://dx.doi.org")){
			ref_id = IRIManager.cleanupDoi(ref_id);
		}
		return ref_id;
	}
	
	
}
