package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;



/**
 * This bean supports individuals that appear in participants
 * @author pmidford
 *
 */
public class IndividualBean implements BeanBase,UpdateableBean,CachingBean{
	
	static final int DBID = 1;
	static final int DBSOURCEID = 2;
	static final int DBGENERATEDID = 3;
	static final int DBLABEL= 4;
	static final int DBTERM = 5;

	private static final Map<Integer, IndividualBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(IndividualBean.class);


	private int id;
	private String source_id;
	private String generated_id;
	private String label;
	private int term;  //the individual's class

	private Set<Integer> narratives = new HashSet<>();


	@Override
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
		source_id = record.getString(DBSOURCEID);
		generated_id = record.getString(DBGENERATEDID);
		label = record.getString(DBLABEL);
		term = record.getInt(DBTERM);
	}

	@Override
	public int getId() {
		return id;
	}
	
	public String getSourceId() {
		return source_id;
	}
	
	public String getGeneratedId() {
		return generated_id;
	}

	public String getLabel(){
		return label;
	}

	public int getTerm(){
		return term;
	}


	@Override
	public void setGeneratedId(String id) {
		generated_id = id;
		
	}

	public Set<Integer> getNaratives(){
		return narratives;
	}

	void addNarrative(int narrative_id){
		narratives.add(narrative_id);
	}

	final static String INDBADDOIGENID = "Individual has neither source id nor generated id: ";

	@Override
	public String getIRIString() {
		if (getSourceId() == null){
			if (getGeneratedId() == null){
				throw new IllegalStateException(INDBADDOIGENID + getId());
			}
			return getGeneratedId();
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
		// TODO Auto-generated method stub
		
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

	public static IndividualBean getCached(int id){
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

}
