package org.arachb.owlbuilder.lib;

public class Synonym {

	int id;
	String name;
	String author;
	String year;
	int valid_name;
	
	static final private String ROWQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,anatomy,participant_list, obo_term_name," + 
    "obo_term_id, nbo_term_name, nbo_term_id, abo_term, description, resolved_taxon_id " +
	"FROM term_usage where term_usage.id = ?";

	static final private String TABLEQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,anatomy,participant_list, obo_term_name, " + 
	"obo_term_id, nbo_term_name, nbo_term_id, abo_term, description, resolved_taxon_id " +
	"FROM term_usage";

	public static String getRowQuery(){
		return ROWQUERY;
	}
	
	public static String getTableQuery(){
		return TABLEQUERY;
	}

	public int get_id(){
		return id;
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
	
	public int get_valid_name(){
		return valid_name;
	}

}
