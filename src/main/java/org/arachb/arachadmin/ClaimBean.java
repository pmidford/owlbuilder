package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class ClaimBean implements BeanBase,UpdateableBean,CachingBean{  //do all claims need IRI's?
	
	final static int DBID = 1;
	final static int DBPUBLICATION = 2;
	final static int DBPUBLICATIONBEHAVIOR = 3;
	final static int DBBEHAVIORTERM = 4;
	final static int DBEVIDENCE = 5;
	final static int DBGENERATEDID = 6;
	final static int DBPUBDOI = 7;
	final static int DBPUBGENERATEDID = 8;
	final static int DBBEHAVIORSOURCEID = 9;
	final static int DBBEHAVIORGENERATEDID = 10;
	final static int DBEVIDENCESOURCEID = 11;
	final static int DBEVIDENCEGENERATEDID = 12;
	
	final static String BADPUBLICATIONIRI =
			"Publication without IRI referenced as publication cited in claim: claim id = %s; publication id = %s";
	final static String BADBEHAVIORIRI =
			"Term without IRI referenced as behavior cited in claim: claim id = %s; behavior id = %s";
	final static String BADEVIDENCEIRI =
			"Term without IRI referenced as evidence type supporting claim; claim id = %s; evidence id = %s";

	private static final Map<Integer, ClaimBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(ClaimBean.class);


	
	private int id;
	private int publication;
	private int behavior;
	private String publicationBehavior;
	private int evidence;
	private String generated_id = null;  //for validity checking
	private String behaviorIRI;
	private String publicationIRI;
	private String evidenceIRI;
	
	static final ClaimBean dummy = new ClaimBean(); 

	//maybe make this a constructor
	@Override
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		publication = record.getInt(DBPUBLICATION);
		publicationBehavior = record.getString(DBPUBLICATIONBEHAVIOR);
		behavior= record.getInt(DBBEHAVIORTERM);
		evidence = record.getInt(DBEVIDENCE);
		generated_id = record.getString(DBGENERATEDID);
		if (publication != 0){
			updatePublicationIRI(record);
		}
		if (behavior != 0){
			updateBehaviorIRI(record);
		}
		if (evidence != 0){
			updateEvidenceIRI(record);
		}
	}
	
	private void updatePublicationIRI(AbstractResults record) throws SQLException{
		if (record.getString(DBPUBDOI) != null){
			this.setPublicationIri(IRIManager.cleanupDoi(record.getString(DBPUBDOI)));
		}
		else if (record.getString(DBPUBGENERATEDID) != null){
			this.setPublicationIri(record.getString(DBPUBGENERATEDID));
		}
		else{
			throwBadState(BADPUBLICATIONIRI);
		}
	}

	private void updateBehaviorIRI(AbstractResults record) throws SQLException{
		if (record.getString(DBBEHAVIORSOURCEID) != null){
			this.setBehaviorIri(record.getString(DBBEHAVIORSOURCEID));
		}
		else if (record.getString(DBBEHAVIORGENERATEDID) != null){
			this.setBehaviorIri(record.getString(DBBEHAVIORGENERATEDID));
		}
		else{
			throwBadState(BADBEHAVIORIRI);
		}
	}

	private void updateEvidenceIRI(AbstractResults record) throws SQLException{
		if (record.getString(DBEVIDENCESOURCEID) != null){
			this.setEvidenceIri(record.getString(DBEVIDENCESOURCEID));
		}
		else if (record.getString(DBEVIDENCEGENERATEDID) != null){
			this.setEvidenceIri(record.getString(DBEVIDENCEGENERATEDID));
		}
		else{
			throwBadState(BADEVIDENCEIRI);
		}
	}
	
	private void throwBadState(String template){
		final String msg = String.format(template, id, evidence);
		throw new IllegalStateException(msg);
	}
	

	/* access methods */

	@Override
	public int getId(){
		return id;
	}
	
	public int getPublication(){
		return publication;
	}
	
	public String getPublicationBehavior(){
		return publicationBehavior;
	}
	
	public int getBehavior(){
		return behavior;
	}
			
	
	public int getEvidence(){
		return evidence;
	}
	

	public String getPublicationIri(){
		return publicationIRI;
	}
	
	public String getEvidenceIri(){
		return evidenceIRI;
	}
	
	public String getGeneratedId(){
		return generated_id;
	}

	
	public String getBehaviorIri(){
		return behaviorIRI;
	}
	

	public void setPublicationIri(String s){
		publicationIRI = s;
	}

	/**
	 * 
	 * @param s representation of IRI for behavior class
	 */
	public void setBehaviorIri(String s){
		behaviorIRI = s;
	}

	public void setEvidenceIri(String s){
		evidenceIRI = s;
	}

	public void setGeneratedId(String id){
		generated_id = id;
	}

	
	final static String NOCLAIMGENID = "Claim has no generated id; db id = %s";

	@Override
	public String getIRIString(){
		final String genId = getGeneratedId();
		if (genId == null){
			final String msg = String.format(NOCLAIMGENID, getId());
			throw new IllegalStateException(msg);
		}
		return genId;
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		if (getGeneratedId() == null){
			manager.generateIRI(this);
		}
		return getGeneratedId();
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException{
		c.updateClaim(this);
	}

	/**
	 * may not be needed, but if we ever need to reopen a database
	 */
	static void flushCache(){
		cache.clear();
	}

	public static boolean isCached(int id){
		return cache.containsKey(id);
	}

	public static ClaimBean getCached(int id){
		assert cache.containsKey(id) : String.format("no cache entry for %d",id);
		return cache.get(id);
	}

	@Override
	public void cache(){
		if (isCached(id)){
			log.warn(String.format("Tried multiple caching of %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
		}
		cache.put(id, this);
	}


	@Override
	public void updatecache(){
		ClaimBean existing = cache.get(getId());
		if (!this.equals(existing)){
			log.warn(String.format("Forcing update of cached bean %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
			cache.put(getId(), this);
		}
	}

}
