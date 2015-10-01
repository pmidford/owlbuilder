/**
 * 
 */
package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author pmidford
 *
 */
public class PublicationCurationBean implements BeanBase,CachingBean {
	
	static final int DBID = 1;
	static final int DBSTATUS = 2;

	private static final Map<Integer, PublicationCurationBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(PublicationCurationBean.class);

	private int id;
	private String status;
	
	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		status = record.getString(DBSTATUS);
	}

	@Override
	public int getId() {
		return id;
	}
	
	public String getStatus(){
		return status;
	}

	static void flushCache(){
		cache.clear();
	}

	public static boolean isCached(int id){
		return cache.containsKey(id);
	}

	public static int cacheSize(){
		return cache.size();
	}


	/* (non-Javadoc)
	 * @see org.arachb.arachadmin.CachingBean#cache()
	 */
	@Override
	public void cache() {
		if (isCached(getId())){
			log.warn(String.format("Tried multiple caching of %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
		}
		cache.put(getId(), this);
	}

	/* (non-Javadoc)
	 * @see org.arachb.arachadmin.CachingBean#updatecache()
	 */
	@Override
	public void updatecache() {
		if (!this.equals(cache.get(getId()))){
			log.warn(String.format("Forcing update of cached bean %s with id %d",
					               getClass().getSimpleName(),
					               getId()));
			cache.put(this.getId(), this);
		}
	}

	public static PublicationCurationBean getCached(int id) {
		assert cache.containsKey(id) : String.format("no cache entry for %d",id);
		return cache.get(id);
	}


}
