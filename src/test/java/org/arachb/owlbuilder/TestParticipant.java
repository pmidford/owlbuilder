package org.arachb.owlbuilder;

import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.ClaimBean;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.ParticipantBean;
import org.arachb.owlbuilder.lib.Participant;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLObject;

public class TestParticipant {

    private static Logger log = Logger.getLogger(TestParticipant.class);

    private AbstractConnection testConnection;

    private final int TESTCLAIMID = 1;
    private ClaimBean testClaim;
    
    private Set<ParticipantBean> beanSet;
	private Owlbuilder builder;
	
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
    	builder = new Owlbuilder();
    	testClaim = testConnection.getClaim(TESTCLAIMID);
		beanSet = testConnection.getParticipants(testClaim);
    	
	}

	@Test
	public void testParticipant() throws Exception{
		for (ParticipantBean pb : beanSet){
			Participant p = new Participant(pb);
		}
	}

	@Test
	public void testGenerateOWL() throws Exception {
		for (ParticipantBean pb : beanSet){
			pb.loadElements(testConnection);
			pb.traverseElements();
			Participant p = new Participant(pb);
			OWLObject o = p.generateOWL(builder);
			//assertNotNull(o);
		}
	}

}
