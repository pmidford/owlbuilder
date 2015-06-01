package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.PElementBean;
import org.arachb.arachadmin.TestParticipantBean;
import org.junit.Before;
import org.junit.Test;

public class TestParticipantElement {

    private AbstractConnection testConnection;

    private static Logger log = Logger.getLogger(TestParticipantBean.class);

	
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

	}
	
//	@Test
//	public void testFillParents() throws Exception{
//		testConnection.fillPElementParents(testElement1);
//		final Set<Integer> parentset = testElement1.getParents();
//		assertEquals(1, parentset.size());
//		testConnection.fillPElementParents(testElement2);
//		final Set<Integer> parentset2 = testElement2.getParents();
//		assertEquals(0, parentset2.size());
//		testConnection.fillPElementParents(testElement61);
//		final Set<Integer> parentset61 = testElement61.getParents();
//		assertEquals(1, parentset61.size());
//	}
//
//	@Test
//	public void testFillChildren() throws Exception{
//		testConnection.fillPElementChildren(testElement1);
//		final Set<Integer> childset = testElement1.getChildren();
//		assertEquals(0, childset.size());
//		testConnection.fillPElementChildren(testElement2);
//		final Set<Integer> childset2 = testElement2.getChildren();
//		assertEquals(1, childset2.size());
//		testConnection.fillPElementChildren(testElement61);
//		final Set<Integer> childset61 = testElement61.getChildren();
//		assertEquals(0, childset61.size());
//	}
//
//	@Test
//	public void testResolveParents() throws Exception{
//		testConnection.fillPElementParents(testElement1);
//		testElement1.resolveParents(testParticipant1,testConnection);
//		testConnection.fillPElementParents(testElement2);
//		testElement2.resolveParents(testParticipant1,testConnection);
//		testConnection.fillPElementParents(testElement61);
//		testElement61.resolveParents(testParticipant29,testConnection);	
//		testConnection.fillPElementChildren(testElement62);
//		testElement62.resolveParents(testParticipant29,testConnection);
//	}
//
//	@Test
//	public void testResolveChildren() throws Exception{
//		testConnection.fillPElementChildren(testElement1);
//		testElement1.resolveChildren(testParticipant1,testConnection);
//		testConnection.fillPElementChildren(testElement2);
//		testElement2.resolveChildren(testParticipant1,testConnection);
//		testConnection.fillPElementChildren(testElement61);
//		testElement61.resolveChildren(testParticipant29,testConnection);
//		testConnection.fillPElementChildren(testElement62);
//		testElement62.resolveChildren(testParticipant29,testConnection);
//	}
//
//
//	@Test
//	public void testGetParentElement() throws Exception{
//		testConnection.fillPElementParents(testElement1);
//		testElement1.resolveParents(testParticipant1,testConnection);
//		for (Integer index : testElement1.getParents()){
//			assertEquals(2,index.intValue());
//			PElementBean parent = testElement1.getParentElement(index);
//			assertNotNull(parent);
//			PropertyBean parentProp = testElement1.getParentProperty(index);
//			assertNotNull(parentProp);
//		}
//		testConnection.fillPElementParents(testElement2);
//		testElement2.resolveParents(testParticipant1,testConnection);
//		assertEquals(0,testElement2.getParents().size());
//		testConnection.fillPElementParents(testElement61);
//		testElement61.resolveParents(testParticipant29,testConnection);	
//		for (Integer index : testElement61.getParents()){
//			assertEquals(62,index.intValue());
//			PropertyBean parentProp = testElement61.getParentProperty(index);
//			assertNotNull(parentProp);
//			PElementBean parent = testElement61.getParentElement(index);
//			assertNotNull(parent);
//		}
//		testConnection.fillPElementParents(testElement62);
//		testElement62.resolveParents(testParticipant29,testConnection);
//		assertEquals(0,testElement62.getParents().size());
//	}
//
//	@Test
//	public void testGetChildElement() throws Exception{
//		testConnection.fillPElementChildren(testElement1);
//		testElement1.resolveChildren(testParticipant1,testConnection);
//		assertEquals(0,testElement1.getChildren().size());
//		testConnection.fillPElementChildren(testElement61);
//		testElement61.resolveChildren(testParticipant1,testConnection);
//		assertEquals(0,testElement61.getChildren().size());
//		testConnection.fillPElementChildren(testElement2);
//		testElement2.resolveChildren(testParticipant1,testConnection);
//		for (Integer index : testElement2.getChildren()){
//			PElementBean child = testElement2.getChildElement(index);
//			assertNotNull(child);
//			PropertyBean childProp = testElement2.getChildProperty(index);
//			assertNotNull(childProp);
//		}
//	}
//
	
	
	
	
	
	

	@Test
	public void testGetElement() throws Exception {
		PElementBean peb = testConnection.getPElement(1);
		assertNotNull(peb);
		ParticipantElement testElement = ParticipantElement.getElement(peb);
		assertNotNull(testElement);
	}

	@Test
	public void testWrapSet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGenerateOWLOwlbuilderMapOfStringOWLObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGenerateOWLOwlbuilder() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testResolve() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetParents() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSingletonParent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetParentElement() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetParentProperty() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetChildren() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSingletonChild() {
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

	@Test
	public void testGetId() {
		fail("Not yet implemented"); // TODO
	}

}
