package org.arachb.arachadmin;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestParticipantBean {

    private static Logger log = Logger.getLogger(TestParticipantBean.class);

    private AbstractConnection testConnection;
    private IRIManager im;

    private final static int SOMECLAIMID = 1;
    private final static int INDIVIDUALCLAIMID = 26;


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
	}

	@Test
	public void testFill() throws Exception {
		Set<ParticipantBean> pbs1 = testConnection.getParticipantTable(SOMECLAIMID);
		assertThat(pbs1.size(), equalTo(1));
		for (ParticipantBean pb : pbs1){
			assertThat(pb.getId(),equalTo(1));
			assertThat(pb.getQuantification(),equalTo("some"));
			assertThat(pb.getLabel(),equalTo(""));  //should improve this
			assertThat(pb.getProperty(),equalTo(306));
			assertThat(pb.getPublicationTaxon(),equalTo("Tetragnatha straminea"));
			assertThat(pb.getPublicationAnatomy(),equalTo(""));
			assertThat(pb.getSubstrate(),equalTo(0));
			assertThat(pb.getHeadElement(),equalTo(1));
		}
		Set<ParticipantBean> pbs29 = testConnection.getParticipantTable(INDIVIDUALCLAIMID);
		assertThat(pbs29.size(),equalTo(1));
		for (ParticipantBean pb : pbs29){
			assertThat(pb.getId(),equalTo(29));
			assertThat(pb.getQuantification(),equalTo("individual"));
			assertThat(pb.getLabel(),equalTo("female"));  //should improve this
			assertThat(pb.getProperty(),equalTo(306));
			assertThat(pb.getPublicationTaxon(),equalTo("Leucauge mariana"));
			assertThat(pb.getPublicationAnatomy(),equalTo("female"));
			assertThat(pb.getSubstrate(),equalTo(0));
			assertThat(pb.getHeadElement(),equalTo(62));
		}
	}




}
