package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
		mockPublicationResults.setInteger(Publication.DBID, 1);
		mockPublicationResults.setString(Publication.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults.setString(Publication.DBDISPENSATION,"");
		mockPublicationResults.setString(Publication.DBDOWNLOADED,"");
		mockPublicationResults.setString(Publication.DBREVIEWED,"");
		mockPublicationResults.setString(Publication.DBTITLE,"");
		mockPublicationResults.setString(Publication.DBALTERNATETITLE,"");
		mockPublicationResults.setString(Publication.DBAUTHORLIST,"");
		mockPublicationResults.setString(Publication.DBEDITORLIST,"");
		mockPublicationResults.setString(Publication.DBSOURCEPUBLICATION,"");
		mockPublicationResults.setInteger(Publication.DBVOLUME,1);
		mockPublicationResults.setString(Publication.DBISSUE,"");
		mockPublicationResults.setString(Publication.DBSERIALIDENTIFIER,"");
		mockPublicationResults.setString(Publication.DBPAGERANGE,"");
		mockPublicationResults.setString(Publication.DBPUBLICATIONDATE,"");
		mockPublicationResults.setString(Publication.DBPUBLICATIONYEAR,"");
		mockPublicationResults.setString(Publication.DBDOI,TESTDOI);
		mockPublicationResults.setString(Publication.DBGENERATEDID,null);
        mockPublicationResults.setString(Publication.DBCURATIONSTATUS,"");
        mockPublicationResults.setString(Publication.DBCURATIONUPDATE,"");
	}
	
	private static MockResults mockPublicationResults2 = new MockResults();
	static {
		mockPublicationResults2.setInteger(Publication.DBID, 2);
		mockPublicationResults2.setString(Publication.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults2.setString(Publication.DBDISPENSATION,"");
		mockPublicationResults2.setString(Publication.DBDOWNLOADED,"");
		mockPublicationResults2.setString(Publication.DBREVIEWED,"");
		mockPublicationResults2.setString(Publication.DBTITLE,"");
		mockPublicationResults2.setString(Publication.DBALTERNATETITLE,"");
		mockPublicationResults2.setString(Publication.DBAUTHORLIST,"");
		mockPublicationResults2.setString(Publication.DBEDITORLIST,"");
		mockPublicationResults2.setString(Publication.DBSOURCEPUBLICATION,"");
		mockPublicationResults2.setInteger(Publication.DBVOLUME,1);
		mockPublicationResults2.setString(Publication.DBISSUE,"");
		mockPublicationResults2.setString(Publication.DBSERIALIDENTIFIER,"");
		mockPublicationResults2.setString(Publication.DBPAGERANGE,"");
		mockPublicationResults2.setString(Publication.DBPUBLICATIONDATE,"");
		mockPublicationResults2.setString(Publication.DBPUBLICATIONYEAR,"");
		mockPublicationResults2.setString(Publication.DBDOI, null);
		mockPublicationResults2.setString(Publication.DBGENERATEDID,TESTARACHBID);
        mockPublicationResults2.setString(Publication.DBCURATIONSTATUS,"");
        mockPublicationResults2.setString(Publication.DBCURATIONUPDATE,"");
	}

	private static MockResults mockTermResults = new MockResults();
	static {
		mockTermResults.setInteger(Term.DBID,1);
		mockTermResults.setString(Term.DBSOURCEID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults.setInteger(Term.DBDOMAIN,3);
		mockTermResults.setInteger(Term.DBAUTHORITY,1);
		mockTermResults.setString(Term.DBLABEL,"Tetragnatha straminea");
		mockTermResults.setString(Term.DBGENERATEDID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults.setString(Term.DBCOMMENT,"");
	}

	private static MockResults mockTermResults2 = new MockResults();
	static {
		mockTermResults2.setInteger(Term.DBID,2);
		mockTermResults2.setString(Term.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults2.setInteger(Term.DBDOMAIN,2);
		mockTermResults2.setInteger(Term.DBAUTHORITY,1);
		mockTermResults2.setString(Term.DBLABEL,"whole organism");
		mockTermResults2.setString(Term.DBGENERATEDID,null);
		mockTermResults2.setString(Term.DBCOMMENT,"");
	}

	
	private static MockResults mockPrimaryParticipantResults = new MockResults();
	static {
		mockPrimaryParticipantResults.setInteger(Participant.DBID,1);
		mockPrimaryParticipantResults.setInteger(Participant.DBTAXON,1);
		mockPrimaryParticipantResults.setInteger(Participant.DBSUBSTRATE,0);
		mockPrimaryParticipantResults.setInteger(Participant.DBANATOMY,2);
		mockPrimaryParticipantResults.setString(Participant.DBQUANTIFICATION,"some");
		mockPrimaryParticipantResults.setString(Participant.DBGENERATEDID,"");
		mockPrimaryParticipantResults.setString(Participant.DBLABEL,"");
		mockPrimaryParticipantResults.setString(Participant.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockPrimaryParticipantResults.setString(Participant.DBPUBLICATIONANATOMY,"");
		mockPrimaryParticipantResults.setString(Participant.DBPUBLICATIONSUBSTRATE,"");
		mockPrimaryParticipantResults.setString(Participant.DBTAXONSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBTAXONGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATESOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATEGENERATEDID,null);
	}

	private static MockResults mockSecondaryParticipantResults = new MockResults();
	static {
		mockSecondaryParticipantResults.setInteger(Participant.DBID,2);
		mockSecondaryParticipantResults.setInteger(Participant.DBTAXON,1);
		mockSecondaryParticipantResults.setInteger(Participant.DBSUBSTRATE,0);
		mockSecondaryParticipantResults.setInteger(Participant.DBANATOMY,4);
		mockSecondaryParticipantResults.setString(Participant.DBQUANTIFICATION,"some");
		mockSecondaryParticipantResults.setString(Participant.DBGENERATEDID,"");
		mockSecondaryParticipantResults.setString(Participant.DBLABEL,"");
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONANATOMY,"");
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONSUBSTRATE,"");
		mockPrimaryParticipantResults.setString(Participant.DBTAXONSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBTAXONGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATESOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATEGENERATEDID,null);
	}
	
	private static MockResults mockClaimResults = new MockResults();
	static{
		mockClaimResults.setInteger(Claim.DBID, 1);
		mockClaimResults.setInteger(Claim.DBPUBLICATION,6);
		mockClaimResults.setInteger(Claim.DBBEHAVIORTERM,9);
		mockClaimResults.setString(Claim.DBPUBLICATIONBEHAVIOR,"behavior");
		mockClaimResults.setInteger(Claim.DBEVIDENCE,0);
		mockClaimResults.setString(Claim.DBGENERATEDID,"");
		mockClaimResults.setString(Claim.DBPUBDOI, TESTDOI);
		mockClaimResults.setString(Claim.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}
	
	private static MockResults mockTaxonResults = new MockResults();
	static{
		mockTaxonResults.setInteger(Taxon.DBID, 1);
	}
	
	private static MockResults mockTaxonResults2 = new MockResults();
	static{
		mockTaxonResults2.setInteger(Taxon.DBID, 12);
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
	public Publication getPublication(int get_publication) throws SQLException {
		Publication result = new Publication();
		if (get_publication == 1){
			result.fill(mockPublicationResults);
		}
		else {
			result.fill(mockPublicationResults2);			
		}
		return result;
	}

	@Override
	public Set<Publication> getPublications() throws SQLException {
		Set<Publication> results = new HashSet<Publication>();
		Publication p1 = new Publication();
		p1.fill(mockPublicationResults);
		results.add(p1);
		Publication p2 = new Publication();
		p2.fill(mockPublicationResults);
		results.add(p2);
		return results;
	}


	@Override
	public Term getTerm(int i) throws SQLException {
		Term result = new Term();
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
	public Set<Term> getTerms() throws SQLException {
		Set<Term> results = new HashSet<Term>();
		Term t1 = new Term();
		t1.fill(mockTermResults);
		results.add(t1);
		Term t2 = new Term();
		t2.fill(mockTermResults2);
		results.add(t2);
		return results;
		
	}

	
	@Override
	public Participant getPrimaryParticipant(Claim a) throws SQLException {
		final MockResults primaryResults = mockPrimaryParticipantResults;
		primaryResults.setString(Participant.DBTAXONSOURCEID, mockTermResults.getString(Term.DBSOURCEID));
		primaryResults.setString(Participant.DBANATOMYSOURCEID, mockTermResults2.getString(Term.DBSOURCEID));
		Participant result = Participant.makeParticipant(primaryResults);
		return result;
	}

	@Override
	public Set<Participant> getParticipants(Claim a) throws SQLException {
		Set<Participant> results = new HashSet<Participant>();
		final MockResults secondaryResults = mockSecondaryParticipantResults;
		secondaryResults.setString(Participant.DBTAXONSOURCEID, mockTermResults.getString(Term.DBSOURCEID));
		secondaryResults.setString(Participant.DBANATOMYSOURCEID, mockTermResults2.getString(Term.DBSOURCEID));
		Participant p1 = Participant.makeParticipant(secondaryResults);
		results.add(p1);
		return results;
	}

	public void updateParticipant(IndividualParticipant p) throws SQLException{
		mockPrimaryParticipantResults.setString(Participant.DBGENERATEDID, p.getGeneratedId());
	}

	@Override
	public Claim getClaim(int i) throws Exception {
		Claim result = new Claim();
        result.fill(mockClaimResults);
		return result;
	}


	@Override
	public Set<Claim> getClaims() throws Exception {
		Set<Claim> results = new HashSet<Claim>();
		Claim a1 = new Claim();
		a1.fill(mockClaimResults);
		results.add(a1);
		return results;
	}
	
	@Override
	public Taxon getTaxon(int id) throws SQLException {
		Taxon result = new Taxon();
		if (id == 1){
			result.fill(mockTaxonResults);
		}
		else {
			result.fill(mockTaxonResults2);			
		}
		return result;
	}

	@Override
	public Set<Taxon> getTaxa() throws SQLException {
		Set<Taxon> results = new HashSet<Taxon>();
		Taxon t1 = new Taxon();
		t1.fill(mockTaxonResults);
		results.add(t1);
		Taxon t2 = new Taxon();
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
	public void updateClaim(Claim cl){
		switch (cl.getId()){
		case 1:
			mockClaimResults.setString(Claim.DBGENERATEDID, cl.getGeneratedId());
		}
	}
	
	@Override
	public void updateIndividualParticipant(IndividualParticipant p){
		
	}
	
	@Override
	public void updatePublication(Publication p){
		switch (p.getId()){
		case 2:
			mockPublicationResults2.setString(Publication.DBGENERATEDID, p.getIriString());
			break;
		default:
			break;
		}
	}		
	
	@Override
	public void updateTerm(Term t){
		switch (t.getId()){
		case 1:
			mockTermResults.setString(Term.DBGENERATEDID, t.getGeneratedId());
			break;
		default:
			mockTermResults2.setString(Term.DBGENERATEDID, t.getGeneratedId());
		}
		
	}
	
	@Override
	public void updateTaxon(Taxon t){
		
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
