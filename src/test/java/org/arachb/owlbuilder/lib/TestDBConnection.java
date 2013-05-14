package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.TestOwlbuilder;
import org.junit.Before;
import org.junit.Test;

public class TestDBConnection {

    private static Logger log = Logger.getLogger(TestDBConnection.class);

    private DBConnection testConnection;
    
	@Before
	public void setUp() throws Exception {
    	testConnection = new DBConnection();
	}

	@Test
	public void testgetPublication() throws SQLException {
		Publication testPub = testConnection.getPublication(1);
		assertNotNull(testPub);
		assertEquals(1,testPub.get_id());
	}
	
	@Test
	public void testgetTerm_usage() throws SQLException{
		Usage testUsage = testConnection.getTerm_usage(1);
		assertNotNull(testUsage);
	}

}
