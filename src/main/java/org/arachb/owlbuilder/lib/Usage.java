package org.arachb.owlbuilder.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Usage {
	
	int id;
	String behavior_term;
	String publication_taxon;
	int direct_source;
	String evidence;
	int secondary_source;
	String anatomy;
	String participant_list;
	String obo_term_name;
	String obo_term_id;
	String nbo_term_name;
	String nbo_term_id;
	String abo_term;
	String description;
	String resolved_taxon_id;
	
	static final Usage dummy = new Usage(); 
	
	static final private String ROWQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,anatomy,participant_list, obo_term_name," + 
    "obo_term_id, nbo_term_name, nbo_term_id, abo_term, description, resolved_taxon_id " +
	"FROM term_usage where term_usage.id = ?";

	static final private String TABLEQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,anatomy,participant_list, obo_term_name, " + 
	"obo_term_id, nbo_term_name, nbo_term_id, abo_term, description, resolved_taxon_id " +
	"FROM term_usage";
	
	
	public static String getRowQuery(){
		return Usage.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Usage.TABLEQUERY;
	}
	

	
	//maybe make this a constructor
	protected void fill(ResultSet record) throws SQLException{
		id = record.getInt("id");
		behavior_term = record.getString("behavior_term");
		publication_taxon = record.getString("publication_taxon");
		direct_source = record.getInt("direct_source");
		evidence = record.getString("evidence");
		secondary_source = record.getInt("secondary_source");
		anatomy = record.getString("anatomy");
		participant_list = record.getString("participant_list");
		obo_term_name = record.getString("obo_term_name");
		obo_term_id = record.getString("obo_term_id");
		nbo_term_name = record.getString("nbo_term_name");
		nbo_term_id = record.getString("nbo_term_id");
		abo_term = record.getString("abo_term");
		description = record.getString("description");
		resolved_taxon_id = record.getString("resolved_taxon_id");
	}

	
	public int get_id(){
		return id;
	}
	
	public String get_behavior_term(){
		return behavior_term;
	}
	
	public String get_publication_taxon(){
		return publication_taxon;
	}
	
	public int direct_source(){
		return direct_source;
	}
	
	public String get_evidence(){
		return evidence;
	}
	
	public int get_secondary_source(){
		return secondary_source;
	}
	
	public String get_anatomy(){
		return anatomy;
	}
	
	public String get_participant_list(){
		return participant_list;
	}
	
	public String get_obo_term_name(){
		return obo_term_name;
	}
	
	public String get_obo_term_id(){
		return obo_term_id;
	}
	
	public String get_nbo_term_name(){
		return nbo_term_name;
	}
	
	public String get_nbo_term_id(){
		return nbo_term_id;
	}
	
	public String get_abo_term(){
		return abo_term;
	}
	
	public String get_description(){
		return description;
	}
	
	public String get_resolved_taxon_id(){
		return resolved_taxon_id;
	}

}
