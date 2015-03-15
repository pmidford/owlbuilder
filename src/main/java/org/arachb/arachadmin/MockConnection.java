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
		mockPublicationResults.setString(PublicationBean.DBGENERATEDID, TESTGENID);
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

	private final static String doi3 = 
			"http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2";
	private static MockResults mockPublicationResults3 = new MockResults();
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

	private static MockResults mockTermResults4838 = new MockResults();
	static {
		mockTermResults4838.setInteger(TermBean.DBID,4838);
		mockTermResults4838.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setInteger(TermBean.DBDOMAIN,3);
		mockTermResults4838.setInteger(TermBean.DBAUTHORITY,1);
		mockTermResults4838.setString(TermBean.DBLABEL,"Tetragnatha straminea");
		mockTermResults4838.setString(TermBean.DBGENERATEDID,"http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults4838.setString(TermBean.DBCOMMENT,"");
	}

	private static MockResults mockTermResults11398 = new MockResults();
	static {
		mockTermResults11398.setInteger(TermBean.DBID,11398);
		mockTermResults11398.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/NBO_0000358");
		mockTermResults11398.setInteger(TermBean.DBDOMAIN,1);
		mockTermResults11398.setInteger(TermBean.DBAUTHORITY,4);
		mockTermResults11398.setString(TermBean.DBLABEL,"resting posture");
		mockTermResults11398.setString(TermBean.DBGENERATEDID,null);
		mockTermResults11398.setString(TermBean.DBCOMMENT,"");
	}
	

	private static MockResults mockTermResults10473 = new MockResults();
	static {
		mockTermResults10473.setInteger(TermBean.DBID, 10473);
		mockTermResults10473.setString(TermBean.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults10473.setInteger(TermBean.DBDOMAIN,2);
		mockTermResults10473.setInteger(TermBean.DBAUTHORITY,4);
		mockTermResults10473.setString(TermBean.DBLABEL,"whole organism");
		mockTermResults10473.setString(TermBean.DBGENERATEDID,null);
		mockTermResults10473.setString(TermBean.DBCOMMENT,"");
		
	}


	
	private static MockResults mockParticipantResults1 = new MockResults();
	static {
		mockParticipantResults1.setInteger(ParticipantBean.DBID,1);
		mockParticipantResults1.setString(ParticipantBean.DBQUANTIFICATION,"some");
		mockParticipantResults1.setString(ParticipantBean.DBLABEL,"");
		mockParticipantResults1.setString(ParticipantBean.DBGENERATEDID,null);
		mockParticipantResults1.setInteger(ParticipantBean.DBPROPERTY, 306);  //used
		mockParticipantResults1.setString(ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockParticipantResults1.setString(ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockParticipantResults1.setString(ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockParticipantResults1.setInteger(ParticipantBean.DBHEADELEMENT,1);
	}

	private static MockResults mockParticipantResults2 = new MockResults();
	static {
		mockParticipantResults2.setInteger(ParticipantBean.DBID,2);
		mockParticipantResults2.setString(ParticipantBean.DBQUANTIFICATION,"some");
		mockParticipantResults2.setString(ParticipantBean.DBLABEL,"");
		mockParticipantResults2.setString(ParticipantBean.DBGENERATEDID,null);
		mockParticipantResults2.setString(ParticipantBean.DBPUBLICATIONTAXON,TESTPUBTAXON);
		mockParticipantResults2.setString(ParticipantBean.DBPUBLICATIONANATOMY,"");
		mockParticipantResults2.setString(ParticipantBean.DBPUBLICATIONSUBSTRATE,"");
		mockParticipantResults2.setInteger(ParticipantBean.DBHEADELEMENT,51);
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
	
	private static MockResults mockIndividualResults94 = new MockResults();
	static{
		mockIndividualResults94.setInteger(IndividualBean.DBID, 94);
		mockIndividualResults94.setString(IndividualBean.DBLABEL, "female");
		mockIndividualResults94.setString(IndividualBean.DBSOURCEID, null);
		mockIndividualResults94.setString(IndividualBean.DBGENERATEDID, null);
		mockIndividualResults94.setInteger(IndividualBean.DBTERM, 111938);
	}

	private static MockResults mockIndividualResults95 = new MockResults();
	static{
		mockIndividualResults95.setInteger(IndividualBean.DBID, 95);
		mockIndividualResults95.setString(IndividualBean.DBLABEL, "whole organism of female");
		mockIndividualResults95.setString(IndividualBean.DBSOURCEID, null);
		mockIndividualResults95.setString(IndividualBean.DBGENERATEDID, null);
		mockIndividualResults95.setInteger(IndividualBean.DBTERM, 10473);
	}


	private static MockResults mockPElementResults = new MockResults();
	static{
		mockPElementResults.setInteger(PElementBean.DBID, 1);
		mockPElementResults.setInteger(PElementBean.DBTYPE, 1);
		mockPElementResults.setInteger(PElementBean.DBPARTICIPANT, 1);		
	}

	private static MockResults mockPElementResults2 = new MockResults();
	static{
		mockPElementResults2.setInteger(PElementBean.DBID, 2);
		mockPElementResults2.setInteger(PElementBean.DBTYPE, 1);
		mockPElementResults2.setInteger(PElementBean.DBPARTICIPANT, 1);
	}
	
	private static MockResults mockPElementResults61 = new MockResults();
	static{
		mockPElementResults61.setInteger(PElementBean.DBID, 61);
		mockPElementResults61.setInteger(PElementBean.DBTYPE, 3);
		mockPElementResults61.setInteger(PElementBean.DBPARTICIPANT, 29);
		
	}
	
	
	private static MockResults mockPEParentResults = new MockResults();
	static {
		mockPEParentResults.setInteger(PElementBean.DBPARENTID, 1);
		mockPEParentResults.setInteger(PElementBean.DBPARENTPROPERTY, 1);
	}
	
	
	private static MockResults mockPEChildResults = new MockResults();
	static {
		mockPEChildResults.setInteger(PElementBean.DBCHILDID, 1);
		mockPEChildResults.setInteger(PElementBean.DBCHILDPROPERTY, 1);		
	}
	
	
	final static String ACTIVELYPARTICIPATESINURL = "http://purl.obolibrary.org/obo/RO_0002217";
	final static String ACTIVELYPARTICIPATESINLABEL = "actively participates in";
	

	private static MockResults mockPropertyResults1 = new MockResults();
	static {
		mockPropertyResults1.setInteger(PropertyBean.DBID, 306);
		mockPropertyResults1.setString(PropertyBean.DBSOURCEID, ACTIVELYPARTICIPATESINURL);
		mockPropertyResults1.setInteger(PropertyBean.DBAUTHORITY,0);
		mockPropertyResults1.setString(PropertyBean.DBLABEL,ACTIVELYPARTICIPATESINLABEL);
		mockPropertyResults1.setString(PropertyBean.DBGENERATEDID,null);
		mockPropertyResults1.setString(PropertyBean.DBCOMMENT,null);		
	}
	
	private static MockResults termFillResultsNull = new MockResults();
	static {
		termFillResultsNull.setInteger(PElementBean.DBTERM, (Integer) null);
	}
	
	private static MockResults termFillResults4838 = new MockResults();
	static {
		termFillResults4838.setInteger(PElementBean.DBTERM, 4838);
	}

	
	private static MockResults termFillResults10473 = new MockResults();
	static {
		termFillResults10473.setInteger(PElementBean.DBTERM, 10473);
	}

	private static MockResults termFillResults11398 = new MockResults();
	static {
		termFillResults11398.setInteger(PElementBean.DBTERM, 11398);
	}

	private static MockResults individualFillResultsNull = new MockResults();
	static {
		individualFillResultsNull.setInteger(PElementBean.DBINDIVIDUAL, (Integer) null);
	}

	private static MockResults individualFillResults94 = new MockResults();
	static {
		individualFillResults94.setInteger(PElementBean.DBINDIVIDUAL, 94);
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
			mockClaimResults.setString(ClaimBean.DBGENERATEDID, cl.getGeneratedId());
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
			mockPublicationResults2.setString(PublicationBean.DBGENERATEDID, p.getGeneratedId());
			break;
		case 3:
			mockPublicationResults3.setString(PublicationBean.DBGENERATEDID, p.getGeneratedId());
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
			mockIndividualResults94.setString(IndividualBean.DBGENERATEDID, ib.getGeneratedId());
			break;
		case 95:
			mockIndividualResults95.setString(IndividualBean.DBGENERATEDID, ib.getGeneratedId());
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
