package org.arachb.arachadmin;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;

public class IRIManager {
	
	
	
	private static final Logger log = Logger.getLogger(IRIManager.class);

	
	private int idCounter=-1;
	private AbstractConnection c;
	
	
	/**
	 * Loads the uid table and scans it (or change to sql query) to initialize
	 * generated id counter
	 * @param connection
	 * @throws Exception
	 */
	public IRIManager(AbstractConnection connection) throws Exception{
		connection.getUidSetTable();
		String lastgenid = connection.getUidSetLastGenId();
		idCounter = extractCount(lastgenid);
		log.info("id counter is " + idCounter);
		c = connection;
	}
	
	private int extractCount(String id){
		if (id != null && id.startsWith(IRIManager.ARACHBPREFIX)){
			try{
				int result = Integer.parseInt(id.substring(IRIManager.ARACHBPREFIX.length()));
				return result;
			}
			catch(NumberFormatException e){
				log.warn("Bad id format: " + id);
				return -1;
			}
		}
		return -1;
	}


	
	
	//TODO this should be configurable...
	public final static String ARACHBPREFIX = "http://arachb.org/arachb/ARACHB_";
	public String generateARACHB_IRI_String(){  //should be private
		idCounter++;
		return String.format("%s%07d",ARACHBPREFIX,idCounter);
	}
	
	//TODO maybe this too
	final static String ncbiprefix = "http://purl.obolibrary.org/obo/NCBITaxon_";
	public IRI getNCBI_IRI(String ncbiID){
		return IRI.create(ncbiprefix+ncbiID);
	}
	
	public void generateIRI(UpdateableBean b) throws SQLException{
		if (b.getGeneratedId() != null)
			return;
		//need to generate
		String newIRI = generateARACHB_IRI_String();
		b.setGeneratedId(newIRI);
		c.updateNamedEntity(b);			
	}
	
	final static String DOICHARENCODING = "UTF-8";
	
	/**
	 * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
	 * @param doi
	 * @return IRI using using doi prefix
	 */
	public static String cleanupDoi(String doi){
		if (doi == null || doi.length() == 0){
			throw new RuntimeException("Invalid empty DOI in publication");
		}
		URL raw = null;
		String cleanpath = null;
		try {
			raw = new URL(doi);
			cleanpath = URLEncoder.encode(raw.getPath().substring(1),DOICHARENCODING);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("DOI could not be converted to URL: " + doi);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("Claims that " + DOICHARENCODING + " is not supported");
		} 
		
		if (log.isDebugEnabled()){
			log.debug("raw is " + raw);
		}
		if (log.isDebugEnabled()){
			log.debug("clean path is " + cleanpath);
		}
		return IRI.create("http://dx.doi.org/",cleanpath).toString();
	}

}
