package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

public class Taxon{

	static final private String ROWQUERY = "SELECT id,name,author,year,ottol_id," +
			"ncbi_id,generated_id " +
			"FROM taxon where taxon.id = ?";

	static final private String TABLEQUERY = "SELECT id,name,author,year,ottol_id, " +
			"ncbi_id,generated_id " +
			"FROM taxon";

	public static String getRowQuery(){
		return Taxon.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Taxon.TABLEQUERY;
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
	
	int id;
	String name;
	String author;
	String year;
	String external_id;
	String authority;
	String parent_name;
	String generated_id;
	int parent_term;
	boolean merged;
	String merge_status;
	

	//maybe make this a constructor
	protected void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		name = record.getString(DBNAME);
		author = record.getString(DBAUTHOR);
		year = record.getString(DBYEAR);
		external_id = record.getString(EXTERNAL_ID);
		authority = record.getString(AUTHORITY);
		parent_name = record.getString(PARENT_TERM);
		generated_id = record.getString(DBGENERATEDID);
		parent_term = record.getInt(PARENT_TERM);
		merged = record.getBoolean(MERGED);
		merge_status = record.getString(MERGE_STATUS);
		
	}
	
	public int get_id(){
		return id;
	}
	
	public String get_available_id(){
		if (get_external_id() == null){
			return get_generated_id();
		}
		else {
			return get_external_id();
		}
	}
	
	public String get_name(){
		return name;
	}

	public String get_author(){
		return author;
	}
	
	public String get_year(){
		return year;
	}
	

	public String get_generated_id(){
		return generated_id;
	}
	
	public String get_external_id(){
		return external_id;
	}
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	public void set_generated_id(String new_id){
		generated_id = new_id;
	}
}
