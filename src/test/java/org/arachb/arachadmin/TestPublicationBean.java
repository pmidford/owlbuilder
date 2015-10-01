/**
 * 
 */
package org.arachb.arachadmin;

import static org.hamcrest.CoreMatchers.equalTo;
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
public class TestPublicationBean {

	
	private static Logger log = Logger.getLogger(TestPublicationBean.class);

    private static AbstractConnection testConnection;
    private static PublicationBean bean3;


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
		bean3 = testConnection.getPublication(3);

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
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#fill(org.arachb.arachadmin.AbstractResults)}.
	 */
	@Test
	public void testFill() {
		assertThat(bean3.getId(),equalTo(3));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getId()}.
	 */
	@Test
	public void testGetId() {
		assertThat(bean3.getId(),equalTo(3));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getPublicationType()}.
	 */
	@Test
	public void testGetPublicationType() {
		assertThat(bean3.getPublicationType(),equalTo("Journal"));
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getDispensation()}.
	 */
	@Test
	public void testGetDispensation() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getDownloaded()}.
	 */
	@Test
	public void testGetDownloaded() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getReviewed()}.
	 */
	@Test
	public void testGetReviewed() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getTitle()}.
	 */
	@Test
	public void testGetTitle() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getAlternateTitle()}.
	 */
	@Test
	public void testGetAlternateTitle() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getAuthorList()}.
	 */
	@Test
	public void testGetAuthorList() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getEditorList()}.
	 */
	@Test
	public void testGetEditorList() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getSourcePublication()}.
	 */
	@Test
	public void testGetSourcePublication() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getVolume()}.
	 */
	@Test
	public void testGetVolume() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getIssue()}.
	 */
	@Test
	public void testGetIssue() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getSerialIdentifier()}.
	 */
	@Test
	public void testGetSerialIdentifier() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getPageRange()}.
	 */
	@Test
	public void testGetPageRange() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getPublicationDate()}.
	 */
	@Test
	public void testGetPublicationDate() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getPublicationYear()}.
	 */
	@Test
	public void testGetPublicationYear() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getDoi()}.
	 */
	@Test
	public void testGetDoi() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getGeneratedId()}.
	 */
	@Test
	public void testGetGeneratedId() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getCurationStatusCode()}.
	 */
	@Test
	public void testGetCurationStatusCode() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getCurationStatus()}.
	 */
	@Test
	public void testGetCurationStatus() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getCurationUpdate()}.
	 */
	@Test
	public void testGetCurationUpdate() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#setGeneratedId(java.lang.String)}.
	 */
	@Test
	public void testSetGeneratedId() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#updateDB(org.arachb.arachadmin.AbstractConnection)}.
	 */
	@Test
	public void testUpdateDB() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getIRIString()}.
	 */
	@Test
	public void testGetIRIString() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#checkIRIString(org.arachb.arachadmin.IRIManager)}.
	 */
	@Test
	public void testCheckIRIString() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#flushCache()}.
	 */
	@Test
	public void testFlushCache() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#isCached(int)}.
	 */
	@Test
	public void testIsCached() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#cacheSize()}.
	 */
	@Test
	public void testCacheSize() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getCached(int)}.
	 */
	@Test
	public void testGetCached() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#cache()}.
	 */
	@Test
	public void testCache() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#updatecache()}.
	 */
	@Test
	public void testUpdatecache() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.arachb.arachadmin.PublicationBean#getuidset()}.
	 */
	@Test
	public void testGetuidset() {
		//fail("Not yet implemented"); // TODO
	}

}
