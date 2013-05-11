package org.arachb.owlbuilder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Unit test for simple App.
 */
public class TestOwlbuilder 
    extends TestCase
{

    private static Logger log = Logger.getLogger(TestOwlbuilder.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestOwlbuilder( String testName )
    {
        super( testName );
	//configure log4j
        PropertyConfigurator.configure(TestOwlbuilder.class.getClassLoader().getResource("log4j.properties"));
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestOwlbuilder.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testbuilder1()
    {
        log.warn("About to assert true");
        assertTrue( true );
    }
}
