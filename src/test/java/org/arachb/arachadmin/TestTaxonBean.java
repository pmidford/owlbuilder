/**
 * 
 */
package org.arachb.arachadmin;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#fill(org.arachb.arachadmin.AbstractResults)}.
	 */
	@Test
	public void testFill() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getId()}.
	 */
	@Test
	public void testGetId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getSourceId()}.
	 */
	@Test
	public void testGetSourceId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getName()}.
	 */
	@Test
	public void testGetName() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_author()}.
	 */
	@Test
	public void testGet_author() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_year()}.
	 */
	@Test
	public void testGet_year() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getGeneratedId()}.
	 */
	@Test
	public void testGetGeneratedId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#setGeneratedId(java.lang.String)}.
	 */
	@Test
	public void testSetGeneratedId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getAuthority()}.
	 */
	@Test
	public void testGetAuthority() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentName()}.
	 */
	@Test
	public void testGetParentName() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMerged()}.
	 */
	@Test
	public void testGetMerged() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMergeStatus()}.
	 */
	@Test
	public void testGetMergeStatus() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentRefId()}.
	 */
	@Test
	public void testGetParentRefId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#updateDB(org.arachb.arachadmin.AbstractConnection)}.
	 */
	@Test
	public void testUpdateDB() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getIRIString()}.
	 */
	@Test
	public void testGetIRIString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#checkIRIString(org.arachb.arachadmin.IRIManager)}.
	 */
	@Test
	public void testCheckIRIString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#flushCache()}.
	 */
	@Test
	public void testFlushCache() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#isCached(int)}.
	 */
	@Test
	public void testIsCached() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getCached(int)}.
	 */
	@Test
	public void testGetCached() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#cache()}.
	 */
	@Test
	public void testCache() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#updatecache()}.
	 */
	@Test
	public void testUpdatecache() {
		fail("Not yet implemented"); // TODO
	}

}
