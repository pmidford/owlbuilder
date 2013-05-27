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
	String resolved_taxon;
	String anatomy;
	String participant_list;
	String obo_term_name;
	String obo_term_id;
	String nbo_term_name;
	String nbo_term_id;
	String abo_term;
	String description;
	
	//maybe make this a constructor
	protected void fill(ResultSet record) throws SQLException{
		id = record.getInt("id");
		behavior_term = record.getString("behavior_term");
		publication_taxon = record.getString("publication_taxon");
		direct_source = record.getInt("direct_source");
		evidence = record.getString("evidence");
		secondary_source = record.getInt("secondary_source");
		resolved_taxon = record.getString("resolved_taxon");
		anatomy = record.getString("anatomy");
		participant_list = record.getString("participant_list");
		obo_term_name = record.getString("obo_term_name");
		obo_term_id = record.getString("obo_term_id");
		nbo_term_name = record.getString("nbo_term_name");
		nbo_term_id = record.getString("nbo_term_id");
		abo_term = record.getString("abo_term");
		description = record.getString("description");
	}


}
