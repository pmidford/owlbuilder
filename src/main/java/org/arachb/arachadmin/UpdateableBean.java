package org.arachb.arachadmin;

import java.sql.SQLException;





public interface UpdateableBean extends BeanBase {
		
	public void setGeneratedId(String id);
		
	/**
	 * need to save the original generated id so old generated id's can
	 * be captured using SAMEAS
	 * @return raw value of generatedId field
	 **/
	public String getGeneratedId();

	/**
	 * 
	 * @return either assigned or generated identifier that can parse into an IRI
	 */
	public String getIRIString() throws IllegalStateException;

	/**
	 * Non exception throwing version for use when generating IRI's
	 * probably better than a catch in the appropriate place
	 * @return string for making IRI's
	 **/
	public String checkIRIString(IRIManager manager) throws SQLException;
	
	/**
	 * 
	 * @param c
	 * @throws SQLException 
	 */
	public void updateDB(AbstractConnection c) throws SQLException;

	
}
