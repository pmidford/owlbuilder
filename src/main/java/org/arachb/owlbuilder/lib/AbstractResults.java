package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

public interface AbstractResults {
	
	int getInt(String field) throws SQLException;
	
	String getString(String field) throws SQLException;
	
	boolean next() throws SQLException;

}
