/**
 * 
 */
package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pmidford
 *
 */
public class TestTaxonBean {

    private static Logger log = Logger.getLogger(TestTaxonBean.class);

    private static AbstractConnection testConnection;
    
    private TaxonBean testbean;
    static final int TESTBEANID = 1;
    static final String TESTBEANURN = "urn:lsid:amnh.org:spidersp:013764";
    static final String TESTBEANNAME = "Leucauge mariana";
    static final String TESTBEANAUTHOR = "Emerton";
    static final String TESTBEANYEAR = "1884";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if (DBConnection.probeTestConnection()){
		    log.info("Testing with live connection");
		    testConnection = DBConnection.getTestConnection();
		}
		else{
		    log.info("Testing with mock connection");
		    testConnection = DBConnection.getMockConnection();
		}

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testbean = testConnection.getTaxonRow(TESTBEANID);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testConnection.close();
	}


	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getId()}.
	 */
	@Test
	public void testGetId() {
		assertEquals(1,testbean.getId());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getSourceId()}.
	 */
	@Test
	public void testGetSourceId() {
		assertEquals(TESTBEANURN,testbean.getSourceId());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getName()}.
	 */
	@Test
	public void testGetName() {
		assertEquals(TESTBEANNAME,testbean.getName());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_author()}.
	 */
	@Test
	public void testGet_author() {
		assertEquals(TESTBEANAUTHOR,testbean.get_author());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_year()}.
	 */
	@Test
	public void testGet_year() {
		assertEquals(TESTBEANYEAR,testbean.get_year());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getGeneratedId()}.
	 */
	@Test
	public void testGetGeneratedId() {
		log.info("generated id is " + testbean.getGeneratedId());
		assertNull(testbean.getGeneratedId());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#setGeneratedId(java.lang.String)}.
	 */
	@Test
	public void testSetGeneratedId() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getAuthority()}.
	 */
	@Test
	public void testGetAuthority() {
		assertNull(testbean.getAuthority());  //TODO ought to be WCS?
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentName()}.
	 */
	@Test
	public void testGetParentName() {
		assertNull(testbean.getParentName());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMerged()}.
	 */
	@Test
	public void testGetMerged() {
		assertFalse(testbean.getMerged()); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMergeStatus()}.
	 */
	@Test
	public void testGetMergeStatus() {
		assertNull(testbean.getMergeStatus()); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentRefId()}.
	 */
	@Test
	public void testGetParentRefId() {
		assertNull(testbean.getParentRefId()); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#updateDB(org.arachb.arachadmin.AbstractConnection)}.
	 */
	@Test
	public void testUpdateDB() {
		//TODO see message
		log.info("Need to implement TaxonBean.updateDB()");
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getIRIString()}.
	 */
	@Test
	public void testGetIRIString() {
		assertEquals(TESTBEANURN,testbean.getIRIString());
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#checkIRIString(org.arachb.arachadmin.IRIManager)}.
	 * @throws SQLException 
	 */
	@Test
	public void testCheckIRIString() throws SQLException {
		IRIManager manager = testConnection.getIRIManager();
		assertEquals(TESTBEANURN,testbean.checkIRIString(manager));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#flushCache()}.
	 */
	@Test
	public void testFlushCache() {
		assertTrue(TaxonBean.isCached(TESTBEANID));
		TaxonBean.flushCache();
		assertFalse(TaxonBean.isCached(TESTBEANID));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#isCached(int)}.
	 */
	@Test
	public void testIsCached() {
		TaxonBean.flushCache();
		assertFalse(TaxonBean.isCached(TESTBEANID));		
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getCached(int)}.
	 */
	@Test
	public void testGetCached() {
		TaxonBean.flushCache();
		testbean.cache();
		assertEquals(testbean,TaxonBean.getCached(TESTBEANID));
		TaxonBean.flushCache();
	}
	
	/**
	 * Test method for testing exceptions from {@link org.arachb.arachadmin.TaxonBean#getCached(int)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testGetCachedException() {
		TaxonBean.flushCache();
		TaxonBean foo = TaxonBean.getCached(TESTBEANID);
	}
	

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#cache()}.
	 */
	@Test
	public void testCache() {
		TaxonBean.flushCache();
		testbean.cache();
		assertEquals(testbean,TaxonBean.getCached(TESTBEANID));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#updatecache()}.
	 */
	@Test
	public void testUpdatecache() {
		testbean.updatecache();
	}

}
