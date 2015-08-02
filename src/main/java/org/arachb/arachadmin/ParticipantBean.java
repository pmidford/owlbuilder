package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class ParticipantBean implements CachingBean,BeanBase{


		
	final static int DBID = 1;
	final static int DBQUANTIFICATION = 2;
	final static int DBLABEL = 3;
	final static int DBPROPERTY = 4;
	final static int DBPUBLICATIONTAXON = 5;
	final static int DBPUBLICATIONANATOMY = 6;
	final static int DBPUBLICATIONSUBSTRATE = 7;
	final static int DBHEADELEMENT = 8;

	final static String BADTAXONIRI =
			"Term without IRI referenced as participant taxon: participant id = %s; taxon id = %s";
	final static String BADANATOMYIRI =
			"Term without IRI referenced as participant anatomy: participant id = %s; anatomy id = %s";
	final static String BADSUBSTRATEIRI =
			"Term without IRI referenced as participant substrate; participant id = %s; substrate id = %s";
	
	private static final Map<Integer, ParticipantBean> cache = new HashMap<>();
	private int id;
	private int taxon;
	private int substrate;
	private int anatomy;
	private String quantification;
	private int property;
	private String publicationTaxon;
	private String label;
	private String publicationAnatomy;
	private String publicationSubstrate;
	private String taxonIRI = null;
	private String substrateIRI = null;
	private String anatomyIRI = null;
	private int headElement;

	
	final private Set<Integer> elements = new HashSet<Integer>();
	
	public final static String INDIVIDUALQUANTIFIER = "INDIVIDUAL";
	public final static String SOMEQUANTIFIER = "SOME";
	
	private static Logger log = Logger.getLogger(ParticipantBean.class);

	
	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		quantification = record.getString(DBQUANTIFICATION);
		label = record.getString(DBLABEL);
		property = record.getInt(DBPROPERTY);
		publicationTaxon = record.getString(DBPUBLICATIONTAXON);
		publicationAnatomy = record.getString(DBPUBLICATIONANATOMY);
		publicationSubstrate = record.getString(DBPUBLICATIONSUBSTRATE);
		headElement = record.getInt(DBHEADELEMENT);
	}
	
	public int getId(){
		return id;
	}
	
	public int getTaxon(){
		return taxon;
	}
	
	public int getSubstrate(){
		return substrate;
	}
	
	public int getAnatomy(){
		return anatomy;
	}
	
	public String getQuantification(){
		return quantification;
	}
	
	public int getProperty(){
		return property;
	}
	
	public String getPublicationTaxon(){
		return publicationTaxon;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getPublicationAnatomy(){
		return publicationAnatomy;
	}
	
	public String getPublicationSubstrate(){
		return publicationSubstrate;
	}

	public String getTaxonIri(){
		return taxonIRI;
	}
		
	public String getSubstrateIri(){
		return substrateIRI;
	}
	
	public String getAnatomyIri(){
		return anatomyIRI;
	}
	
	public int getHeadElement(){
		return headElement;
	}
	
	void setElements(Set<Integer> pes){
		elements.clear();
		elements.addAll(pes);
	}

	public Set<Integer> getElements(){
		return elements;
	}
	
	public int getHeadProperty(){
		return property;
	}

	final static String NOPARTICGENID = "Participant has no generated id; db id = %s";


	/**
	 * may not be needed, but if we ever need to reopen a database
	 */
	static void flushCache(){
		cache.clear();
	}

	public static boolean isCached(int id){
		return cache.containsKey(id);
	}

	public static ParticipantBean getCached(int id){
		assert cache.containsKey(id) : String.format("no cache entry for %d",id);
		return cache.get(id);
	}

	@Override
	public void cache(){
		if (isCached(getId())){
			log.warn(String.format("Tried multiple caching of %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
		}
		cache.put(getId(), this);
	}


	@Override
	public void updatecache(){
		if (!this.equals(cache.get(getId()))){
			log.warn(String.format("Forcing update of cached bean %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
			cache.put(getId(), this);
		}
	}

}
