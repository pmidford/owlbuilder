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
    private final static String testID = "http://arachb.org/arachb/ARACHB_0000001";
    private final static String testGenID = "http://arachb.org/arachb/ARACHB_0000099";
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
		assertEquals(testGenID,testPub.getGeneratedId());
		PublicationBean testPub3 = testConnection.getPublication(3);
		assertEquals(null,testPub3.getGeneratedId());		
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
		assertEquals(3,testSet.size());
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
	public void testgetParticipants() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		Set<ParticipantBean> testSet = testConnection.getParticipants(testClaim);
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
		for (ParticipantBean p : testSet){
			assertEquals(1,p.getId());
			assertEquals(testPubTaxon,p.getPublicationTaxon());
			assertEquals(1,p.getHeadElement());
		}
	}
	
	@Test
	public void testupdateParticipant() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		Set<ParticipantBean> testSet = testConnection.getParticipants(testClaim);
		//TODO make this test something?
	}

	
	
	@Test
	public void testgetPElements() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		Set<ParticipantBean> testSet = testConnection.getParticipants(testClaim);
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
		for (ParticipantBean p : testSet){
			Set<PElementBean> elements = testConnection.getPElements(p);
			assertEquals(2,elements.size());
		}
	}
	
	
	@Test
	public void testgetPElement() throws Exception{
		PElementBean testElement = testConnection.getPElement(1);
		assertNotNull(testElement);
		
	}
	
	
	final static String ACTIVELYPARTICIPATESINURL = "http://purl.obolibrary.org/obo/RO_0002217";
	final static String ACTIVELYPARTICIPATESINLABEL = "actively participates in";
	
	@Test
	public void testgetProperty() throws Exception{
		PropertyBean testProperty = testConnection.getProperty(306);
		assertNotNull(testProperty);
		assertEquals(306,testProperty.getId());
		assertEquals(ACTIVELYPARTICIPATESINURL,testProperty.getSourceId());
		assertEquals(0,testProperty.getAuthority());
		assertEquals(ACTIVELYPARTICIPATESINLABEL,testProperty.getLabel());
		assertEquals(null,testProperty.getGeneratedId());
		assertEquals(null,testProperty.getComment());
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
