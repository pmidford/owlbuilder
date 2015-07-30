package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;




public class MockConnection implements AbstractConnection {
	
	final private static String JOURNALTYPE = "Journal";
	final private static String TESTDOI = 
			"http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	final private static String TESTGENID = "http://arachb.org/arachb/ARACHB_0000099";
	final private static String TESTARACHBID = "http://arachb.org/arachb/TEST_0000001";
	final private static String TESTBEHAVIOR = "http://purl.obolibrary.org/obo/NBO_0000355";
	final private static String TESTPUBTAXON = "Tetragnatha straminea";
	final private static int MAXMOCKIDVALUE = 10;  //might need to assign this realistically
	
	private static StaticMockResults mockPublicationResults = new StaticMockResults();
	static {
		mockPublicationResults.setInteger(PublicationBean.DBID, 1);
		mockPublicationResults.setString(PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults.setString(PublicationBean.DBDISPENSATION,"");
		mockPublicationResults.setString(PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults.setString(PublicationBean.DBREVIEWED,"");
		mockPublicationResults.setString(PublicationBean.DBTITLE,"");
		mockPublicationResults.setString(PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults.setString(PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults.setString(PublicationBean.DBEDITORLIST,"");
		mockPublicationResults.setString(PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults.setInteger(PublicationBean.DBVOLUME,1);
		mockPublicationResults.setString(PublicationBean.DBISSUE,"");
		mockPublicationResults.setString(PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults.setString(PublicationBean.DBPAGERANGE,"");
		mockPublicationResults.setString(PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults.setString(PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults.setString(PublicationBean.DBDOI,TESTDOI);
		mockPublicationResults.setString(PublicationBean.DBGENERATEDID, TESTGENID);
        mockPublicationResults.setString(PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults.setString(PublicationBean.DBCURATIONUPDATE,"");
	}
	
	private static StaticMockResults mockPublicationResults2 = new StaticMockResults();
	static {
		mockPublicationResults2.setInteger(PublicationBean.DBID, 2);
		mockPublicationResults2.setString(PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults2.setString(PublicationBean.DBDISPENSATION,"");
		mockPublicationResults2.setString(PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults2.setString(PublicationBean.DBREVIEWED,"");
		mockPublicationResults2.setString(PublicationBean.DBTITLE,"");
		mockPublicationResults2.setString(PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults2.setString(PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults2.setString(PublicationBean.DBEDITORLIST,"");
		mockPublicationResults2.setString(PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults2.setInteger(PublicationBean.DBVOLUME,1);
		mockPublicationResults2.setString(PublicationBean.DBISSUE,"");
		mockPublicationResults2.setString(PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults2.setString(PublicationBean.DBPAGERANGE,"");
		mockPublicationResults2.setString(PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults2.setString(PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults2.setString(PublicationBean.DBDOI, null);
		mockPublicationResults2.setString(PublicationBean.DBGENERATEDID,TESTARACHBID);
        mockPublicationResults2.setString(PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults2.setString(PublicationBean.DBCURATIONUPDATE,"");
	}

	
	private final static String doi3 = 
			"http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	private static StaticMockResults mockPublicationResults3 = new StaticMockResults();
	static {
		mockPublicationResults3.setInteger(PublicationBean.DBID, 3);
		mockPublicationResults3.setString(PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults3.setString(PublicationBean.DBDISPENSATION,"");
		mockPublicationResults3.setString(PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults3.setString(PublicationBean.DBREVIEWED,"");
		mockPublicationResults3.setString(PublicationBean.DBTITLE,"");
		mockPublicationResults3.setString(PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults3.setString(PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults3.setString(PublicationBean.DBEDITORLIST,"");
		mockPublicationResults3.setString(PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults3.setInteger(PublicationBean.DBVOLUME,1);
		mockPublicationResults3.setString(PublicationBean.DBISSUE,"");
		mockPublicationResults3.setString(PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults3.setString(PublicationBean.DBPAGERANGE,"");
		mockPublicationResults3.setString(PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults3.setString(PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults3.setString(PublicationBean.DBDOI, doi3);
		mockPublicationResults3.setString(PublicationBean.DBGENERATEDID, null);
        mockPublicationResults3.setString(PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults3.setString(PublicationBean.DBCURATIONUPDATE,"");
	}

	private static String pub123genID = "http://arachb.org/arachb/ARACHB_0000311";
	private static String pub123title = "Courtship, copulation, and sperm transfer in " +
	                                    "Leucauge mariana (Araneae, Tetragnathidae) with " +
	                                    "implications for higher classification";
	private static StaticMockResults mockPublicationResults123 = new StaticMockResults();
	static {
		mockPublicationResults123.setInteger(PublicationBean.DBID, 123);
		mockPublicationResults123.setString(PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults123.setString(PublicationBean.DBDISPENSATION,"Downloaded");
		mockPublicationResults123.setString(PublicationBean.DBDOWNLOADED,"2011-05-11");
		mockPublicationResults123.setString(PublicationBean.DBREVIEWED, null);
		mockPublicationResults123.setString(PublicationBean.DBTITLE,pub123title);
		mockPublicationResults123.setString(PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults123.setString(PublicationBean.DBAUTHORLIST,"Eberhard, William G.; Huber, Bernhard A.");
		mockPublicationResults123.setString(PublicationBean.DBEDITORLIST,"");
		mockPublicationResults123.setString(PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults123.setInteger(PublicationBean.DBVOLUME,26);
		mockPublicationResults123.setString(PublicationBean.DBISSUE,"3");
		mockPublicationResults123.setString(PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults123.setString(PublicationBean.DBPAGERANGE,"342-368");
		mockPublicationResults123.setString(PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults123.setString(PublicationBean.DBPUBLICATIONYEAR,"1998");
		mockPublicationResults123.setString(PublicationBean.DBDOI, null);
		mockPublicationResults123.setString(PublicationBean.DBGENERATEDID, pub123genID);
		mockPublicationResults123.setString(PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults123.setString(PublicationBean.DBCURATIONUPDATE,"");
	}


	private static StaticMockResults mockTermResults11052 = new StaticMockResults();
	static {
		mockTermResults11052.setInteger(TermBean.DBID,110522);
		mockTermResults11052.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NBO_0000002");
		mockTermResults11052.setInteger(TermBean.DBDOMAIN,1);
		mockTermResults11052.setInteger(TermBean.DBAUTHORITY,4);
		mockTermResults11052.setString(TermBean.DBLABEL,"whole body movement");
		mockTermResults11052.setString(TermBean.DBGENERATEDID,null);
		mockTermResults11052.setString(TermBean.DBCOMMENT,"");
	}

	private static StaticMockResults mockTermResults4838 = new StaticMockResults();
	static {
		mockTermResults4838.setInteger(TermBean.DBID,4838);
		mockTermResults4838.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setInteger(TermBean.DBDOMAIN,3);
		mockTermResults4838.setInteger(TermBean.DBAUTHORITY,1);
		mockTermResults4838.setString(TermBean.DBLABEL,"Tetragnatha straminea");
		mockTermResults4838.setString(TermBean.DBGENERATEDID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setString(TermBean.DBCOMMENT,"");
	}

	private static StaticMockResults mockTermResults11398 = new StaticMockResults();
	static {
		mockTermResults11398.setInteger(0,TermBean.DBID,11398);
		mockTermResults11398.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NBO_0000358");
		mockTermResults11398.setInteger(0,TermBean.DBDOMAIN,1);
		mockTermResults11398.setInteger(0,TermBean.DBAUTHORITY,4);
		mockTermResults11398.setString(0,TermBean.DBLABEL,"resting posture");
		mockTermResults11398.setString(0,TermBean.DBGENERATEDID,null);
		mockTermResults11398.setString(0,TermBean.DBCOMMENT,"");
	}
	

	private static MockResults mockTermResults10473 = new StaticMockResults();
	static {
		mockTermResults10473.setInteger(0,TermBean.DBID, 10473);
		mockTermResults10473.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults10473.setInteger(0,TermBean.DBDOMAIN,2);
		mockTermResults10473.setInteger(0,TermBean.DBAUTHORITY,4);
		mockTermResults10473.setString(0,TermBean.DBLABEL,"whole organism");
		mockTermResults10473.setString(0,TermBean.DBGENERATEDID,null);
		mockTermResults10473.setString(0,TermBean.DBCOMMENT,"");
		
	}

	
	private static MockResults mockParticipantResults1 = new StaticMockResults();
	static {
		mockParticipantResults1.setInteger(0,ParticipantBean.DBID,1);
		mockParticipantResults1.setString(0,ParticipantBean.DBQUANTIFICATION,"some");
		mockParticipantResults1.setString(0,ParticipantBean.DBLABEL,"");
		mockParticipantResults1.setString(0,ParticipantBean.DBGENERATEDID,null);
		mockParticipantResults1.setInteger(0,ParticipantBean.DBPROPERTY, 306);  //used
		mockParticipantResults1.setString(0,ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockParticipantResults1.setString(0,ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockParticipantResults1.setString(0,ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockParticipantResults1.setInteger(0,ParticipantBean.DBHEADELEMENT,1);
	}

	private static MockResults mockParticipantResults29 = new StaticMockResults();
	static {
		mockParticipantResults29.setInteger(0,ParticipantBean.DBID,29);
		mockParticipantResults29.setString(0,ParticipantBean.DBQUANTIFICATION,"individual");
		mockParticipantResults29.setString(0,ParticipantBean.DBLABEL,"female");
		mockParticipantResults29.setString(0,ParticipantBean.DBGENERATEDID,"http://arachb.org/arachb/ARACHB_0000349");
		mockParticipantResults29.setInteger(0,ParticipantBean.DBPROPERTY, 306);  //used
		mockParticipantResults29.setString(0,ParticipantBean.DBPUBLICATIONTAXON,"Leucauge mariana");
		mockParticipantResults29.setString(0,ParticipantBean.DBPUBLICATIONANATOMY,"female");
		mockParticipantResults29.setString(0,ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockParticipantResults29.setInteger(0,ParticipantBean.DBHEADELEMENT,62);
	}

	
	private static MockResults mockParticipantResults2 = new StaticMockResults();
	static {
		mockParticipantResults2.setInteger(0,ParticipantBean.DBID,2);
		mockParticipantResults2.setString(0,ParticipantBean.DBQUANTIFICATION,"some");
		mockParticipantResults2.setString(0,ParticipantBean.DBLABEL,"");
		mockParticipantResults2.setString(0,ParticipantBean.DBGENERATEDID,null);
		mockParticipantResults2.setString(0,ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockParticipantResults2.setString(0,ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockParticipantResults2.setString(0,ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockParticipantResults2.setInteger(0,ParticipantBean.DBHEADELEMENT,51);
	}
	

	private static StaticMockResults mockClaimResults1 = new StaticMockResults();
	static{
		mockClaimResults1.setInteger(0,ClaimBean.DBID, 1);
		mockClaimResults1.setInteger(0,ClaimBean.DBPUBLICATION,3);
		mockClaimResults1.setInteger(0,ClaimBean.DBBEHAVIORTERM,11398);
		mockClaimResults1.setString(0,ClaimBean.DBPUBLICATIONBEHAVIOR,"Stick-like posture");
		mockClaimResults1.setInteger(0,ClaimBean.DBEVIDENCE,0);
		mockClaimResults1.setString(0,ClaimBean.DBGENERATEDID,"");
		mockClaimResults1.setString(0,ClaimBean.DBPUBDOI, TESTDOI);
		mockClaimResults1.setString(0,ClaimBean.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}

	private static StaticMockResults mockClaimResults2 = new StaticMockResults();
	static{
		mockClaimResults2.setInteger(0,ClaimBean.DBID, 2);
		mockClaimResults2.setInteger(0,ClaimBean.DBPUBLICATION,3);
		mockClaimResults2.setInteger(0,ClaimBean.DBBEHAVIORTERM,11132);
		mockClaimResults2.setString(0,ClaimBean.DBPUBLICATIONBEHAVIOR,"prey capture");
		mockClaimResults2.setInteger(0,ClaimBean.DBEVIDENCE,0);
		mockClaimResults2.setString(0,ClaimBean.DBGENERATEDID,"");
		mockClaimResults2.setString(0,ClaimBean.DBPUBDOI, TESTDOI);
		mockClaimResults2.setString(0,ClaimBean.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}

	private static StaticMockResults mockClaimResults26 = new StaticMockResults();
	static{
		mockClaimResults26.setInteger(0,ClaimBean.DBID, 26);
		mockClaimResults26.setInteger(0,ClaimBean.DBPUBLICATION,123);
		mockClaimResults26.setInteger(0,ClaimBean.DBBEHAVIORTERM,11052);
		mockClaimResults26.setString(0,ClaimBean.DBPUBLICATIONBEHAVIOR,"turn toward male");
		mockClaimResults26.setInteger(0,ClaimBean.DBEVIDENCE,0);
		mockClaimResults26.setString(0,ClaimBean.DBGENERATEDID,"");
		mockClaimResults26.setString(0,ClaimBean.DBPUBDOI, TESTDOI);
		mockClaimResults26.setString(0,ClaimBean.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}

	
	private static MockResults mockTaxonResults = new StaticMockResults();
	static{
		mockTaxonResults.setInteger(0,TaxonBean.DBID, 1);
	}
	
	private static MockResults mockTaxonResults2 = new StaticMockResults();
	static{
		mockTaxonResults2.setInteger(0,TaxonBean.DBID, 12);
	}
	
	private static MockResults mockIndividualResults94 = new StaticMockResults();
	static{
		mockIndividualResults94.setInteger(0,IndividualBean.DBID, 94);
		mockIndividualResults94.setString(0,IndividualBean.DBLABEL, "female");
		mockIndividualResults94.setString(0,IndividualBean.DBSOURCEID, null);
		mockIndividualResults94.setString(0,IndividualBean.DBGENERATEDID, null);
		mockIndividualResults94.setInteger(0,IndividualBean.DBTERM, 111938);
	}

	private static MockResults mockIndividualResults95 = new StaticMockResults();
	static{
		mockIndividualResults95.setInteger(0,IndividualBean.DBID, 95);
		mockIndividualResults95.setString(0,IndividualBean.DBLABEL, "whole organism of female");
		mockIndividualResults95.setString(0,IndividualBean.DBSOURCEID, null);
		mockIndividualResults95.setString(0,IndividualBean.DBGENERATEDID, null);
		mockIndividualResults95.setInteger(0,IndividualBean.DBTERM, 10473);
	}

	private static MockResults mockNarrativeResults1 = new StaticMockResults();
	static{
		mockNarrativeResults1.setInteger(0, NarrativeBean.DBID, 1);
		mockNarrativeResults1.setInteger(0, NarrativeBean.DBPUBLICATION, 123);
		mockNarrativeResults1.setString(0, NarrativeBean.DBLABEL, "courtship sequence");
		mockNarrativeResults1.setString(0, NarrativeBean.DBDESCRIPTION, "");
	}
	
	private static MockResults mockNarrativeResults2 = new StaticMockResults();
	static{
		mockNarrativeResults2.setInteger(0, NarrativeBean.DBID, 2);		
		mockNarrativeResults2.setInteger(0, NarrativeBean.DBPUBLICATION, 123);
		mockNarrativeResults2.setString(0, NarrativeBean.DBLABEL, "sperm induction");
		mockNarrativeResults2.setString(0, NarrativeBean.DBDESCRIPTION, "");
	}

	private static MockResults mockPElementResults = new StaticMockResults();
	static{
		mockPElementResults.setInteger(0,PElementBean.DBID, 1);
		mockPElementResults.setInteger(0,PElementBean.DBTYPE, 1);
		mockPElementResults.setInteger(0,PElementBean.DBPARTICIPANT, 1);		
	}

	private static MockResults mockPElementResults2 = new StaticMockResults();
	static{
		mockPElementResults2.setInteger(0,PElementBean.DBID, 2);
		mockPElementResults2.setInteger(0,PElementBean.DBTYPE, 1);
		mockPElementResults2.setInteger(0,PElementBean.DBPARTICIPANT, 1);
	}
	
	private static MockResults mockPElementResults61 = new StaticMockResults();
	static{
		mockPElementResults61.setInteger(0,PElementBean.DBID, 61);
		mockPElementResults61.setInteger(0,PElementBean.DBTYPE, 3);
		mockPElementResults61.setInteger(0,PElementBean.DBPARTICIPANT, 29);
		
	}
	
	private static MockResults mockPElementResults62 = new StaticMockResults();
	static{
		mockPElementResults62.setInteger(0,PElementBean.DBID, 62);
		mockPElementResults62.setInteger(0,PElementBean.DBTYPE, 3);
		mockPElementResults62.setInteger(0,PElementBean.DBPARTICIPANT, 29);
		
	}
	
//	private static MockResults mockPEParent1Results = new MockResults(1);
//	static {
//		mockPEParent1Results.setInteger(0,PElementBean.DBPARENTID, 2);
//		mockPEParent1Results.setInteger(0,PElementBean.DBPARENTPROPERTY, 15);
//	}
//	
//	private static MockResults mockPEParent61Results = new MockResults(1);
//	static {
//		mockPEParent61Results.setInteger(0,PElementBean.DBPARENTID, 62);
//		mockPEParent61Results.setInteger(0,PElementBean.DBPARENTPROPERTY, 15);
//	}
//	
//	
//	private static MockResults mockPEChild2Results = new MockResults(1);
//	static {
//		mockPEChild2Results.setInteger(0,PElementBean.DBCHILDID, 1);
//		mockPEChild2Results.setInteger(0,PElementBean.DBCHILDPROPERTY, 15);		
//	}
//	
//	
//	private static MockResults mockPEChild62Results = new MockResults(1);
//	static {
//		mockPEChild62Results.setInteger(0,PElementBean.DBCHILDID, 61);
//		mockPEChild62Results.setInteger(0,PElementBean.DBCHILDPROPERTY, 15);		
//	}
	
	final static String ACTIVELYPARTICIPATESINURL = "http://purl.obolibrary.org/obo/RO_0002217";
	final static String ACTIVELYPARTICIPATESINLABEL = "actively participates in";	

	private static MockResults mockPropertyResults306 = new StaticMockResults();
	static {
		mockPropertyResults306.setInteger(0,PropertyBean.DBID, 306);
		mockPropertyResults306.setString(0,PropertyBean.DBSOURCEID, ACTIVELYPARTICIPATESINURL);
		mockPropertyResults306.setInteger(0,PropertyBean.DBAUTHORITY,0);
		mockPropertyResults306.setString(0,PropertyBean.DBLABEL,ACTIVELYPARTICIPATESINLABEL);
		mockPropertyResults306.setString(0,PropertyBean.DBGENERATEDID,null);
		mockPropertyResults306.setString(0,PropertyBean.DBCOMMENT,null);		
	}

	final static String PARTOFURL = "http://purl.obolibrary.org/obo/BFO_0000050";
	final static String PARTOFLABEL = "part_of";	

	private static MockResults mockPropertyResults15 = new StaticMockResults();
	static {
		mockPropertyResults15.setInteger(0,PropertyBean.DBID, 15);
		mockPropertyResults15.setString(0,PropertyBean.DBSOURCEID, PARTOFURL);
		mockPropertyResults15.setInteger(0,PropertyBean.DBAUTHORITY,0);
		mockPropertyResults15.setString(0,PropertyBean.DBLABEL,PARTOFLABEL);
		mockPropertyResults15.setString(0,PropertyBean.DBGENERATEDID,null);
		mockPropertyResults15.setString(0,PropertyBean.DBCOMMENT,null);		
	}

	
	private static MockResults termFillResultsNull = new StaticMockResults();
	static {
		termFillResultsNull.setInteger(0,PElementBean.DBTERM, (Integer) null);
	}
	
	private static MockResults termFillResults4838 = new StaticMockResults();
	static {
		termFillResults4838.setInteger(0,PElementBean.DBTERM, 4838);
	}

	
	private static MockResults termFillResults10473 = new StaticMockResults();
	static {
		termFillResults10473.setInteger(0,PElementBean.DBTERM, 10473);
	}

	private static MockResults termFillResults11398 = new StaticMockResults();
	static {
		termFillResults11398.setInteger(0,PElementBean.DBTERM, 11398);
	}

	private static MockResults individualFillResultsNull = new StaticMockResults();
	static {
		individualFillResultsNull.setInteger(0,PElementBean.DBINDIVIDUAL, (Integer) null);
	}


	private static MockResults individualFillResults94 = new StaticMockResults();
	static {
		individualFillResults94.setInteger(0,PElementBean.DBINDIVIDUAL, 94);
	}

	private static MockResults individualFillResults95 = new StaticMockResults();
	static {
		individualFillResults95.setInteger(0,PElementBean.DBINDIVIDUAL, 95);
	}
	
	private static Map<String,String> mockImportSourceMap = new HashMap<String,String>();
	static{
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/eco.owl", "evidence");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/ncbitaxon.owl", "taxonomy");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/spd.owl", "anatomy");
		mockImportSourceMap.put("http://behavior-ontology.googlecode.com/svn/trunk/behavior.owl", "behavior");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/go.owl", "gene products");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/pato.owl", "qualities");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/chebi.owl", "chemistry");
		mockImportSourceMap.put("http://purl.obolibrary.org/obo/uberon.owl", "bilateralian anatomy");
	}

	private static Map<String,String> mockOntologyNamesMap = new HashMap<String,String>();
	static{
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/eco.owl", "Evidence Codes (ECO)");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/ncbitaxon.owl", "NCBI Taxonomy");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/spd.owl", "Spider Anatomy");
		mockOntologyNamesMap.put("http://behavior-ontology.googlecode.com/svn/trunk/behavior.owl", 
				                 "NeuroBehavior Ontology");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/go.owl", "Gene Ontology");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/pato.owl", "Phenotype and Trait Ontology");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/chebi.owl", "CHEBI");
		mockOntologyNamesMap.put("http://purl.obolibrary.org/obo/uberon.owl", "Uberon");
	}

	
	private IRIManager irimanager;



	public MockConnection() throws Exception{
		irimanager = new IRIManager(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Important utility - since abstract results need to work like real ResultSets and advance
	 * a cursor using next, this wraps up that behavior for initializing from known singleton 
	 * MockResults.
	 * @param bn a bean to fill
	 * @param r a result set (most likely a singleton result)
	 * @throws SQLException because fill might have thrown if the the ResultSet actually came from SQL.
	 */
	private void fillOrThrow(BeanBase bn, AbstractResults r) throws SQLException{
		if (r.next()){
			bn.fill(r);
		}
		else {
			throw new IllegalStateException();
		}
	}

	
	@Override
	public PublicationBean getPublication(int pub) throws SQLException {
		PublicationBean result = new PublicationBean();
		switch (pub){
			case 1:{
			result.fill(mockPublicationResults);
			break;
		}
			case 2: {
			result.fill(mockPublicationResults2);
			break;
		}
			case 3: {
			result.fill(mockPublicationResults3);
			break;
		}
		default:
			result = null;
	}	
	return result;
	}

	@Override
	public Set<PublicationBean> getPublicationTable() throws SQLException {
		Set<PublicationBean> results = new HashSet<PublicationBean>();
		PublicationBean p1 = new PublicationBean();
		p1.fill(mockPublicationResults);
		results.add(p1);
		PublicationBean p2 = new PublicationBean();
		p2.fill(mockPublicationResults2);
		results.add(p2);
		PublicationBean p3 = new PublicationBean();
		p3.fill(mockPublicationResults3);
		results.add(p3);
		PublicationBean p123 = new PublicationBean();
		p123.fill(mockPublicationResults123);
		results.add(p123);
		return results;
	}

	private Map <Integer,TermBean> termBeanCache = new HashMap <Integer,TermBean>();
	
	@Override
	public TermBean getTerm(int i) throws SQLException {
		if (termBeanCache.containsKey(i)){
			return termBeanCache.get(i);
		}
		TermBean tb = new TermBean();
		switch (i){
		case 4838:
			fillOrThrow(tb,mockTermResults4838);
			break;
		case 2:
			fillOrThrow(tb,mockTermResults11052);
			break;
		case 10473:
			fillOrThrow(tb,mockTermResults10473);
			break;
		case 11398:
			fillOrThrow(tb,mockTermResults11398);
			break;
		case 0:
			tb = null;
			break;
		default:
			throw new RuntimeException("Bad value to getTerm: " + i);
		}
		termBeanCache.put(i, tb);
		return tb;
	}



	@Override
	public void updateTerm(TermBean termBean) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public Set<ParticipantBean> getParticipantTable(int claimId) throws SQLException {
		Set<ParticipantBean> results = new HashSet<ParticipantBean>();	
		switch (claimId){
		case 1:
			ParticipantBean p1 = new ParticipantBean();
			fillOrThrow(p1,mockParticipantResults1);
			results.add(p1);
			break;
		case 26:
			ParticipantBean p29 = new ParticipantBean();
			fillOrThrow(p29,mockParticipantResults29);
			results.add(p29);
			break;
		default:
			throw new IllegalStateException("mock Claim Bean has unknown id: " + claimId);
		}
		return results;
	}


	@Override
	public ClaimBean getClaim(int i) throws Exception {
		ClaimBean result = new ClaimBean();
		switch (i){
		case 1:
		case 2:
			fillOrThrow(result,mockClaimResults1);
			break;
		case 26:
			fillOrThrow(result,mockClaimResults26);
			break;
		default:
			throw new IllegalStateException("bad id to getClaim has unknown id: " + i);
		}
		return result;
	}


	@Override
	public Set<ClaimBean> getClaimTable() throws Exception {
		Set<ClaimBean> results = new HashSet<ClaimBean>();
		ClaimBean c1 = new ClaimBean();
		fillOrThrow(c1,mockClaimResults1);
		ClaimBean c2 = new ClaimBean();
		fillOrThrow(c2,mockClaimResults2);
		ClaimBean c26 = new ClaimBean();
		fillOrThrow(c26,mockClaimResults26);
		results.add(c1);
		results.add(c2);
		results.add(c26);
		return results;
	}
	
	@Override
	public TaxonBean getTaxonRow(int id) throws SQLException {
		TaxonBean result = new TaxonBean();
		switch (id){
		case 1:
			fillOrThrow(result,mockTaxonResults);
			break;
		case 2:
			fillOrThrow(result,mockTaxonResults2);
			break;
		default:
			throw new IllegalStateException("mock Taxon Bean has unknown id: " + id);
		}
		return result;
	}

	@Override
	public Set<TaxonBean> getTaxonTable() throws SQLException {
		Set<TaxonBean> results = new HashSet<TaxonBean>();
		TaxonBean t1 = new TaxonBean();
		fillOrThrow(t1,mockTaxonResults);
		results.add(t1);
		TaxonBean t2 = new TaxonBean();
		fillOrThrow(t2,mockTaxonResults2);
		results.add(t2);
		return results;
	}



	// Sort of ugly (do better somehow?)
	@Override
	public void updateNamedEntity(UpdateableBean b) throws SQLException{
		b.updateDB(this);
	}
	
	@Override
	public void updateClaim(ClaimBean cl){
		switch (cl.getId()){
		case 1:
			mockClaimResults1.setString(0,ClaimBean.DBGENERATEDID, cl.getGeneratedId());
			break;
		default:
			break;
		}
	}
	
	@Override
	public void updateParticipant(ParticipantBean p){
		
	}
	
	@Override
	public void updatePublication(PublicationBean p){
		switch (p.getId()){
		case 2:
			mockPublicationResults2.setString(0,PublicationBean.DBGENERATEDID, p.getGeneratedId());
			break;
		case 3:
			mockPublicationResults3.setString(0,PublicationBean.DBGENERATEDID, p.getGeneratedId());
			break;
		default:
			break;
		}
	}		
	
	
	@Override
	public void updateTaxon(TaxonBean t){
		
	}

	@Override
	public Map<String, String> loadImportSourceMap() throws Exception {
		return mockImportSourceMap;
	}

	@Override
	public Map<String, String> loadOntologyNamesForLoading() throws SQLException {
		return mockOntologyNamesMap;
	}
	
	
	private final Map <Integer,IndividualBean> individualBeanCache = new HashMap<Integer,IndividualBean>();
	
	@Override
	public IndividualBean getIndividual(int id) throws SQLException {
		if (individualBeanCache.containsKey(id)){
			return individualBeanCache.get(id);
		}
		IndividualBean ib = new IndividualBean();
		switch (id){
		case 94:
			fillOrThrow(ib,mockIndividualResults94);
			break;

		case 95: 
			fillOrThrow(ib,mockIndividualResults95);
			break;
		case 0:
			ib = null;
			break;
		default:
			throw new IllegalStateException("mock Individual Bean has unknown id: " + id);
		}
		individualBeanCache.put(id, ib);
		return ib;
	}	

	@Override
	public void updateIndividual(IndividualBean ib){
		switch (ib.getId()){
		case 94:
			mockIndividualResults94.setString(0,IndividualBean.DBGENERATEDID, ib.getGeneratedId());
			break;
		case 95:
			mockIndividualResults95.setString(0,IndividualBean.DBGENERATEDID, ib.getGeneratedId());
			break;
		default:
			throw new IllegalStateException("mock Individual Bean has unknown id");
		}
	}		

	
	@Override
	public NarrativeBean getNarrative(int nId) throws SQLException{
		NarrativeBean nb = new NarrativeBean();
		switch (nId){
		case 1:
			fillOrThrow(nb,mockIndividualResults94);
			break;
		case 2: 
			fillOrThrow(nb,mockIndividualResults95);
			break;
		case 0:
			nb = null;
			break;
		default:
			throw new IllegalStateException("mock Narrative Bean has unknown id: " + nId);
		}
		return nb;
	}

	@Override
	public void updateNarrative(NarrativeBean nb) throws SQLException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Set<PElementBean> getPElementTable(ParticipantBean p) throws Exception {
		final Set<PElementBean> result = new HashSet<PElementBean>();
		switch (p.getId()){
		case 1:
			PElementBean e1 = new PElementBean();
			PElementBean e2 = new PElementBean();
			e1.fill(mockPElementResults);
			e2.fill(mockPElementResults2);
//			fillPElementTerm(e1);
//			fillPElementIndividual(e1);
//			fillPElementTerm(e2);
//			fillPElementIndividual(e2);			
//			fillPElementParents(e1);
//			fillPElementChildren(e1);
//			fillPElementChildren(e2);
//			fillPElementChildren(e2);
			result.add(e1);
			result.add(e2);
			break;
		case 29:
			PElementBean e61 = new PElementBean();
			PElementBean e62 = new PElementBean();
			e61.fill(mockPElementResults61);
			e62.fill(mockPElementResults62);
//			fillPElementTerm(e61);
//			fillPElementIndividual(e61);
//			fillPElementTerm(e62);
//			fillPElementIndividual(e62);			
//			fillPElementParents(e61);
//			fillPElementChildren(e61);
//			fillPElementChildren(e62);
//			fillPElementChildren(e62);
			result.add(e61);
			result.add(e62);
			break;
			
		default:
			throw new RuntimeException("getPElements (Mock) got participant with bad id: " + p.getId());
		}
		return result;
	}

	@Override
	public PElementBean getPElement(int id) throws Exception {
		final PElementBean result = new PElementBean();
		switch (id){
		case 1:{
			result.fill(mockPElementResults);
//			fillPElementParents(result);
//			fillPElementChildren(result);
			break;
		}
		case 2:{
			result.fill(mockPElementResults2);
//			fillPElementParents(result);
//			fillPElementChildren(result);
			break;
		}
		case 61:{
			result.fill(mockPElementResults61);
//			fillPElementParents(result);
//			fillPElementChildren(result);
			break;
		}
		case 62:{
			result.fill(mockPElementResults62);
//			fillPElementParents(result);
//			fillPElementChildren(result);
			break;
		}
		default :{
			throw new RuntimeException("Bad id " + id + " to mock getPElement");
		}
		}
		return result;
	}

//	public void fillPElementParents(PElementBean result) throws Exception{
//		switch (result.getId()){
//		case 1:
//			mockPEParent1Results.reset();
//			result.fillParents(mockPEParent1Results);
//			break;
//		case 61:
//			mockPEParent61Results.reset();
//			result.fillParents(mockPEParent61Results);
//		default:
//			result.fillParents(MockResults.NullResults);
//		}
//	}
//
//	public void fillPElementChildren(PElementBean result) throws Exception{
//		switch (result.getId()){
//		case 2:
//			mockPEChild2Results.reset();
//			result.fillChildren(mockPEChild2Results);
//			break;
//		case 62:
//			mockPEChild62Results.reset();
//			result.fillChildren(mockPEChild62Results);
//			break;
//		default:
//			result.fillChildren(MockResults.NullResults);
//		}
//		
//	}

	@Override
	public PropertyBean getProperty(int id) throws Exception {
		final PropertyBean result = new PropertyBean();
		switch (id){
		case 306:
			result.fill(mockPropertyResults306);
			break;
		case 15:
			result.fill(mockPropertyResults15);
			break;
		default:
			return null;
		}
		return result;
	}

	@Override
	public PropertyBean getPropertyFromSourceId(String uid) throws Exception {
		final PropertyBean result = new PropertyBean();
		switch (uid){
			case ACTIVELYPARTICIPATESINURL:
				result.fill(mockPropertyResults306);
				break;
			case PARTOFURL:
				result.fill(mockPropertyResults15);
				break;
			default:
				return null;
		}
		return result;
	}

//	@Override
//	public void fillPElementTerm(PElementBean pb) throws Exception {
//		switch (pb.getId()){
//		case 1:
//			pb.fillTerm(termFillResults4838, this);
//			break;
//		case 2: 
//			pb.fillTerm(termFillResults10473, this);
//			break;
//		case 61: 
//		case 62: 
//			pb.fillTerm(termFillResultsNull, this);
//			break;
//		
//		default:
//			throw new RuntimeException("Bad pelementbean id: " + pb.getId());
//		}
//		
//	}
//
//	@Override
//	public void fillPElementIndividual(PElementBean pb) throws Exception {
//		switch (pb.getId()){
//		case 1:
//		case 2:
//			break;
//		case 61:
//			pb.fillIndividual(individualFillResults94, this);
//			break;
//		case 62:
//			pb.fillIndividual(individualFillResults95, this);
//			break;
//		default:
//			throw new RuntimeException("Bad pelementbean id: " + pb.getId());
//		}
//	}
//		// TODO Auto-generated method stub
//


	@Override
	public ParticipantBean getParticipant(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

@Override
public Set<Integer> getParticipantSet(int claimId) throws Exception {
	// TODO Auto-generated method stub
	return null;
}


@Override
public Set<Integer> getPElementSet(ParticipantBean p) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IRIManager getIRIManager(){
	//TODO Not sure how to implement this
	return null;
}

@Override
public UidSet getUidSet(int setId) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Set<UidSet> getUidSetTable() throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void updateUidSet(UidSet s) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public String getUidSetLastGenId() throws Exception {
	// TODO Auto-generated method stub
	return null;
}
		





}
