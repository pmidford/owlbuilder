package org.arachb.owlbuilder.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBResults implements AbstractResults {

	private ResultSet results;
	
	DBResults(ResultSet r){
		results = r;
	}
	
	@Override
	public int getInt(String field) throws SQLException{
		return results.getInt(field);
	}

	@Override
	public String getString(String field) throws SQLException{
		return results.getString(field);
	}

	@Override
	public boolean next() throws SQLException {
		return results.next();
	}

}
