package org.arachb.arachadmin;

import java.sql.SQLException;

public interface AbstractResults {
	
	int getInt(int field) throws SQLException;
	
	String getString(int field) throws SQLException;
	
	boolean getBoolean(int field) throws SQLException;
	
	boolean next() throws SQLException;
	
	void close() throws SQLException;

}
