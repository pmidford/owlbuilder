package org.arachb.arachadmin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;



public class DBConnection implements AbstractConnection{

	//properties file names
	static final String DEFAULTPROPERTIESFILE = "Connection.properties";

	static final String TESTPROPERTIESFILE = "TestConnection.properties";


	//Query strings

	/* may never need to load singleton publications, but in case */
	static final String PUBLICATIONROWQUERY = 
			"SELECT id, publication_type,dispensation," +
					"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
					"source_publication, volume,issue,serial_identifier,page_range,publication_date," +
					"publication_year,doi,generated_id,curation_status,curation_update,uidset " +
					"FROM publication where publication.id = ?";

	static final String PUBLICATIONSETQUERY = "SELECT id FROM publication";

	/* keep this, because it may be faster to process the whole publication table */
	static final String PUBLICATIONTABLEQUERY = 
			"SELECT id, publication_type,dispensation," +
					"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
					"source_publication,volume,issue,serial_identifier,page_range,publication_date," +
					"publication_year,doi,generated_id,curation_status, curation_update, uidset " +
					"FROM publication";

	static final String PUBLICATIONUPDATESTATEMENT = 
			"UPDATE publication SET generated_id = ? WHERE id = ?";


	/* arachadmin has the union of all support ontologies - DO NOT want to load the whole
	 * term table, stay with singletons and caching.
	 */
	static final String TERMROWQUERY = 
			"SELECT id,source_id,domain,authority, " +
					"label,generated_id,comment,uidset " +
					"FROM term where term.id = ?";

	static final String TERMSETQUERY = "SELECT id FROM term";

	static final String TERMUPDATESTATEMENT = 
			"UPDATE term SET generated_id = ? WHERE id = ?";

	static final String TERMIRIQUERY = 
			"SELECT id,source_id,generated_id FROM term where term.id = ?";

