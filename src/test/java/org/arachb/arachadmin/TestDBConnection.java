package org.arachb.arachadmin;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;


import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestDBConnection {

    private static Logger log = Logger.getLogger(TestDBConnection.class);

    private AbstractConnection testConnection;
    private IRIManager im;
    private final static String testID = "http://arachb.org/arachb/ARACHB_0000001";
    private final static String testGenID = "http://arachb.org/arachb/ARACHB_0000099";

    //TODO use this
    //private final static String testTaxon = "http://purl.obolibrary.org/obo/NCBITaxon_336608";
    private final static int TESTPUBTAXONDBID = 4838;
    private final static String testPubTaxon = "Tetragnatha straminea";

    //TODO use this
    //private final static String testAnatomy = "http://purl.obolibrary.org/obo/SPD_0000001";
    
    //TODO use or better substitute with URI
    //private final static int TESTPUBANATOMYID = 10473;

    
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
		UidSet.flushCache();   //is this being clobbered somehow?
		im = new IRIManager(testConnection);
	}

	final static String doi3 = "http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	@Test
	public void testgetPublication() throws SQLException {
		PublicationBean testPub = testConnection.getPublication(3);
		assertThat(testPub,notNullValue(PublicationBean.class));
		assertThat(testPub.getId(),is(3));
		assertThat(testPub.getPublicationType(),is("Journal"));
		assertThat(testPub.getDoi(),is(doi3));
	}

	@Test 
	public void testGetPublicationTable() throws SQLException{
		Set<PublicationBean> testTable = testConnection.getPublicationTable();
		assertThat(testTable,notNullValue());
		assertThat(testTable.size(),is(4));
	}

