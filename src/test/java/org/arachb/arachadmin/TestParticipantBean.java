package org.arachb.arachadmin;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestParticipantBean {

    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;
    private IRIManager im;

    private ClaimBean testClaim1;
    private ClaimBean testClaim26;


	@Before
	public void setUp() throws Exception {
		if (DBConnection.probeTestConnection()){
			log.info("Testing with live connection");
			testConnection = DBConnection.getTestConnection();
		}
		else{
			log.info("Testing with mock connection");
			testConnection = DBConnection.getMockConnection();
		}
		im = new IRIManager(testConnection);
		testClaim1 = testConnection.getClaim(1);
		testClaim26 = testConnection.getClaim(26);
	}

	@Test
	public void testFill() throws Exception {
		Set<ParticipantBean> pbs1 = testConnection.getParticipantTable(1);
		assertEquals(1, pbs1.size());
		for (ParticipantBean pb : pbs1){
			assertEquals(1, pb.getId());
			assertEquals("some",pb.getQuantification());
			assertEquals("",pb.getLabel());  //should improve this
			assertEquals(306,pb.getProperty());
			assertEquals("Tetragnatha straminea", pb.getPublicationTaxon());
			assertEquals("", pb.getPublicationAnatomy());
			assertEquals(0, pb.getSubstrate());
			assertEquals(1, pb.getHeadElement());
		}
		Set<ParticipantBean> pbs29 = testConnection.getParticipantTable(26);
		assertEquals(1, pbs29.size());
		for (ParticipantBean pb : pbs29){
			assertEquals(29, pb.getId());
			assertEquals("individual",pb.getQuantification());
			assertEquals("female",pb.getLabel());  //should improve this
			assertEquals(306,pb.getProperty());
			assertEquals("Leucauge mariana", pb.getPublicationTaxon());
			assertEquals("female", pb.getPublicationAnatomy());
			assertEquals(0, pb.getSubstrate());
			assertEquals(62, pb.getHeadElement());
		}
	}




}
