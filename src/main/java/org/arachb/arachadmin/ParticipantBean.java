package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ParticipantBean implements UpdateableBean{

	

		
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

	
	final Map<Integer, PElementBean> elements = new HashMap<Integer, PElementBean>();
	
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

	final static String NOPARTICGENID = "Participant has no generated id; db id = %s";

	@Override
	public String getIRIString() {
		final String genId = getGeneratedId();
		if (genId == null){
			final String msg = String.format(NOPARTICGENID, getId());
			throw new IllegalStateException(msg);
		}
		return genId;
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		if (getGeneratedId() == null){
			manager.generateIRI(this);
		}
		return getGeneratedId();
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}





}
