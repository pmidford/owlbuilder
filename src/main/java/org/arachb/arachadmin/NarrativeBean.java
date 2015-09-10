package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class NarrativeBean implements UpdateableBean,CachingBean {
		
	static final int DBID = 1;
	static final int DBPUBLICATION = 2;
	static final int DBLABEL = 3;
	static final int DBDESCRIPTION = 4;
	static final int DBGENERATEDID = 5;
	static final int DBUIDSET = 6;
	static final int DBBEHAVIORANNOTATION = 7;
	
	private static final Map<Integer, NarrativeBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(NarrativeBean.class);


	
	private int id;
	private int publicationid;
	private String label;
	private String description;
	private String generated_id;
	private int uidset;
	private int behavior_annotation;
	
	

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		publicationid = record.getInt(DBPUBLICATION);
		label = record.getString(DBLABEL);
		description = record.getString(DBDESCRIPTION);
		generated_id = record.getString(DBGENERATEDID);
		uidset = record.getInt(DBUIDSET);
		behavior_annotation = record.getInt(DBBEHAVIORANNOTATION);
	}

	/* access methods */
	
	@Override
	public int getId() {
		return id;
	}
	
	public int getPublicationId(){
		return publicationid;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getBehaviorAnnotation(){
		return behavior_annotation;
	}

	@Override
	public void setGeneratedId(String id) {
		UidSet.getCached(uidset).setGeneratedId(id);
	}

	@Override
	public String getGeneratedId() {
		//This will fail if uidset is not cached, but that error should be seen
		return UidSet.getCached(uidset).getGeneratedId();
	}
	
	@Override
	public String checkIRIString(IRIManager manager) throws SQLException {
		return UidSet.getCached(uidset).checkIRIString(manager);
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		c.updateUidSet(UidSet.getCached(uidset));
		//c.updateNarrative(this);
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
	
	public static NarrativeBean getCached(int id){
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
		CachingBean existing = cache.get(getId());
		if (!this.equals(existing)){
			log.warn(String.format("Forcing update of cached bean %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
			cache.put(getId(), this);
		}
	}


	@Override
	public String getIRIString() throws IllegalStateException {
		return getGeneratedId();
	}

	

}
