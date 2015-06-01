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
		
	private static final Map<Integer, TermBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(TermBean.class);


	
	private int id;
	private String source_id;
	private int domain;
	private int authority;
	private String label;
	private String generated_id;
	private String comment;
	
		

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCEID);
		domain = record.getInt(DBDOMAIN);
		authority = record.getInt(DBAUTHORITY);
		label = record.getString(DBLABEL);
		generated_id = record.getString(DBGENERATEDID);
		comment = record.getString(DBCOMMENT);
	}

	
	
	/* accessor */
	
	public int getId(){
		return id;
	}
	
	
	public String getSourceId(){
		return source_id;
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
		generated_id = id;
	}


	@Override
	public String getGeneratedId() {
		return generated_id;
	}


	final private static String NOTERMGENID = "Term has no source or generated id; db id = %s";

	@Override
	public String getIRIString(){
		if (getSourceId() == null){
			final String genId = getGeneratedId();
			if (genId == null){
				final String msg = String.format(NOTERMGENID, getId());
				throw new IllegalStateException(msg);
			}
			return genId;
		}
		return getSourceId();
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		if (getSourceId() == null){
			if (getGeneratedId() == null){
				manager.generateIRI(this);
			}
			return getGeneratedId();
		}
		return getSourceId();
	}


	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		c.updateTerm(this);
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
