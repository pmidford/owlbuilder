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
    
	@Before
	public void setUp() throws Exception {
		if (DBConnection.testConnection()){
			testConnection = DBConnection.getTestConnection();
		}
		else{
			testConnection = DBConnection.getMockConnection();
		}
	}

	@Test
	public void testgetPublication() throws SQLException {
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(1,testPub.get_id());
		assertEquals("Journal",testPub.get_publication_type());
		assertEquals("",testPub.get_generated_id());
	}

	@Test
	public void testgetPublications() throws SQLException{
		Set<Publication> testSet = testConnection.getPublications();
		assertNotNull(testSet);
		assertEquals(1,testSet.size());
	}

	@Test
	public void testupdatePublication() throws SQLException{
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		testPub.set_generated_id(testID);
		testConnection.updatePublication(testPub);
		Publication updatedPub = testConnection.getPublication(1);
		assertEquals(testID,updatedPub.get_generated_id());
		updatedPub.set_generated_id("");
		testConnection.updatePublication(updatedPub);
		Publication updatePub2 = testConnection.getPublication(1);
		assertEquals("",updatePub2.get_generated_id());
	}
	
	@Test 
	public void testgetTerm() throws SQLException{
		Term testTerm = testConnection.getTerm(1);
		assertNotNull(testTerm);
        assertEquals(1,testTerm.get_id());
//		testTerm.set_generated_id(testID);
//		testConnection.updateTerm(testTerm);
	}
	
	@Test
	public void testgetTerms() throws SQLException{
//		Set<Term> testSet = testConnection.getTerms();
//		assertNotNull(testSet);
//		assertEquals(1,testSet.size());
	}

	@Test
	public void testupdateTerm() throws SQLException{
//		Term testTerm = testConnection.getTerm(1);
//		assertNotNull(testTerm);
//		testTerm.set_generated_id(testID);
//		testConnection.updateTerm(testTerm);
//		Term updatedTerm = testConnection.getTerm(1);
//		assertEquals(testID,updatedTerm.get_generated_id());
//		updatedTerm.set_generated_id("");
//		testConnection.updateTerm(updatedTerm);
//		Term updatedTerm2 = testConnection.getTerm(1);
//		assertEquals("",updatedTerm2.get_generated_id());
	}

	@Test
	public void testgetAssertion() throws SQLException{
//		Assertion testAssertion = testConnection.getAssertion(1);
//		assertNotNull(testAssertion);
//		assertEquals(1,testAssertion.get_id());
	}
	
	@Test
	public void testgetAssertions() throws SQLException{
//		Set<Assertion> testSet = testConnection.getAssertions();
//		assertNotNull(testSet);
//		assertEquals(1,testSet.size());
	}
	
	@Test
	public void testupdateAssertion() throws SQLException{
//		Assertion testAssertion = testConnection.getAssertion(1);
//		assertNotNull(testAssertion);
//		testAssertion.set_generated_id(testID);
//		testConnection.updateAssertion(testAssertion);
//		Assertion updatedAssertion = testConnection.getAssertion(1);
//		assertEquals(testID,updatedAssertion.get_generated_id());
//		updatedAssertion.set_generated_id("");
//		testConnection.updateAssertion(updatedAssertion);
//		Assertion updatedAssertion2 = testConnection.getAssertion(1);
//		assertEquals("",updatedAssertion2.get_generated_id());
	}
	
	@Test
	public void testloadImportSourceMap() throws Exception{
//		Map<String,String> testmap = testConnection.loadImportSourceMap();
//		assertNotNull(testmap);
//		assertTrue(testmap.size()>0);
	}
	
	

}
