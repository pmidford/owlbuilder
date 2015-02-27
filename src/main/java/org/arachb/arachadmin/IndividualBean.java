package org.arachb.arachadmin;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.arachb.owlbuilder.lib.AbstractNamedEntity;
import org.semanticweb.owlapi.model.OWLObject;


/**
 * This bean supports individuals that appear in participants
 * @author pmidford
 *
 */
public class IndividualBean extends CachingBean implements AbstractNamedEntity{
	
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
	
	
	private static Logger log = Logger.getLogger(IndividualBean.class);


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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIriString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object checkIriString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
