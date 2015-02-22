package org.arachb.arachadmin;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.lib.AbstractResults;
import org.junit.Before;
import org.junit.Test;

public class TestParticipantBean {

    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;

    private int TESTCLAIMID = 1;
    private ClaimBean testClaim;
    
	
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
	}

	@Test
	public void testFill() throws Exception {
		Set<ParticipantBean> pbs = testConnection.getParticipants(testClaim);
		assertEquals(1, pbs.size());
		for (ParticipantBean pb : pbs){
			assertEquals(1, pb.getId());
			assertEquals("some",pb.getQuantification());
			assertEquals("",pb.getLabel());  //should improve this
			assertEquals(null,pb.getGeneratedId());  //improve ??
			assertEquals(306,pb.getProperty());
			assertEquals("Tetragnatha straminea", pb.getPublicationTaxon());
			assertEquals("", pb.getPublicationAnatomy());
			assertEquals(0, pb.getSubstrate());
			assertEquals(0, pb.getParticipationProperty());
			assertEquals(1, pb.getHeadElement());
		}
	}


	@Test
	public void testLoadElements() throws Exception {
		Set<ParticipantBean> pbs = testConnection.getParticipants(testClaim);
		assertEquals(1, pbs.size());
		for (ParticipantBean pb : pbs){
			log.info("What is this: " + pb.getElementBean(1));
			assertNull(pb.getElementBean(1));
			pb.loadElements(testConnection);
			assertNotNull(pb.getElementBean(1));
		}
	}


}
