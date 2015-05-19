package org.arachb.arachadmin;

import java.sql.SQLException;

public class NarrativeBean implements BeanBase {
		
	static final int DBID = 1;
	static final int DBPUBLICATION = 2;
	static final int DBLABEL = 3;
	static final int DBDESCRIPTION = 4;
	
	private int id;
	private int publicationid;
	private String label;
	private String description;

	
	
	/* accessors */
	
	@Override
	public int getId() {
		return id;
	}
	
	public int getPublicationId(){
		return publicationid;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getDescription(){
		return description;
	}

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		publicationid = record.getInt(DBPUBLICATION);
		label = record.getString(DBLABEL);
		description = record.getString(DBDESCRIPTION);
	}

}
