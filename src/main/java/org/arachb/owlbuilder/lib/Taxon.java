package org.arachb.owlbuilder.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Taxon{

	int id;
	String name;
	String author;
	String year;
	String ottol_id;
	String ncbi_id;
	String generated_id;
	
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

	//maybe make this a constructor
	protected void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		name = record.getString("name");
		author = record.getString("author");
		year = record.getString("year");
		ottol_id = record.getString("ottol_id");
		ncbi_id = record.getString("ncbi_id");
		generated_id = record.getString("generated_id");
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
