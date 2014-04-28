package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public class Taxon implements AbstractNamedEntity{

	static final private String ROWQUERY = "SELECT t.id,t.name,t.author, " +
			"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
			"t.parent_term, t.merged, t.merge_status, parent_record.source_id " +
			"FROM taxon AS t where taxon.id = ? " +
			"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) ";


	static final private String TABLEQUERY = "SELECT t.id,t.name,t.author, " +
			"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
			"t.parent_term,t.merged,t.merge_status, parent_record.source_id " +
			"FROM taxon AS t " +
			"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) ";
			

	static final private String ROWUPDATE = "";
	
	final static String BADPARENTIRI =
			"Taxon without IRI referenced as parent of taxon: taxon id = %s; parent id = %s";

	
	public static String getRowQuery(){
		return Taxon.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Taxon.TABLEQUERY;
	}
	
	public String getUpdateStatement(){
		return Taxon.ROWUPDATE;
	}

	static final int DBID = 1;
	static final int DBNAME = 2;
	static final int DBAUTHOR = 3;
	static final int DBYEAR = 4;
	static final int EXTERNAL_ID = 5;
	static final int AUTHORITY = 6;
	static final int PARENT_NAME = 7;
	static final int DBGENERATEDID = 8;
	static final int PARENT_TERM = 9;
	static final int MERGED = 10;
	static final int MERGE_STATUS = 11;
	static final int PARENT_SOURCEID = 12;
	
	private int id;
	private String name;
	private String author;
	private String year;
	private String external_id;
	private String authority;
	private String parent_name;
	private String generated_id;
	private int parent_term;
	private boolean merged;
	private String merge_status;
	private String parent_sourceid;
	

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		name = record.getString(DBNAME);
		author = record.getString(DBAUTHOR);
		year = record.getString(DBYEAR);
		external_id = record.getString(EXTERNAL_ID);
		authority = record.getString(AUTHORITY);
		parent_name = record.getString(PARENT_NAME);
		generated_id = record.getString(DBGENERATEDID);
		parent_term = record.getInt(PARENT_TERM);
		merged = record.getBoolean(MERGED);
		merge_status = record.getString(MERGE_STATUS);
		if (parent_term != 0){
			updateParentIRI(record);
		}

	}
	
	private void updateParentIRI(AbstractResults record) throws SQLException {
		if (record.getString(PARENT_SOURCEID) != null){
			parent_sourceid = record.getString(PARENT_SOURCEID);
		}
		else{
			throwBadState(BADPARENTIRI);
		}
		// TODO Auto-generated method stub
		
	}

	private void throwBadState(String template){
		final String msg = String.format(template, id, parent_term);
		throw new IllegalStateException(msg);
	}

	
	public int getId(){
		return id;
	}
	
	public String get_available_id(){
		if (external_id == null){
			return generated_id;
		}
		else {
			return external_id;
		}
	}
	
	public String getName(){
		return name;
	}

	public String get_author(){
		return author;
	}
	
	public String get_year(){
		return year;
	}
	

	public String getGeneratedId(){
		return generated_id;
	}
	
	public String getIriString(){
		return external_id;
	}
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	public void setGeneratedId(String new_id){
		generated_id = new_id;
	}
	
	public String getAuthority(){
		return authority;
	}
	
	public String getParentName(){
		return parent_name;
	}
	
	public boolean getMerged(){
		return merged;
	}
	
	public String getMergeStatus(){
		return merge_status;
	}
	
	public String getParentSourceId(){
		return parent_sourceid;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object checkIriString() {
		// TODO Auto-generated method stub
		return null;
	}
}
