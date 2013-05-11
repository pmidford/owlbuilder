package org.arachb.owlbuilder.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

	public void close() throws Exception {
		c.close();
	}
	
}
