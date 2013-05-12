package org.arachb.owlbuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Unit test for simple App.
 */
public class TestOwlbuilder 
{

    private static Logger log = Logger.getLogger(TestOwlbuilder.class);

    @Before
    public void setup(){
	//configure log4j
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
