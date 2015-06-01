package org.arachb.arachadmin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IRIManager;
import org.arachb.arachadmin.MockConnection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.semanticweb.owlapi.model.IRI;

public class TestIRIManager {
	
	private AbstractConnection c;

	@Before
	public void setup() throws Exception {
		c = new MockConnection();

	}

	@Test
	public void testIRIManager() throws Exception{
		IRIManager testManager = new IRIManager(c);
		assertNotNull(testManager);
	}

	@Test
	public void testGenerateARACHB_IRI_String() throws Exception{
		IRIManager testManager = new IRIManager(c);
		String idString = testManager.generateARACHB_IRI_String();
		assertNotNull(idString);
	}

	@Test
	public void testGetNCBI_IRI() throws Exception{
		String test1 = "6866";
		IRIManager testManager = new IRIManager(c);
		IRI testid = testManager.getNCBI_IRI(test1);
		assertNotNull(testid);
		assertEquals("http://purl.obolibrary.org/obo/NCBITaxon_6866",testid.toString());
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();


}
