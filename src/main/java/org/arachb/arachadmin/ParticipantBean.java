package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.arachb.owlbuilder.lib.AbstractNamedEntity;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ParticipantBean implements BeanBase, AbstractNamedEntity{

	

		
	final static int DBID = 1;
	final static int DBQUANTIFICATION = 2;
	final static int DBLABEL = 3;
	final static int DBGENERATEDID = 4;
	final static int DBPROPERTY = 5;
	final static int DBPUBLICATIONTAXON = 6;
	final static int DBPUBLICATIONANATOMY = 7;
	final static int DBPUBLICATIONSUBSTRATE = 8;
	final static int DBHEADELEMENT = 9;

	final static String BADTAXONIRI =
			"Term without IRI referenced as participant taxon: participant id = %s; taxon id = %s";
	final static String BADANATOMYIRI =
			"Term without IRI referenced as participant anatomy: participant id = %s; anatomy id = %s";
	final static String BADSUBSTRATEIRI =
			"Term without IRI referenced as participant substrate; participant id = %s; substrate id = %s";
	
    
	private int id;
	private int taxon;
	private int substrate;
	private int anatomy;
	private String quantification;
	private String generatedId;
	private int property;
	private String publicationTaxon;
	private String label;
	private String publicationAnatomy;
	private String publicationSubstrate;
	private String taxonIRI = null;
	private String substrateIRI = null;
	private String anatomyIRI = null;
	private PropertyBean propertyBean;
	private int headElement;
	private PElementBean headBean;

	
	private final Map<Integer, PElementBean> elements = new HashMap<Integer, PElementBean>();
	
	public final static String INDIVIDUALQUANTIFIER = "INDIVIDUAL";
	public final static String SOMEQUANTIFIER = "SOME";
	
	private static Logger log = Logger.getLogger(ParticipantBean.class);

	
	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		quantification = record.getString(DBQUANTIFICATION);
		label = record.getString(DBLABEL);
		generatedId = record.getString(DBGENERATEDID);
		property = record.getInt(DBPROPERTY);
		publicationTaxon = record.getString(DBPUBLICATIONTAXON);
		publicationAnatomy = record.getString(DBPUBLICATIONANATOMY);
		publicationSubstrate = record.getString(DBPUBLICATIONSUBSTRATE);
		headElement = record.getInt(DBHEADELEMENT);
	}
	
	public int getId(){
		return id;
	}
	
	public int getTaxon(){
		return taxon;
	}
	
	public int getSubstrate(){
		return substrate;
	}
	
	public int getAnatomy(){
		return anatomy;
	}
	
	public String getQuantification(){
		return quantification;
	}
	
	public int getProperty(){
		return property;
	}
	
	public String getPublicationTaxon(){
		return publicationTaxon;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getPublicationAnatomy(){
		return publicationAnatomy;
	}
	
	public String getPublicationSubstrate(){
		return publicationSubstrate;
	}

	public String getTaxonIri(){
		return taxonIRI;
	}
		
	public String getSubstrateIri(){
		return substrateIRI;
	}
	
	public String getAnatomyIri(){
		return anatomyIRI;
	}
	
	public String getGeneratedId(){
		return generatedId;
	}
	
	public void setGeneratedId(String s){
		generatedId = s;
	}
	
	public int getHeadElement(){
		return headElement;
	}
	

	public PElementBean getElementBean(int index){
		return elements.get(index);
	}
	
	public PropertyBean getParticipationBean(){
		return propertyBean;
	}

//TODO should these be merged?
	
	public void loadElements(AbstractConnection c) throws Exception{
		// special handling for head participation property
		if (property > 0){
			propertyBean = c.getProperty(property);
		}
		else {
			throw new IllegalStateException("No participantProperty specified");
		}
		Set<PElementBean> elementset = c.getPElements(this);
		for (PElementBean e : elementset){
			elements.put(e.getId(), e);
		}
	}

	public void resolveElements(AbstractConnection c) throws Exception{
		assert elements.size() > 0;
		assert elements.containsKey(getHeadElement());
		headBean = c.getPElement(getHeadElement());
		propertyBean = c.getProperty(getProperty());
		assert headBean != null;
		assert propertyBean != null;
		for (PElementBean element : elements.values()){
			element.resolveParents(c);
			element.resolveChildren(c);
		}
	}
	
	void processTaxon(Owlbuilder builder,OWLClass taxon){
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){  //add appropriate when figured out
			log.info("Need to add taxon: " + taxon.getIRI());
			//log.info("Defining Axioms");
			manager.addAxioms(extracted, merged.getAxioms(taxon));
			//log.info("Annotations");
			Set<OWLAnnotationAssertionAxiom> taxonAnnotations = merged.getAnnotationAssertionAxioms(taxon.getIRI());
			for (OWLAnnotationAssertionAxiom a : taxonAnnotations){
				//log.info("   Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
	}

	public void processAnatomy(Owlbuilder builder, OWLClass anatomyClass) {
		final OWLOntologyManager manager = builder.getOntologyManager();
		final OWLOntology merged = builder.getMergedSources();
		final OWLOntology extracted = builder.getTarget();
		if (true){
			log.info("Need to add anatomy: " + anatomyClass.getIRI());
			Set<OWLClassAxiom> anatAxioms = merged.getAxioms(anatomyClass);
			manager.addAxioms(extracted, anatAxioms);
			Set<OWLAnnotationAssertionAxiom> anatAnnotations = 
					merged.getAnnotationAssertionAxioms(anatomyClass.getIRI());
			for (OWLAnnotationAssertionAxiom a : anatAnnotations){
				//log.info("   Annotation Axiom: " + a.toString());
				if (a.getAnnotation().getProperty().isLabel()){
					log.info("Label is " + a.getAnnotation().getValue().toString());
					manager.addAxiom(extracted, a);
				}
			}
		}
    	builder.initializeMiscTermAndParents(anatomyClass);
		
	}

	public void processSubstrate(Owlbuilder builder, OWLClass substrateClass) {
		builder.initializeMiscTermAndParents(substrateClass);
		
	}

	@Override
	public String getIriString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object checkIriString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}





}
