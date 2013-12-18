package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

public class Term implements AbstractNamedEntity{

	private int id;
	private String source_id;
	private int domain;
	private int authority;
	private String label;
	private String generated_id;
	private String comment;
	
	
	static final private String ROWQUERY = "SELECT id,source_id,domain," +
	    "authority,label,generated_id,comment " +
	    "FROM term where term.id = ?";
	
	static final private String TABLEQUERY = "SELECT id,source_id,domain," +
	    "authority,label,generated_id,comment " +
	    "FROM term";
		
	static final private String ROWUPDATE = "UPDATE term " +
		"SET generated_id = ? WHERE id = ?";
	
	public static String getRowQuery(){
		return Term.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Term.TABLEQUERY;
	}
	
	public String getUpdateStatement(){
		return Term.ROWUPDATE;
	}

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		source_id = record.getString("source_id");
		domain = record.getInt("domain");
		authority = record.getInt("authority");
		label = record.getString("label");
		generated_id = record.getString("generated_id");
		comment = record.getString("comment");
	}

	
	public int get_id(){
		return id;
	}
	
	public String getIRI_String(){
		if (get_source_id() == null){
			return get_generated_id();
		}
		else {
			return get_source_id();
		}
	}

	
	public String get_source_id(){
		return source_id;
	}

	public int get_domain(){
		return domain;
	}
	
	public int get_authority(){
		return authority;
	}
	
	public String get_label(){
		return label;
	}
	
	public String get_comment(){
		return comment;
	}
	
	public String get_generated_id(){
		return generated_id;
	}

	public void setGeneratedID(String new_id){
		generated_id = new_id;
	}
	
	@Override
	public OWLObject generateOWL(OWLOntology o, OWLDataFactory factory,
			IRIManager iriManager) {
		// TODO Auto-generated method stub
		return null;
	}


}
