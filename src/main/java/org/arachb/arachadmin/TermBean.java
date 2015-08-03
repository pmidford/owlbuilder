package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class TermBean implements CachingBean, UpdateableBean{


	static final int DBID = 1;
	static final int DBSOURCEID = 2;
	static final int DBDOMAIN = 3;
	static final int DBAUTHORITY = 4;
	static final int DBLABEL = 5;
	static final int DBGENERATEDID = 6;
	static final int DBCOMMENT = 7;
	static final int DBUIDSET = 8;
		
	private static final Map<Integer, TermBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(TermBean.class);


	
	private int id;
	private String source_id;
	private int domain;
	private int authority;
	private String label;
	private String generated_id;
	private String comment;
	private int uidset;
	
		

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCEID);
		domain = record.getInt(DBDOMAIN);
		authority = record.getInt(DBAUTHORITY);
		label = record.getString(DBLABEL);
		generated_id = record.getString(DBGENERATEDID);
		comment = record.getString(DBCOMMENT);
		uidset = record.getInt(DBUIDSET);
	}

	
	
	/* accessor */
	
	public int getId(){
		return id;
	}
	
	
	public String getSourceId(){
		//exception if not cached, want to see that error
		return UidSet.getCached(uidset).getGeneratedId();
	}

	public int getDomain(){
		return domain;
	}
	
	public int getAuthority(){
		return authority;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getComment(){
		return comment;
	}


	@Override
	public void setGeneratedId(String id) {
		//exception if not cached, want to see that error
		UidSet.getCached(uidset).setGeneratedId(id);
	}


	@Override
	public String getGeneratedId() {
		//exception if not cached, want to see that error
		return UidSet.getCached(uidset).getGeneratedId();
	}


	final private static String NOTERMGENID = "Term has no source or generated id; db id = %s";

	@Override
	public String getIRIString(){
		String refid = UidSet.getCached(uidset).getRefId();
		if (refid == null){
			final String msg = String.format(NOTERMGENID, getId());
			throw new IllegalStateException(msg);			
		}
		return refid;
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		return UidSet.getCached(uidset).checkIRIString(manager);
	}


	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		c.updateUidSet(UidSet.getCached(uidset));   //hide uncaching?
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
	
	public static TermBean getCached(int id){
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
