package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Term implements AbstractNamedEntity{

	static final private String ROWQUERY = "SELECT id,source_id,domain," +
		    "authority,label,generated_id,comment " +
		    "FROM term where term.id = ?";
		
	static final private String TABLEQUERY = "SELECT id,source_id,domain," +
		    "authority,label,generated_id,comment " +
		    "FROM term";
			
	static final private String ROWUPDATE = "UPDATE term " +
			"SET generated_id = ? WHERE id = ?";

	static final int DBID = 1;
	static final int DBSOURCEID = 2;
	static final int DBDOMAIN = 3;
	static final int DBAUTHORITY = 4;
	static final int DBLABEL = 5;
	static final int DBGENERATEDID = 6;
	static final int DBCOMMENT = 7;
		
	private int id;
	private String source_id;
	private int domain;
	private int authority;
	private String label;
	private String generated_id;
	private String comment;
	
	
	public static String getRowQuery(){
		return Term.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Term.TABLEQUERY;
	}
	
	public String getUpdateStatement(){
		return Term.ROWUPDATE;
	}
	
	static final private String IRIQUERY = 
			"SELECT id,source_id,generated_id FROM term where term.id = ?";

	public String getIRIQuery(){
		return Term.IRIQUERY;
	}

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCEID);
		domain = record.getInt(DBDOMAIN);
		authority = record.getInt(DBAUTHORITY);
		label = record.getString(DBLABEL);
		generated_id = record.getString(DBGENERATEDID);
		comment = record.getString(DBCOMMENT);
	}

	
	public int getId(){
		return id;
	}
	
	public String getIriString(){
		if (getSourceId() == null){
			return getGeneratedId();
		}
		else {
			return getSourceId();
		}
	}

	
	public String getSourceId(){
		return source_id;
	}

	public int getDomain(){
		return domain;
	}
	
	public int getAuthority(){
		return authority;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getComment(){
		return comment;
	}
	
	public String getGeneatedID(){
		return generated_id;
	}

	public void setGeneratedId(String new_id){
		generated_id = new_id;
	}
	
	public String getGeneratedId(){
		return generated_id;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder b) {
		// TODO Auto-generated method stub
		return null;
	}


}
