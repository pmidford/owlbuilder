package org.arachb.arachadmin;

import java.sql.ResultSet;
import java.sql.SQLException;


public class DBResults implements AbstractResults {

	private ResultSet results;
	
	DBResults(ResultSet r){
		results = r;
	}
	
	@Override
	public int getInt(int field) throws SQLException{
		return results.getInt(field);
	}

	@Override
	public String getString(int field) throws SQLException{
		return results.getString(field);
	}
	
	@Override
	public boolean getBoolean(int field) throws SQLException{
		return results.getBoolean(field);
	}

	@Override
	public boolean next() throws SQLException {
		return results.next();
	}

	@Override
	public void close() throws SQLException {
		results.close();
	}

}
