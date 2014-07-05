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

public class Publication implements AbstractNamedEntity {
	
	
	private final PublicationBean bean;

	private String generatedLabel;
	
	private static Logger log = Logger.getLogger(Publication.class);
	
	


	public Publication(PublicationBean b){
		bean = b;
        generatedLabel = generateLabel();
	}


	@Override
	public int getId() {
		return bean.getId();
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


	/**
	 * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
	 * @param doi
	 * @return IRI using using doi prefix
	 * @throws Exception either MalformedURL or Encoding exceptions can be thrown
	 */
	public static String cleanupDoi(String doi) throws Exception{
		if (doi == null || doi.length() == 0){
			throw new RuntimeException("Invalid empty DOI in publication");
		}
		URL raw = new URL(doi);
		String cleanpath = URLEncoder.encode(raw.getPath().substring(1),"UTF-8");
		if (log.isDebugEnabled()){
			log.debug("raw is " + raw);
		}
		if (log.isDebugEnabled()){
			log.debug("clean path is " + cleanpath);
		}
		return IRI.create("http://dx.doi.org/",cleanpath).toString();
	}

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws SQLException {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass pubAboutInvestigationClass = 
				factory.getOWLClass(IRIManager.pubAboutInvestigation);

		builder.getIRIManager().validateIRI(this);
		IRI publication_id = IRI.create(getIriString());
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
	
	
	final static String PUBBADDOIGENID = "Publication has neither doi nor generated id";
	@Override
	public String getIriString() {
		if (bean.getDoi() == null){
			if (bean.getGeneratedId() == null){
				throw new IllegalStateException(PUBBADDOIGENID);
			}
			return getGeneratedId();
		}
		else {
			try {
				return cleanupDoi(bean.getDoi());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}


	
	
	
	@Override
	public String checkIriString() {
		if (bean.getDoi() == null){
			return getGeneratedId();
		}
		else {
			try {
				return cleanupDoi(bean.getDoi());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}

	


	

	@Override
	public void setGeneratedId(String id) {
		bean.setGeneratedId(id);
	}


	@Override
	public String getGeneratedId() {
		return bean.getGeneratedId();
	}


	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	
	
}
