package org.arachb.owlbuilder.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assertion {
	
	int id;
	int publication;
	int behavior_term;
	String publication_behavior;
	int taxon;
	String publication_taxon;
	String publication_anatomy;
	int evidence;
	
	static final Assertion dummy = new Assertion(); 
	
	static final private String ROWQUERY = "SELECT id, publication, " +
	"publication_behavior, behavior_term, publication_taxon, taxon," +
	"publication_anatomy, evidence " +
    "FROM assertion where assertion.id = ?";

	static final private String TABLEQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,anatomy,participant_list, obo_term_name, " + 
	"obo_term_id, nbo_term_name, nbo_term_id, abo_term, description, resolved_taxon_id, " +
    "generated_behavior_id, generated_anatomy_id " +
	"FROM assertion";
	
	
	public static String getRowQuery(){
		return Assertion.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Assertion.TABLEQUERY;
	}
	

	
	//maybe make this a constructor
	protected void fill(ResultSet record) throws SQLException{
		id = record.getInt("id");
		publication = record.getInt("publication");
		publication_behavior = record.getString("publication_behavior");
		behavior_term = record.getInt("behavior_term");
		publication_taxon = record.getString("publication_taxon");
		taxon = record.getInt("taxon");
		publication_anatomy = record.getString("publication_anatomy");
		evidence = record.getInt("evidence");
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

}
