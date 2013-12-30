package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockConnection implements AbstractConnection {
	
	private static MockResults mockPublicationResults = new MockResults();
	static {
		mockPublicationResults.setInteger(Publication.DBID, 1);
		mockPublicationResults.setString(Publication.DBPUBLICATIONTYPE,"Journal");
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
		mockPublicationResults.setString(Publication.DBDOI,
				                         "http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2");
		mockPublicationResults.setString(Publication.DBGENERATEDID,null);
        mockPublicationResults.setString(Publication.DBCURATIONSTATUS,"");
        mockPublicationResults.setString(Publication.DBCURATIONUPDATE,"");
	}
	
	private static MockResults mockPublicationResults2 = new MockResults();
	static {
		mockPublicationResults2.setInteger(Publication.DBID, 2);
		mockPublicationResults2.setString(Publication.DBPUBLICATIONTYPE,"Journal");
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
		mockPublicationResults2.setString(Publication.DBGENERATEDID,
				                          "http://arachb.org/arachb/TEST_0000001");
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
		mockTermResults.setString(Term.DBGENERATEDID,"");
		mockTermResults.setString(Term.DBCOMMENT,"");
	}

	private static MockResults mockTermResults2 = new MockResults();
	static {
		mockTermResults2.setInteger(Term.DBID,2);
		mockTermResults2.setString(Term.DBSOURCEID,"http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults2.setInteger(Term.DBDOMAIN,2);
		mockTermResults2.setInteger(Term.DBAUTHORITY,1);
		mockTermResults2.setString(Term.DBLABEL,"whole organism");
		mockTermResults2.setString(Term.DBGENERATEDID,"");
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
		mockPrimaryParticipantResults.setString(Participant.DBPUBLICATIONTAXON,"");
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
		mockSecondaryParticipantResults.setInteger(Participant.DBTAXON,3);
		mockSecondaryParticipantResults.setInteger(Participant.DBSUBSTRATE,0);
		mockSecondaryParticipantResults.setInteger(Participant.DBANATOMY,4);
		mockSecondaryParticipantResults.setString(Participant.DBQUANTIFICATION,"some");
		mockSecondaryParticipantResults.setString(Participant.DBGENERATEDID,"");
		mockSecondaryParticipantResults.setString(Participant.DBLABEL,"");
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONTAXON,"");
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONANATOMY,"");
		mockSecondaryParticipantResults.setString(Participant.DBPUBLICATIONSUBSTRATE,"");
		mockPrimaryParticipantResults.setString(Participant.DBTAXONSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBTAXONGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYSOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBANATOMYGENERATEDID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATESOURCEID,null);
		mockPrimaryParticipantResults.setString(Participant.DBSUBSTRATEGENERATEDID,null);
	}
	
	private static MockResults mockAssertionResults = new MockResults();
	static{
		mockAssertionResults.setInteger(Assertion.DBID, 1);
		mockAssertionResults.setInteger(Assertion.DBPUBLICATION,6);
		mockAssertionResults.setInteger(Assertion.DBBEHAVIORTERM,9);
		mockAssertionResults.setString(Assertion.DBPUBLICATIONBEHAVIOR,"behavior");
		mockAssertionResults.setInteger(Assertion.DBTAXON, 12);
		mockAssertionResults.setString(Assertion.DBPUBLICATIONTAXON,"Arachida");
		mockAssertionResults.setString(Assertion.DBPUBLICATIONANATOMY,"whole body");
		mockAssertionResults.setInteger(Assertion.DBEVIDENCE,15);
		mockAssertionResults.setString(Assertion.DBGENERATEDID,"");

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
        result.fill(mockTermResults);
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
	public Participant getPrimaryParticipant(Assertion a) throws SQLException {
		final MockResults primaryResults = mockPrimaryParticipantResults;
		primaryResults.setString(Participant.DBTAXONSOURCEID, mockTermResults.getString(Term.DBSOURCEID));
		primaryResults.setString(Participant.DBANATOMYSOURCEID, mockTermResults2.getString(Term.DBSOURCEID));
		Participant result = Participant.makeParticipant(primaryResults);
		return result;
	}

	@Override
	public Set<Participant> getParticipants(Assertion a) throws SQLException {
		Set<Participant> results = new HashSet<Participant>();
		final MockResults secondaryResults = mockSecondaryParticipantResults;
		secondaryResults.setString(Participant.DBTAXONSOURCEID, mockTermResults.getString(Term.DBSOURCEID));
		secondaryResults.setString(Participant.DBANATOMYSOURCEID, mockTermResults2.getString(Term.DBSOURCEID));
		Participant p1 = Participant.makeParticipant(secondaryResults);
		results.add(p1);
		return results;
	}

	public void updateParticipant(IndividualParticipant p) throws SQLException{
		mockPrimaryParticipantResults.setString(Participant.DBGENERATEDID, p.get_generated_id());
	}

	@Override
	public Assertion getAssertion(int i) throws SQLException {
		Assertion result = new Assertion();
        result.fill(mockAssertionResults);
		return result;
	}


	@Override
	public Set<Assertion> getAssertions() throws SQLException {
		Set<Assertion> results = new HashSet<Assertion>();
		Assertion a1 = new Assertion();
		a1.fill(mockAssertionResults);
		results.add(a1);
		return results;
	}

	// Sort of ugly (do better somehow?)
	@Override
	public void updateNamedEntity(AbstractNamedEntity e) throws SQLException{
		if (e instanceof Publication){
			if (e.get_id() == 2){
				mockPublicationResults2.setString(Publication.DBGENERATEDID, e.getIRI_String());
			}
		}
		if (e instanceof Term){
			if (e.get_id() == 1){
				mockTermResults.setString(Term.DBGENERATEDID, e.getIRI_String());
			}
		}
		else{
			mockAssertionResults.setString(Assertion.DBGENERATEDID, e.getIRI_String());		
		}
	}

	@Override
	public Map<String, String> loadImportSourceMap() throws Exception {
		return mockImportSourceMap;
	}

	@Override
	public Map<String, String> loadOntologyNamesForLoading() throws SQLException {
		return mockOntologyNamesMap;
	}



}
