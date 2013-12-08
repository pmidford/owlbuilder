package org.arachb.owlbuilder.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;



public class DBConnection implements AbstractConnection{

	static final String DEFAULTPROPERTIESFILE = "Connection.properties";
	
	static final String TESTPROPERTIESFILE = "TestConnection.properties";
	
	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;
	
	private final HashMap<Integer,String>id2domain = new HashMap<Integer,String>();
	
	private final HashMap<String,Integer>domain2id = new HashMap<String,Integer>();

	private static Logger log = Logger.getLogger(DBConnection.class);

	
	public static boolean testConnection(){
		Connection c = null;
		String connectionSpec = DEFAULTPROPERTIESFILE;
		try {
			final Properties properties = new Properties();
			properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
			Class.forName("com.mysql.jdbc.Driver");
			final String host = properties.getProperty("host");
			final String db = properties.getProperty("dbname");
			final String user = properties.getProperty("user");
			final String password = properties.getProperty("password");
			c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
		}
		catch (Exception e){
			return false;
		}
		finally{
			try {
				if (c != null){
					c.close();
				}
			}
			catch (SQLException e){
				log.error("Error while closing test connection" + e);
				return false;
			}
		}
		return true;
	}
	
	private DBConnection(String connectionSpec) throws Exception{
		final Properties properties = new Properties();
		properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
		Class.forName("com.mysql.jdbc.Driver");
		final String host = properties.getProperty("host");
		final String db = properties.getProperty("dbname");
		final String user = properties.getProperty("user");
		final String password = properties.getProperty("password");
		c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
        initDomains(c);
	}
	
	
	private final String DOMAINSQUERY = "SELECT id,name FROM domain";
	
	private void initDomains(Connection c) throws SQLException{
		Statement domainStatement = c.createStatement();
		ResultSet domainSet = domainStatement.executeQuery(DOMAINSQUERY);
		while (domainSet.next()){
			int id = domainSet.getInt("id");
			String label = domainSet.getString("name");
			id2domain.put(id, label);
			domain2id.put(label, id);
		}
		domainStatement.close();
	}
	
	public static DBConnection getDBConnection() throws Exception{
		return new DBConnection(DEFAULTPROPERTIESFILE);
	}

	
	public static DBConnection getTestConnection() throws Exception{
		return new DBConnection(TESTPROPERTIESFILE);
	}
	
	public static AbstractConnection getMockConnection() {
		return new MockConnection();
	}

	
	public Connection getConnection(){
		return c;
	}
		
	public List<String> privateIDStrings() throws SQLException{
		List<String> result = new ArrayList<String>();
		return result;
	}
	
	public Publication getPublication(int id) throws SQLException{
		PreparedStatement publicationStatement = c.prepareStatement(Publication.getRowQuery());
		publicationStatement.setInt(1, id);
		ResultSet publicationSet = publicationStatement.executeQuery();
		if (publicationSet.next()){
			Publication result = new Publication();
			result.fill(publicationSet);
			return result;
		}
		else {
			return null;
		}
	}
	
	public Set<Publication> getPublications() throws SQLException{
		final Set<Publication> result = new HashSet<Publication>();
		Statement allpubStatement = c.createStatement();
		ResultSet publicationSet = allpubStatement.executeQuery(Publication.getTableQuery());
		while (publicationSet.next()){
			Publication pub = new Publication();
			pub.fill(publicationSet);
			result.add(pub);
		}
		return result;
	}
	
	public void updatePublication(Publication pub) throws SQLException{
		PreparedStatement updateStatement = 
				c.prepareStatement(Publication.getUpdateStatement());
		updateStatement.setString(1, pub.get_generated_id());
		updateStatement.setInt(2,pub.get_id());
		int count = updateStatement.executeUpdate();
		if (count != 1){
			logger.error("publication update failed; row count = " + count);
		}
	}
	
	public Term getTerm(int id) throws SQLException{
		PreparedStatement termStatement = c.prepareStatement(Term.getRowQuery());
		termStatement.setInt(1, id);
		ResultSet termSet = termStatement.executeQuery();
		if (termSet.next()){
			Term result = new Term();
			result.fill(termSet);
			return result;
		}
		else {
			return null;
		}
	}
	
	public Set<Term> getTerms() throws SQLException{
		final Set<Term> result = new HashSet<Term>();
		final Statement allTermStatement = c.createStatement();
		final ResultSet termSet = allTermStatement.executeQuery(Term.getTableQuery());
		while (termSet.next()){
			Term t = new Term();
			t.fill(termSet);
			result.add(t);
		}
		return result;
	}
	
