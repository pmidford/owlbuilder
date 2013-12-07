package org.arachb.owlbuilder.lib;

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
	int subsrate;
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

}
