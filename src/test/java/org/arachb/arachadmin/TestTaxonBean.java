/**
 * 
 */
package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.equalTo;

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
		assertThat(testbean.getId(),equalTo(1));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getSourceId()}.
	 */
	@Test
	public void testGetSourceId() {
		assertThat(testbean.getSourceId(),equalTo(TESTBEANURN));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getName()}.
	 */
	@Test
	public void testGetName() {
		assertThat(testbean.getName(),equalTo(TESTBEANNAME));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_author()}.
	 */
	@Test
	public void testGet_author() {
		assertThat(testbean.get_author(),equalTo(TESTBEANAUTHOR));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#get_year()}.
	 */
	@Test
	public void testGet_year() {
		assertThat(testbean.get_year(),equalTo(TESTBEANYEAR));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getGeneratedId()}.
	 */
	@Test
	public void testGetGeneratedId() {
		log.info("generated id is " + testbean.getGeneratedId());
		assertThat(testbean.getGeneratedId(),nullValue(String.class));
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
	public void testGetAuthority(){
		assertThat(testbean.getAuthority(),nullValue(String.class));  //TODO ought to be WCS?
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentName()}.
	 */
	@Test
	public void testGetParentName() {
		assertThat(testbean.getParentName(),nullValue(String.class));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMerged()}.
	 */
	@Test
	public void testGetMerged() {
		assertThat(testbean.getMerged(),equalTo(false)); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getMergeStatus()}.
	 */
	@Test
	public void testGetMergeStatus() {
		assertThat(testbean.getMergeStatus(),nullValue(String.class)); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getParentRefId()}.
	 */
	@Test
	public void testGetParentRefId() {
		assertThat(testbean.getParentRefId(),nullValue(String.class)); // TODO
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
		assertThat(testbean.getIRIString(),equalTo(TESTBEANURN));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#checkIRIString(org.arachb.arachadmin.IRIManager)}.
	 * @throws SQLException 
	 */
	@Test
	public void testCheckIRIString() throws SQLException {
		IRIManager manager = testConnection.getIRIManager();
		assertThat(testbean.checkIRIString(manager),equalTo(TESTBEANURN));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#flushCache()}.
	 */
	@Test
	public void testFlushCache() {
		assertThat(TaxonBean.isCached(TESTBEANID),equalTo(true));
		TaxonBean.flushCache();
		assertThat(TaxonBean.isCached(TESTBEANID),equalTo(false));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#isCached(int)}.
	 */
	@Test
	public void testIsCached() {
		TaxonBean.flushCache();
		assertThat(TaxonBean.isCached(TESTBEANID),equalTo(false));		
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#getCached(int)}.
	 */
	@Test
	public void testGetCached() {
		TaxonBean.flushCache();
		testbean.cache();
		assertThat(TaxonBean.getCached(TESTBEANID),equalTo(testbean));
		TaxonBean.flushCache();
	}
	
	/**
	 * Test method for testing exceptions from {@link org.arachb.arachadmin.TaxonBean#getCached(int)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testGetCachedException() {
		TaxonBean.flushCache();
		TaxonBean.getCached(TESTBEANID);  //throw out value, just want the exception
	}
	

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#cache()}.
	 */
	@Test
	public void testCache() {
		TaxonBean.flushCache();
		testbean.cache();
		assertThat(TaxonBean.getCached(TESTBEANID),equalTo(testbean));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.TaxonBean#updatecache()}.
	 */
	@Test
	public void testUpdatecache() {
		testbean.updatecache();
	}

}
