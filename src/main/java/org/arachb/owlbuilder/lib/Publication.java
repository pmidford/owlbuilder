package org.arachb.owlbuilder.lib;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.apache.log4j.Logger;
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


public class Publication implements AbstractNamedEntity{


	static final int DBID = 1;
	static final int DBPUBLICATIONTYPE = 2;
	static final int DBDISPENSATION = 3;
	static final int DBDOWNLOADED = 4;
	static final int DBREVIEWED = 5;
	static final int DBTITLE = 6;
	static final int DBALTERNATETITLE = 7;
	static final int DBAUTHORLIST = 8;
	static final int DBEDITORLIST = 9;
	static final int DBSOURCEPUBLICATION = 10;
	static final int DBVOLUME = 11;
	static final int DBISSUE = 12;
	static final int DBSERIALIDENTIFIER = 13;
	static final int DBPAGERANGE = 14;
	static final int DBPUBLICATIONDATE = 15;
	static final int DBPUBLICATIONYEAR = 16;
	static final int DBDOI = 17;
	static final int DBGENERATEDID = 18;
	static final int DBCURATIONSTATUS = 19;
	static final int DBCURATIONUPDATE = 20;
	

	
	private int id;
	private String publication_type;
	private String dispensation;
	private String downloaded;
	private String reviewed;
	private String title;
	private String alternate_title;
	private String author_list;
	private String editor_list;
	private String source_publication;
	private int volume;
	private String issue;
	private String serial_identifier;
	private String page_range;
	private String publication_date;
	private String publication_year;
	private String doi;
	private String generated_id;
	private String curation_status;
	private String curation_update;
	
	private String generated_label;
	
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	
	
	@Override
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		publication_type = record.getString(DBPUBLICATIONTYPE);
		dispensation = record.getString(DBDISPENSATION);
		downloaded = record.getString(DBDOWNLOADED);
		reviewed = record.getString(DBREVIEWED);
		title = record.getString(DBTITLE);
		alternate_title = record.getString(DBALTERNATETITLE);
		author_list = record.getString(DBAUTHORLIST);
		editor_list = record.getString(DBEDITORLIST);
		source_publication = record.getString(DBSOURCEPUBLICATION);
		volume = record.getInt(DBVOLUME);
		issue = record.getString(DBISSUE);
		serial_identifier = record.getString(DBSERIALIDENTIFIER);
		page_range = record.getString(DBPAGERANGE);
		publication_date = record.getString(DBPUBLICATIONDATE);
		publication_year = record.getString(DBPUBLICATIONYEAR);
		doi = record.getString(DBDOI);
		generated_id = record.getString(DBGENERATEDID);
        curation_status = record.getString(DBCURATIONSTATUS);
        curation_update = record.getString(DBCURATIONUPDATE);        
        generated_label = generateLabel();
	}
	
	@Override
	public int getId(){
		return id;
	}
	
	
	public String getPublicationType(){
		return publication_type;
	}
	
	public String getDispensation(){
		return dispensation;
	}
	
	public String getDownloaded(){
		return downloaded;
	}
	
	public String getReviewed(){
		return reviewed;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAlternateTitle(){
		return alternate_title;
	}
	
	public String getAuthorList(){
		return author_list;
	}
	
	public String getEditorList(){
		return editor_list;
	}
	
	public String getSourcePublication(){
		return source_publication;
	}
	
	public int getVolume(){
		return volume;
	}
	
	public String getIssue(){
		return issue;
	}
	
	public String getSerialIdentifier(){
		return serial_identifier;
	}
	
	public String getPageRange(){
		return page_range;
	}
	
	public String getPublicationDate(){
		return publication_date;
	}
	
	public String getPublicationYear(){
		return publication_year;
	}
	
	public String getDoi(){
		return doi;
	}

	public String getGeneratedId(){
		return generated_id;
	}

	public String getCurationStatus(){
		return curation_status;
	}
	
	public String getCurationUpdate(){
		return curation_update;
	}
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	@Override
	public void setGeneratedId(String id) {
		generated_id = id;
	}

    //generate a label; this should gradually get smarter
	private String generateLabel(){
		StringBuilder b = new StringBuilder(100);
        b.append(author_list);
        b.append(' ');
        b.append(publication_year);
		return b.toString();
	}
	
	@Override
	public String getIriString() {
		if (getDoi() == null){
			if (generated_id == null){
				throw new IllegalStateException("Publication has neither doi nor generated id");
			}
			return getGeneratedId();
		}
		else {
			try {
				return cleanupDoi(getDoi());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}
	
	
	@Override
	public String checkIriString() {
		if (getDoi() == null){
			return getGeneratedId();
		}
		else {
			try {
				return cleanupDoi(getDoi());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException{
		c.updatePublication(this);
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
		if (publication_id != null){
			OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
			OWLClassAssertionAxiom classAssertion = 
					factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
			manager.addAxiom(builder.getTarget(), classAssertion);
			if (generated_label != null){
				OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
						factory.getOWLLiteral(generated_label));
				OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(publication_id, labelAnno);
			    // Add the axiom to the ontology
			    manager.addAxiom(builder.getTarget(),ax);
			}	
			return pub_ind;
		}
		return null;
	}
}
