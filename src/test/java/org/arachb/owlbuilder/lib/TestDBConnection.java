package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

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
    private final static String testAnatomy = "http://purl.obolibrary.org/obo/SPD_0000001";
    
	@Before
	public void setUp() throws Exception {
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
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(1,testPub.getId());
		assertEquals("Journal",testPub.getPublicationType());
		assertEquals(doi1,testPub.getDoi());
	}

	@Test
	public void testgetPublications() throws SQLException{
		Set<Publication> testSet = testConnection.getPublications();
		assertNotNull(testSet);
		assertEquals(2,testSet.size());
	}

	@Test
	public void testupdatePublication() throws SQLException{
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(null,testPub.getGeneratedId());
		Publication testPub2 = testConnection.getPublication(2);
		assertEquals(testID,testPub2.getGeneratedId());		
		String saved_id = testPub2.getGeneratedId();
		testPub2.setGeneratedId(doi1);
		testConnection.updateNamedEntity(testPub2);
		Publication updatedPub = testConnection.getPublication(2);
		assertEquals(doi1,updatedPub.getIriString());
		updatedPub.setGeneratedId(saved_id);
		testConnection.updateNamedEntity(updatedPub);
	}
	
	@Test 
	public void testgetTerm() throws SQLException{
		Term testTerm = testConnection.getTerm(1);
		assertNotNull(testTerm);
        assertEquals(1,testTerm.getId());
		testTerm.setGeneratedId(testID);
		testConnection.updateNamedEntity(testTerm);
	}
	
	@Test
	public void testgetTerms() throws SQLException{
		Set<Term> testSet = testConnection.getTerms();
		assertNotNull(testSet);
		assertEquals(2,testSet.size());
	}

	@Test
	public void testupdateTerm() throws SQLException{
		Term testTerm = testConnection.getTerm(2);
		assertNotNull(testTerm);
        String old_id = testTerm.getGeneratedId();
		testTerm.setGeneratedId(testAnatomy);
		testConnection.updateNamedEntity(testTerm);
		Term updatedTerm = testConnection.getTerm(2);
		assertEquals(testAnatomy,updatedTerm.getGeneratedId());
		updatedTerm.setGeneratedId(old_id);
		testConnection.updateNamedEntity(updatedTerm);
		Term updatedTerm2 = testConnection.getTerm(2);
		assertEquals(old_id, updatedTerm2.getGeneratedId());
	}

	@Test
	public void testgetPrimaryParticipant() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		Participant testParticipant = testConnection.getPrimaryParticipant(testAssertion);
		assertNotNull(testParticipant);
		assertEquals(1,testParticipant.getId());  //not really the best test...
		assertEquals(1,testParticipant.getTaxon());
		assertEquals(2,testParticipant.getAnatomy());
		assertEquals(testTaxon,testParticipant.getTaxonIri());
		assertEquals(testAnatomy,testParticipant.getAnatomyIri());
	}
	
	@Test
	public void testgetParticipants() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		Set<Participant> testSet = testConnection.getParticipants(testAssertion);
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
	}
	
	@Test
	public void testupdateParticipant() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		Participant testParticipant = testConnection.getPrimaryParticipant(testAssertion);
		assertNotNull(testParticipant);
		//testParticipant. (testID);
		//testConnection.updateParticipant(testParticipant);
		//Participant updatedParticipant = testConnection.getPrimaryParticipant(testAssertion);
		//assertEquals(testID,updatedParticipant.get_generated_id());
		//updatedParticipant.set_generated_id("");
		//testConnection.updateParticipant(updatedParticipant);
		//Participant updatedParticipant2 = testConnection.getPrimaryParticipant(testAssertion);
		//assertEquals("",updatedParticipant2.get_generated_id());
	}

	
	@Test
	public void testgetAssertion() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		assertNotNull(testAssertion);
		assertEquals(1,testAssertion.getId());
	}
	
	@Test
	public void testgetAssertions() throws Exception{
		Set<Assertion> testSet = testConnection.getAssertions();
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
	}
	
	@Test
	public void testupdateAssertion() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		assertNotNull(testAssertion);
		testAssertion.setGeneratedId(testID);
		testConnection.updateNamedEntity(testAssertion);
		Assertion updatedAssertion = testConnection.getAssertion(1);
		assertEquals(testID,updatedAssertion.getIriString());
		updatedAssertion.setGeneratedId("");
		testConnection.updateNamedEntity(updatedAssertion);
		Assertion updatedAssertion2 = testConnection.getAssertion(1);
		assertEquals("",updatedAssertion2.getIriString());  //bogus test here
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
