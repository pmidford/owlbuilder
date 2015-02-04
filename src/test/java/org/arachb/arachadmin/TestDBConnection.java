package org.arachb.arachadmin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
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
			log.info("Testing with live connection");
			testConnection = DBConnection.getTestConnection();
		}
		else{
			log.info("Testing with mock connection");
			testConnection = DBConnection.getMockConnection();
		}
	}

	final static String doi3 = "http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	@Test
	public void testgetPublication() throws SQLException {
		PublicationBean testPub = testConnection.getPublication(3);
		assertNotNull(testPub);
		assertEquals(3,testPub.getId());
		assertEquals("Journal",testPub.getPublicationType());
		assertEquals(doi3,testPub.getDoi());
	}

	@Test
	public void testgetPublications() throws SQLException{
		Set<PublicationBean> testSet = testConnection.getPublications();
		assertNotNull(testSet);
		assertEquals(3,testSet.size());
	}

	@Test
	public void testupdatePublication() throws SQLException{
		PublicationBean testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		//assertEquals(null,testPub.getGeneratedId());
		PublicationBean testPub3 = testConnection.getPublication(3);
		assertEquals(testID,testPub3.getGeneratedId());		
		String saved_id = testPub3.getGeneratedId();
		testPub3.setGeneratedId(doi3);
		testConnection.updatePublication(testPub3);
		PublicationBean updatedPub = testConnection.getPublication(3);
		assertEquals(doi3,updatedPub.getGeneratedId());
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
	public void testgetPElements() throws SQLException{
		
	}
	
	@Test
	public void testgetPElement() throws Exception{
		PElementBean testElement = testConnection.getPElement(1);
		assertNotNull(testElement);
		
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
		//TODO make this test something?
	}

	
	@Test
	public void testgetClaim() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		assertNotNull(testClaim);
		assertEquals(1,testClaim.getId());
	}
	
	
	@Test
	public void testupdateClaim() throws Exception{
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
