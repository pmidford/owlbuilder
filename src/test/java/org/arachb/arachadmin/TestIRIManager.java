package org.arachb.arachadmin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.semanticweb.owlapi.model.IRI;

public class TestIRIManager {
	
	private AbstractConnection c;

	@Before
	public void setUp() throws Exception {
		c = new MockConnection();

	}

	@Test
	public void testIRIManager() throws Exception{
		IRIManager testManager = new IRIManager(c);
		assertThat(testManager,notNullValue(IRIManager.class));
	}

	@Test
	public void testGenerateARACHB_IRI_String() throws Exception{
		IRIManager testManager = new IRIManager(c);
		String idString = testManager.generateARACHB_IRI_String();
		assertThat(idString,notNullValue(String.class));
	}

	
	static final String TESTTAXONURI = "http://purl.obolibrary.org/obo/NCBITaxon_6866";
	static final String TESTTAXONID = "6866";
	
	@Test
	public void testGetNCBI_IRI() throws Exception{
		IRIManager testManager = new IRIManager(c);
		IRI testid = testManager.getNCBI_IRI(TESTTAXONID);
		assertThat(testid,notNullValue(IRI.class));
		assertThat(testid.toString(),equalTo(TESTTAXONURI));
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();


}
