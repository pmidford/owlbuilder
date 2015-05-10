package org.arachb.arachadmin;

import java.sql.SQLException;



public class PropertyBean extends CachingBean {

	final static int DBID = 1;
	final static int DBSOURCEID = 2;
	final static int DBAUTHORITY = 3;
	final static int DBLABEL = 4;
	final static int DBGENERATEDID = 5;
	final static int DBCOMMENT = 6;

	private int id;
	private String sourceId;
	private int authority;
	private String label;
	private String generatedId;  //this is dubious
	private String comment;
	
	
	public final static String INDIVIDUALQUANTIFIER = "INDIVIDUAL";
	public final static String SOMEQUANTIFIER = "SOME";
	


	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		sourceId = record.getString(DBSOURCEID);
		authority = record.getInt(DBAUTHORITY);
		label = record.getString(DBLABEL);
		generatedId = record.getString(DBGENERATEDID);
		comment = record.getString(DBCOMMENT);		
	}
	
	
	public String getSourceId(){
		return sourceId;
	}

	public int getAuthority(){
		return authority;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getGeneratedId(){
		return generatedId;
	}
	
	public String getComment(){
		return comment;
	}
	
	
	
}
