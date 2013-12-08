package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class MockConnection implements AbstractConnection {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Publication> getPublications() throws SQLException {
		// TODO Auto-generated method stub
		return null;
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
	public Map<String, String> loadOntologyNamesForLoading() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePublication(Publication pub) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaxon(Taxon t) {
		// TODO Auto-generated method stub
		
	}

}
