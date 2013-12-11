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

	Participant getPrimaryParticipant(Assertion a) throws SQLException;

	Set<Participant> getParticipants(Assertion a) throws SQLException;

	void updateParticipant(Participant p) throws SQLException;

	Map<String, String> loadOntologyNamesForLoading() throws SQLException;

	void updatePublication(Publication pub) throws SQLException;

	Term getTerm(int i) throws SQLException;

	void updateTerm(Term testTerm) throws SQLException;

	Set<Term> getTerms() throws SQLException;

	Assertion getAssertion(int i) throws SQLException;


}
