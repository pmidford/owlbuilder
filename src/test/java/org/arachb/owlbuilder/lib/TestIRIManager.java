package org.arachb.owlbuilder.lib;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.semanticweb.owlapi.model.IRI;

public class TestIRIManager {
	
	private AbstractConnection c;
	private Term t;

	@Before
	public void setup() throws Exception {
		c = new MockConnection();
		t = c.getTerm(1);

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

	@Test
	public void testValidateIRI() throws Exception {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Term has neither assigned nor generated id");
		IRIManager testManager = new IRIManager(c);
		Term badTerm = new Term();  //uninitialized - should throw error
		testManager.validateIRI(badTerm);
		// should work
		testManager.validateIRI(t);
	}

}
