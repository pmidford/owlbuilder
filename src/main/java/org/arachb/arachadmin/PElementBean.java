package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;



public class PElementBean implements CachingBean,BeanBase {
	
	final static int DBID = 1;
	final static int DBTYPE = 2;
	final static int DBPARTICIPANT = 3;
	final static int DBTERM = 4;
	final static int DBINDIVIDUAL = 5;
	
	private static final Map<Integer, PElementBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(PElementBean.class);


	
	private int id;
	private int eletype;
	private int participant;
	private Integer termId = null;
	private Integer individualId = null;

	private final Map<Integer,Integer> parents = new HashMap<Integer,Integer>();
	private final Map<Integer,Integer> children = new HashMap<Integer,Integer>();

	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		eletype = record.getInt(DBTYPE);
		participant = record.getInt(DBPARTICIPANT);
		termId = record.getInt(DBTERM);
		individualId = record.getInt(DBINDIVIDUAL);
	}
	
	
	
	
	
	@Override
	public int getId() {
		return id;
	}

	
	public int getType(){
		return eletype;
	}
	
	
	public int getParticipant(){
		return participant;
	}
	
	
	public Integer getTermId(){
		return termId;
	}

	
	public Integer getIndividualId(){
		return individualId;
	}
	
	/* package visibility for next two is deliberate */
	
	void addParent(Integer parent, Integer property){
		parents.put(parent, property);
	}
	
	void addChild(Integer child, Integer property){
		children.put(child, property);
	}
	
	
	public Set<Integer> getParents(){
		return parents.keySet();
	}
	
	
	public Set<Integer> getChildren(){
		return children.keySet();
	}
	
	public Integer getParentProperty(Integer parent){
		if (parents.containsKey(parent)){
			return parents.get(parent);
		}
		throw new RuntimeException(String.format("PElement %d has no parent %d",getId(),parent));
	}
	
	public Integer getChildProperty(Integer child){
		if (children.containsKey(child)){
			return children.get(child);
		}
		throw new RuntimeException(String.format("PElement %d has no child %d",getId(),child));
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
	
	public static PElementBean getCached(int id){
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
