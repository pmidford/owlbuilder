package org.arachb.owlbuilder.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;



public class DBConnection {

	static final String DEFAULTPROPERTIESFILE = "Connection.properties";
	
	static final String TESTPROPERTIESFILE = "TestConnection.properties";
	
	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;
	
	
	private DBConnection(String connectionSpec) throws Exception{
		final Properties properties = new Properties();
		properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
		Class.forName("com.mysql.jdbc.Driver");
		final String host = properties.getProperty("host");
		final String db = properties.getProperty("dbname");
		final String user = properties.getProperty("user");
		final String password = properties.getProperty("password");
		c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
	}
	
	public static DBConnection getDBConnection() throws Exception{
		return new DBConnection(DEFAULTPROPERTIESFILE);
	}

	
	public static DBConnection getTestConnection() throws Exception{
		return new DBConnection(TESTPROPERTIESFILE);
	}
	
	public Connection getConnection(){
		return c;
	}
	
	public int lastPublication(){
		return 0;
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
		final Statement allUsageStatement = c.createStatement();
		final ResultSet assertionSet = allUsageStatement.executeQuery(Assertion.getTableQuery());
		while (assertionSet.next()){
			Assertion a = new Assertion();
			a.fill(assertionSet);
			result.add(a);
		}
		return result;
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

	public void close() throws Exception {
		c.close();
	}
	
}