//	@Test
//	public void testupdatePublication() throws SQLException{
//		PublicationBean testPub = testConnection.getPublication(1);
//		assertNotNull(testPub);
//		assertEquals(testGenID,testPub.getGeneratedId());
//		PublicationBean testPub3 = testConnection.getPublication(3);
//		assertEquals(null,testPub3.getGeneratedId());		
//		String saved_id = testPub3.getGeneratedId();
//		testPub3.setGeneratedId(doi3);
//		testConnection.updatePublication(testPub3);
//		PublicationBean updatedPub = testConnection.getPublication(3);
//		assertEquals(doi3,updatedPub.getGeneratedId());
//		updatedPub.setGeneratedId(saved_id);
//		testConnection.updatePublication(updatedPub);
//	}
	
	@Test 
	public void testgetTerm() throws SQLException{
		TermBean testTerm = testConnection.getTerm(TESTPUBTAXONDBID);
		assertThat(testTerm,notNullValue(TermBean.class));
        assertThat(testTerm.getId(),is(TESTPUBTAXONDBID));
	}
	
	@Test
	public void testgetIndividual() throws SQLException{
		IndividualBean testIndividual = testConnection.getIndividual(94);
		assertThat(testIndividual,notNullValue(IndividualBean.class));
		assertThat(testIndividual.getId(),is(94));
		testIndividual.setGeneratedId(testID);
		testConnection.updateIndividual(testIndividual);
	}
	
	@Test
	public void testgetNarrative() throws SQLException{
		NarrativeBean testNarrative1 = testConnection.getNarrative(1);
		assertThat(testNarrative1,notNullValue(NarrativeBean.class));
		assertThat(testNarrative1.getId(),is(1));
		assertThat(testNarrative1.getPublicationId(),equalTo(123));
		assertThat(testNarrative1.getLabel(),is("courtship sequence"));
		assertThat(testNarrative1.getDescription(),equalTo(""));
		NarrativeBean testNarrative2 = testConnection.getNarrative(2);
		assertThat(testNarrative2,notNullValue(NarrativeBean.class));
		assertThat(testNarrative2.getId(),equalTo(2));
		assertThat(testNarrative2.getPublicationId(),equalTo(123));
		assertThat(testNarrative2.getDescription(),is(""));
	}
	

	@Test
	public void testgetClaim() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		assertThat(testClaim,notNullValue(ClaimBean.class));
		assertThat(testClaim.getId(),equalTo(1));
		assertThat(testClaim.getBehavior(),not(equalTo(0)));
		assertThat(testClaim.getPublication(),not(equalTo(0)));
		assertThat(testClaim.getNarrative(),equalTo(0));
		ClaimBean testClaim26 = testConnection.getClaim(26);
		assertThat(testClaim26,notNullValue(ClaimBean.class));
		assertThat(testClaim26.getId(),equalTo(26));
		assertThat(testClaim26.getNarrative(),not(equalTo(0)));
	}
	
	
	@Test
	public void testupdateClaim() throws Exception{
		ClaimBean testClaim = testConnection.getClaim(1);
		assertThat(testClaim,notNullValue(ClaimBean.class));
		testClaim.setGeneratedId(testID);
		testConnection.updateClaim(testClaim);
		ClaimBean updatedClaim = testConnection.getClaim(1);
		assertThat(updatedClaim.getGeneratedId(),equalTo(testID));
		updatedClaim.setGeneratedId("");
		testConnection.updateClaim(updatedClaim);
		ClaimBean updatedAssertion2 = testConnection.getClaim(1);
		assertThat(updatedAssertion2.getGeneratedId(),equalTo(""));  //bogus test here
	}


	
	@Test
	public void testgetParticipantSet() throws Exception{
		Set<Integer> testSet = testConnection.getParticipantSet(1);
		assertThat(testSet,notNullValue(Set.class));
		assertThat(testSet.size(),equalTo(1));
		for (Integer index : testSet){
			final int i = index.intValue();
			assertThat(i,equalTo(1));
			ParticipantBean pb;
			if (ParticipantBean.isCached(index)){
				pb = ParticipantBean.getCached(index);
			}
			else {
				pb = testConnection.getParticipant(index);
			}
			assertThat(pb,notNullValue(ParticipantBean.class));
			assertThat(pb.getId(),equalTo(i));
			assertThat(pb.getPublicationTaxon(),equalTo(testPubTaxon));
			assertThat(pb.getHeadElement(),equalTo(1));
		}
	}
	
	@Test
	public void testupdateParticipant() throws Exception{
		//TODO implement
	}

	
	
	@Test
	public void testgetPElements() throws Exception{
		Set<Integer> testSet = testConnection.getParticipantSet(1);
		assertThat(testSet,notNullValue(Set.class));
		assertThat(testSet.size(),equalTo(1));
		for (Integer index : testSet){
			ParticipantBean pb = ParticipantBean.getCached(index);
			assertThat(pb,notNullValue(ParticipantBean.class));
			Set<Integer> elements = pb.getElements();
			assertThat(elements,notNullValue(Set.class));
			assertThat(elements.size(),equalTo(2));
			
		}
	}
	
	
	@Test
	public void testgetPElement() throws Exception{
		PElementBean testElement = testConnection.getPElement(1);
		assertThat(testElement,notNullValue(PElementBean.class));
	}
	
	
	final static String ACTIVELYPARTICIPATESINURL = "http://purl.obolibrary.org/obo/RO_0002217";
	final static String ACTIVELYPARTICIPATESINLABEL = "actively participates in";
	
	@Test
	public void testgetProperty() throws Exception{
		PropertyBean testProperty = testConnection.getProperty(306);
		assertThat(testProperty,notNullValue(PropertyBean.class));
		assertThat(testProperty.getId(),equalTo(306));
		assertThat(testProperty.getSourceId(),equalTo(ACTIVELYPARTICIPATESINURL));
		assertThat(testProperty.getAuthority(),equalTo(0));
		assertThat(testProperty.getLabel(),equalTo(ACTIVELYPARTICIPATESINLABEL));
		assertThat(testProperty.getGeneratedId(),nullValue(String.class));
		assertThat(testProperty.getComment(),nullValue(String.class));
	}
	
	@Test
	public void testloadImportSourceMap() throws Exception{
		Map<String,String> testmap = testConnection.loadImportSourceMap();
		assertThat(testmap,notNullValue(Map.class));
		assertThat(testmap.size(),equalTo(8));
	}
	
	@Test
	public void testloadOntologyNamesForLoading() throws Exception{
		Map<String,String> testmap = testConnection.loadImportSourceMap();
		assertThat(testmap,notNullValue(Map.class));
		assertThat(testmap.size(),equalTo(8));
		
	}

}
