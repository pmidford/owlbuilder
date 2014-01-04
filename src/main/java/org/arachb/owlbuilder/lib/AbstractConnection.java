package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public interface AbstractConnection {

	Map<String, String> loadImportSourceMap() throws Exception;

	void close() throws Exception;

	Set<Assertion> getAssertions() throws Exception;

	Publication getPublication(int get_publication) throws SQLException;

	Set<Publication> getPublications() throws SQLException;

	Participant getPrimaryParticipant(Assertion a) throws Exception;

	Set<Participant> getParticipants(Assertion a) throws SQLException;

	Map<String, String> loadOntologyNamesForLoading() throws SQLException;

	Term getTerm(int i) throws SQLException;

	Set<Term> getTerms() throws SQLException;

	Assertion getAssertion(int i) throws Exception;

	void updateNamedEntity(AbstractNamedEntity e) throws SQLException;


}
