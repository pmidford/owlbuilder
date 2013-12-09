package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
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
		mockPublicationResults.setString("doi","");
		mockPublicationResults.setString("generated_id","");
        mockPublicationResults.setString("curation_status","");
        mockPublicationResults.setString("curation_update","");

	}
	
	private static MockResults mockTermResults = new MockResults();
	static {
		mockTermResults.setInteger("id",1);
		mockTermResults.setString("source_id","");
		mockTermResults.setInteger("domain",1);
		mockTermResults.setInteger("authority",1);
		mockTermResults.setString("label","");
		mockTermResults.setString("generated_id","");
		mockTermResults.setString("comment","");
	}
	

	public MockConnection() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, String> loadImportSourceMap() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Assertion> getAssertions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAssertion(Assertion a) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Publication getPublication(int get_publication) throws SQLException {
		Publication result = new Publication();
        result.fill(mockPublicationResults);
		return result;
	}

	@Override
	public Set<Publication> getPublications() throws SQLException {
		Set<Publication> results = new HashSet<Publication>();
		Publication p1 = new Publication();
		p1.fill(mockPublicationResults);
		results.add(p1);
		return results;
	}

	@Override
	public void updatePublication(Publication pub) throws SQLException {
		mockPublicationResults.setString("generated_id", pub.get_generated_id());
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public Set<Taxon> getTaxa() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Participant getPrimaryParticipant(Assertion a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Participant> getParticipants(Assertion a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Taxon getTaxon(int get_taxon) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> loadOntologyNamesForLoading() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void updateTaxon(Taxon t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Term getTerm(int i) throws SQLException {
		Term result = new Term();
        result.fill(mockTermResults);
		return result;
	}

	@Override
	public void updateTerm(Term testTerm) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Term> getTerms() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Assertion getAssertion(int i) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
