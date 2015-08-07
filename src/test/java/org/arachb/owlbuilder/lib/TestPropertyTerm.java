package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.DBConnection;
import org.arachb.arachadmin.PropertyBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

public class TestPropertyTerm {

	private Owlbuilder builder;
	private static Logger log = Logger.getLogger(TestPropertyTerm.class);
	private static AbstractConnection testConnection;

	private static String hasPartIRI = Vocabulary.hasPartProperty.toString();
	private static PropertyBean hasPartBean;
	private static PropertyTerm hasPartPropertyTerm;

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
		hasPartBean = testConnection.getPropertyFromSourceId(hasPartIRI);
		hasPartPropertyTerm = new PropertyTerm(hasPartBean);
	}

	@Before
	public void setUp() throws Exception {
		builder = new Owlbuilder(testConnection);
		builder.setUpForTesting();
	}


	@Test
	public void testGenerateOwl1arg() throws Exception {
		OWLObject owlObj = hasPartPropertyTerm.generateOWL(builder);
		assertNotNull(owlObj);
		assert(owlObj instanceof OWLObjectPropertyExpression);
	}


	@Test
	public void testGenerateOwl2arg() throws Exception{
		Map<String,OWLObject> elements = new HashMap<>();
		OWLObject owlObj = hasPartPropertyTerm.generateOWL(builder, elements);
		assertEquals(1,elements.size());
		assertNotNull(elements.get(hasPartIRI));
		assertEquals(owlObj,elements.get(hasPartIRI));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		testConnection.close();
	}

}
