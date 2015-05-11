package org.arachb.arachadmin;

import java.sql.SQLException;


public interface BeanBase {

	/**
	 * 
	 * @return internal id - unique across all database records of a particular AE subtype
	 */
	public int getId();

	/**
	 * Method for filling the fields of a new instance of an AE subtype; 
	 * @param record key value pairs to fill instance from - typically wraps an SQL ResultSet
	 * @throws SQLException
	 */
	void fill(AbstractResults record) throws SQLException;

}
