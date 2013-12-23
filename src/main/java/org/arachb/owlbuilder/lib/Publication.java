package org.arachb.owlbuilder.lib;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class Publication implements AbstractNamedEntity{

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
	
	static final private String ROWQUERY = "SELECT id, publication_type,dispensation," +
	"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
	"source_publication, volume,issue,serial_identifier,page_range,publication_date," +
	"publication_year,doi,generated_id,curation_status,curation_update " +
	"FROM publication where publication.id = ?";
	
	static final private String TABLEQUERY = "SELECT id, publication_type,dispensation," +
	"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
	"source_publication,volume,issue,serial_identifier,page_range,publication_date," +
	"publication_year,doi,generated_id,curation_status, curation_update " +
	"FROM publication";
	
	static final private String ROWUPDATE = "UPDATE publication " +
			"SET generated_id = ? WHERE id = ?";
	
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	
	public static String getRowQuery(){
		return Publication.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Publication.TABLEQUERY;
	}
	
	public String getUpdateStatement(){
		return Publication.ROWUPDATE;
	}

	
	@Override
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		publication_type = record.getString("publication_type");
		dispensation = record.getString("dispensation");
		downloaded = record.getString("downloaded");
		reviewed = record.getString("reviewed");
		title = record.getString("title");
		alternate_title = record.getString("alternate_title");
		author_list = record.getString("author_list");
		editor_list = record.getString("editor_list");
		source_publication = record.getString("source_publication");
		volume = record.getInt("volume");
		issue = record.getString("issue");
		serial_identifier = record.getString("serial_identifier");
		page_range = record.getString("page_range");
		publication_date = record.getString("publication_date");
		publication_year = record.getString("publication_year");
		doi = record.getString("doi");
		generated_id = record.getString("generated_id");
        curation_status = record.getString("curation_status");
        curation_update = record.getString("curation_update");
	}
	
	public int get_id(){
		return id;
	}
	

	
	public String get_publication_type(){
		return publication_type;
	}
	
	public String get_dispensation(){
		return dispensation;
	}
	
	public String get_downloaded(){
		return downloaded;
	}
	
	public String get_reviewed(){
		return reviewed;
	}
	
	public String get_title(){
		return title;
	}
	
	public String get_alternate_title(){
		return alternate_title;
	}
	
	public String get_author_list(){
		return author_list;
	}
	
	public String get_editor_list(){
		return editor_list;
	}
	
	public String get_source_publication(){
		return source_publication;
	}
	
	public int get_volume(){
		return volume;
	}
	
	public String get_issue(){
		return issue;
	}
	
	public String get_serial_identifier(){
		return serial_identifier;
	}
	
	public String get_page_range(){
		return page_range;
	}
	
	public String get_publication_date(){
		return publication_date;
	}
	
	public String get_publication_year(){
		return publication_year;
	}
	
	public String get_doi(){
		return doi;
	}

	public String get_generated_id(){
		return generated_id;
	}

	public String get_curation_status(){
		return curation_status;
	}
	
	public String get_curation_update(){
		return curation_update;
	}
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	@Override
	public void setGeneratedID(String id) {
		generated_id = id;
	}

	@Override
	public String getIRI_String() {
		if (get_doi() == null){
			if (generated_id == null){
				throw new IllegalStateException("Publication has neither doi nor generated id");
			}
			return get_generated_id();
		}
		else {
			return get_doi();
		}
	}
	
	/**
	 * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
	 * @param doi
	 * @return IRI using using doi prefix
	 * @throws Exception either MalformedURL or Encoding exceptions can be thrown
	 */
	private String cleanupDOI(String doi) throws Exception{
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

	private void processStuff(IRIManager iriManager,AbstractConnection connection) throws Exception{
		String pubID;
		if(get_doi() != null){
			String clean = cleanupDOI(get_doi());
			pubID = clean;
			if (get_generated_id() != null){
			//TODO check for existing arachb id - generate owl:sameas - general, should be somewhere else
			}
		}
		else if (get_generated_id() == null){ //generate arachb IRI (maybe temporary)
			pubID = iriManager.generateARACHB_IRI_String();
			setGeneratedID(pubID);
			//connection.updatePublication(this);
		}
		else{
			pubID = get_generated_id();
		}

	}
	
	@Override
	public OWLObject generateOWL(OWLOntology o, OWLOntologyManager manager, IRIManager iriManager) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
