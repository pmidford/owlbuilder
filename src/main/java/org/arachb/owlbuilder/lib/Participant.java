package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

public class Participant {

	
	static final private String PRIMARYQUERY = "SELECT part.id, " + 
	"part.taxon, part.substrate, part.anatomy, " +
	"part.quantification, part.generated_id, part.publication_taxon, " +
	"part.label, part.publication_anatomy, part.publication_substrate " +
	"FROM participant2assertion as p2a " + 
	"JOIN participant AS part ON (p2a.participant = part.id) " +
	"WHERE p2a.assertion = ? AND p2a.participant_index = 1";
	
	static final private String RESTQUERY = "SELECT part.id, " + 
	"part.taxon, part.substrate, part.anatomy, " +
	"part.quantification, part.generated_id, part.publication_taxon, " +
	"part.label, part.publication_anatomy, part.publication_substrate " +
	"FROM participant2assertion as p2a " + 
	"JOIN participant AS part ON (p2a.participant = part.id) " +
	"WHERE p2a.assertion = ? AND p2a.participant_index > 1";
	
	static final private String ROWUPDATE = "UPDATE participant " +
	"SET generated_id = ? WHERE id = ?";
	
	int id;
	int taxon;
	int substrate;
	int anatomy;
	String quantification;
	String generated_id;
	String publication_taxon;
	String label;
	String publication_anatomy;
	String publication_substrate;
	
	public static String getPrimaryQuery(){
		return Participant.PRIMARYQUERY;
	}
	
	public static String getRestQuery(){
		return Participant.RESTQUERY;
	}
	
	public static String getUpdateStatement(){
		return Participant.ROWUPDATE;
	}
	
	//maybe make this a constructor
	protected void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		taxon = record.getInt("taxon");
		substrate = record.getInt("substrate");
		anatomy = record.getInt("anatomy");
		quantification = record.getString("quantification");
		label = record.getString("label");
		generated_id = record.getString("generated_id");
		publication_taxon = record.getString("publication_taxon");
		publication_anatomy = record.getString("publication_anatomy");
		publication_substrate = record.getString("publication_substrate");
	}

	public int get_id(){
		return id;
	}
	
	public String get_generated_id(){
		return generated_id;
	}
	
	public void set_generated_id(String s){
		generated_id = s;
	}

	public String get_available_id(){
		return get_generated_id();
	}
	
	public int get_taxon(){
		return taxon;
	}
	
	public int get_substrate(){
		return substrate;
	}
	
	public int get_anatomy(){
		return anatomy;
	}
	
	public String get_quantification(){
		return quantification;
	}
	
	public String get_publication_taxon(){
		return publication_taxon;
	}
	
	public String get_label(){
		return label;
	}
	
	public String get_publication_anatomy(){
		return publication_anatomy;
	}
	
	public String get_publication_substrate(){
		return publication_substrate;
	}


}
