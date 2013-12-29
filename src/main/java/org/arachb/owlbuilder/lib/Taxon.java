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
	static final int DBOTTOLID = 5;
	static final int DBNCBIID = 6;
	static final int DBGENERATEDID = 7;
	
	int id;
	String name;
	String author;
	String year;
	String ottol_id;
	String ncbi_id;
	String generated_id;
	

	//maybe make this a constructor
	protected void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		name = record.getString(DBNAME);
		author = record.getString(DBAUTHOR);
		year = record.getString(DBYEAR);
		ottol_id = record.getString(DBOTTOLID);
		ncbi_id = record.getString(DBNCBIID);
		generated_id = record.getString(DBGENERATEDID);
	}
	
	public int get_id(){
		return id;
	}
	
	public String get_available_id(){
		if (get_ncbi_id() == null){
			return get_generated_id();
		}
		else {
			return get_ncbi_id();
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
	
	public String get_ottol_id(){
		return ottol_id;
	}
	
	public String get_ncbi_id(){
		return ncbi_id;
	}

	public String get_generated_id(){
		return generated_id;
	}
	
	//Just updates the id in the bean - method for updating db is in DBConnection
	public void set_generated_id(String new_id){
		generated_id = new_id;
	}
}
