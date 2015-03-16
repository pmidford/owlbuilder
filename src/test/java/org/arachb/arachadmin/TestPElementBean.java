package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestPElementBean {

	
    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;

    private int TESTCLAIMID = 1;
    private final static int TESTPUBTAXONDBID = 4838;
    private final static int TESTPUBANATOMYID = 10473;
    private ClaimBean testClaim1;
    private ClaimBean testClaim26;
    private ParticipantBean testParticipant1;
    private ParticipantBean testParticipant29;
    private PElementBean testElement1;
    private PElementBean testElement2;
    private PElementBean testElement61;
    private PElementBean testElement62;
	
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
		testClaim1 = testConnection.getClaim(1);
		testClaim26 = testConnection.getClaim(26);
		assertNotNull(testClaim26);
		testParticipant1 = getParticipantForClaim(testClaim1);
		testParticipant1.loadElements(testConnection);
		testParticipant29 = getParticipantForClaim(testClaim26);
		testParticipant29.loadElements(testConnection);
		assertNotNull(testParticipant29);
		testElement1 = testConnection.getPElement(1);
		testElement2 = testConnection.getPElement(2);
		testElement61 = testConnection.getPElement(61);
		testElement62 = testConnection.getPElement(62);
		assertNotNull(testElement61);
	}
	
	private ParticipantBean getParticipantForClaim(ClaimBean cb) throws Exception{
		final Set<ParticipantBean>testParticipants = 
				testConnection.getParticipants(cb);
		assertFalse(testParticipants.isEmpty());
		Iterator<ParticipantBean> piter = testParticipants.iterator();
		if (piter.hasNext()){
			return piter.next();
		}
		return null;
	}


	@Test
	public void testFill() throws Exception{
		assertEquals(1, testElement1.getId());
		assertEquals(1, testElement1.getType());
		assertEquals(1, testElement1.getParticipant());
		assertEquals(2, testElement2.getId());
		assertEquals(1, testElement2.getType());
		assertEquals(1, testElement2.getParticipant());
		assertEquals(61, testElement61.getId());
		assertEquals(3, testElement61.getType());
		assertEquals(29, testElement61.getParticipant());
	}

	@Test
	public void testFillTerm() throws Exception{
		final TermBean t1 = testConnection.getTerm(TESTPUBTAXONDBID);
		testConnection.fillPElementTerm(testElement1);
		final TermBean tx = testElement1.getTerm();
		assertNotNull(tx);
		assertEquals(t1, tx);
		final TermBean t2 = testConnection.getTerm(TESTPUBANATOMYID);
		testConnection.fillPElementTerm(testElement2);
		final TermBean tx2 = testElement2.getTerm();
		assertNotNull(tx2);
		assertEquals(t2, tx2);
		testConnection.fillPElementTerm(testElement61);
		assertNull(testElement61.getTerm());
	}

	@Test
	public void testFillIndividual() throws Exception {
		final IndividualBean i1 = testConnection.getIndividual(94);
		testConnection.fillPElementIndividual(testElement61);
		IndividualBean ix = testElement61.getIndividual();
		assertNotNull(ix);
		assertEquals(i1, ix);
		testConnection.fillPElementIndividual(testElement1);
		assertNull(testElement1.getIndividual());
		testConnection.fillPElementIndividual(testElement2);
		assertNull(testElement2.getIndividual());
	}

	@Test
	public void testFillParents() throws Exception{
		testConnection.fillPElementParents(testElement1);
		final Set<Integer> parentset = testElement1.getParents();
		assertEquals(1, parentset.size());
		testConnection.fillPElementParents(testElement2);
		final Set<Integer> parentset2 = testElement2.getParents();
		assertEquals(0, parentset2.size());
		testConnection.fillPElementParents(testElement61);
		final Set<Integer> parentset61 = testElement61.getParents();
		assertEquals(1, parentset61.size());
	}

	@Test
	public void testFillChildren() throws Exception{
		testConnection.fillPElementChildren(testElement1);
		final Set<Integer> childset = testElement1.getChildren();
		assertEquals(0, childset.size());
		testConnection.fillPElementChildren(testElement2);
		final Set<Integer> childset2 = testElement2.getChildren();
		assertEquals(1, childset2.size());
		testConnection.fillPElementChildren(testElement61);
		final Set<Integer> childset61 = testElement61.getChildren();
		assertEquals(0, childset61.size());
	}

	@Test
	public void testResolveParents() throws Exception{
		testConnection.fillPElementParents(testElement1);
		testElement1.resolveParents(testParticipant1,testConnection);
		testConnection.fillPElementParents(testElement2);
		testElement2.resolveParents(testParticipant1,testConnection);
		testConnection.fillPElementParents(testElement61);
		testElement61.resolveParents(testParticipant29,testConnection);	
		testConnection.fillPElementChildren(testElement62);
		testElement62.resolveParents(testParticipant29,testConnection);
	}

	@Test
	public void testResolveChildren() throws Exception{
		testConnection.fillPElementChildren(testElement1);
		testElement1.resolveParents(testParticipant1,testConnection);
		testConnection.fillPElementChildren(testElement2);
		testElement2.resolveParents(testParticipant1,testConnection);
		testConnection.fillPElementChildren(testElement61);
		testElement61.resolveParents(testParticipant29,testConnection);
		testConnection.fillPElementChildren(testElement62);
		testElement62.resolveParents(testParticipant29,testConnection);
	}


	@Test
	public void testGetParentElement() throws Exception{
		testConnection.fillPElementParents(testElement1);
		testElement1.resolveParents(testParticipant1,testConnection);
		for (Integer index : testElement1.getParents()){
			assertEquals(2,index.intValue());
			PElementBean parent = testElement1.getParentElement(index);
			assertNotNull(parent);
			PropertyBean parentProp = testElement1.getParentProperty(index);
			assertNotNull(parentProp);
		}
		testConnection.fillPElementParents(testElement2);
		testElement2.resolveParents(testParticipant1,testConnection);
		assertEquals(0,testElement2.getParents().size());
		testConnection.fillPElementParents(testElement61);
		testElement61.resolveParents(testParticipant29,testConnection);	
		for (Integer index : testElement61.getParents()){
			assertEquals(62,index.intValue());
			PropertyBean parentProp = testElement61.getParentProperty(index);
			assertNotNull(parentProp);
			PElementBean parent = testElement61.getParentElement(index);
			assertNotNull(parent);
		}
		testConnection.fillPElementParents(testElement62);
		testElement62.resolveParents(testParticipant29,testConnection);
		assertEquals(0,testElement62.getParents().size());
	}

	@Test
	public void testGetChildElement() throws Exception{
		testConnection.fillPElementChildren(testElement1);
		testElement1.resolveChildren(testParticipant1,testConnection);
		assertEquals(0,testElement1.getChildren().size());
		testConnection.fillPElementChildren(testElement61);
		testElement61.resolveChildren(testParticipant1,testConnection);
		assertEquals(0,testElement61.getChildren().size());
		testConnection.fillPElementChildren(testElement2);
		testElement2.resolveChildren(testParticipant1,testConnection);
		for (Integer index : testElement2.getChildren()){
			PElementBean child = testElement2.getChildElement(index);
			assertNotNull(child);
			PropertyBean childProp = testElement2.getChildProperty(index);
			assertNotNull(childProp);
		}
	}

}
