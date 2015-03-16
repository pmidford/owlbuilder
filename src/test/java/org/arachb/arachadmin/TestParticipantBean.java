package org.arachb.arachadmin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestParticipantBean {

    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;

    private ClaimBean testClaim1;
    private ClaimBean testClaim26;
    
	
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
	}

	@Test
	public void testFill() throws Exception {
		Set<ParticipantBean> pbs1 = testConnection.getParticipants(testClaim1);
		assertEquals(1, pbs1.size());
		for (ParticipantBean pb : pbs1){
			assertEquals(1, pb.getId());
			assertEquals("some",pb.getQuantification());
			assertEquals("",pb.getLabel());  //should improve this
			assertEquals(null,pb.getGeneratedId());  //improve ??
			assertEquals(306,pb.getProperty());
			assertEquals("Tetragnatha straminea", pb.getPublicationTaxon());
			assertEquals("", pb.getPublicationAnatomy());
			assertEquals(0, pb.getSubstrate());
			assertEquals(1, pb.getHeadElement());
		}
		Set<ParticipantBean> pbs29 = testConnection.getParticipants(testClaim26);
		assertEquals(1, pbs29.size());
		for (ParticipantBean pb : pbs29){
			assertEquals(29, pb.getId());
			assertEquals("individual",pb.getQuantification());
			assertEquals("female",pb.getLabel());  //should improve this
			assertEquals("http://arachb.org/arachb/ARACHB_0000349",pb.getGeneratedId());  //improve ??
			assertEquals(306,pb.getProperty());
			assertEquals("Leucauge mariana", pb.getPublicationTaxon());
			assertEquals("female", pb.getPublicationAnatomy());
			assertEquals(0, pb.getSubstrate());
			assertEquals(61, pb.getHeadElement());
		}
	}


	@Test
	public void testLoadElements() throws Exception {
		final Set<ParticipantBean> pbs1 = testConnection.getParticipants(testClaim1);
		assertEquals(1, pbs1.size());
		for (ParticipantBean pb : pbs1){
			assertNull(pb.getElementBean(1));
			pb.loadElements(testConnection);
			assertNotNull(pb.getElementBean(1));
		}
		Set<ParticipantBean> pbs29 = testConnection.getParticipants(testClaim26);
		assertEquals(1, pbs29.size());
		for (ParticipantBean pb : pbs29){
			assertNull(pb.getElementBean(1));
			pb.loadElements(testConnection);
			assertNotNull(pb.getElementBean(61));
		}
	}


}
