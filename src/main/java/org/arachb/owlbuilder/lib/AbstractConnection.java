package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface AbstractConnection {

	Map<String, String> loadImportSourceMap() throws Exception;

	void close() throws Exception;

	Set<Assertion> getAssertions() throws SQLException;

	void updateAssertion(Assertion a) throws SQLException;

	Publication getPublication(int get_publication) throws SQLException;

	Set<Publication> getPublications() throws SQLException;

	Set<Taxon> getTaxa() throws SQLException;

	Participant getPrimaryParticipant(Assertion a);

	Set<Participant> getParticipants(Assertion a);

	Taxon getTaxon(int get_taxon) throws SQLException;

	Map<String, String> loadOntologyNamesForLoading() throws Exception;

	void updatePublication(Publication pub) throws SQLException;

	void updateTaxon(Taxon t);

}
