package org.arachb.owlbuilder.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;



public class DBConnection {

	static final String DEFAULTPROPERTIESFILE = "Connection.properties";
	
	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;
	
	public DBConnection() throws Exception{
		
		String connectionSpec = DEFAULTPROPERTIESFILE;
		final Properties properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream(connectionSpec));
		Class.forName("com.mysql.jdbc.Driver");
		final String host = properties.getProperty("host");
		final String db = properties.getProperty("dbname");
		final String user = properties.getProperty("user");
		final String password = properties.getProperty("password");
		c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
	}
	
	public Connection getConnection(){
		return c;
	}
	
	public int lastPublication(){
		return 0;
	}
	
	
	static final private String PUBLICATIONQUERY = "SELECT id, publication_type,dispensation," +
	"downloaded,reviewed,title,alternate_title,author_list,editor_list,source_publication," +
	"volume,issue,serial_identifier,page_range,publication_date,publication_year,doi " +
	"FROM publication where publication.id = ?";
	
	public Publication getPublication(int id) throws SQLException{
		PreparedStatement publicationStatement = c.prepareStatement(PUBLICATIONQUERY);
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
	
	static final private String ALLPUBLICATIONSQUERY = "SELECT id, publication_type,dispensation," +
			"downloaded,reviewed,title,alternate_title,author_list,editor_list,source_publication," +
			"volume,issue,serial_identifier,page_range,publication_date,publication_year,doi " +
			"FROM publication";
	
	public Set<Publication> getPublications() throws SQLException{
		final Set<Publication> result = new HashSet<Publication>();
		Statement allpubStatement = c.createStatement();
		ResultSet publicationSet = allpubStatement.executeQuery(ALLPUBLICATIONSQUERY);
		while (publicationSet.next()){
			Publication pub = new Publication();
			pub.fill(publicationSet);
			result.add(pub);
		}
		return result;
	}
	
	static final private String USAGEQUERY = "SELECT id,behavior_term,publication_taxon," +
	"direct_source,evidence,secondary_source,resolved_taxon,anatomy,participant_list," + 
    "obo_term_name, obo_term_id, nbo_term_name, nbo_term_id, abo_term, description " +
	"FROM term_usage where term_usage.id = ?";

	public Usage getTerm_usage(int id) throws SQLException{
		PreparedStatement usageStatement = c.prepareStatement(USAGEQUERY);
		usageStatement.setInt(1, id);
		ResultSet usageSet = usageStatement.executeQuery();
		if (usageSet.next()){
			Usage result = new Usage();
			return result;
		}
		else {
			return null;
		}
	}
	
	
	public void close() throws Exception {
		c.close();
	}
	
}