	//TODO needs review
	static final String PRIMARYPARTICIPANTQUERY =
			"SELECT part.id, part.taxon, part.substrate, part.anatomy, " +
					"part.quantification, part.generated_id, part.publication_taxon, " +
					"part.label, part.publication_anatomy, part.publication_substrate, " +
					"taxon.source_id, taxon.generated_id, " +
					"substrate.source_id, substrate.generated_id, " +
					"anatomy.source_id, anatomy.generated_id " +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) " +
					"LEFT JOIN term AS taxon ON (part.taxon = taxon.id) " +
					"LEFT JOIN term AS substrate ON (part.substrate = substrate.id) " +
					"LEFT JOIN term AS anatomy ON (part.anatomy = anatomy.id) " +
					"WHERE p2c.claim = ? AND p2c.participant_index = 1";


	// Cache all the participants for a particular claim
	static final String PARTICIPANTSINCLAIMSETQUERY = 
			"SELECT part.id " +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) " +
					"WHERE p2c.claim = ?";

	static final String PARTICIPANTSINCLAIMTABLEQUERY =
			"SELECT part.id, part.quantification, part.label, " +
					"p2c.property, part.publication_taxon, part.publication_anatomy, " +
					"part.publication_substrate, part.head_element " +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) " +
					"WHERE p2c.claim = ?";

	// should we just cache all the participants at once?
	static final String PARTICIPANTTABLEQUERY = 
			"SELECT part.id, part.quantification, part.label," +
					"p2c.property, part.publication_taxon, part.publication_anatomy, " +
					"part.publication_substrate, part.head_element " +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) ";

	// id's of participants in claim
	static final String PARTICIPANTCLAIMSETQUERY = 
			"SELECT part.id" +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) " +
					"WHERE p2c.claim = ?";

	// singleton participant
	static final String PARTICIPANTROWQUERY = 
			"SELECT part.id, part.quantification, part.label, " +
					"p2c.property, part.publication_taxon, part.publication_anatomy, " +
					"part.publication_substrate, part.head_element " +
					"FROM participant AS part " +
					"JOIN participant2claim AS p2c ON (part.id = p2c.participant) " +
					"WHERE part.id = ?";


	static final String PARTICIPANTUPDATESTATEMENT = "";

	static final String PELEMENTSETFROMPARTICIPANTQUERY =
			"SELECT id FROM participant_element WHERE participant = ?";

	static final String PELEMENTROWQUERY =
			"SELECT ele.id, ele.type, ele.participant, p2t.term, p2i.individual " +
					"FROM participant_element as ele " +
					"LEFT JOIN pelement2term as p2t ON (p2t.element = ele.id) " +
					"LEFT JOIN pelement2individual as p2i ON (p2i.element = ele.id) " +
					"WHERE ele.id = ?";

	// set of all pelement ids
	static final String PELEMENTSETQUERY = "SELECT ele.id FROM participant_element AS ele";


	// table of all pelements
	static final String PELEMENTTABLEQUERY =
			"SELECT ele.id, ele.type, ele.participant, p2t.term, p2i.individual " +
					"FROM participant_element as ele " +
					"LEFT JOIN pelement2term as p2t ON (p2t.element = ele.id) " +
					"LEFT JOIN pelement2individual as p2i ON (p2i.element = ele.id) " +
					"WHERE ele.participant = ?";

	static final String PELEMENTTERMQUERY =
			"SELECT p2t.term FROM pelement2term as p2t WHERE element = ?";


	static final String PELEMENTINDIVIDUALQUERY =
			"SELECT p2i.individual FROM pelement2individual as p2i WHERE element = ?";

	static final String PELEMENTPARENTSQUERY =
			"SELECT link.child,link.parent,link.property FROM participant_link as link " +
					"WHERE link.child = ?";

	static final String PELEMENTCHILDRENQUERY =
			"SELECT link.child,link.parent,link.property FROM participant_link as link " +
					"WHERE link.parent = ?";

	static final String CLAIMROWQUERY =
			"SELECT c.id, c.publication, c.publication_behavior, c.behavior_term, " +
					"c.evidence, c.generated_id, pub.doi, " +
					"pub.generated_id, behavior.source_id, behavior.generated_id, " +
					"c.uidset " +
					"FROM claim AS c " +
					"LEFT JOIN publication AS pub ON (c.publication = pub.id) " +
					"LEFT JOIN term AS behavior ON (c.behavior_term = behavior.id) " +
					"LEFT JOIN term AS evidence ON (c.evidence = evidence.id) " +
					"WHERE c.id = ?";

	static final String CLAIMTABLEQUERY =
			"SELECT c.id, c.publication, c.publication_behavior, c.behavior_term, " +
					"c.evidence, c.generated_id, pub.doi, " +
					"pub.generated_id, behavior.source_id, behavior.generated_id, " +
					"c.uidset " +
					"FROM claim AS c " +
					"LEFT JOIN publication AS pub ON (c.publication = pub.id) " +
					"LEFT JOIN term AS behavior ON (c.behavior_term = behavior.id) " +
					"LEFT JOIN term AS evidence ON (c.evidence = evidence.id) ";


	static final String CLAIMUPDATESTATEMENT = 
			"UPDATE claim SET generated_id = ? WHERE id = ?";


	static final String TAXONROWQUERY = 
			"SELECT t.id,t.name,t.author, " +
					"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
					"t.parent_term, t.merged, t.merge_status, parent_uids.ref_id, t.uidset " +
					"FROM taxon AS t where taxon.id = ? " +
					"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) " +
					"LEFT JOIN uidset AS parent_uids ON (parent_record.uidset = parent_uids.id)";

	static final String TAXONTABLEQUERY = 
			"SELECT t.id,t.name,t.author, " +
					"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
					"t.parent_term,t.merged,t.merge_status, parent_uids.ref_id, t.uidset " +
					"FROM taxon AS t " +
					"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) " +
					"LEFT JOIN uidset AS parent_uids ON (parent_record.uidset = parent_uids.id)";
	
	static final String TAXONUPDATESTATEMENT = "";

	static final String INDIVIDUALROWQUERY =
			"SELECT i.id, i.source_id, i.generated_id, i.label, i.term, i.uidset " +
					"FROM individual AS i WHERE i.id = ?";

	static final String INDIVIDUALTABLEQUERY =
			"SELECT i.id, i.source_id, i.generated_id, i.label, i.term, i.uidset " +
					"FROM individual AS i";

	static final String INDIVIDUALNARRATIVESQUERY =
			"SELECT i2n.narrative FROM individual2narrative as i2n " +
					"WHERE i2n.individual = ?";


	static final String INDIVIDUALUPDATESTATEMENT =
			"UPDATE individual SET generated_id = ? WHERE id = ?";	

	static final String NARRATIVEROWQUERY =
			"SELECT n.id, n.publication, n.label, n.description, n.generated_id, n.uidset " +
					"FROM narrative AS n WHERE n.id = ?";

	static final String NARRATIVEUPDATESTATEMENT =
			"UPDATE narrative SET generated_id = ? WHERE id = ?";		

	static final String PROPERTYROWQUERY = 
			"SELECT p.id, p.source_id, p.authority, p.label, p.generated_id, p.comment " +
					"FROM property AS p WHERE p.id = ?";

	static final String PROPERTYROWBYSOURCEIDQUERY = 
			"SELECT p.id, p.source_id, p.authority, p.label, p.generated_id, p.comment " +
					"FROM property AS p WHERE p.source_id = ?";

	static final String UIDSETROWQUERY = 
			"SELECT u.id, u,source_id, u.generated_id, u.ref_id " +
			        "FROM uidset AS u where u.source_id = ?";

	static final String UIDSETTABLEQUERY = 
			"SELECT u.id, u.source_id, u.generated_id, u.ref_id " +
			        "FROM uidset AS u";
	
	static final String UIDSETLASTGENID = 
			"SELECT MAX(generated_id) FROM uidset";

	static final String UIDSETUPDATEGENIDSTATEMENT = 
			"UPDATE uidset SET generated_id = ? WHERE id = ?";
	
	static final String UIDSETUPDATEREFIDSTATEMENT = 
			"UPDATE uidset SET ref_id = ? WHERE id = ?";
	
	
	
	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;

	private IRIManager irimanager;

	private final HashMap<Integer,String>id2domain = new HashMap<Integer,String>();

	private final HashMap<String,Integer>domain2id = new HashMap<String,Integer>();

	private static Logger log = Logger.getLogger(DBConnection.class);

	private static final String DRIVERSPEC = "com.mysql.jdbc.Driver";

	private static final String CONNECTIONSPEC = "jdbc:mysql://%s/%s";

	/**
	 * 
	 * @return whether the connection succeeded
	 * resource warning suppressed because the connection remains open until the end
	 */
	@SuppressWarnings("resource")
	public static boolean testConnection(){
		Connection c = null;
		final String connectionSpec = DEFAULTPROPERTIESFILE;
		try {
			final Properties properties = new Properties();
			properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
			Class.forName(DRIVERSPEC);
			final String host = properties.getProperty("host");
			final String db = properties.getProperty("dbname");
			final String user = properties.getProperty("user");
			final String password = properties.getProperty("password");
			c = DriverManager.getConnection(String.format(CONNECTIONSPEC,host,db),user,password);
		}
		catch (Exception e){
			log.error("DBConnection setup failed, encountered:",e);
			if (c != null){
				try {
					c.close();
				} catch (SQLException e1) {
					return false;
				}
			}
			return false;
		}
		finally{
			try {
				if (c != null){
					c.close();
				}
			}
			catch (Exception e){
				// log.error("Error while closing test connection" + e);
				return false;
			}
		}
		return true;
	}

	private DBConnection(String connectionSpec) throws Exception{
		final Properties properties = new Properties();
		properties.load(DBConnection.class.getClassLoader().getResourceAsStream(connectionSpec));
		Class.forName("com.mysql.jdbc.Driver");
		final String host = properties.getProperty("host");
		final String db = properties.getProperty("dbname");
		final String user = properties.getProperty("user");
		final String password = properties.getProperty("password");
		c = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",host,db),user,password);
		initDomains(c);
		irimanager = new IRIManager(this);
	}


	private final String DOMAINSQUERY = "SELECT id,name FROM domain";

	@SuppressWarnings("resource")
	private void initDomains(Connection c) throws SQLException{
		final Statement domainStatement = c.createStatement();
		ResultSet domainResult = null;
		try{
			domainResult = domainStatement.executeQuery(DOMAINSQUERY); 
			if (domainResult.next()){
				int id = domainResult.getInt("id");
				String label = domainResult.getString("name");
				id2domain.put(id, label);
				domain2id.put(label, id);
			}
		}
		finally{
			if (domainResult != null){
				domainResult.close();
			}
			domainStatement.close();
		}
	}

	/**
	 * 
	 * @return connection to default database identified in properties file
	 * @throws Exception
	 */
	public static DBConnection getDBConnection() throws Exception{
		return new DBConnection(DEFAULTPROPERTIESFILE);
	}


	/**
	 * 
	 * @return connection to a database of test data
	 * @throws Exception
	 */
	public static DBConnection getTestConnection() throws Exception{
		return new DBConnection(TESTPROPERTIESFILE);
	}

	public static AbstractConnection getMockConnection() throws Exception{
		return new MockConnection();
	}


	public Connection getConnection(){
		return c;
	}

	public List<String> privateIDStrings() throws SQLException{
		List<String> result = new ArrayList<String>();
		return result;
	}

	public PublicationBean getPublication(int id) throws SQLException{
		if (PublicationBean.isCached(id)){
			return PublicationBean.getCached(id);
		}
		final PreparedStatement publicationStatement = c.prepareStatement(PUBLICATIONROWQUERY);
		try{
			publicationStatement.setInt(1, id);
			final AbstractResults publicationResults = executeAndAbstract(publicationStatement);
			PublicationBean results;
			if (publicationResults.next()){
				results = new PublicationBean();
				results.fill(publicationResults);
				results.cache();
			}
			else {
				results = null;
			}
			publicationResults.close();
			return results;
		}
		finally{
			publicationStatement.close();
		}
	}

	private AbstractResults executeAndAbstract(PreparedStatement p) throws SQLException{
		return new DBResults(p.executeQuery());
	}

	public Set<Integer> getPublicationSet() throws SQLException{
		final Set<Integer> result = new HashSet<Integer>();
		Statement allpubStatement = c.createStatement();
		try{
			ResultSet rawResults = allpubStatement.executeQuery(PUBLICATIONSETQUERY);
			while (rawResults.next()){
				result.add(rawResults.getInt(1));
			}
			rawResults.close();
			return result;
		}
		finally{
			allpubStatement.close();
		}
	}

	public Set<PublicationBean> getPublicationTable() throws SQLException{
		final Set<PublicationBean> result = new HashSet<PublicationBean>();
		Statement allpubStatement = c.createStatement();
		try{
			ResultSet rawResults = allpubStatement.executeQuery(PUBLICATIONTABLEQUERY);
			AbstractResults publicationResults = new DBResults(rawResults);
			while (publicationResults.next()){
				PublicationBean pub = new PublicationBean();
				pub.fill(publicationResults);
				pub.cache();
				result.add(pub);
			}
			rawResults.close();
			return result;
		}
		finally{
			allpubStatement.close();
		}
	}


	final static private String UPDATEPUBLICATIONFAIL = 
			"publication (%s) update failed; row count = %d";


	public TermBean getTerm(int id) throws SQLException{
		if (TermBean.isCached(id)){
			return TermBean.getCached(id);
		}
		final PreparedStatement termStatement = c.prepareStatement(TERMROWQUERY);
		try{
			termStatement.setInt(1, id);
			final ResultSet termSet = termStatement.executeQuery();
			TermBean result;
			if (termSet.next()){
				result = new TermBean();
				final AbstractResults termResults = new DBResults(termSet);
				result.fill(termResults);
				result.checkIRIString(irimanager);
				result.cache();
			}
			else{
				if (termStatement.getWarnings() != null){
					log.info("statement warning is " + termStatement.getWarnings().getMessage());
				}
				if (termSet.getWarnings() != null){
					log.info("result set warning is " + termSet.getWarnings().getMessage());
				}
				termSet.close();
				throw new RuntimeException("Term row query with id " + id + " failed to return a result");
			}
			termSet.close();
			return result;
		}
		finally{
			termStatement.close();
		}
	}


	public ClaimBean getClaim(int id) throws Exception{
		if (ClaimBean.isCached(id)){
			return ClaimBean.getCached(id);
		}
		final PreparedStatement claimStatement = c.prepareStatement(CLAIMROWQUERY);
		try{
			claimStatement.setInt(1, id);
			final ResultSet r = claimStatement.executeQuery();
			final AbstractResults claimSet = new DBResults(r);
			ClaimBean result;
			if (claimSet.next()){
				result = new ClaimBean();
				result.fill(claimSet);
				result.cache();
			}
			else{
				result = null;
			}
			r.close();
			return result;
		}
		finally{
			claimStatement.close();
		}
	}


	public Set<ClaimBean> getClaimTable() throws Exception {
		final Statement allClaimStatement = c.createStatement();
		try{
			final Set<ClaimBean> result = new HashSet<ClaimBean>();
			final ResultSet r = allClaimStatement.executeQuery(CLAIMTABLEQUERY);
			final AbstractResults claimSet = new DBResults(r);
			while (claimSet.next()){
				ClaimBean cb = new ClaimBean();
				cb.fill(claimSet);
				result.add(cb);
				if (!ClaimBean.isCached(cb.getId())){
					cb.cache();
				}
			}
			r.close();
			return result;
		}
		finally {
			allClaimStatement.close();
		}
	}

	public void updateClaim(ClaimBean cl) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(CLAIMUPDATESTATEMENT);
		try{
			updateStatement.setString(1, cl.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2,cl.getId());
			int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error("entity update failed; row count = " + count);
			}
			else{
				cl.updatecache();
			}
		}
		finally{
			updateStatement.close();
		}
	}

	/* Never called outside of tests? */
	public Set<Integer> getParticipantSet(int claimId) throws Exception {
		final Set<Integer> result = new HashSet<Integer>();
		final PreparedStatement participantsStatement =
				c.prepareStatement(PARTICIPANTSINCLAIMSETQUERY);
		try{
			participantsStatement.setInt(1, claimId);
			final ResultSet r = participantsStatement.executeQuery();
			final AbstractResults participantSet = new DBResults(r);
			while (participantSet.next()){
				result.add(participantSet.getInt(1));
			}
			r.close();
			return result;
		}
		finally{
			participantsStatement.close();
		}
	}


	@Override
	public ParticipantBean getParticipant(int id) throws Exception {
		if (ParticipantBean.isCached(id)){
			return ParticipantBean.getCached(id);
		}
		final PreparedStatement participantStatement = c.prepareStatement(PARTICIPANTROWQUERY);
		ParticipantBean result;
		try{
			participantStatement.setInt(1, id);
			final AbstractResults participantSet = executeAndAbstract(participantStatement);
			if (participantSet.next()){
				result = new ParticipantBean();
				result.fill(participantSet);
				result.cache();
			}
			else{
				result = null;
			}
			participantSet.close();
		}
		finally{
			participantStatement.close();
		}
		if (result != null) {
			final Set<Integer> elements = new HashSet<Integer>();
			final PreparedStatement participantsStatement =
					c.prepareStatement(PELEMENTSETFROMPARTICIPANTQUERY);
			try{
				participantsStatement.setInt(1, result.getId());
				final ResultSet r = participantsStatement.executeQuery();
				while (r.next()){
					elements.add(r.getInt(1));
				}
				r.close();
				result.setElements(elements);
				log.info("Participant " + result.getId() + " loaded " + elements.size() + " element ids");
				return result;
			}
			finally{
				participantsStatement.close();
			}
		}
		return result;
	}


	@Override
	public Set<ParticipantBean> getParticipantTable(int claimId) throws Exception{
		final Set<ParticipantBean> result = new HashSet<ParticipantBean>();
		final PreparedStatement participantsStatement =
				c.prepareStatement(PARTICIPANTSINCLAIMTABLEQUERY);
		try{
			participantsStatement.setInt(1, claimId);
			final ResultSet r = participantsStatement.executeQuery();
			final AbstractResults participantSet = new DBResults(r);
			while (participantSet.next()){
				ParticipantBean pb = new ParticipantBean();
				pb.fill(participantSet);
				if (!ParticipantBean.isCached(pb.getId())){
					pb.cache();
				}
				result.add(pb);
			}
			r.close();
		}
		finally{
			participantsStatement.close();
		}
		if (!result.isEmpty()) {
			log.info("adding elements to " + result.size() + " participants");
			final PreparedStatement elementsStatement =
					c.prepareStatement(PELEMENTSETFROMPARTICIPANTQUERY);
			try{
				for (ParticipantBean pb : result){

					log.info("About to load elements for participant " + pb.getId());
					final Set<Integer> elements = new HashSet<Integer>();
					elementsStatement.setInt(1, pb.getId());
					final ResultSet r = elementsStatement.executeQuery();
					while (r.next()){
						elements.add(r.getInt(1));
					}
					r.close();
					pb.setElements(elements);
					log.info("Participant " + pb.getId() + " loaded " + elements.size() + " element ids");
				}
			}
			finally{
				elementsStatement.close();
			}
			log.info("checking and adding head elements if not found");
			for (ParticipantBean pb : result){
				if (PElementBean.isCached(pb.getHeadElement())){
					log.warn("why is head pelement " + pb.getHeadElement() + " already cached");
				}
				else {
					getPElement(pb.getHeadElement()).cache();
				}
			}
			log.info("adding properties to " + result.size() + " participants");
			for (ParticipantBean pb : result){
				log.info("About to add property for participant " + pb.getId());
				getProperty(pb.getProperty()).cache();
			}
			return result;
		}
		return result;
	}


	@Override
	public void updateNamedEntity(UpdateableBean b) throws SQLException{
		b.updateDB(this);
	}


	@Override
	public Set<PElementBean> getPElementTable(ParticipantBean p) throws Exception {
		final PreparedStatement pElementStatement = c.prepareStatement(PELEMENTTABLEQUERY);
		final Set<PElementBean> result = new HashSet<PElementBean>();
		try{
			pElementStatement.setInt(1,p.getId());
			final ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementsResults = new DBResults(rawResults);
			while (pElementsResults.next()){
				PElementBean bean = new PElementBean();
				bean.fill(pElementsResults);
				if (!PElementBean.isCached(bean.getId())){
					bean.cache();
				}
				if (bean.getTermId().intValue() != 0){
					if (!TermBean.isCached(bean.getTermId())){
						getTerm(bean.getTermId()).cache();
					}
				}
				else if (bean.getIndividualId().intValue() != 0){
					if (!IndividualBean.isCached(bean.getIndividualId())){
						getIndividual(bean.getIndividualId()).cache();
					}
				}
				result.add(bean);
			}
			rawResults.close();
		}
		finally{
			pElementStatement.close();
		}
		return result;
	}

	@Override
	public Set<Integer> getPElementSet(ParticipantBean p) throws Exception {
		final Set<Integer> result = new HashSet<Integer>();
		final PreparedStatement participantsStatement =
				c.prepareStatement(PELEMENTSETQUERY);
		try{
			participantsStatement.setInt(1, p.getId());
			final ResultSet r = participantsStatement.executeQuery();
			while (r.next()){
				result.add(r.getInt(1));
			}
			r.close();
			return result;
		}
		finally{
			participantsStatement.close();
		}
	}

	//TODO this needs test code and refactoring
	@Override
	public PElementBean getPElement(int id) throws Exception{
		final PreparedStatement pElementStatement = c.prepareStatement(PELEMENTROWQUERY);
		PElementBean pb;
		try{
			pElementStatement.setInt(1, id);
			final ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementResults = new DBResults(rawResults);
			if (pElementResults.next()){
				pb = new PElementBean();
				pb.fill(pElementResults);
				if (!PElementBean.isCached(pb.getId())){
					pb.cache();
				}
				if (pb.getTermId() > 0){
					if (!TermBean.isCached(pb.getTermId())){
						getTerm(pb.getTermId()).cache();
					}
				}
				else if (pb.getIndividualId() > 0){
					if (!IndividualBean.isCached(pb.getIndividualId())){
						getIndividual(pb.getIndividualId()).cache();
					}
				}
				else {
					rawResults.close();
					throw new RuntimeException("PElement bean with id " + id + " has neither term or individual");
				}
				rawResults.close();
			}
			else{
				pb = null;
			}
			rawResults.close();
		}
		finally{
			pElementStatement.close();
		}
		if (pb != null){
			final PreparedStatement pElementParentsStatement = c.prepareStatement(PELEMENTPARENTSQUERY);
			try{
				pElementParentsStatement.setInt(1, pb.getId());
				ResultSet parentResults = pElementParentsStatement.executeQuery();
				while (parentResults.next()){
					int parent = parentResults.getInt(2);  //Constant for this?
					int property = parentResults.getInt(3);  //Constant for this?
					pb.addParent(parent,property);
				}
				parentResults.close();
			}
			finally{
				pElementParentsStatement.close();
			}
			final PreparedStatement pElementChildStatement = c.prepareStatement(PELEMENTCHILDRENQUERY);
			try{
				pElementChildStatement.setInt(1, pb.getId());
				ResultSet childResults = pElementChildStatement.executeQuery();
				while (childResults.next()){
					int child = childResults.getInt(1);  //Constant for this?
					int property = childResults.getInt(3);  //Constant for this?
					pb.addChild(child,property);
				}
				childResults.close();
			}
			finally{
				pElementChildStatement.close();
			}
		}			
		return pb;
	}



	public TaxonBean getTaxonRow(int id) throws SQLException{
		if (TaxonBean.isCached(id)){
			return TaxonBean.getCached(id);
		}
		final PreparedStatement taxonStatement = c.prepareStatement(TAXONROWQUERY);
		try{
			taxonStatement.setInt(1, id);
			AbstractResults taxonResults = executeAndAbstract(taxonStatement);
			TaxonBean result;
			if (taxonResults.next()){
				result = new TaxonBean();
				result.fill(taxonResults);
				String usable = result.checkIRIString(irimanager);
				if (usable == null){
					throw new RuntimeException("No usable IRI for curator added taxon: " + result.getId());
				}
				result.cache();
			}
			else{
				result = null;
			}
			taxonResults.close();
			return result;
		}
		finally{
			taxonStatement.close();
		}
	}


	@Override
	public Set<TaxonBean> getTaxonTable() throws SQLException {
		final Set<TaxonBean> result = new HashSet<TaxonBean>();
		final Statement taxonStatement = c.createStatement();
		try{
			final ResultSet r = taxonStatement.executeQuery(TAXONTABLEQUERY);
			final AbstractResults TaxonSet = new DBResults(r);
			while (TaxonSet.next()){
				TaxonBean tb = new TaxonBean();
				tb.fill(TaxonSet);
				if (!TaxonBean.isCached(tb.getId())){
					tb.cache();
				}
				result.add(tb);
			}
			r.close();
			return result;
		}
		finally{
			taxonStatement.close();
		}
	}



	public void updateTaxon(TaxonBean t) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(TAXONUPDATESTATEMENT);
		try{
			updateStatement.setString(1, t.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2, t.getId());
			int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error("entity update failed; row count = " + count);
			}
			else{
				t.updatecache();
			}
		}
		finally{
			updateStatement.close();
		}
	}


	public IndividualBean getIndividual(int id) throws SQLException{
		if (IndividualBean.isCached(id)){
			return IndividualBean.getCached(id);
		}
		IndividualBean ib;
		final PreparedStatement individualStatement = c.prepareStatement(INDIVIDUALROWQUERY);
		try{
			individualStatement.setInt(1, id);
			final ResultSet individualSet = individualStatement.executeQuery();
			if (individualSet.next()){
				ib = new IndividualBean();
				ib.fill(new DBResults(individualSet));
				ib.checkIRIString(irimanager);
				if (!TermBean.isCached(ib.getTerm())){
					log.info("Term id = " + ib.getTerm());
					getTerm(ib.getTerm()).cache();
				}
				ib.cache();
			}
			else{
				ib = null;
			}
			individualSet.close();
		}
		finally{
			individualStatement.close();
		}
		if (ib != null){
			final PreparedStatement individualNarrativesStatement = c.prepareStatement(INDIVIDUALNARRATIVESQUERY);
			try{
				individualNarrativesStatement.setInt(1, ib.getId());
				ResultSet narratives = individualNarrativesStatement.executeQuery();
				while (narratives.next()){
					int n = narratives.getInt(1);
					ib.addNarrative(n);
					if (!NarrativeBean.isCached(n)){
						getNarrative(n).cache();
					}
				}
				narratives.close();
			}
			finally{
				individualNarrativesStatement.close();
			}
		}			
		return ib;
	}

	public void updateIndividual(IndividualBean ib) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(INDIVIDUALUPDATESTATEMENT);
		try{
			updateStatement.setString(1, ib.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2,ib.getId());
			final int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error("individual update failed; row count = " + count);
			}
		}
		finally{
			updateStatement.close();
		}
	}

	@Override
	public NarrativeBean getNarrative(int nId) throws SQLException{
		final PreparedStatement narrativeStatement = c.prepareStatement(NARRATIVEROWQUERY);
		NarrativeBean result;
		try{
			narrativeStatement.setInt(1, nId);
			final ResultSet narrativeSet = narrativeStatement.executeQuery();
			if (narrativeSet.next()){
				result = new NarrativeBean();
				final AbstractResults narrativeResults = new DBResults(narrativeSet);
				result.fill(narrativeResults);
			}
			else{
				result = null;
			}
			narrativeSet.close();
		}
		finally{
			narrativeStatement.close();
		}
		if (result != null) {
			getPublication(result.getPublicationId()).cache();
		}
		return result;
	}



	@Override
	public PropertyBean getProperty(int id) throws Exception{
		if (PropertyBean.isCached(id)){
			return PropertyBean.getCached(id);
		}
		final PreparedStatement propertyStatement = c.prepareStatement(PROPERTYROWQUERY);
		try{
			propertyStatement.setInt(1, id);
			ResultSet propertySet = propertyStatement.executeQuery();
			AbstractResults propertyResults = new DBResults(propertySet);
			PropertyBean result;
			if (propertyResults.next()){
				result = new PropertyBean();
				result.fill(propertyResults);
				result.cache();
			}
			else{
				result = null;
			}
			propertySet.close();
			return result;
		}
		finally{
			propertyStatement.close();
		}
	}

	@Override
	public PropertyBean getPropertyFromSourceId(String uid) throws Exception{
		if (PropertyBean.isSourceIdCached(uid)){
			return PropertyBean.getSourceIdCached(uid);
		}
		final PreparedStatement propertyStatement = c.prepareStatement(PROPERTYROWBYSOURCEIDQUERY);
		try{
			propertyStatement.setString(1, uid);
			ResultSet propertySet = propertyStatement.executeQuery();
			AbstractResults propertyResults = new DBResults(propertySet);
			PropertyBean result;
			if (propertyResults.next()){
				result = new PropertyBean();
				result.fill(propertyResults);
				result.cache();
			}
			else{
				result = null;
			}
			propertySet.close();
			return result;
		}
		finally{
			propertyStatement.close();
		}
	}




	private final String ONTOLOGYSOURCEQUERY = "SELECT source_url,domain FROM ontology_source";

	@Override
	public Map<String,String>loadImportSourceMap() throws Exception{
		final Map<String,String> result = new HashMap<String,String>();
		final Statement sourceOntStatement = c.createStatement();
		try{
			ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYSOURCEQUERY);
			while (sourceOntSet.next()){
				final int domain_id = sourceOntSet.getInt("domain");
				final String domain = domainName(domain_id);
				final String source = sourceOntSet.getString("source_url");
				result.put(source,domain);
			}
			sourceOntStatement.close();
			sourceOntSet.close();
			return result;
		}
		finally{
			sourceOntStatement.close();
		}
	}

	private final String ONTOLOGYNAMEQUERY = "SELECT source_url,name FROM ontology_source";

	@Override
	public Map<String,String>loadOntologyNamesForLoading() throws SQLException{
		final Map<String,String> result = new HashMap<String,String>();
		final Statement sourceOntStatement = c.createStatement();
		try{
			ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYNAMEQUERY);
			while (sourceOntSet.next()){
				final String source = sourceOntSet.getString("source_url");
				final String name = sourceOntSet.getString("name");
				result.put(source,name);
			}
			sourceOntStatement.close();
			sourceOntSet.close();
			return result;
		}
		finally{
			sourceOntStatement.close();
		}
	}


	public String domainName(int domain_id){
		return id2domain.get(domain_id);
	}

	public int domainId(String name){
		return domain2id.get(name);
	}



	@Override
	public IRIManager getIRIManager() {
		return irimanager;
	}

	public void close() throws Exception {
		c.close();
	}

	
	@Override
	public UidSet getUidSet(int setId) throws Exception {
		if (UidSet.isCached(setId)){
			return UidSet.getCached(setId);
		}
		final PreparedStatement uidStatement = c.prepareStatement(UIDSETROWQUERY);
		try{
			uidStatement.setInt(1, setId);
			AbstractResults uidResults = executeAndAbstract(uidStatement);
			UidSet result;
			if (uidResults.next()){
				result = new UidSet();
				result.fill(uidResults);
				String usable = result.checkIRIString(irimanager);
				if (usable == null){
					throw new RuntimeException("No usable IRI for uidset: " + result.getId());
				}
				result.cache();
			}
			else{
				result = null;
			}
			uidResults.close();
			return result;
		}
		finally{
			uidStatement.close();
		}
	}

	@Override
	public Set<UidSet> getUidSetTable() throws Exception {
		final Set<UidSet> result = new HashSet<>();
		final Statement uidStatement = c.createStatement();
		try{
			final ResultSet r = uidStatement.executeQuery(UIDSETTABLEQUERY);
			final AbstractResults uidSets = new DBResults(r);
			while (uidSets.next()){
				UidSet u = new UidSet();
				u.fill(uidSets);
				if (!UidSet.isCached(u.getId())){
					u.cache();
				}
				result.add(u);
			}
			r.close();
			return result;
		}
		finally{
			uidStatement.close();
		}
	}

	
	@Override
	public String getUidSetLastGenId() throws Exception {
		String result = null;
		final Statement countStatement = c.createStatement();
		try{
			ResultSet r = countStatement.executeQuery(UIDSETLASTGENID);
			if (r.next()){
				result = r.getString(1);
			}
			r.close();
			return result;
		}
		finally{
			countStatement.close();
		}
	}

	
	final static String UIDSETUPDATEFAILURE = "Uidset %s update failed; row count = %d";
	@Override
	public void updateUidSet(UidSet s) throws SQLException {
		final PreparedStatement updateGenIdStatement = 
				c.prepareStatement(UIDSETUPDATEGENIDSTATEMENT);
		final PreparedStatement updateRefIdStatement = 
				c.prepareStatement(UIDSETUPDATEREFIDSTATEMENT);
		try{
			updateGenIdStatement.setString(1, s.getGeneratedId()); 
			updateGenIdStatement.setInt(2,s.getId());
			int count = updateGenIdStatement.executeUpdate();
			if (count != 1){
				throw new RuntimeException(String.format(UIDSETUPDATEFAILURE,"generated_id",count)); 
			}
			if (s.getSourceId() == null){
				updateRefIdStatement.setString(1, s.getRefId()); 
				updateRefIdStatement.setInt(2,s.getId());
				count = updateGenIdStatement.executeUpdate();
				if (count != 1){
					throw new RuntimeException(String.format(UIDSETUPDATEFAILURE,"reference_id",count)); 
				}
			}
			s.updatecache();
		}
		finally{
			updateRefIdStatement.close();
			updateGenIdStatement.close();
		}
	}





}

