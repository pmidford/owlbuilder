package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.IRIManager;
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
        generatedLabel = generateCitation();
	}



	public boolean hasGeneratedLabel(){
		return (generatedLabel != null);
	}
	
	public String getGeneratedLabel(){
		return generatedLabel;
	}
	
    /**
     * generate a citation; much like the python version in arachadmin
     * @return citation with first one or two authors and a year
     */
	public String generateCitation(){
		String authors[] = bean.getAuthorList().split(";");
		if (authors.length == 1){
			String author = authors[0].trim();
			return String.format("%s (%s)", author,bean.getPublicationYear());
		}
		else if (authors.length == 2){
			String author1 = authors[0].trim();
			String author2 = authors[1].trim();
			return String.format("%s and %s (%s)", author1, author2,bean.getPublicationYear());
		}
		else{
			String author = authors[0].trim();
			return String.format("%s et al. (%s)", author, bean.getPublicationYear());
		}
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
		String ref_id = bean.checkIRIString(builder.getIRIManager());
		if (ref_id.startsWith("http://dx.doi.org")){
			ref_id = IRIManager.cleanupDoi(ref_id);
		}
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
		final String iComment = "Individual from publication owlgeneration, id = " + bean.getId();
		OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getRDFSComment(), 
				                                             factory.getOWLLiteral(iComment));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(publication_id, commentAnno);
		manager.addAxiom(builder.getTarget(),ax);

		return pub_ind;
	}

}
