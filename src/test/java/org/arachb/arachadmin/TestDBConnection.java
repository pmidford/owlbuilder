package org.arachb.arachadmin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.lib.Publication;
import org.junit.Before;
import org.junit.Test;

public class TestDBConnection {

    private static Logger log = Logger.getLogger(TestDBConnection.class);

    private AbstractConnection testConnection;
    private final static String testID = "http://arachb.org/arachb/TEST_0000001";
    private final static String testTaxon = "http://purl.obolibrary.org/obo/NCBITaxon_336608";
    private final static String testPubTaxon = "Tetragnatha straminea";
    private final static String testAnatomy = "http://purl.obolibrary.org/obo/SPD_0000001";
    
	@Before
	public void setup() throws Exception {
		if (DBConnection.testConnection()){
			testConnection = DBConnection.getTestConnection();
		}
		else{
			testConnection = DBConnection.getMockConnection();
		}
	}

	final static String doi1 = "http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	@Test
	public void testgetPublication() throws SQLException {
		PublicationBean testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(1,testPub.getId());
		assertEquals("Journal",testPub.getPublicationType());
		assertEquals(doi1,testPub.getDoi());
	}

	@Test
	public void testgetPublications() throws SQLException{
		Set<PublicationBean> testSet = testConnection.getPublications();
		assertNotNull(testSet);
		assertEquals(2,testSet.size());
	}

	@Test
	public void testupdatePublication() throws SQLException{
		PublicationBean testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(null,testPub.getGeneratedId());
		PublicationBean testPub2 = testConnection.getPublication(2);
		assertEquals(testID,testPub2.getGeneratedId());		
		String saved_id = testPub2.getGeneratedId();
		testPub2.setGeneratedId(doi1);
		testConnection.updatePublication(testPub2);
		PublicationBean updatedPub = testConnection.getPublication(2);
		assertEquals(doi1,updatedPub.getGeneratedId());
		updatedPub.setGeneratedId(saved_id);
		testConnection.updatePublication(updatedPub);
	}
	
	@Test 
	public void testgetTerm() throws SQLException{
		TermBean testTerm = testConnection.getTerm(1);
		assertNotNull(testTerm);
        assertEquals(1,testTerm.getId());
		testTerm.setGeneratedId(testID);
		testConnection.updateTerm(testTerm);
	}
	
	@Test
	public void testgetTerms() throws SQLException{
		Set<TermBean> testSet = testConnection.getTerms();
		assertNotNull(testSet);
		assertEquals(2,testSet.size());
	}

	@Test
	public void testupdateTerm() throws SQLException{
		TermBean testTerm = testConnection.getTerm(2);
		assertNotNull(testTerm);
        String old_id = testTerm.getGeneratedId();
		testTerm.setGeneratedId(testAnatomy);
		testConnection.updateNamedEntity(testTerm);
		TermBean updatedTerm = testConnection.getTerm(2);
		assertEquals(testAnatomy,updatedTerm.getGeneratedId());
		updatedTerm.setGeneratedId(old_id);
		testConnection.updateNamedEntity(updatedTerm);
		TermBean updatedTerm2 = testConnection.getTerm(2);
		assertEquals(old_id, updatedTerm2.getGeneratedId());
	}

	@Test
	public void testgetPrimaryParticipant() throws Exception{
		ClaimBean testAssertion = testConnection.getClaim(1);
		ParticipantBean testParticipant = testConnection.getPrimaryParticipant(testAssertion);
		assertNotNull(testParticipant);
		assertEquals(1,testParticipant.getId());  //not really the best test...
		assertEquals(1,testParticipant.getTaxon());
		assertEquals(2,testParticipant.getAnatomy());
		assertEquals(testTaxon,testParticipant.getTaxonIri());
		assertEquals(testAnatomy,testParticipant.getAnatomyIri());
		assertEquals(testPubTaxon,testParticipant.getPublicationTaxon());
	}
	
	@Test
	public void testgetParticipants() throws Exception{
		ClaimBean testAssertion = testConnection.getClaim(1);
		Set<ParticipantBean> testSet = testConnection.getParticipants(testAssertion);
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
		for (ParticipantBean p : testSet){
			assertEquals(2,p.getId());
			assertEquals(1,p.getTaxon());
			assertEquals(testTaxon,p.getTaxonIri());
			assertEquals(testPubTaxon,p.getPublicationTaxon());
		}
	}
	
	@Test
	public void testupdateParticipant() throws Exception{
		ClaimBean testAssertion = testConnection.getClaim(1);
		ParticipantBean testParticipant = testConnection.getPrimaryParticipant(testAssertion);
		assertNotNull(testParticipant);
	}

	
	@Test
	public void testgetAssertion() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		assertNotNull(testClaim);
		assertEquals(1,testClaim.getId());
	}
	
	@Test
	public void testgetAssertions() throws Exception{
		Set<ClaimBean> testSet = testConnection.getClaims();
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
	}
	
	@Test
	public void testupdateAssertion() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		assertNotNull(testClaim);
		testClaim.setGeneratedId(testID);
		testConnection.updateClaim(testClaim);
		ClaimBean updatedClaim = testConnection.getClaim(1);
		assertEquals(testID,updatedClaim.getGeneratedId());
		updatedClaim.setGeneratedId("");
		testConnection.updateClaim(updatedClaim);
		ClaimBean updatedAssertion2 = testConnection.getClaim(1);
		assertEquals("",updatedAssertion2.getGeneratedId());  //bogus test here
	}
	
	@Test
	public void testloadImportSourceMap() throws Exception{
		Map<String,String> testmap = testConnection.loadImportSourceMap();
		assertNotNull(testmap);
		assertEquals(8,testmap.size());
	}
	
	@Test
	public void testloadOntologyNamesForLoading() throws Exception{
		Map<String,String> testmap = testConnection.loadImportSourceMap();
		assertNotNull(testmap);
		assertEquals(8,testmap.size());
		
	}

}
