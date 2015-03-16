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
	
	private static MockResults mockPublicationResults = new MockResults(1);
	static {
		mockPublicationResults.setInteger(0,PublicationBean.DBID, 1);
		mockPublicationResults.setString(0,PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults.setString(0,PublicationBean.DBDISPENSATION,"");
		mockPublicationResults.setString(0,PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults.setString(0,PublicationBean.DBREVIEWED,"");
		mockPublicationResults.setString(0,PublicationBean.DBTITLE,"");
		mockPublicationResults.setString(0,PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults.setString(0,PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults.setString(0,PublicationBean.DBEDITORLIST,"");
		mockPublicationResults.setString(0,PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults.setInteger(0,PublicationBean.DBVOLUME,1);
		mockPublicationResults.setString(0,PublicationBean.DBISSUE,"");
		mockPublicationResults.setString(0,PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults.setString(0,PublicationBean.DBPAGERANGE,"");
		mockPublicationResults.setString(0,PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults.setString(0,PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults.setString(0,PublicationBean.DBDOI,TESTDOI);
		mockPublicationResults.setString(0,PublicationBean.DBGENERATEDID, TESTGENID);
        mockPublicationResults.setString(0,PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults.setString(0,PublicationBean.DBCURATIONUPDATE,"");
	}
	
	private static MockResults mockPublicationResults2 = new MockResults(1);
	static {
		mockPublicationResults2.setInteger(0,PublicationBean.DBID, 2);
		mockPublicationResults2.setString(0,PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults2.setString(0,PublicationBean.DBDISPENSATION,"");
		mockPublicationResults2.setString(0,PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults2.setString(0,PublicationBean.DBREVIEWED,"");
		mockPublicationResults2.setString(0,PublicationBean.DBTITLE,"");
		mockPublicationResults2.setString(0,PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults2.setString(0,PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults2.setString(0,PublicationBean.DBEDITORLIST,"");
		mockPublicationResults2.setString(0,PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults2.setInteger(0,PublicationBean.DBVOLUME,1);
		mockPublicationResults2.setString(0,PublicationBean.DBISSUE,"");
		mockPublicationResults2.setString(0,PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults2.setString(0,PublicationBean.DBPAGERANGE,"");
		mockPublicationResults2.setString(0,PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults2.setString(0,PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults2.setString(0,PublicationBean.DBDOI, null);
		mockPublicationResults2.setString(0,PublicationBean.DBGENERATEDID,TESTARACHBID);
        mockPublicationResults2.setString(0,PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults2.setString(0,PublicationBean.DBCURATIONUPDATE,"");
	}

	private final static String doi3 = 
			"http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	private static MockResults mockPublicationResults3 = new MockResults(1);
	static {
		mockPublicationResults3.setInteger(0,PublicationBean.DBID, 3);
		mockPublicationResults3.setString(0,PublicationBean.DBPUBLICATIONTYPE,JOURNALTYPE);
		mockPublicationResults3.setString(0,PublicationBean.DBDISPENSATION,"");
		mockPublicationResults3.setString(0,PublicationBean.DBDOWNLOADED,"");
		mockPublicationResults3.setString(0,PublicationBean.DBREVIEWED,"");
		mockPublicationResults3.setString(0,PublicationBean.DBTITLE,"");
		mockPublicationResults3.setString(0,PublicationBean.DBALTERNATETITLE,"");
		mockPublicationResults3.setString(0,PublicationBean.DBAUTHORLIST,"");
		mockPublicationResults3.setString(0,PublicationBean.DBEDITORLIST,"");
		mockPublicationResults3.setString(0,PublicationBean.DBSOURCEPUBLICATION,"");
		mockPublicationResults3.setInteger(0,PublicationBean.DBVOLUME,1);
		mockPublicationResults3.setString(0,PublicationBean.DBISSUE,"");
		mockPublicationResults3.setString(0,PublicationBean.DBSERIALIDENTIFIER,"");
		mockPublicationResults3.setString(0,PublicationBean.DBPAGERANGE,"");
		mockPublicationResults3.setString(0,PublicationBean.DBPUBLICATIONDATE,"");
		mockPublicationResults3.setString(0,PublicationBean.DBPUBLICATIONYEAR,"");
		mockPublicationResults3.setString(0,PublicationBean.DBDOI, doi3);
		mockPublicationResults3.setString(0,PublicationBean.DBGENERATEDID, null);
        mockPublicationResults3.setString(0,PublicationBean.DBCURATIONSTATUS,"");
        mockPublicationResults3.setString(0,PublicationBean.DBCURATIONUPDATE,"");
	}


	private static MockResults mockTermResults2 = new MockResults(1);
	static {
		mockTermResults2.setInteger(0,TermBean.DBID,2);
		mockTermResults2.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults2.setInteger(0,TermBean.DBDOMAIN,2);
		mockTermResults2.setInteger(0,TermBean.DBAUTHORITY,1);
		mockTermResults2.setString(0,TermBean.DBLABEL,"whole organism");
		mockTermResults2.setString(0,TermBean.DBGENERATEDID,null);
		mockTermResults2.setString(0,TermBean.DBCOMMENT,"");
	}

	private static MockResults mockTermResults4838 = new MockResults(1);
	static {
		mockTermResults4838.setInteger(0,TermBean.DBID,4838);
		mockTermResults4838.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setInteger(0,TermBean.DBDOMAIN,3);
		mockTermResults4838.setInteger(0,TermBean.DBAUTHORITY,1);
		mockTermResults4838.setString(0,TermBean.DBLABEL,"Tetragnatha straminea");
		mockTermResults4838.setString(0,TermBean.DBGENERATEDID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setString(0,TermBean.DBCOMMENT,"");
	}

	private static MockResults mockTermResults11398 = new MockResults(1);
	static {
		mockTermResults11398.setInteger(0,TermBean.DBID,11398);
		mockTermResults11398.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NBO_0000358");
		mockTermResults11398.setInteger(0,TermBean.DBDOMAIN,1);
		mockTermResults11398.setInteger(0,TermBean.DBAUTHORITY,4);
		mockTermResults11398.setString(0,TermBean.DBLABEL,"resting posture");
		mockTermResults11398.setString(0,TermBean.DBGENERATEDID,null);
		mockTermResults11398.setString(0,TermBean.DBCOMMENT,"");
	}
	

	private static MockResults mockTermResults10473 = new MockResults(1);
	static {
		mockTermResults10473.setInteger(0,TermBean.DBID, 10473);
		mockTermResults10473.setString(0,TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults10473.setInteger(0,TermBean.DBDOMAIN,2);
		mockTermResults10473.setInteger(0,TermBean.DBAUTHORITY,4);
		mockTermResults10473.setString(0,TermBean.DBLABEL,"whole organism");
		mockTermResults10473.setString(0,TermBean.DBGENERATEDID,null);
		mockTermResults10473.setString(0,TermBean.DBCOMMENT,"");
		
	}

	
	private static MockResults mockParticipantResults1 = new MockResults(1);
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

	private static MockResults mockParticipantResults2 = new MockResults(1);
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
	
	private static MockResults mockClaimResults = new MockResults(1);
	static{
		mockClaimResults.setInteger(0,ClaimBean.DBID, 1);
		mockClaimResults.setInteger(0,ClaimBean.DBPUBLICATION,6);
		mockClaimResults.setInteger(0,ClaimBean.DBBEHAVIORTERM,9);
		mockClaimResults.setString(0,ClaimBean.DBPUBLICATIONBEHAVIOR,"behavior");
		mockClaimResults.setInteger(0,ClaimBean.DBEVIDENCE,0);
		mockClaimResults.setString(0,ClaimBean.DBGENERATEDID,"");
		mockClaimResults.setString(0,ClaimBean.DBPUBDOI, TESTDOI);
		mockClaimResults.setString(0,ClaimBean.DBBEHAVIORSOURCEID, TESTBEHAVIOR);
	}
	
	private static MockResults mockTaxonResults = new MockResults(1);
	static{
		mockTaxonResults.setInteger(0,TaxonBean.DBID, 1);
	}
	
	private static MockResults mockTaxonResults2 = new MockResults(1);
	static{
		mockTaxonResults2.setInteger(0,TaxonBean.DBID, 12);
	}
	
	private static MockResults mockIndividualResults94 = new MockResults(1);
	static{
		mockIndividualResults94.setInteger(0,IndividualBean.DBID, 94);
		mockIndividualResults94.setString(0,IndividualBean.DBLABEL, "female");
		mockIndividualResults94.setString(0,IndividualBean.DBSOURCEID, null);
		mockIndividualResults94.setString(0,IndividualBean.DBGENERATEDID, null);
		mockIndividualResults94.setInteger(0,IndividualBean.DBTERM, 111938);
	}

	private static MockResults mockIndividualResults95 = new MockResults(1);
	static{
		mockIndividualResults95.setInteger(0,IndividualBean.DBID, 95);
		mockIndividualResults95.setString(0,IndividualBean.DBLABEL, "whole organism of female");
		mockIndividualResults95.setString(0,IndividualBean.DBSOURCEID, null);
		mockIndividualResults95.setString(0,IndividualBean.DBGENERATEDID, null);
		mockIndividualResults95.setInteger(0,IndividualBean.DBTERM, 10473);
	}


	private static MockResults mockPElementResults = new MockResults(1);
	static{
		mockPElementResults.setInteger(0,PElementBean.DBID, 1);
		mockPElementResults.setInteger(0,PElementBean.DBTYPE, 1);
		mockPElementResults.setInteger(0,PElementBean.DBPARTICIPANT, 1);		
	}

	private static MockResults mockPElementResults2 = new MockResults(1);
	static{
		mockPElementResults2.setInteger(0,PElementBean.DBID, 2);
		mockPElementResults2.setInteger(0,PElementBean.DBTYPE, 1);
		mockPElementResults2.setInteger(0,PElementBean.DBPARTICIPANT, 1);
	}
	
	private static MockResults mockPElementResults61 = new MockResults(1);
	static{
		mockPElementResults61.setInteger(0,PElementBean.DBID, 61);
		mockPElementResults61.setInteger(0,PElementBean.DBTYPE, 3);
		mockPElementResults61.setInteger(0,PElementBean.DBPARTICIPANT, 29);
		
	}
	
	
	private static MockResults mockPEParentResults = new MockResults(1);
	static {
		mockPEParentResults.setInteger(0,PElementBean.DBPARENTID, 1);
		mockPEParentResults.setInteger(0,PElementBean.DBPARENTPROPERTY, 1);
	}
	
	
	private static MockResults mockPEChildResults = new MockResults(1);
	static {
		mockPEChildResults.setInteger(0,PElementBean.DBCHILDID, 1);
		mockPEChildResults.setInteger(0,PElementBean.DBCHILDPROPERTY, 1);		
	}
	
	
	final static String ACTIVELYPARTICIPATESINURL = "http://purl.obolibrary.org/obo/RO_0002217";
	final static String ACTIVELYPARTICIPATESINLABEL = "actively participates in";
	

	private static MockResults mockPropertyResults1 = new MockResults(1);
	static {
		mockPropertyResults1.setInteger(0,PropertyBean.DBID, 306);
		mockPropertyResults1.setString(0,PropertyBean.DBSOURCEID, ACTIVELYPARTICIPATESINURL);
		mockPropertyResults1.setInteger(0,PropertyBean.DBAUTHORITY,0);
		mockPropertyResults1.setString(0,PropertyBean.DBLABEL,ACTIVELYPARTICIPATESINLABEL);
		mockPropertyResults1.setString(0,PropertyBean.DBGENERATEDID,null);
		mockPropertyResults1.setString(0,PropertyBean.DBCOMMENT,null);		
	}
	
	private static MockResults termFillResultsNull = new MockResults(1);
	static {
		termFillResultsNull.setInteger(0,PElementBean.DBTERM, (Integer) null);
	}
	
	private static MockResults termFillResults4838 = new MockResults(1);
	static {
		termFillResults4838.setInteger(0,PElementBean.DBTERM, 4838);
	}

	
	private static MockResults termFillResults10473 = new MockResults(1);
	static {
		termFillResults10473.setInteger(0,PElementBean.DBTERM, 10473);
	}

	private static MockResults termFillResults11398 = new MockResults(1);
	static {
		termFillResults11398.setInteger(0,PElementBean.DBTERM, 11398);
	}

	private static MockResults individualFillResultsNull = new MockResults(1);
	static {
		individualFillResultsNull.setInteger(0,PElementBean.DBINDIVIDUAL, (Integer) null);
	}

	private static MockResults individualFillResults94 = new MockResults(1);
	static {
		individualFillResults94.setInteger(0,PElementBean.DBINDIVIDUAL, 94);
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
	public Set<PublicationBean> getPublications() throws SQLException {
		Set<PublicationBean> results = new HashSet<PublicationBean>();
		PublicationBean p1 = new PublicationBean();
		p1.fill(mockPublicationResults);
		results.add(p1);
		PublicationBean p2 = new PublicationBean();
		p2.fill(mockPublicationResults2);
		results.add(p2);
		PublicationBean p3 = new PublicationBean();
		p3.fill(mockPublicationResults2);
		results.add(p3);
		return results;
	}

	private Map <Integer,TermBean> termBeanCache = new HashMap <Integer,TermBean>();
	
	@Override
	public TermBean getTerm(int i) throws SQLException {
		if (termBeanCache.containsKey(i)){
			return termBeanCache.get(i);
		}
		else{
		TermBean tb = new TermBean();
			switch (i){
			case 4838:
				tb.fill(mockTermResults4838);
				break;
			case 2:
				tb.fill(mockTermResults2);
				break;
			case 10473:
				tb.fill(mockTermResults10473);
				break;
			case 11398:
				tb.fill(mockTermResults11398);
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
	}

	@Override
	public Set<TermBean> getTerms() throws SQLException {
		Set<TermBean> results = new HashSet<TermBean>();
		TermBean t1 = new TermBean();
		t1.fill(mockTermResults4838);
		results.add(t1);
		TermBean t2 = new TermBean();
		t2.fill(mockTermResults2);
		results.add(t2);
		TermBean t11398 = new TermBean();
		t11398.fill(mockTermResults11398);
		results.add(t11398);
		return results;
		
	}

	@Override
	public void updateTerm(TermBean termBean) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Set<ParticipantBean> getParticipants(ClaimBean a) throws SQLException {
		Set<ParticipantBean> results = new HashSet<ParticipantBean>();		
		final MockResults mock1 = mockParticipantResults1;
		ParticipantBean p1 = new ParticipantBean();
		p1.fill(mock1);
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
	public void updateNamedEntity(UpdateableBean b) throws SQLException{
		b.updateDB(this);
	}
	
	@Override
	public void updateClaim(ClaimBean cl){
		switch (cl.getId()){
		case 1:
			mockClaimResults.setString(0,ClaimBean.DBGENERATEDID, cl.getGeneratedId());
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
		else{
			IndividualBean ib = new IndividualBean();
			switch (id){
			case 94:
				ib.fill(mockIndividualResults94);
				break;

			case 95: 
				ib.fill(mockIndividualResults95);
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
	public int scanPrivateIDs(){
		return MAXMOCKIDVALUE;
	}

	@Override
	public Set<ParticipantBean> getParticipantsWithProperty(ClaimBean a,Object p) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Need to implement getParticipantWithProperty");
	}

	@Override
	public Set<PElementBean> getPElements(ParticipantBean p) throws Exception {
		final Set<PElementBean> result = new HashSet<PElementBean>();
		switch (p.getId()){
		case 1:
			PElementBean e1 = new PElementBean();
			PElementBean e2 = new PElementBean();
			e1.fill(mockPElementResults);
			e2.fill(mockPElementResults2);
			fillPElementParents(e1);
			fillPElementChildren(e1);
			fillPElementChildren(e2);
			fillPElementChildren(e2);
			result.add(e1);
			result.add(e2);
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
			fillPElementParents(result);
			fillPElementChildren(result);
			break;
		}
		case 2:{
			result.fill(mockPElementResults2);
			fillPElementParents(result);
			fillPElementChildren(result);
			break;
		}
		case 61:{
			result.fill(mockPElementResults61);
			fillPElementParents(result);
			fillPElementChildren(result);
			break;
		}
		default :{
			throw new RuntimeException("Bad id " + id + " to mock getPElement");
		}
		}
		return result;
	}

	public void fillPElementParents(PElementBean result) throws Exception{
		switch (result.getId()){
		case 1:
			result.fillParents(mockPEParentResults, this);
			break;
		default:
			result.fillParents(mockPEParentResults, this);
		}
	}

	public void fillPElementChildren(PElementBean result) throws Exception{
		switch (result.getId()){
		case 1:
			result.fillChildren(mockPEChildResults, this);
			break;
		default:
			result.fillChildren(mockPEChildResults, this);
		}
		
	}

	@Override
	public PropertyBean getProperty(int id) throws Exception {
		final PropertyBean result = new PropertyBean();
		switch (id){
		case 306:
			result.fill(mockPropertyResults1);
			break;
		default:
			return null;
		}
		return result;
	}

	@Override
	public void fillPElementTerm(PElementBean pb) throws Exception {
		switch (pb.getId()){
		case 1:{
			pb.fillTerm(termFillResults4838, this);
			break;
		}
		case 2: {
			pb.fillTerm(termFillResults10473, this);
			break;
		}
		case 61: {
			pb.fillTerm(termFillResultsNull, this);
			break;
		}
		default:
			throw new RuntimeException("Bad pelementbean id: " + pb.getId());
		}
		
	}

	@Override
	public void fillPElementIndividual(PElementBean pb) throws Exception {
		switch (pb.getId()){
		case 1:
		case 2:
			pb.fillIndividual(individualFillResultsNull, this);
			break;
		case 61:{
			pb.fillIndividual(individualFillResults94, this);
			break;
		}
		default:
			throw new RuntimeException("Bad pelementbean id: " + pb.getId());
		}
	}
		// TODO Auto-generated method stub
		





}
