package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class TestDBConnection {

    //private static Logger log = Logger.getLogger(TestDBConnection.class);

    private DBConnection testConnection;
    
	@Before
	public void setUp() throws Exception {
    	testConnection = DBConnection.getTestConnection();
	}

	@Test
	public void testgetPublication() throws SQLException {
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(1,testPub.get_id());
	}
	
	@Test
	public void testgetAssertion() throws SQLException{
		Assertion testAssertion = testConnection.getAssertion(1);
		assertNotNull(testAssertion);
	}

}
