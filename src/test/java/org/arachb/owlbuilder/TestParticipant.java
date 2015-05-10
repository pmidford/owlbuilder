package org.arachb.owlbuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.ClaimBean;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.PElementBean;
import org.arachb.arachadmin.ParticipantBean;
import org.arachb.owlbuilder.lib.Participant;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;

public class TestParticipant {

    private static Logger log = Logger.getLogger(TestParticipant.class);

    private AbstractConnection testConnection;

    private final int TESTCLAIMID1 = 1;
    private final int TESTCLAIMID26 = 26;
    private ClaimBean testClaim1;
    private ClaimBean testClaim26;
    private Set<ParticipantBean> beanSet1;
    private Set<ParticipantBean> beanSet26;
    private final Set<ParticipantBean> allParticipants = new HashSet<ParticipantBean>();
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
    	builder.setUpForTesting();
    	testClaim1 = testConnection.getClaim(TESTCLAIMID1);
    	testClaim26 = testConnection.getClaim(TESTCLAIMID26);
		beanSet1 = testConnection.getParticipants(testClaim1);
		beanSet26 = testConnection.getParticipants(testClaim26);
		allParticipants.clear();
		allParticipants.addAll(beanSet1);
		allParticipants.addAll(beanSet26);
    	
	}

	@Test
	public void testParticipant() throws Exception{
		for (ParticipantBean pb : allParticipants){
			Participant p = new Participant(pb);
			assertNotNull(p);
		}
	}

	@Test
	public void testGenerateElementOWL() throws Exception{
		for (ParticipantBean pb : beanSet1){
			OWLObject o = setUpGenerateElement(pb);
			assertNotNull(o);
			assertTrue(o instanceof OWLClassExpression);
		}
			//TODO add tests for individuals and 'bad child' beans
		for (ParticipantBean pb : beanSet26){
			OWLObject o = setUpGenerateElement(pb);
			assertNotNull(o);
			assertTrue(o instanceof OWLIndividual);			
		}
	}
	
	
	private OWLObject setUpGenerateElement(ParticipantBean pb) throws Exception{
		pb.loadElements(testConnection);
		pb.resolveElements(testConnection);
		Participant p = new Participant(pb);
		PElementBean headBean = pb.getElementBean(pb.getHeadElement());
		Map<String, OWLObject> elements = new HashMap<String, OWLObject>(); 
		return p.generateElementOWL(headBean, builder, elements);		
	}
	
	

//	@Test
//	public void testGenerateTermOWL(){
//		fail("Not implemented");
//	}
//	
//
//	@Test
//	public void testGenerateIndividualOWL(){
//		fail("Not implemented");
//	}

	
	
	@Test
	public void testGenerateOWL() throws Exception {
		for (ParticipantBean pb : allParticipants){
			pb.loadElements(testConnection);
			pb.resolveElements(testConnection);
			Participant p = new Participant(pb);
			OWLObject o = p.generateOWL(builder);
			assertNotNull(o);
			log.info("OWL result is: " + o);
		}
	}

}
