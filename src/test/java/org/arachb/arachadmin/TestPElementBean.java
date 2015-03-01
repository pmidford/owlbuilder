package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.sql.SQLException;
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
    private PElementBean testElement2;
	
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
		testElement2 = testConnection.getPElement(61);
	}


	@Test
	public void testFill() throws Exception{
		assertEquals(1, testElement.getId());
		assertEquals(1, testElement.getType());
		assertEquals(1, testElement.getParticipant());
		assertEquals(61, testElement2.getId());
		assertEquals(3, testElement2.getType());
		assertEquals(29, testElement2.getParticipant());
	}

	@Test
	public void testFillTerm() throws Exception{
		final TermBean t1 = testConnection.getTerm(4838);
		testConnection.fillPElementTerm(testElement);
		TermBean tx = testElement.getTerm();
		assertNotNull(tx);
		assertEquals(t1, tx);
		testConnection.fillPElementTerm(testElement2);
		assertNull(testElement2.getTerm());
	}

	@Test
	public void testFillIndividual() throws Exception {
		final IndividualBean i1 = testConnection.getIndividual(94);
		testConnection.fillPElementIndividual(testElement2);
		IndividualBean ix = testElement2.getIndividual();
		assertNotNull(ix);
		assertEquals(i1, ix);
		testConnection.fillPElementIndividual(testElement);
		assertNull(testElement.getIndividual());
	}

	@Test
	public void testFillParents() throws Exception{
		testConnection.fillPElementParents(testElement);
		Set<Integer> parentset = testElement.getParents();
		assertEquals(1, parentset.size());
		testConnection.fillPElementParents(testElement2);
		Set<Integer> parentset2 = testElement2.getParents();
		assertEquals(1, parentset2.size());
	}

	@Test
	public void testFillChildren() throws Exception{
		testConnection.fillPElementChildren(testElement);
		Set<Integer> childset = testElement.getChildren();
		assertEquals(0, childset.size());
		testConnection.fillPElementChildren(testElement2);
		Set<Integer> childset2 = testElement2.getChildren();
		assertEquals(0, childset2.size());
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
