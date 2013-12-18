package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockConnection implements AbstractConnection {
	
	private static MockResults mockPublicationResults = new MockResults();
	static {
		mockPublicationResults.setInteger("id", 1);
		mockPublicationResults.setString("publication_type","Journal");
		mockPublicationResults.setString("dispensation","");
		mockPublicationResults.setString("downloaded","");
		mockPublicationResults.setString("reviewed","");
		mockPublicationResults.setString("title","");
		mockPublicationResults.setString("alternate_title","");
		mockPublicationResults.setString("author_list","");
		mockPublicationResults.setString("editor_list","");
		mockPublicationResults.setString("source_publication","");
		mockPublicationResults.setInteger("volume",1);
		mockPublicationResults.setString("issue","");
		mockPublicationResults.setString("serial_identifier","");
		mockPublicationResults.setString("page_range","");
		mockPublicationResults.setString("publication_date","");
		mockPublicationResults.setString("publication_year","");
		mockPublicationResults.setString("doi",
				                         "http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2");
		mockPublicationResults.setString("generated_id",null);
        mockPublicationResults.setString("curation_status","");
        mockPublicationResults.setString("curation_update","");
	}
	
	private static MockResults mockPublicationResults2 = new MockResults();
	static {
		mockPublicationResults2.setInteger("id", 2);
		mockPublicationResults2.setString("publication_type","Journal");
		mockPublicationResults2.setString("dispensation","");
		mockPublicationResults2.setString("downloaded","");
		mockPublicationResults2.setString("reviewed","");
		mockPublicationResults2.setString("title","");
		mockPublicationResults2.setString("alternate_title","");
		mockPublicationResults2.setString("author_list","");
		mockPublicationResults2.setString("editor_list","");
		mockPublicationResults2.setString("source_publication","");
		mockPublicationResults2.setInteger("volume",1);
		mockPublicationResults2.setString("issue","");
		mockPublicationResults2.setString("serial_identifier","");
		mockPublicationResults2.setString("page_range","");
		mockPublicationResults2.setString("publication_date","");
		mockPublicationResults2.setString("publication_year","");
		mockPublicationResults2.setString("doi", null);
		mockPublicationResults2.setString("generated_id","http://arachb.org/arachb/TEST_0000001");
        mockPublicationResults2.setString("curation_status","");
        mockPublicationResults2.setString("curation_update","");
	}

	private static MockResults mockTermResults = new MockResults();
	static {
		mockTermResults.setInteger("id",1);
		mockTermResults.setString("source_id","http://purl.obolibrary.org/obo/NCBITaxon_336608");
		mockTermResults.setInteger("domain",3);
		mockTermResults.setInteger("authority",1);
		mockTermResults.setString("label","Tetragnatha straminea");
		mockTermResults.setString("generated_id","");
		mockTermResults.setString("comment","");
	}

	private static MockResults mockTermResults2 = new MockResults();
	static {
		mockTermResults2.setInteger("id",2);
		mockTermResults2.setString("source_id","http://purl.obolibrary.org/obo/SPD_0000001");
		mockTermResults2.setInteger("domain",2);
		mockTermResults2.setInteger("authority",1);
		mockTermResults2.setString("label","whole organism");
		mockTermResults2.setString("generated_id","");
		mockTermResults2.setString("comment","");
	}

	
	private static MockResults mockPrimaryParticipantResults = new MockResults();
	static {
		mockPrimaryParticipantResults.setInteger("id",1);
		mockPrimaryParticipantResults.setInteger("taxon",1);
		mockPrimaryParticipantResults.setInteger("substrate",3);
		mockPrimaryParticipantResults.setInteger("anatomy",2);
		mockPrimaryParticipantResults.setString("quantification","some");
		mockPrimaryParticipantResults.setString("generated_id","");
		mockPrimaryParticipantResults.setString("label","");
		mockPrimaryParticipantResults.setString("publication_taxon","");
		mockPrimaryParticipantResults.setString("publication_anatomy","");
		mockPrimaryParticipantResults.setString("publication_substrate","");
	}

	private static MockResults mockSecondaryParticipantResults = new MockResults();
	static {
		mockSecondaryParticipantResults.setInteger("id",2);
		mockSecondaryParticipantResults.setInteger("taxon",4);
		mockSecondaryParticipantResults.setInteger("substrate",6);
		mockSecondaryParticipantResults.setInteger("anatomy",8);
		mockSecondaryParticipantResults.setString("quantification","some");
		mockSecondaryParticipantResults.setString("generated_id","");
		mockSecondaryParticipantResults.setString("label","");
		mockSecondaryParticipantResults.setString("publication_taxon","");
		mockSecondaryParticipantResults.setString("publication_anatomy","");
		mockSecondaryParticipantResults.setString("publication_substrate","");
	}
	
	private static MockResults mockAssertionResults = new MockResults();
	static{
		mockAssertionResults.setInteger("id", 1);
		mockAssertionResults.setInteger("publication",6);
		mockAssertionResults.setInteger("behavior_term",9);
		mockAssertionResults.setString("publication_behavior","behavior");
		mockAssertionResults.setInteger("taxon", 12);
		mockAssertionResults.setString("publication_taxon","Arachida");
		mockAssertionResults.setString("publication_anatomy","whole body");
		mockAssertionResults.setInteger("evidence",15);
		mockAssertionResults.setString("generated_id","");

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
		Participant result = Participant.makeParticipant(mockPrimaryParticipantResults);
		return result;
	}

	@Override
	public Set<Participant> getParticipants(Assertion a) throws SQLException {
		Set<Participant> results = new HashSet<Participant>();
		Participant p1 = Participant.makeParticipant(mockSecondaryParticipantResults);
		results.add(p1);
		return results;
	}

	public void updateParticipant(IndividualParticipant p) throws SQLException{
		mockPrimaryParticipantResults.setString("generated_id", p.get_generated_id());
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
				mockPublicationResults2.setString("generated_id", e.getIRI_String());
			}
		}
		if (e instanceof Term){
			if (e.get_id() == 1){
				mockTermResults.setString("generated_id", e.getIRI_String());
			}
		}
		else{
			mockAssertionResults.setString("generated_id", e.getIRI_String());		
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
