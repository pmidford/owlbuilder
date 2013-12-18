package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.apache.log4j.Logger;

public abstract class Participant implements AbstractEntity{

	
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
	
	private static String INDIVIDUALQUANTIFIER = "INDIVIDUAL";
	private static String SOMEQUANTIFIER = "SOME";
	
	private static Logger log = Logger.getLogger(Participant.class);

	
	public static Participant makeParticipant(AbstractResults record) throws SQLException{
		final String quantification = record.getString("quantification");
		if (INDIVIDUALQUANTIFIER.equalsIgnoreCase(quantification)){
			IndividualParticipant result = new IndividualParticipant();
			result.fill(record);
			return result;
		}
		else if (SOMEQUANTIFIER.equalsIgnoreCase(quantification)){
			QuantifiedParticipant result = new QuantifiedParticipant();
			result.fill(record);
			return result;
		}
		else{
			final String msg = "Participant had bad quantification: " + quantification;
			log.error(msg);
			throw new IllegalArgumentException(msg);
		}
	}
	

	public int get_id(){
		return id;
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
