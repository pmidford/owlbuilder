package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestPElementBean {

	
    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;

    private int TESTCLAIMID = 1;
    private ClaimBean testClaim;
    private PElementBean testElement;
	
	@Before
	public void setUp() throws Exception {
		if (DBConnection.testConnection()){
			log.info("Testing with live connection");
			testConnection = DBConnection.getTestConnection();
		}
		else{
			log.info("Testing with mock connection");
			testConnection = DBConnection.getMockConnection();
		}
		testClaim = testConnection.getClaim(TESTCLAIMID);
		testElement = testConnection.getPElement(1);
	}


	@Test
	public void testFill() throws Exception{
		assertEquals(1, testElement.getId());
		assertEquals(1, testElement.getType());
		assertEquals(1, testElement.getParticipant());
	}

	@Test
	public void testFillTerm() throws Exception{
		TermBean t1 = testConnection.getTerm(4838);
		assertEquals(t1, testElement.getTerm());
	}

	@Test
	public void testFillIndividual() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFillParents() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFillChildren() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testResolveParents() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testResolveChildren() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetParents() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetChildren() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetChildElement() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetChildProperty() {
		fail("Not yet implemented"); // TODO
	}

}
