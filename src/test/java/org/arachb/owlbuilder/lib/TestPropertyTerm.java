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

	private static String partOfIRI = Vocabulary.partOfProperty.toString();
	private static PropertyBean partOfBean;
	private static PropertyTerm partOfPropertyTerm;

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
		partOfBean = testConnection.getPropertyFromSourceId(partOfIRI);
		partOfPropertyTerm = new PropertyTerm(partOfBean);
	}

	@Before
	public void setUp() throws Exception {
		builder = new Owlbuilder(testConnection);
		builder.setUpForTesting();
	}


	@Test
	public void testGenerateOwl1arg() throws Exception {
		OWLObject owlObj = partOfPropertyTerm.generateOWL(builder);
		assertNotNull(owlObj);
		assert(owlObj instanceof OWLObjectPropertyExpression);
	}


	@Test
	public void testGenerateOwl2arg() throws Exception{
		Map<String,OWLObject> elements = new HashMap<>();
		OWLObject owlObj = partOfPropertyTerm.generateOWL(builder, elements);
		assertEquals(1,elements.size());
		assertNotNull(elements.get(partOfIRI));
		assertEquals(owlObj,elements.get(partOfIRI));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		testConnection.close();
	}

}
