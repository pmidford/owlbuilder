package org.arachb.arachadmin;

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



public class DBConnection{

	//properties file names
	static final String DEFAULTPROPERTIESFILE = "Connection.properties";

	static final String TESTPROPERTIESFILE = "TestConnection.properties";


	//Query strings


	static final String NARRATIVEUPDATESTATEMENT =
			"UPDATE narrative SET generated_id = ? WHERE id = ?";





	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;

	private IRIManager irimanager;

	private final HashMap<Integer,String>id2domain = new HashMap<Integer,String>();

	private final HashMap<String,Integer>domain2id = new HashMap<String,Integer>();

	private static Logger log = Logger.getLogger(DBConnection.class);

	private static final String DRIVERSPEC = "com.mysql.jdbc.Driver";

	private static final String CONNECTIONSPEC = "jdbc:mysql://%s/%s";

	/**
	 * 
	 */
	public static boolean probeConnection(){
		return probeUsingProperties(DEFAULTPROPERTIESFILE);
	}
	
	public static boolean probeTestConnection(){
		return probeUsingProperties(TESTPROPERTIESFILE);
	}
	
	/**
	 *
	 * @return whether the connection succeeded
	 * resource warning suppressed because the connection remains open until the end
	 */
	//@SuppressWarnings("resource")
	private static boolean probeUsingProperties(String connectionSpec){
		Connection c = null;
		try {
			final Properties properties = new Properties();
			properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
			Class.forName(DRIVERSPEC);
			final String host = properties.getProperty("host");
			final String db = properties.getProperty("dbname");
			final String user = properties.getProperty("user");
			final String password = properties.getProperty("password");
			c = DriverManager.getConnection(String.format(CONNECTIONSPEC,host,db),user,password);
		}
		catch (Exception e){
			log.error("DBConnection setup failed, encountered:",e);
			if (c != null){
				try {
					c.close();
				} catch (SQLException e1) {
					return false;
				}
			}
			return false;
		}
		finally{
			try {
				if (c != null){
					c.close();
				}
			}
			catch (Exception e){
				// log.error("Error while closing test connection" + e);
				return false;
			}
		}
		return true;
	}


	private DBConnection(String connectionSpec) throws Exception{
		log.info("reading properties file: " + connectionSpec);
		final Properties properties = new Properties();
		properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
		Class.forName("com.mysql.jdbc.Driver");
		final String host = properties.getProperty("host");
		final String db = properties.getProperty("dbname");
		final String user = properties.getProperty("user");
		final String password = properties.getProperty("password");
		c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
	}



	/**
	 *
	 * @return connection to default database identified in properties file
	 * @throws Exception
	 */
	public static DBConnection getDBConnection() throws Exception{
		return new DBConnection(DEFAULTPROPERTIESFILE);
	}


	/**
	 *
	 * @return connection to a database of test data
	 * @throws Exception
	 */
	public static DBConnection getTestConnection() throws Exception{
		return new DBConnection(TESTPROPERTIESFILE);
	}


	public Connection getConnection(){
		return c;
	}

	public List<String> privateIDStrings() throws SQLException{
		List<String> result = new ArrayList<String>();
		return result;
	}


	private AbstractResults executeAndAbstract(PreparedStatement p) throws SQLException{
		return new DBResults(p.executeQuery());
	}


















}

