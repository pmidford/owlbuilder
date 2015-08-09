package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;


public interface AbstractConnection {

	/**
	 * Methods for connecting to an arachadmin database or mock of the same.
	 * Many methods return a single row from a table, as specified by an id,
	 * and represented by a bean appropriate to the table being queried
	 * or a set of ids (frequently filtered by an SQL where clause) or a table
	 * consisting of a set of beans.  Update methods operate on a single row and
	 * usually just update the generated_id column for that row, since that is the
	 * only field that can be filled at generation time.
	 * Not every table has methods for every type of access.
	 * Note that the implementation of beans is limited to strings, integers, and
	 * sets of integers (doubles aren't used but wouldn't break the design).  One to
	 * many and many to many are each captured as sets of indices (currently limited
	 * to integers, though that's a design pattern (imposed by web2py) that will be 
	 * tossed when web2py is replaced in the curation tool.
	 * @author pmidford
	 */
	

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
	Set<ClaimBean> getClaimTable() throws Exception;

	/**
	 * 
	 */
	void updateClaim(ClaimBean c) throws SQLException;

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
	Set<PublicationBean> getPublicationTable() throws SQLException;

	/**
	 * 
	 * @param claimId id of claim with associated participants
	 * @return id's of participant beans associated with claim
	 * @throws SQLException
	 */
	public Set<Integer> getParticipantSet(int claimId) throws Exception;

	/**
	 * 
	 * @param claimId id of claim with associated participants
	 * @return id's of participant beans associated with claim
	 * @throws SQLException
	 */
	public Set<ParticipantBean> getParticipantTable(int claimId) throws Exception;


	/**
	 * 
	 * @return bean associated with id
	 */
	public ParticipantBean getParticipant(int id) throws Exception;

	/**
	 * @param p specifies the participant that packages these elements
	 * @throws SQLException
	 */
	Set<Integer> getPElementSet(ParticipantBean p) throws Exception;

	/**
	 * 
	 * @param p
	 * @return set of PElementBeans associated with p's participant
	 * @throws Exception
	 */
	Set<PElementBean> getPElementTable(ParticipantBean p) throws Exception;


	/**
	 * 
	 * @param id
	 * @return bean reflecting record indexed by id
	 * @throws Exception
	 */
	PElementBean getPElement(int id) throws Exception;

	/**
	 * 
	 * @return set of taxa in TaxonTable as beans
	 * @throws SQLException
	 */
	Set<TaxonBean>getTaxonTable() throws SQLException;

	/**
	 * returns a single (curator added) taxon 
	 * @param id integer id of the (curator added) taxon to return
	 * @return object filled with the fields from the taxon requested
	 * @throws SQLException
	 */
	TaxonBean getTaxonRow(int id) throws SQLException;


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
	 * 
	 * @return
	 * @throws SQLException
	 */
	Set<NarrativeBean>getNarrativeTable() throws SQLException;


	
	/**
	 * @param pId index of property record
	 * @return bean containing record of owl property indexed by id
	 * @throws Exception 
	 */
	PropertyBean getProperty(int pId) throws Exception;

	/**
	 * This is the right way to access properties.
	 * @param uid unique string id (generally a uri) for the property
	 * @return bean filled from the appropriate row in the database
	 */
	PropertyBean getPropertyFromSourceId(String uid) throws Exception;

	/**
	 * @param setId web2py integer key
	 * @return bean filled from appropriate row in uids table
	 */
	UidSet getUidSet(int setId) throws Exception;

	/**
	 * 
	 * @return set of beans filled from appropriate row in uids table
	 */
	Set<UidSet> getUidSetTable() throws Exception;

	/**
	 * 
	 * @param s
	 * @throws SQLException
	 */
	void updateUidSet(UidSet s) throws SQLException;
	
	/**
	 * 
	 * @return next available serial number in generated id space - not the same as resource id
	 * @throws Exception
	 */
//	int scanPrivateIDs() throws Exception;

	/**
	 * 
	 * @param pb
	 * @throws Exception
	 */
	//void fillPElementTerm(PElementBean pb) throws Exception;

	/**
	 * 
	 * @param pb
	 * @throws Exception
	 */
	//void fillPElementIndividual(PElementBean pb) throws Exception;

	/**
	 * @return object responsible for tracking generated IRI's 
	 */
	public IRIManager getIRIManager();

	/**
	 * Called by IRIManager to get the highest valued generated id string
	 * @return last generated id
	 * @throws Exception
	 */
	String getUidSetLastGenId() throws Exception;



}
