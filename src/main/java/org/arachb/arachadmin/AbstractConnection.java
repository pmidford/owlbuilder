package org.arachb.arachadmin;

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
	 * @param a claim with associated participants
	 * @return participant beans associated with claim
	 * @throws SQLException
	 */
	Set<ParticipantBean> getParticipants(ClaimBean a) throws Exception;
	
	/**
	 * 
	 * @param a holds the claim
	 * @param p specifies the property (e.g., activelyparticipatesin)
	 * @throws SQLException
	 */
	Set<ParticipantBean> getParticipantsWithProperty(ClaimBean a, Object p);
	
	/**
	 * 
	 * @param p
	 * @throws SQLException
	 */
	void updateParticipant(ParticipantBean p) throws SQLException;
	
	/**
	 * @param p specifies the participant that packages these elements
	 * @throws SQLException
	 */
	Set<PElementBean> getPElements(ParticipantBean p) throws Exception;
	
	
	/**
	 * 
	 * @param id
	 * @return bean reflecting record indexed by id
	 * @throws Exception
	 */
	PElementBean getPElement(int id) throws Exception;

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
	 * @return set of all term records in database
	 * @throws SQLException
	 */
	Set<TermBean> getTerms() throws SQLException;
	//TODO why?
	
	/**
	 * 
	 * @param termBean
	 */
	void updateTerm(TermBean termBean) throws SQLException;

	
	/**
	 * should update the database record corresponding to the entity
	 * @param e
	 * @throws SQLException
	 */
	void updateNamedEntity(UpdateableBean b) throws SQLException;

	
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
	 * @param t
	 * @throws SQLException
	 */
	void updateIndividual(IndividualBean t) throws SQLException;
	
	
	/**
	 * 
	 * @param nId
	 * @return bean for narrative record indexed by id
	 * @throws SQLException
	 */
	NarrativeBean getNarrative(int nId) throws SQLException;
	

	/**
	 * @param pId index of property record
	 * @return bean containing record of owl property indexed by id
	 * @throws Exception 
	 */
	PropertyBean getProperty(int pId) throws Exception;

	/**
	 * 
	 * @return next available serial number in generated id space - not the same as resource id
	 * @throws Exception
	 */
	int scanPrivateIDs() throws Exception;

	/**
	 * 
	 * @param pb
	 * @throws Exception
	 */
	void fillPElementTerm(PElementBean pb) throws Exception;

	/**
	 * 
	 * @param pb
	 * @throws Exception
	 */
	void fillPElementIndividual(PElementBean pb) throws Exception;

	/**
	 * 
	 * @param result
	 * @throws Exception
	 */
	void fillPElementParents(PElementBean result) throws Exception;

	/**
	 * 
	 * @param result
	 * @throws Exception
	 */
	void fillPElementChildren(PElementBean result) throws Exception;


}
