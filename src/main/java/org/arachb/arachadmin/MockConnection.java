package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.arachb.owlbuilder.lib.AbstractNamedEntity;
import org.arachb.owlbuilder.lib.IndividualParticipant;

public class MockConnection implements AbstractConnection {
	
	final private static String JOURNALTYPE = "Journal";
	final private static String TESTDOI = 
			"http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	final private static String TESTARACHBID = "http://arachb.org/arachb/TEST_0000001";
	final private static String TESTBEHAVIOR = "http://purl.obolibrary.org/obo/NBO_0000355";
	final private static String TESTPUBTAXON = "Tetragnatha straminea";
	final private static int MAXMOCKIDVALUE = 10;  //might need to assign this realistically
	
	private static MockResults mockPublicationResults = new MockResults();
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
		mockPublicationResults.setString(PublicationBean.DBGENERATEDID,null);
        mockPublicationResults.setString(PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults.setString(PublicationBean.DBCURATIONUPDATE,"");
	}
	
	private static MockResults mockPublicationResults2 = new MockResults();
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

	private static MockResults mockTermResults = new MockResults();
	static {
		mockTermResults.setInteger(TermBean.DBID,1);
		mockTermResults.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults.setInteger(TermBean.DBDOMAIN,3);
		mockTermResults.setInteger(TermBean.DBAUTHORITY,1);
		mockTermResults.setString(TermBean.DBLABEL,"Tetragnatha straminea");
		mockTermResults.setString(TermBean.DBGENERATEDID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults.setString(TermBean.DBCOMMENT,"");
	}

	private static MockResults mockTermResults2 = new MockResults();
	static {
		mockTermResults2.setInteger(TermBean.DBID,2);
		mockTermResults2.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults2.setInteger(TermBean.DBDOMAIN,2);
		mockTermResults2.setInteger(TermBean.DBAUTHORITY,1);
		mockTermResults2.setString(TermBean.DBLABEL,"whole organism");
		mockTermResults2.setString(TermBean.DBGENERATEDID,null);
		mockTermResults2.setString(TermBean.DBCOMMENT,"");
	}

	
	private static MockResults mockPrimaryParticipantResults = new MockResults();
	static {
		mockPrimaryParticipantResults.setInteger(ParticipantBean.DBID,1);
		mockPrimaryParticipantResults.setInteger(ParticipantBean.DBTAXON,1);
		mockPrimaryParticipantResults.setInteger(ParticipantBean.DBSUBSTRATE,0);
		mockPrimaryParticipantResults.setInteger(ParticipantBean.DBANATOMY,2);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBQUANTIFICATION,"some");
		mockPrimaryParticipantResults.setString(ParticipantBean.DBGENERATEDID,"");
		mockPrimaryParticipantResults.setString(ParticipantBean.DBLABEL,"");
		mockPrimaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockPrimaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockPrimaryParticipantResults.setString(ParticipantBean.DBTAXONSOURCEID,null);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBTAXONGENERATEDID,null);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBANATOMYSOURCEID,null);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBANATOMYGENERATEDID,null);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBSUBSTRATESOURCEID,null);
		mockPrimaryParticipantResults.setString(ParticipantBean.DBSUBSTRATEGENERATEDID,null);
	}

	private static MockResults mockSecondaryParticipantResults = new MockResults();
	static {
		mockSecondaryParticipantResults.setInteger(ParticipantBean.DBID,2);
		mockSecondaryParticipantResults.setInteger(ParticipantBean.DBTAXON,1);
		mockSecondaryParticipantResults.setInteger(ParticipantBean.DBSUBSTRATE,0);
		mockSecondaryParticipantResults.setInteger(ParticipantBean.DBANATOMY,4);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBQUANTIFICATION,"some");
		mockSecondaryParticipantResults.setString(ParticipantBean.DBGENERATEDID,"");
		mockSecondaryParticipantResults.setString(ParticipantBean.DBLABEL,"");
		mockSecondaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockSecondaryParticipantResults.setString(ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockSecondaryParticipantResults.setString(ParticipantBean.DBTAXONSOURCEID,null);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBTAXONGENERATEDID,null);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBANATOMYSOURCEID,null);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBANATOMYGENERATEDID,null);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBSUBSTRATESOURCEID,null);
		mockSecondaryParticipantResults.setString(ParticipantBean.DBSUBSTRATEGENERATEDID,null);
	}
	
	private static MockResults mockClaimResults = new MockResults();
	static{
		mockClaimResults.setInteger(ClaimBean.DBID, 1);
		mockClaimResults.setInteger(ClaimBean.DBPUBLICATION,6);
		mockClaimResults.setInteger(ClaimBean.DBBEHAVIORTERM,9);
		mockClaimResults.setString(ClaimBean.DBPUBLICATIONBEHAVIOR,"behavior");
		mockClaimResults.setInteger(ClaimBean.DBEVIDENCE,0);
		mockClaimResults.setString(ClaimBean.DBGENERATEDID,"");
		mockClaimResults.setString(ClaimBean.DBPUBDOI, TESTDOI);
		mockClaimResults.setString(ClaimBean.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}
	
	private static MockResults mockTaxonResults = new MockResults();
	static{
		mockTaxonResults.setInteger(TaxonBean.DBID, 1);
	}
	
	private static MockResults mockTaxonResults2 = new MockResults();
	static{
		mockTaxonResults2.setInteger(TaxonBean.DBID, 12);
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

	public MockConnection() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PublicationBean getPublication(int get_publication) throws SQLException {
		PublicationBean result = new PublicationBean();
		if (get_publication == 1){
			result.fill(mockPublicationResults);
		}
		else {
			result.fill(mockPublicationResults2);			
		}
		return result;
	}

	@Override
	public Set<PublicationBean> getPublications() throws SQLException {
		Set<PublicationBean> results = new HashSet<PublicationBean>();
		PublicationBean p1 = new PublicationBean();
		p1.fill(mockPublicationResults);
		results.add(p1);
		PublicationBean p2 = new PublicationBean();
		p2.fill(mockPublicationResults);
		results.add(p2);
		return results;
	}


	@Override
	public TermBean getTerm(int i) throws SQLException {
		TermBean result = new TermBean();
		switch (i){
		case 1:
			result.fill(mockTermResults);
			break;
		default:
			result.fill(mockTermResults2);
		}
		return result;
	}

	@Override
	public Set<TermBean> getTerms() throws SQLException {
		Set<TermBean> results = new HashSet<TermBean>();
		TermBean t1 = new TermBean();
		t1.fill(mockTermResults);
		results.add(t1);
		TermBean t2 = new TermBean();
		t2.fill(mockTermResults2);
		results.add(t2);
		return results;
		
	}

	
	@Override
	public ParticipantBean getPrimaryParticipant(ClaimBean a) throws SQLException {
		final MockResults primaryResults = mockPrimaryParticipantResults;
		primaryResults.setString(ParticipantBean.DBTAXONSOURCEID, mockTermResults.getString(TermBean.DBSOURCEID));
		primaryResults.setString(ParticipantBean.DBANATOMYSOURCEID, mockTermResults2.getString(TermBean.DBSOURCEID));
		ParticipantBean result = new ParticipantBean();
		result.fill(primaryResults);
		return result;
	}

	@Override
	public Set<ParticipantBean> getParticipants(ClaimBean a) throws SQLException {
		Set<ParticipantBean> results = new HashSet<ParticipantBean>();
		final MockResults secondaryResults = mockSecondaryParticipantResults;
		secondaryResults.setString(ParticipantBean.DBTAXONSOURCEID, mockTermResults.getString(TermBean.DBSOURCEID));
		secondaryResults.setString(ParticipantBean.DBANATOMYSOURCEID, mockTermResults2.getString(TermBean.DBSOURCEID));
		ParticipantBean p1 = new ParticipantBean();
		p1.fill(secondaryResults);
		results.add(p1);
		return results;
	}


	@Override
	public ClaimBean getClaim(int i) throws Exception {
		ClaimBean result = new ClaimBean();
        result.fill(mockClaimResults);
		return result;
	}


	@Override
	public Set<ClaimBean> getClaims() throws Exception {
		Set<ClaimBean> results = new HashSet<ClaimBean>();
		ClaimBean a1 = new ClaimBean();
		a1.fill(mockClaimResults);
		results.add(a1);
		return results;
	}
	
	@Override
	public TaxonBean getTaxon(int id) throws SQLException {
		TaxonBean result = new TaxonBean();
		if (id == 1){
			result.fill(mockTaxonResults);
		}
		else {
			result.fill(mockTaxonResults2);			
		}
		return result;
	}

	@Override
	public Set<TaxonBean> getTaxa() throws SQLException {
		Set<TaxonBean> results = new HashSet<TaxonBean>();
		TaxonBean t1 = new TaxonBean();
		t1.fill(mockTaxonResults);
		results.add(t1);
		TaxonBean t2 = new TaxonBean();
		t2.fill(mockTaxonResults2);
		results.add(t2);
		return results;
	}



	// Sort of ugly (do better somehow?)
	@Override
	public void updateNamedEntity(AbstractNamedEntity e) throws SQLException{
		e.updateDB(this);
	}
	
	@Override
	public void updateClaim(ClaimBean cl){
		switch (cl.getId()){
		case 1:
			mockClaimResults.setString(ClaimBean.DBGENERATEDID, cl.getGeneratedId());
		}
	}
	
	@Override
	public void updateParticipant(ParticipantBean p){
		
	}
	
	@Override
	public void updatePublication(PublicationBean p){
		switch (p.getId()){
		case 2:
			mockPublicationResults2.setString(PublicationBean.DBGENERATEDID, p.getGeneratedId());
			break;
		default:
			break;
		}
	}		
	
	@Override
	public void updateTerm(TermBean t){
		switch (t.getId()){
		case 1:
			mockTermResults.setString(TermBean.DBGENERATEDID, t.getGeneratedId());
			break;
		default:
			mockTermResults2.setString(TermBean.DBGENERATEDID, t.getGeneratedId());
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
	

	@Override
	public int scanPrivateIDs(){
		return MAXMOCKIDVALUE;
	}



}
