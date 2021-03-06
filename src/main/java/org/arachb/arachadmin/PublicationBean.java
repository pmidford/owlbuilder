package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;




public class PublicationBean implements CachingBean,UpdateableBean {


	static final int DBID = 1;
	static final int DBPUBLICATIONTYPE = 2;
	static final int DBDISPENSATION = 3;
	static final int DBDOWNLOADED = 4;
	static final int DBREVIEWED = 5;
	static final int DBTITLE = 6;
	static final int DBALTERNATETITLE = 7;
	static final int DBAUTHORLIST = 8;
	static final int DBEDITORLIST = 9;
	static final int DBSOURCEPUBLICATION = 10;
	static final int DBVOLUME = 11;
	static final int DBISSUE = 12;
	static final int DBSERIALIDENTIFIER = 13;
	static final int DBPAGERANGE = 14;
	static final int DBPUBLICATIONDATE = 15;
	static final int DBPUBLICATIONYEAR = 16;
	static final int DBDOI = 17;
	static final int DBGENERATEDID = 18;
	static final int DBCURATIONSTATUS = 19;
	static final int DBCURATIONUPDATE = 20;
	static final int DBUIDSET = 21;
	

	private static final Map<Integer, PublicationBean> cache = new HashMap<>();
	private static Logger log = Logger.getLogger(PublicationBean.class);


	private int id;
	private String publication_type;
	private String dispensation;
	private String downloaded;
	private String reviewed;
	private String title;
	private String alternate_title;
	private String author_list;
	private String editor_list;
	private String source_publication;
	private int volume;
	private String issue;
	private String serial_identifier;
	private String page_range;
	private String publication_date;
	private String publication_year;
	private String doi;
	private String generated_id;
	private String curation_status;
	private String curation_update;
	private int uidset;

	
	
	@Override
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt(DBID);
		publication_type = record.getString(DBPUBLICATIONTYPE);
		dispensation = record.getString(DBDISPENSATION);
		downloaded = record.getString(DBDOWNLOADED);
		reviewed = record.getString(DBREVIEWED);
		title = record.getString(DBTITLE);
		alternate_title = record.getString(DBALTERNATETITLE);
		author_list = record.getString(DBAUTHORLIST);
		editor_list = record.getString(DBEDITORLIST);
		source_publication = record.getString(DBSOURCEPUBLICATION);
		volume = record.getInt(DBVOLUME);
		issue = record.getString(DBISSUE);
		serial_identifier = record.getString(DBSERIALIDENTIFIER);
		page_range = record.getString(DBPAGERANGE);
		publication_date = record.getString(DBPUBLICATIONDATE);
		publication_year = record.getString(DBPUBLICATIONYEAR);
		doi = record.getString(DBDOI);
		generated_id = record.getString(DBGENERATEDID);
        curation_status = record.getString(DBCURATIONSTATUS);
        curation_update = record.getString(DBCURATIONUPDATE);
        uidset = record.getInt(DBUIDSET);
	}
	
	/* accessors */

	@Override
	public int getId(){
		return id;
	}
	
	
	public String getPublicationType(){
		return publication_type;
	}
	
	public String getDispensation(){
		return dispensation;
	}
	
	public String getDownloaded(){
		return downloaded;
	}
	
	public String getReviewed(){
		return reviewed;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAlternateTitle(){
		return alternate_title;
	}
	
	public String getAuthorList(){
		return author_list;
	}
	
	public String getEditorList(){
		return editor_list;
	}
	
	public String getSourcePublication(){
		return source_publication;
	}
	
	public int getVolume(){
		return volume;
	}
	
	public String getIssue(){
		return issue;
	}
	
	public String getSerialIdentifier(){
		return serial_identifier;
	}
	
	public String getPageRange(){
		return page_range;
	}
	
	public String getPublicationDate(){
		return publication_date;
	}
	
	public String getPublicationYear(){
		return publication_year;
	}
	
	public String getDoi(){
		return UidSet.getCached(uidset).getSourceId();
	}

	public String getGeneratedId(){
		return UidSet.getCached(uidset).getGeneratedId();
	}

	public String getCurationStatus(){
		return curation_status;
	}
	
	public String getCurationUpdate(){
		return curation_update;
	}
	
	/* Updaters */

	//Just updates the id in the bean - method for updating db is in DBConnection
	public void setGeneratedId(String id) {
		UidSet.getCached(uidset).setGeneratedId(id);
	}

	
	public void updateDB(AbstractConnection c) throws SQLException{
		c.updateUidSet(UidSet.getCached(uidset));   //hide uncaching?
	}
	
	final private static String NOTERMGENID = "Publication has no doi or generated id; db id = %s";

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

	/**
	 * may not be needed, but if we ever need to reopen a database
	 */
	static void flushCache(){
		cache.clear();
	}

	public static boolean isCached(int id){
		return cache.containsKey(id);
	}

	public static int cacheSize(){
		return cache.size();
	}

	public static PublicationBean getCached(int id){
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
			cache.put(this.getId(), this);
		}
	}

	public int getuidset() {
		// TODO Auto-generated method stub
		return uidset;
	}
	
}
