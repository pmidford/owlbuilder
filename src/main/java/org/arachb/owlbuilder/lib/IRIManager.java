package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;

public class IRIManager {
	
	public static final IRI pubAboutInvestigation = 
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000312");
	public static final IRI denotesProperty =
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000219");
	public static final IRI informationContentEntity = 
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000030");
	public static final IRI textualEntity =
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000300");

	public static Set<IRI> iaoImports = new HashSet<IRI>();
	static {
		iaoImports.add(pubAboutInvestigation);
		iaoImports.add(denotesProperty);
		iaoImports.add(informationContentEntity);
	}
	
	public static final IRI partOfProperty = 
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
	public static final IRI hasParticipantProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000057");

	public static Set<IRI> bfoImports = new HashSet<IRI>();
	static {
		bfoImports.add(partOfProperty);
		bfoImports.add(hasParticipantProperty);
	}
	
	private int idCounter=0;
	private AbstractConnection c;
	
	public IRIManager(AbstractConnection connection){
		idCounter = scanPrivateIDs();
		c = connection;
	}
	
	private int scanPrivateIDs(){
		return -1;
	}
	
	final static String arachbprefix = "http://arachb.org/arachb/ARACHB_";
	public String generateARACHB_IRI_String(){  //should be private
		idCounter++;
		return String.format("%s%07d",arachbprefix,idCounter);
	}
	
	final static String ncbiprefix = "http://purl.obolibrary.org/obo/NCBITaxon_";
	public IRI getNCBI_IRI(String ncbiID){
		return IRI.create(ncbiprefix+ncbiID);
	}
	
	public void validateIRI(AbstractNamedEntity e) throws SQLException{
		if (e.getIRI_String() != null)
			return;
		//need to generate
		String newIRI = generateARACHB_IRI_String();
		e.setGeneratedID(newIRI);
		c.updateNamedEntity(e);			
			
	}
	
}
