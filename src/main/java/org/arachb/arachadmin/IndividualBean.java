package org.arachb.arachadmin;

import java.sql.SQLException;



/**
 * This bean supports individuals that appear in participants
 * @author pmidford
 *
 */
public class IndividualBean extends CachingBean implements UpdateableBean{
	
	static final int DBID = 1;
	static final int DBSOURCEID = 2;
	static final int DBGENERATEDID = 3;
	static final int DBLABEL= 4;
	static final int DBTERM = 5;

	
	private int id;
	private String source_id;
	private String generated_id;
	private String label;
	private int term;  //the individual's class
	
	

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCEID);
		generated_id = record.getString(DBGENERATEDID);
		label = record.getString(DBLABEL);
		term = record.getInt(DBTERM);
	}

	@Override
	public int getId() {
		return id;
	}
	
	public String getSourceId() {
		return source_id;
	}
	
	public String getGeneratedId() {
		return generated_id;
	}

	public String getLabel(){
		return label;
	}

	public int getTerm(){
		return term;
	}


	@Override
	public void setGeneratedId(String id) {
		generated_id = id;
		
	}

	final static String INDBADDOIGENID = "Individual has neither source id nor generated id";

	@Override
	public String getIRIString() {
		if (getSourceId() == null){
			if (getGeneratedId() == null){
				throw new IllegalStateException();
			}
			return getGeneratedId();
		}
		return getSourceId();
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		if (getGeneratedId() == null){
			manager.generateIRI(this);
		}
		return getGeneratedId();
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
