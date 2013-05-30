package org.arachb.owlbuilder.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Publication {

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
	
	static final private String ROWQUERY = "SELECT id, publication_type,dispensation," +
	"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
	"source_publication, volume,issue,serial_identifier,page_range,publication_date," +
	"publication_year,doi,generated_id " +
	"FROM publication where publication.id = ?";
	
	static final private String TABLEQUERY = "SELECT id, publication_type,dispensation," +
	"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
	"source_publication,volume,issue,serial_identifier,page_range,publication_date," +
	"publication_year,doi,generated_id " +
	"FROM publication";
	
	static final private String ROWUPDATE = "UPDATE publication " +
			"SET generated_id = ? WHERE id = ?";
	
	public static String getRowQuery(){
		return Publication.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Publication.TABLEQUERY;
	}
	
	public static String getUpdateStatement(){
		return Publication.ROWUPDATE;
	}

	
	//maybe make this a constructor
	protected void fill(ResultSet record) throws SQLException{
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
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	public void set_generated_id(String new_id){
		generated_id = new_id;
	}
	
}
