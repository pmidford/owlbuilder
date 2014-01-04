package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestDBConnection {

    //private static Logger log = Logger.getLogger(TestDBConnection.class);

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
		assertEquals(1,testPub.get_id());
		assertEquals("Journal",testPub.get_publication_type());
		assertEquals(doi1,testPub.get_doi());
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
		assertEquals(null,testPub.get_generated_id());
		Publication testPub2 = testConnection.getPublication(2);
		assertEquals(testID,testPub2.get_generated_id());		
		String saved_id = testPub2.get_generated_id();
		testPub2.setGeneratedID(doi1);
		testConnection.updateNamedEntity(testPub2);
		Publication updatedPub = testConnection.getPublication(2);
		assertEquals(doi1,updatedPub.getIRI_String());
		updatedPub.setGeneratedID(saved_id);
		testConnection.updateNamedEntity(updatedPub);
	}
	
	@Test 
	public void testgetTerm() throws SQLException{
		Term testTerm = testConnection.getTerm(1);
		assertNotNull(testTerm);
        assertEquals(1,testTerm.get_id());
		testTerm.setGeneratedID(testID);
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
		Term testTerm = testConnection.getTerm(1);
		assertNotNull(testTerm);
        String old_id = testTerm.get_generated_id();
		testTerm.setGeneratedID(testTaxon);
		testConnection.updateNamedEntity(testTerm);
		Term updatedTerm = testConnection.getTerm(1);
		assertEquals(testTaxon,updatedTerm.get_generated_id());
		updatedTerm.setGeneratedID(old_id);
		testConnection.updateNamedEntity(updatedTerm);
		Term updatedTerm2 = testConnection.getTerm(1);
		assertEquals(old_id,updatedTerm2.get_generated_id());
	}

	@Test
	public void testgetPrimaryParticipant() throws Exception{
		Assertion testAssertion = testConnection.getAssertion(1);
		Participant testParticipant = testConnection.getPrimaryParticipant(testAssertion);
		assertNotNull(testParticipant);
		assertEquals(1,testParticipant.get_id());  //not really the best test...
		assertEquals(1,testParticipant.get_taxon());
		assertEquals(2,testParticipant.get_anatomy());
		assertEquals(testTaxon,testParticipant.get_taxonIRI());
		assertEquals(testAnatomy,testParticipant.get_anatomyIRI());
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
		assertEquals(1,testAssertion.get_id());
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
		testAssertion.setGeneratedID(testID);
		testConnection.updateNamedEntity(testAssertion);
		Assertion updatedAssertion = testConnection.getAssertion(1);
		assertEquals(testID,updatedAssertion.getIRI_String());
		updatedAssertion.setGeneratedID("");
		testConnection.updateNamedEntity(updatedAssertion);
		Assertion updatedAssertion2 = testConnection.getAssertion(1);
		assertEquals("",updatedAssertion2.getIRI_String());  //bogus test here
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
