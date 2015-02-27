package org.arachb.owlbuilder.lib;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
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
	
	private static Logger log = Logger.getLogger(Publication.class);
	
	


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
				factory.getOWLClass(IRIManager.pubAboutInvestigation);

		builder.getIRIManager().validateIRI(bean);
		IRI publication_id = IRI.create(bean.getIriString());
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
	
	


	
	
	

	
	
}
