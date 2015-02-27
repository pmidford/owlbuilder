package org.arachb.arachadmin;

import java.sql.SQLException;

import org.arachb.owlbuilder.Owlbuilder;
import org.arachb.owlbuilder.lib.AbstractNamedEntity;
import org.semanticweb.owlapi.model.OWLObject;

public class TermBean extends CachingBean{


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
		return source_id;
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
	
	



}
