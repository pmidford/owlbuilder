package org.arachb.owlbuilder;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for owlbuilder tool
 */
public class TestOwlbuilder 
{

    private static Logger log = Logger.getLogger(TestOwlbuilder.class);

    /**
     * currently just initializes the logging system for tests 
     */
    @Before
    public void setup(){
        PropertyConfigurator.configure(TestOwlbuilder.class.getClassLoader().getResource("log4j.properties"));
    }


    @Test
    public void testOwlbuilderConstructor() throws Exception
    {
    	Owlbuilder b = new Owlbuilder();
    	assertNotNull(b);
    	assertNotNull(b.getOntologyManager());
    	assertNotNull(b.getDataFactory());
    	assertNotNull(b.getConnection());
    	b.shutdown();
    }
    
    
}
