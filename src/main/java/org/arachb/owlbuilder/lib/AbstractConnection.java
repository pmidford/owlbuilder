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
	 */
	void updateClaim(Claim c) throws SQLException;

	/**
	 * 
	 * @param p
	 * @throws SQLException
	 */
	void updatePublication(Publication p) throws SQLException;

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
	 * 
	 * @param p
	 * @throws SQLException
	 */
	void updateIndividualParticipant(IndividualParticipant p) throws SQLException;
	
	/**
	 * returns a single (curator added) taxon 
	 * @param id integer id of the (curator added) taxon to return
	 * @return object filled with the fields from the taxon requested
	 * @throws SQLException
	 */
	Taxon getTaxon(int id) throws SQLException;

	/**
	 * 
	 * @return all the taxa in the supplying resource
	 * @throws SQLException
	 */
	Set<Taxon> getTaxa() throws SQLException;

	/**
	 * 
	 * @param t
	 * @throws SQLException
	 */
	void updateTaxon(Taxon t) throws SQLException;

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

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	Set<Term> getTerms() throws SQLException;

	/**
	 * 
	 * @param t
	 * @throws SQLException
	 */
	void updateTerm(Term t) throws SQLException;
	
	/**
	 * 
	 * @param e
	 * @throws SQLException
	 */
	void updateNamedEntity(AbstractNamedEntity e) throws SQLException;

	/**
	 * 
	 * @param claimId
	 * @return
	 * @throws Exception
	 */
	Claim getClaim(int claimId) throws Exception;


	/**
	 * 
	 * @return next available serial number in generated id space - not the same as resource id
	 * @throws Exception
	 */
	int scanPrivateIDs() throws Exception;


}
