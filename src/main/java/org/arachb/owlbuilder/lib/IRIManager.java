package org.arachb.owlbuilder.lib;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.BeanWithIRI;
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

	public static final Set<IRI> iaoImports = new HashSet<IRI>();
	static {
		iaoImports.add(pubAboutInvestigation);
		iaoImports.add(denotesProperty);
		iaoImports.add(informationContentEntity);
	}
	
	public static final IRI partOfProperty = 
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
	public static final IRI hasParticipantProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000057");

	public static final Set<IRI> bfoImports = new HashSet<IRI>();
	static {
		bfoImports.add(partOfProperty);
		bfoImports.add(hasParticipantProperty);
	}
	
	public static final IRI arachnidaTaxon = 
			IRI.create("http://purl.obolibrary.org/obo/NCBITaxon_6854");
	
	
	private static final Logger log = Logger.getLogger(IRIManager.class);

	
	private int idCounter=-1;
	private AbstractConnection c;
	
	public IRIManager(AbstractConnection connection) throws Exception{
		idCounter = connection.scanPrivateIDs();
		c = connection;
	}
	
	
	public final static String ARACHBPREFIX = "http://arachb.org/arachb/ARACHB_";
	public String generateARACHB_IRI_String(){  //should be private
		idCounter++;
		return String.format("%s%07d",ARACHBPREFIX,idCounter);
	}
	
	final static String ncbiprefix = "http://purl.obolibrary.org/obo/NCBITaxon_";
	public IRI getNCBI_IRI(String ncbiID){
		return IRI.create(ncbiprefix+ncbiID);
	}
	
	public void validateIRI(BeanWithIRI b) throws SQLException{
		if (b.checkIriString() != null)
			return;
		//need to generate
		String newIRI = generateARACHB_IRI_String();
		b.setGeneratedId(newIRI);
		c.updateNamedEntity(b);			
			
	}
	
	
	/**
	 * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
	 * @param doi
	 * @return IRI using using doi prefix
	 * @throws Exception either MalformedURL or Encoding exceptions can be thrown
	 */
	public static String cleanupDoi(String doi) throws Exception{
		if (doi == null || doi.length() == 0){
			throw new RuntimeException("Invalid empty DOI in publication");
		}
		URL raw = new URL(doi);
		String cleanpath = URLEncoder.encode(raw.getPath().substring(1),"UTF-8");
		if (log.isDebugEnabled()){
			log.debug("raw is " + raw);
		}
		if (log.isDebugEnabled()){
			log.debug("clean path is " + cleanpath);
		}
		return IRI.create("http://dx.doi.org/",cleanpath).toString();
	}

}
