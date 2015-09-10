package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TaxonBean implements CachingBean,UpdateableBean{
	

	static final int DBID = 1;
	static final int DBNAME = 2;
	static final int DBAUTHOR = 3;
	static final int DBYEAR = 4;
	static final int EXTERNAL_ID = 5;
	static final int AUTHORITY = 6;
	static final int PARENT_NAME = 7;
	static final int DBGENERATEDID = 8;
	static final int PARENT_TERM = 9;
	static final int MERGED = 10;
	static final int MERGE_STATUS = 11;
	static final int DBPARENTREFID = 12;
	static final int DBUIDSET = 13;
	
	private static final Map<Integer, TaxonBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(TaxonBean.class);


	
	private int id;
	private String name;
	private String author;
	private String year;
	private String external_id;
	private String authority;
	private String parent_name;
	private String generated_id;
	private int parent_term;
	private boolean merged;
	private String merge_status;
	private String parent_refid;
	private int uidset;
	

	//maybe make this a constructor
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		name = record.getString(DBNAME);
		author = record.getString(DBAUTHOR);
		year = record.getString(DBYEAR);
		external_id = record.getString(EXTERNAL_ID);
		authority = record.getString(AUTHORITY);
		parent_name = record.getString(PARENT_NAME);
		generated_id = record.getString(DBGENERATEDID);
		parent_term = record.getInt(PARENT_TERM);
		merged = record.getBoolean(MERGED);
		merge_status = record.getString(MERGE_STATUS);
		if (parent_term != 0){
			setParentIRI(record);
		}
		uidset = record.getInt(DBUIDSET);
	}
	
	
	public int getId(){
		return id;
	}

	//TODO rename this field?
	public String getSourceId(){
		return external_id;
	}
	
	
	public String getName(){
		return name;
	}

	public String get_author(){
		return author;
	}
	
	public String get_year(){
		return year;
	}
	

	public String getGeneratedId(){
		return UidSet.getCached(uidset).getGeneratedId();
	}
	
	
	//Just updates the id in the uidset - method for updating db is in DBConnection
	public void setGeneratedId(String new_id){
		UidSet.getCached(uidset).setGeneratedId(new_id);
	}
	
	public String getAuthority(){
		return authority;
	}
	
	public String getParentName(){
		return parent_name;
	}
	
	public boolean getMerged(){
		return merged;
	}
	
	public String getMergeStatus(){
		return merge_status;
	}
	
	public String getParentRefId(){
		return parent_refid;
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException{
		c.updateTaxon(this);
	}
	
	final static String TAXONBADSOURCEGENID = "Taxon has neither source nor generated id";
	
	@Override
	public String getIRIString() {
		String refid = UidSet.getCached(uidset).getRefId();
		if (refid == null){
				throw new IllegalStateException(TAXONBADSOURCEGENID);
		}
		return refid;
	}
	
	@Override
	public String checkIRIString(IRIManager manager) throws SQLException{
		return UidSet.getCached(uidset).checkIRIString(manager);
	}

	private void setParentIRI(AbstractResults record) throws SQLException {
		if (record.getString(DBPARENTREFID) != null){
			parent_refid = record.getString(DBPARENTREFID);
		}
		else{
			throwBadState(BADPARENTIRI);
		}
	}

	private final static String BADPARENTIRI =
			"Taxon without IRI referenced as parent of taxon: taxon id = %s; parent id = %s";

	private void throwBadState(String template){
		final String msg = String.format(template, id, parent_term);
		throw new IllegalStateException(msg);
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
	
	/**
	 * Retrieves a bean from the class-specific cache
	 * @param id identifies Taxonbean desired
	 * @return cached taxonbean with specified id
	 * @throws RuntimeException
	 */
	public static TaxonBean getCached(int id){
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		throw new RuntimeException(String.format("Cache miss for TaxonBean, id = %d",id));
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
