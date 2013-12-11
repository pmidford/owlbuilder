package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

public class Assertion {
	
	private int id;
	private int publication;
	private int behavior_term;
	private String publication_behavior;
	private int taxon;
	private String publication_taxon;
	private String publication_anatomy;
	private int evidence;
	private String generated_id;
	
	static final Assertion dummy = new Assertion(); 
	
	static final private String ROWQUERY = "SELECT id, publication, " +
	"publication_behavior, behavior_term, publication_taxon, taxon," +
	"publication_anatomy, evidence, generated_id " +
    "FROM assertion where assertion.id = ?";

	static final private String TABLEQUERY = "SELECT id, publication, " +
	"publication_behavior, behavior_term, publication_taxon, taxon, " +
    "publication_anatomy, evidence, generated_id " +
	"FROM assertion";
	
	static final private String ROWUPDATE = "UPDATE assertion " +
			"SET generated_id = ? WHERE id = ?";
	

	
	public static String getRowQuery(){
		return Assertion.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Assertion.TABLEQUERY;
	}
	
	public static String getUpdateStatement(){
		return Assertion.ROWUPDATE;
	}
	

	
	//maybe make this a constructor
	protected void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		publication = record.getInt("publication");
		publication_behavior = record.getString("publication_behavior");
		behavior_term = record.getInt("behavior_term");
		publication_taxon = record.getString("publication_taxon");
		taxon = record.getInt("taxon");
		publication_anatomy = record.getString("publication_anatomy");
		evidence = record.getInt("evidence");
		generated_id = record.getString("generated_id");
	}

	
	public int get_id(){
		return id;
	}
	
	public int get_publication(){
		return publication;
	}
	
	public String get_publication_behavior(){
		return publication_behavior;
	}
	
	public int get_behavior_term(){
		return behavior_term;
	}
	
	public String get_publication_taxon(){
		return publication_taxon;
	}
	
	public int get_taxon(){
		return taxon;
	}
	
	public String get_publication_anatomy(){
		return publication_anatomy;
	}
	
	public int get_evidence(){
		return evidence;
	}
	
	public String get_generated_id(){
		return generated_id;
	}

	public void set_generated_id(String id){
		generated_id = id;
	}
}
