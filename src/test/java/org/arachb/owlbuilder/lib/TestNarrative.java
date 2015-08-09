/**
 * 
 */
package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.NarrativeBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * @author pmidford
 *
 */
public class TestNarrative {

	
	private Owlbuilder builder;
	private static Logger log = Logger.getLogger(TestPropertyTerm.class);
	private static AbstractConnection testConnection;

    private final int TESTNARRATIVE1 = 1;
    private final int TESTNARRATIVE2 = 2;
    
    private NarrativeBean nb1;
    private NarrativeBean nb2;

	/**
	 * @throws java.lang.Exception
	 */	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		if (DBConnection.probeTestConnection()){
			log.info("Testing with live connection");
			testConnection = DBConnection.getTestConnection();
		}
		else{
			log.info("Testing with mock connection");
			testConnection = DBConnection.getMockConnection();
		}		
	}

	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		builder = new Owlbuilder(testConnection);
		builder.setUpForTesting();
		nb1 = testConnection.getNarrative(TESTNARRATIVE1);
		nb2 = testConnection.getNarrative(TESTNARRATIVE2);
	}

	/**
	 * Test method for {@link org.arachb.owlbuilder.lib.Narrative#Narrative(org.arachb.arachadmin.NarrativeBean)}.
	 */
	@Test
	public void testNarrative() {
		Narrative n1 = new Narrative(nb1);
		assertNotNull(n1);
		Narrative n2 = new Narrative(nb2);
		assertNotNull(n2);
	}

	/**
	 * Test method for {@link org.arachb.owlbuilder.lib.Narrative#generateOWL(org.arachb.owlbuilder.Owlbuilder, java.util.Map)}.
	 * @throws Exception 
	 */
	@Test
	public void testGenerateOWL2arg() throws Exception {
		Narrative n1 = new Narrative(nb1);
		Map<String,OWLObject> elements = new HashMap<>();
		OWLObject owlObj = n1.generateOWL(builder, elements);
		assertNotNull(owlObj);
		assertEquals(1,elements.size());
	}

	/**
	 * Test method for {@link org.arachb.owlbuilder.lib.Narrative#generateOWL(org.arachb.owlbuilder.Owlbuilder)}.
	 * @throws Exception 
	 */
	@Test
	public void testGenerateOWL1arg() throws Exception {
		Narrative n1 = new Narrative(nb1);
		OWLObject owlObj = n1.generateOWL(builder);
		assertNotNull(owlObj);
		assertTrue(owlObj instanceof OWLNamedIndividual);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		testConnection.close();
	}

}
