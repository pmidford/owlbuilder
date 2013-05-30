package org.arachb.owlbuilder.lib;

import org.semanticweb.owlapi.model.IRI;

public class IRIManager {
	
	public static final IRI pubAboutInvestigation = 
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000312");

	
	private int idCounter=0; 
	
	public IRIManager(DBConnection c){
		idCounter = scanPrivateIDs(); 
	}
	
	private int scanPrivateIDs(){
		return -1;
	}
	
	final static String arachbprefix = "http://arachb.org/arachb/ARACHB_";
	public IRI getARACHB_IRI(){
		idCounter++;
		return IRI.create(String.format("%s%07d",arachbprefix,idCounter));
	}
	
	final static String ncbiprefix = "http://purl.obolibrary.org/obo/NCBITaxon_";
	public IRI getNCBI_IRI(String ncbiID){
		return IRI.create(ncbiprefix+ncbiID);
	}
	
}
