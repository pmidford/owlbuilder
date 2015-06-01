package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;



public class PropertyBean implements CachingBean,BeanBase {

	final static int DBID = 1;
	final static int DBSOURCEID = 2;
	final static int DBAUTHORITY = 3;
	final static int DBLABEL = 4;
	final static int DBGENERATEDID = 5;
	final static int DBCOMMENT = 6;

	private static final Map<Integer, PropertyBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(PropertyBean.class);


	
	private int id;
	private String sourceId;
	private int authority;
	private String label;
	private String generatedId;  //this is dubious
	private String comment;
	
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		sourceId = record.getString(DBSOURCEID);
		authority = record.getInt(DBAUTHORITY);
		label = record.getString(DBLABEL);
		generatedId = record.getString(DBGENERATEDID);
		comment = record.getString(DBCOMMENT);		
	}
	
	
	public String getSourceId(){
		return sourceId;
	}

	public int getAuthority(){
		return authority;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getGeneratedId(){
		return generatedId;
	}
	
	public String getComment(){
		return comment;
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
	
	public static PropertyBean getCached(int id){
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
