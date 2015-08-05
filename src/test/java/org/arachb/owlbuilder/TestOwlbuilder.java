package org.arachb.owlbuilder;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.DBConnection;
import org.arachb.owlbuilder.lib.TestPropertyTerm;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for owlbuilder tool
 */
public class TestOwlbuilder {

	AbstractConnection testConnection;
    static {
    	PropertyConfigurator.configure(TestOwlbuilder.class.getClassLoader().getResource("log4j.properties"));
    }
	private static Logger log = Logger.getLogger(TestOwlbuilder.class);

	
    @Before
    public void setup() throws Exception{
		if (DBConnection.probeTestConnection()){
			log.info("Testing with live connection");
			testConnection = DBConnection.getTestConnection();
		}
		else{
			log.info("Testing with mock connection");
			testConnection = DBConnection.getMockConnection();
		}
    }


    @Test
    public void testOwlbuilderConstructor() throws Exception
    {
    	Owlbuilder b = new Owlbuilder(testConnection);
    	assertNotNull(b);
    	assertNotNull(b.getOntologyManager());
    	assertNotNull(b.getDataFactory());
    	assertNotNull(b.getConnection());
    	b.shutdown();
    }
    
    
}
