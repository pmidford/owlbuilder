package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface AbstractConnection {

	/**
	 * performs query and returns a mapping from uri's of support ontologies used
	 * in this build to their domains (e.g., taxonomy, anatomy, behavior, etc.)
	 * @return map uri -> domain string
	 * @throws Exception
	 */
	Map<String, String> loadImportSourceMap() throws Exception;

	/**
	 * Closes whatever database or resource is supplying data to this connection
	 * @throws Exception
	 */
	void close() throws Exception;

	/**
	 * 
	 * @return all the claims in the supplying resource
	 * @throws Exception
	 */
	Set<Claim> getClaims() throws Exception;

	/**
	 * returns a single publication
	 * @param pubId integer id of the publication to return
	 * @return object filled with the fields from the publication requested
	 * @throws SQLException
	 */
	Publication getPublication(int pubId) throws SQLException;

	/**
	 * 
	 * @return all the publications in the supplying resource
	 * @throws SQLException
	 */
	Set<Publication> getPublications() throws SQLException;

	/**
	 * 
	 * @param a 
	 * @return object filled with the fields of the participant requested
	 * @throws Exception
	 */
	Participant getPrimaryParticipant(Claim a) throws Exception;

	/**
	 * 
	 * @param a
	 * @return
	 * @throws SQLException
	 */
	Set<Participant> getParticipants(Claim a) throws SQLException;

	/**
	 * performs query and returns a mapping from uri's of support ontologies used
	 * in this build to human readable names of the ontologies
	 * @return map uri -> name string
	 * @throws SQLException
	 */
	Map<String, String> loadOntologyNamesForLoading() throws SQLException;

	/**
	 * returns a single term
	 * @param termId integer id of the term to return
	 * @return object filled with the fields from the publication requested
	 * @throws SQLException
	 */
	Term getTerm(int termId) throws SQLException;

	Set<Term> getTerms() throws SQLException;

	Claim getClaim(int i) throws Exception;

	void updateNamedEntity(AbstractNamedEntity e) throws SQLException;

	/**
	 * 
	 * @return next available serial number in generated id space - not the same as resource id
	 * @throws Exception
	 */
	int scanPrivateIDs() throws Exception;


}
