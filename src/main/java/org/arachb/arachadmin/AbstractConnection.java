package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.arachb.owlbuilder.lib.AbstractNamedEntity;

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
	Set<ClaimBean> getClaims() throws Exception;

	/**
	 * returns a single publication
	 * @param pubId integer id of the publication to return
	 * @return object filled with the fields from the publication requested
	 * @throws SQLException
	 */
	PublicationBean getPublication(int pubId) throws SQLException;

	/**
	 * 
	 * @return all the publications in the supplying resource
	 * @throws SQLException
	 */
	Set<PublicationBean> getPublications() throws SQLException;
	
	/**
	 * 
	 */
	void updateClaim(ClaimBean c) throws SQLException;

	/**
	 * 
	 * @param p
	 * @throws SQLException
	 */
	void updatePublication(PublicationBean p) throws SQLException;

	/**
	 * 
	 * @param a 
	 * @return object filled with the fields of the participant requested
	 * @throws Exception
	 */
	ParticipantBean getPrimaryParticipant(ClaimBean a) throws Exception;

	/**
	 * 
	 * @param a
	 * @return
	 * @throws SQLException
	 */
	Set<ParticipantBean> getParticipants(ClaimBean a) throws SQLException;
	
	/**
	 * 
	 * @param p
	 * @throws SQLException
	 */
	void updateParticipant(ParticipantBean p) throws SQLException;
	
	/**
	 * returns a single (curator added) taxon 
	 * @param id integer id of the (curator added) taxon to return
	 * @return object filled with the fields from the taxon requested
	 * @throws SQLException
	 */
	TaxonBean getTaxon(int id) throws SQLException;

	/**
	 * 
	 * @return all the taxa in the supplying resource
	 * @throws SQLException
	 */
	Set<TaxonBean> getTaxa() throws SQLException;

	/**
	 * 
	 * @param t
	 * @throws SQLException
	 */
	void updateTaxon(TaxonBean t) throws SQLException;

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
	TermBean getTerm(int termId) throws SQLException;

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	Set<TermBean> getTerms() throws SQLException;

	/**
	 * 
	 * @param t
	 * @throws SQLException
	 */
	void updateTerm(TermBean t) throws SQLException;
	
	/**
	 * should update the database record corresponding to the entity
	 * @param e
	 * @throws SQLException
	 */
	void updateNamedEntity(AbstractNamedEntity e) throws SQLException;

	/**
	 * returns representation of a single claim
	 * @param claimId
	 * @return object filled with fields from the claim requested
	 * @throws Exception
	 */
	ClaimBean getClaim(int claimId) throws Exception;
	
	/**
	 * returns a single individual
	 * @param inId integer id of the individual to return
	 * @return object filled with the fields from the individual requested
	 * @throws SQLException
	 */
	IndividualBean getIndividual(int inId) throws SQLException;



	/**
	 * 
	 * @return next available serial number in generated id space - not the same as resource id
	 * @throws Exception
	 */
	int scanPrivateIDs() throws Exception;


}
