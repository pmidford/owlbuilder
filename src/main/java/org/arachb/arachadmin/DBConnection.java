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
	
	static final String PUBLICATIONROWQUERY = 
			"SELECT id, publication_type,dispensation," +
					"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
					"source_publication, volume,issue,serial_identifier,page_range,publication_date," +
					"publication_year,doi,generated_id,curation_status,curation_update " +
					"FROM publication where publication.id = ?";

	static final String PUBLICATIONTABLEQUERY = 
			"SELECT id, publication_type,dispensation," +
					"downloaded,reviewed,title,alternate_title,author_list,editor_list," +
					"source_publication,volume,issue,serial_identifier,page_range,publication_date," +
					"publication_year,doi,generated_id,curation_status, curation_update " +
					"FROM publication";
	
	static final String PUBLICATIONUPDATESTATEMENT = 
			"UPDATE publication SET generated_id = ? WHERE id = ?";
				

	static final String TERMROWQUERY = 
			"SELECT id,source_id,domain," +
					"authority,label,generated_id,comment " +
					"FROM term where term.id = ?";
		
	static final String TERMTABLEQUERY = 
			"SELECT id,source_id,domain," +
					"authority,label,generated_id,comment " +
					"FROM term";
			
	static final String TERMUPDATESTATEMENT = 
			"UPDATE term SET generated_id = ? WHERE id = ?";

	static final String TERMIRIQUERY = 
			"SELECT id,source_id,generated_id FROM term where term.id = ?";
		
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
	
	static final String PARTICIPANTSQUERY = 
			"SELECT part.id, part.quantification, part.label, part.generated_id, " +
					"p2c.property, part.publication_taxon, part.publication_anatomy, " +
					"part.publication_substrate, part.head_element " +
					"FROM participant2claim as p2c " + 
					"JOIN participant AS part ON (p2c.participant = part.id) " +
					"WHERE p2c.claim = ?";
	
	static final String PARTICIPANTUPDATESTATEMENT = "";
	
	static final String PELEMENTSFROMPARTICIPANTQUERY =
			"SELECT id,type,participant FROM participant_element WHERE participant = ?";

	static final String PELEMENTQUERY =
			"SELECT ele.id, ele.type, ele.participant, p2t.term, p2i.individual " +
			        "FROM participant_element as ele " +
				    "LEFT JOIN pelement2term as p2t ON (p2t.element = ele.id) " +
					"LEFT JOIN pelement2individual as p2i ON (p2i.element = ele.id) " +
				    "WHERE ele.id = ?";
	
	
	static final String PELEMENTTERMQUERY =
			"SELECT p2t.term FROM pelement2term as p2t WHERE element = ?";
	
	
	static final String PELEMENTINDIVIDUALQUERY =
			"SELECT p2i.individual FROM pelement2individual as p2i WHERE element = ?";

	static final String PELEMENTPARENTSQUERY =
			"SELECT link.parent,link.property FROM participant_link as link " +
			        "WHERE link.child = ?";

	static final String PELEMENTCHILDRENQUERY =
			"SELECT link.child,link.property FROM participant_link as link " +
			        "WHERE link.parent = ?";
	
	
	static final String CLAIMROWQUERY =
			"SELECT c.id, c.publication, c.publication_behavior, c.behavior_term, " +
					"c.evidence, c.generated_id, pub.doi, " +
					"pub.generated_id, behavior.source_id, behavior.generated_id " +
				    "FROM claim AS c " +
					"LEFT JOIN publication AS pub ON (c.publication = pub.id) " +
					"LEFT JOIN term AS behavior ON (c.behavior_term = behavior.id) " +
					"LEFT JOIN term AS evidence ON (c.evidence = evidence.id) " +
				    "WHERE c.id = ?";

	static final String CLAIMTABLEQUERY =
			"SELECT c.id, c.publication, c.publication_behavior, c.behavior_term, " +
					"c.evidence, c.generated_id, pub.doi, " +
					"pub.generated_id, behavior.source_id, behavior.generated_id " +
					"FROM claim AS c " +
					"LEFT JOIN publication AS pub ON (c.publication = pub.id) " +
					"LEFT JOIN term AS behavior ON (c.behavior_term = behavior.id) " +
					"LEFT JOIN term AS evidence ON (c.evidence = evidence.id) ";

	
	static final String CLAIMUPDATESTATEMENT = 
			"UPDATE claim SET generated_id = ? WHERE id = ?";

	
	static final String TAXONROWQUERY = 
			"SELECT t.id,t.name,t.author, " +
					"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
					"t.parent_term, t.merged, t.merge_status, parent_record.source_id " +
					"FROM taxon AS t where taxon.id = ? " +
					"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) ";

	static final String TAXONTABLEQUERY = 
			"SELECT t.id,t.name,t.author, " +
					"t.year,t.external_id,t.authority,t.parent,t.generated_id, " +
					"t.parent_term,t.merged,t.merge_status, parent_record.source_id " +
					"FROM taxon AS t " +
					"LEFT JOIN term AS parent_record ON (t.parent_term = parent_record.id) ";

	static final String TAXONUPDATESTATEMENT = "";
	
	static final String INDIVIDUALROWQUERY =
			"SELECT i.id, i.source_id, i.generated_id, i.label, i.term " +
			"FROM individual AS i WHERE i.id = ?";
	
	static final String INDIVIDUALTABLEQUERY =
			"SELECT i.id, i.source_id, i.generated_id, i.label, i.term " +
			"FROM individual AS i";
	
	static final String INDIVIDUALUPDATESTATEMENT =
			"UPDATE individual SET generated_id = ? WHERE id = ?";
	
	static final String PROPERTYROWQUERY = 
			"SELECT p.id, p.source_id, p.authority, p.label, p.generated_id, p.comment " +
			"FROM property AS p WHERE p.id = ?";

	
	static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private Connection c;
	
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
	 * @return
	 * @throws Exception
	 */
	public static DBConnection getDBConnection() throws Exception{
		return new DBConnection(DEFAULTPROPERTIESFILE);
	}

	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DBConnection getTestConnection() throws Exception{
		return new DBConnection(TESTPROPERTIESFILE);
	}
	
	public static AbstractConnection getMockConnection() {
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
		final PreparedStatement publicationStatement = c.prepareStatement(PUBLICATIONROWQUERY);
		ResultSet rawResults = null;
		PublicationBean result = null;
		try{
			publicationStatement.setInt(1, id);
			rawResults = publicationStatement.executeQuery();
			AbstractResults publicationResults = new DBResults(rawResults);
			if (publicationResults.next()){
				result = new PublicationBean();
				result.fill(publicationResults);
			}
		}
		finally{
			publicationStatement.close();
			if (rawResults != null){
				rawResults.close();
				return result;
			}
		}
		return null;
	}
	
	public Set<PublicationBean> getPublications() throws SQLException{
		final Set<PublicationBean> result = new HashSet<PublicationBean>();
		Statement allpubStatement = c.createStatement();
		try{
			ResultSet rawResults = allpubStatement.executeQuery(PUBLICATIONTABLEQUERY);
			AbstractResults publicationResults = new DBResults(rawResults);
			while (publicationResults.next()){
				PublicationBean pub = new PublicationBean();
				pub.fill(publicationResults);
				result.add(pub);
			}
			rawResults.close();
			return result;
		}
		finally{
			allpubStatement.close();
		}
	}

	final static private String UPDATEPUBLICATIONFAIL = "publication (%s) update failed; row count = %d";
	public void updatePublication(PublicationBean p) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(PUBLICATIONUPDATESTATEMENT);
		try{
			updateStatement.setString(1, p.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2,p.getId());
			final int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error(String.format(UPDATEPUBLICATIONFAIL,p,count));
			}
		}
		finally{
			updateStatement.close();
		}
	}
	
	
	public TermBean getTerm(int id) throws SQLException{
		if (TermBean.isCached(id)){
			return (TermBean)TermBean.getCached(id);
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
				TermBean.cache(result);
			}
			else{
				result = null;
			}
			termSet.close();
			return result;
		}
		finally{
			termStatement.close();
		}
	}


	
	public Set<TermBean> getTerms() throws SQLException{
		final Set<TermBean> result = new HashSet<TermBean>();
		final Statement allTermStatement = c.createStatement();
		try{
			final ResultSet termSet = allTermStatement.executeQuery(TERMTABLEQUERY);
			final AbstractResults termResults = new DBResults(termSet);
			while (termSet.next()){
				TermBean t = new TermBean();
				t.fill(termResults);
				result.add(t);
			}
			termSet.close();
			return result;
		}
		finally{
			allTermStatement.close();
		}
	}

	public void updateTerm(TermBean t) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(TERMUPDATESTATEMENT);
		try{
			updateStatement.setString(1, t.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2, t.getId());
			int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error("term update failed; row count = " + count);
			}
		}
		finally{
			updateStatement.close();
		}
	}



	public ClaimBean getClaim(int id) throws Exception{
		final PreparedStatement claimStatement = c.prepareStatement(CLAIMROWQUERY);
		try{
			claimStatement.setInt(1, id);
			final ResultSet r = claimStatement.executeQuery();
			final AbstractResults claimSet = new DBResults(r);
			ClaimBean result;
			if (claimSet.next()){
				result = new ClaimBean();
				result.fill(claimSet);
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
	
	public Set<ClaimBean> getClaims() throws Exception {
		final Statement allClaimStatement = c.createStatement();
		try{
			final Set<ClaimBean> result = new HashSet<ClaimBean>();
			final ResultSet r = allClaimStatement.executeQuery(CLAIMTABLEQUERY);
			final AbstractResults claimSet = new DBResults(r);
			while (claimSet.next()){
				ClaimBean cb = new ClaimBean();
				cb.fill(claimSet);
				result.add(cb);
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
		}
		finally{
			updateStatement.close();
		}
	}

	
	public Set<ParticipantBean> getParticipants(ClaimBean a) throws Exception {
		final Set<ParticipantBean> result = new HashSet<ParticipantBean>();
		final int assertion_id = a.getId();
		final PreparedStatement participantsStatement =
				c.prepareStatement(PARTICIPANTSQUERY);
		try{
			participantsStatement.setInt(1, assertion_id);
			final ResultSet r = participantsStatement.executeQuery();
			final AbstractResults participantSet = new DBResults(r);
			while (participantSet.next()){                   //TODO too much going on here, hard to test
				ParticipantBean p = new ParticipantBean();
				p.fill(participantSet);
				result.add(p);
			}
			r.close();
			return result;
		}
		finally{
			participantsStatement.close();
		}
	}

	@Override
	public void updateNamedEntity(UpdateableBean b) throws SQLException{
		b.updateDB(this);
	}
	
	public void updateParticipant(ParticipantBean b) throws SQLException{
		final PreparedStatement updateStatement = 
				c.prepareStatement(PARTICIPANTUPDATESTATEMENT);
		try{
			updateStatement.setString(1, b.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2, b.getId());
			int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error("entity update failed; row count = " + count);
			}
		}
		finally{
			updateStatement.close();
		}
	}

	
	public TaxonBean getTaxon(int id) throws SQLException{
		final PreparedStatement taxonStatement = c.prepareStatement(TAXONROWQUERY);
		try{
			taxonStatement.setInt(1, id);
			ResultSet rawResults = taxonStatement.executeQuery();
			AbstractResults taxonResults = new DBResults(rawResults);
			TaxonBean result;
			if (taxonResults.next()){
				result = new TaxonBean();
				result.fill(taxonResults);
			}
			else{
				result = null;
			}
			rawResults.close();
			return result;
		}
		finally{
			taxonStatement.close();
		}
	}
	

	public Set<TaxonBean> getTaxa() throws SQLException{
		final Set<TaxonBean> result = new HashSet<TaxonBean>();
		final Statement allTaxaStatement = c.createStatement();
		try{
			ResultSet rawResults = allTaxaStatement.executeQuery(TAXONTABLEQUERY);
			AbstractResults taxaResults = new DBResults(rawResults);
			while (taxaResults.next()){
				TaxonBean tax = new TaxonBean();
				tax.fill(taxaResults);
				result.add(tax);
			}
			rawResults.close();
			return result;
		}
		finally{
			allTaxaStatement.close();
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
		}
		finally{
			updateStatement.close();
		}
	}

	
	public IndividualBean getIndividual(int id) throws SQLException{
		if (IndividualBean.isCached(id)){
			return (IndividualBean)IndividualBean.getCached(id);
		}
		final PreparedStatement individualStatement = c.prepareStatement(INDIVIDUALROWQUERY);
		try{
			individualStatement.setInt(1, id);
			final ResultSet individualSet = individualStatement.executeQuery();
			IndividualBean result;
			if (individualSet.next()){
				result = new IndividualBean();
				final AbstractResults individualResults = new DBResults(individualSet);
				result.fill(individualResults);
				IndividualBean.cache(result);
			}
			else{
				result = null;
			}
			individualSet.close();
			return result;
		}
		finally{
			individualStatement.close();
		}
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

	
	
	public PropertyBean getProperty(int id) throws Exception{
		if (PropertyBean.isCached(id)){
			return (PropertyBean)PropertyBean.getCached(id);
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
				PropertyBean.cache(result);
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

	/**
	 * 
	 * @return maps source_urls, which are unique, to domain identifiers
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @return maps source_urls, which are unique, to domain identifiers
	 * @throws SQLException
	 */
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
	
	
	private static final String PUBLICATIONIDCOUNTERQUERY = 
			"SELECT generated_id FROM publication";
	
	private static final String CLAIMIDCOUNTERQUERY = 
			"SELECT generated_id FROM claim";
	
	private static final String INDIVIDUALIDCOUNTERQUERY = 
			"SELECT generated_id FROM individual";

	public int scanPrivateIDs() throws Exception{
		int maxid=0;
		final Statement countStatement = c.createStatement();
		try {
			ResultSet publicationSet = countStatement.executeQuery(PUBLICATIONIDCOUNTERQUERY);
			while(publicationSet.next()){
				final String source = publicationSet.getString("generated_id");
				final int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			}
			publicationSet.close();
			ResultSet assertionSet = countStatement.executeQuery(CLAIMIDCOUNTERQUERY);
			while(assertionSet.next()){
				final String source = assertionSet.getString("generated_id");
				final int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			}
			assertionSet.close();
			ResultSet individualSet = countStatement.executeQuery(INDIVIDUALIDCOUNTERQUERY);
			while(individualSet.next()){
				final String source = individualSet.getString("generated_id");
				final int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			}
			individualSet.close();
		}
		finally {
			countStatement.close();
		}
		return maxid;
	}
	
	private int extractCount(String id){
		if (id != null && id.startsWith(IRIManager.ARACHBPREFIX)){
			try{
				int result = Integer.parseInt(id.substring(IRIManager.ARACHBPREFIX.length()));
				return result;
			}
			catch(NumberFormatException e){
				log.warn("Bad id format: " + id);
				return -1;
			}
		}
		return -1;
	}


	public void close() throws Exception {
		c.close();
	}

	@Override
	public Set<ParticipantBean> getParticipantsWithProperty(ClaimBean a,
			Object p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PElementBean> getPElements(ParticipantBean p) throws Exception {
		final PreparedStatement pElementStatement = c.prepareStatement(PELEMENTSFROMPARTICIPANTQUERY);
		final Set<PElementBean> result = new HashSet<PElementBean>();
		try{
			pElementStatement.setInt(1,p.getId());
			final ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementsResults = new DBResults(rawResults);
			while (pElementsResults.next()){
				PElementBean bean = new PElementBean();
				bean.fill(pElementsResults);
				fillPElementTerm(bean);
				fillPElementIndividual(bean);
				fillPElementParents(bean);
				fillPElementChildren(bean);
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
	public PElementBean getPElement(int id) throws Exception{
		final PreparedStatement pElementStatement = c.prepareStatement(PELEMENTQUERY);
		try{
			pElementStatement.setInt(1, id);
			final ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementResults = new DBResults(rawResults);
			PElementBean pb;
			if (pElementResults.next()){
				pb = new PElementBean();
				pb.fill(pElementResults);
				fillPElementParents(pb);
				fillPElementChildren(pb);
				rawResults.close();
			}
			else{
				pb = null;
			}
			rawResults.close();
			return pb;
		}
		finally{
			pElementStatement.close();
		}
	}
	
	@Override
	public void fillPElementTerm(PElementBean pb) throws Exception{
		final PreparedStatement pElementTermStatement = c.prepareStatement(PELEMENTTERMQUERY);
		try{
			pElementTermStatement.setInt(1, pb.getId());
			final ResultSet results = pElementTermStatement.executeQuery();
			if (results.next()){
				AbstractResults tresults = new DBResults(results);
				pb.fillTerm(tresults, this);
			}
			results.close();
		}
		finally{
			pElementTermStatement.close();
		}
	}
	
	@Override
	public void fillPElementIndividual(PElementBean pb) throws Exception{
		final PreparedStatement pElementIndividualStatement = c.prepareStatement(PELEMENTINDIVIDUALQUERY);
		try{
			pElementIndividualStatement.setInt(1, pb.getId());
			final ResultSet results = pElementIndividualStatement.executeQuery();
			if (results.next()){
				AbstractResults iresults = new DBResults(results);
				pb.fillIndividual(iresults, this);
			}
			results.close();
		}
		finally{
			pElementIndividualStatement.close();
		}
	}
	
	@Override
	public void fillPElementParents(PElementBean result) throws Exception{
		final PreparedStatement pElementParentsStatement = c.prepareStatement(PELEMENTPARENTSQUERY);
		try{
			pElementParentsStatement.setInt(1, result.getId());
			ResultSet rawResults = pElementParentsStatement.executeQuery();
			AbstractResults parentResults = new DBResults(rawResults);
			result.fillParents(parentResults);
			rawResults.close();
		}
		finally{
			pElementParentsStatement.close();
		}
	}
	
	@Override
	public void fillPElementChildren(PElementBean result) throws Exception{
		final PreparedStatement pElementChildrenStatement = c.prepareStatement(PELEMENTCHILDRENQUERY);
		try{
			pElementChildrenStatement.setInt(1, result.getId());
			ResultSet rawResults = pElementChildrenStatement.executeQuery();
			AbstractResults childrenResults = new DBResults(rawResults);
			result.fillChildren(childrenResults);
			rawResults.close();
		}
		finally{
			pElementChildrenStatement.close();
		}
	}

}

