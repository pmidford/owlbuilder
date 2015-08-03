package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class UidSet implements BeanBase, CachingBean, UpdateableBean {

	final static int DBID = 1;
	final static int DBSOURCE_ID = 2;
	final static int DBGENERATED_ID = 3;
	final static int DBREF_ID = 4;

	private static final Map<Integer, UidSet> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(UidSet.class);

	private int id;  //table id, wish this could go away 
	private String source_id;
	private String generated_id;
	private String ref_id;


	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCE_ID);
		generated_id = record.getString(DBGENERATED_ID);
		ref_id = record.getString(DBREF_ID);
	}

	public String getSourceId(){
		return source_id;
	}

	public String getRefId(){
		return ref_id;
	}
	
	@Override
	public void setGeneratedId(String id) {
		generated_id = id;		
	}

	@Override
	public String getGeneratedId() {
		return generated_id;
	}

	
	final static String NOGENID = "UID entry has no source or generated id; db id = %s";
	
	@Override
	public String getIRIString() throws IllegalStateException {
		if (getSourceId() == null){
			final String genId = getGeneratedId();
			if (genId == null){
				final String msg = String.format(NOGENID, getId());
				throw new IllegalStateException(msg);
			}
			return genId;
		}
		return getSourceId();
	}

	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		if (getSourceId() == null){
			final String genId = getGeneratedId();
			if (genId == null){
				manager.generateIRI(this);
			}
			return getGeneratedId();
		}
		return getSourceId();
	}

	
	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		c.updateUidSet(this);
	}

	static void flushCache(){
		cache.clear();
	}
	
	public static boolean isCached(int id){
		return cache.containsKey(id);
	}
	
	public static UidSet getCached(int id){
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