	public void updateTerm(Term a) throws SQLException{
		PreparedStatement updateStatement = 
				c.prepareStatement(Term.getUpdateStatement());
		updateStatement.setString(1, a.get_generated_id());
		updateStatement.setInt(2,a.get_id());
		int count = updateStatement.executeUpdate();
		if (count != 1){
			logger.error("term update failed; row count = " + count);
		}
	}

	public Assertion getAssertion(int id) throws SQLException{
		PreparedStatement assertionStatement = c.prepareStatement(Assertion.getRowQuery());
		assertionStatement.setInt(1, id);
		ResultSet assertionSet = assertionStatement.executeQuery();
		if (assertionSet.next()){
			Assertion result = new Assertion();
			result.fill(assertionSet);
			return result;
		}
		else {
			return null;
		}
	}
	
	public Set<Assertion> getAssertions() throws SQLException {
		final Set<Assertion> result = new HashSet<Assertion>();
		final Statement allAssertionStatement = c.createStatement();
		final ResultSet assertionSet = allAssertionStatement.executeQuery(Assertion.getTableQuery());
		while (assertionSet.next()){
			Assertion a = new Assertion();
			a.fill(assertionSet);
			result.add(a);
		}
		return result;
	}
	
	public void updateAssertion(Assertion a) throws SQLException{
		PreparedStatement updateStatement = 
				c.prepareStatement(Assertion.getUpdateStatement());
		updateStatement.setString(1, a.get_generated_id());
		updateStatement.setInt(2,a.get_id());
		int count = updateStatement.executeUpdate();
		if (count != 1){
			logger.error("assertion update failed; row count = " + count);
		}
	}
	
	public Participant getPrimaryParticipant(Assertion a){
		return null;
	}
	
	public Set<Participant> getParticipants(Assertion a) {
		final Set<Participant> result = new HashSet<Participant>();
		int assertion_id = a.get_id();
		return result;
	}

	public void updateParticipant(Participant p) throws SQLException{
		
	}
	
	public Taxon getTaxon(int id) throws SQLException{
		PreparedStatement taxonStatement = c.prepareStatement(Taxon.getRowQuery());
		taxonStatement.setInt(1, id);
		ResultSet taxonSet = taxonStatement.executeQuery();
		if (taxonSet.next()){
			Taxon result = new Taxon();
			result.fill(taxonSet);
			return result;
		}
		else {
			return null;
		}
	}
	
	public Set<Taxon> getTaxa() throws SQLException {
		final Set<Taxon> result = new HashSet<Taxon>();
		final Statement allTaxonStatement = c.createStatement();
		final ResultSet taxonSet = allTaxonStatement.executeQuery(Taxon.getTableQuery());
		while (taxonSet.next()){
			Taxon t = new Taxon();
			t.fill(taxonSet);
			result.add(t);
		}
		return result;
	}
	
	public void updateTaxon(Taxon tx){
		
	}
	
	private final String ONTOLOGYSOURCEQUERY = "SELECT source_url,domain FROM ontology_source";

	/**
	 * 
	 * @return maps source_urls, which are unique, to domain identifiers
	 * @throws Exception
	 */
	public Map<String,String>loadImportSourceMap() throws Exception{
		final Map<String,String> result = new HashMap<String,String>();
		Statement sourceOntStatement = c.createStatement();
		ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYSOURCEQUERY);
		while (sourceOntSet.next()){
			final int domain_id = sourceOntSet.getInt("domain");
			final String domain = domainName(domain_id);
			final String source = sourceOntSet.getString("source_url");
			result.put(source,domain);
		}
		sourceOntStatement.close();
		return result;
	}

	private final String ONTOLOGYNAMEQUERY = "SELECT source_url,name FROM ontology_source";

	/**
	 * 
	 * @return maps source_urls, which are unique, to domain identifiers
	 * @throws Exception
	 */
	public Map<String,String>loadOntologyNamesForLoading() throws Exception{
		final Map<String,String> result = new HashMap<String,String>();
		Statement sourceOntStatement = c.createStatement();
		ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYNAMEQUERY);
		while (sourceOntSet.next()){
			final String source = sourceOntSet.getString("source_url");
			final String name = sourceOntSet.getString("name");
			result.put(source,name);
		}
		sourceOntStatement.close();
		return result;
	}

	
	public String domainName(int domain_id){
		return id2domain.get(domain_id);
	}
	
	public int domainId(String name){
		return domain2id.get(name);
	}

	public void close() throws Exception {
		c.close();
	}


	
}
