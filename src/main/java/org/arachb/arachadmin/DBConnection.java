package org.arachb.arachadmin;

import java.io.IOException;
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
import org.arachb.owlbuilder.lib.AbstractNamedEntity;
import org.arachb.owlbuilder.lib.AbstractResults;
import org.arachb.owlbuilder.lib.IRIManager;



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
					"part.publication_substrate, part.participation_property, part.head_element " +
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
	

	static final String PELEMENTPARENTSQUERY =
			"SELECT link.parent,link.property FROM participant_link as link " +
			        "WHERE link.child = ?";

	static final String PELEMENTCHILDRENQUERY =
			"SELECT link.child,link.property FROM participant_link as link " +
			        "WHERE link.child = ?";
	
	
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
	 */
	public static boolean testConnection(){
		Connection c = null;
		String connectionSpec = DEFAULTPROPERTIESFILE;
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
		catch (SQLException e){
			log.error("DBConnection setup failed, encountered:",e);
			return false;
		}
		catch (IOException e){
			log.error("DBConnection setup failed, encountered:",e);
			return false;
		}
		catch (ClassNotFoundException e){
			log.error("DBConnection setup failed, encountered:",e);
			return false;
		}
		catch (NullPointerException e){
			log.error("DBConnection setup failed, encountered:",e);
			return false;
		}
		finally{
			try {
				if (c != null){
					c.close();
				}
			}
			catch (SQLException e){
				log.error("Error while closing test connection" + e);
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
	
	private void initDomains(Connection c) throws SQLException{
		Statement domainStatement = c.createStatement();
		ResultSet domainSet = domainStatement.executeQuery(DOMAINSQUERY);
		while (domainSet.next()){
			int id = domainSet.getInt("id");
			String label = domainSet.getString("name");
			id2domain.put(id, label);
			domain2id.put(label, id);
		}
		domainStatement.close();
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
		PreparedStatement publicationStatement = c.prepareStatement(PUBLICATIONROWQUERY);
		try{
			publicationStatement.setInt(1, id);
			ResultSet rawResults = publicationStatement.executeQuery();
			AbstractResults publicationResults = new DBResults(rawResults);
			if (publicationResults.next()){
				PublicationBean result = new PublicationBean();
				result.fill(publicationResults);
				return result;
			}
			else {
				return null;
			}
		}
		finally{
			publicationStatement.close();
		}
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
			return result;
		}
		finally{
			allpubStatement.close();
		}
	}

	final static private String UPDATEPUBLICATIONFAIL = "publication (%s) update failed; row count = %d";
	public void updatePublication(PublicationBean p) throws SQLException{
		PreparedStatement updateStatement = 
				c.prepareStatement(PUBLICATIONUPDATESTATEMENT);
		try{
			updateStatement.setString(1, p.getGeneratedId());  //getIRI_String() is wrong - 
			updateStatement.setInt(2,p.getId());
			int count = updateStatement.executeUpdate();
			if (count != 1){
				logger.error(String.format(UPDATEPUBLICATIONFAIL,p,count));
			}
		}
		finally{
			updateStatement.close();
		}
	}
	
	public TermBean getTerm(int id) throws SQLException{
		PreparedStatement termStatement = c.prepareStatement(TERMROWQUERY);
		try{
			termStatement.setInt(1, id);
			ResultSet termSet = termStatement.executeQuery();
			AbstractResults termResults = new DBResults(termSet);
			if (termSet.next()){
				TermBean result = new TermBean();
				result.fill(termResults);
				return result;
			}
			else {
				return null;
			}
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
			return result;
		}
		finally{
			allTermStatement.close();
		}
	}

	
	public void updateTerm(TermBean t) throws SQLException{
		PreparedStatement updateStatement = 
				c.prepareStatement(TERMUPDATESTATEMENT);
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


	public ClaimBean getClaim(int id) throws Exception{
		final PreparedStatement claimStatement = c.prepareStatement(CLAIMROWQUERY);
		try{
			claimStatement.setInt(1, id);
			final ResultSet r = claimStatement.executeQuery();
			final AbstractResults claimSet = new DBResults(r);
			if (claimSet.next()){
				ClaimBean result = new ClaimBean();
				result.fill(claimSet);
				return result;
			}
			else {
				return null;
			}
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
				ClaimBean a = new ClaimBean();
				a.fill(claimSet);
				result.add(a);
			}
			return result;
		}
		finally {
			allClaimStatement.close();
		}
	}
		
	public void updateClaim(ClaimBean cl) throws SQLException{
		PreparedStatement updateStatement = 
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
		PreparedStatement participantsStatement =
				c.prepareStatement(PARTICIPANTSQUERY);
		try{
			participantsStatement.setInt(1, assertion_id);
			final ResultSet r = participantsStatement.executeQuery();
			final AbstractResults participantSet = new DBResults(r);
			while (participantSet.next()){
				ParticipantBean p = new ParticipantBean();
				p.fill(participantSet);
				p.loadElements(this);
				result.add(p);
			}
			return result;
		}
		finally{
			participantsStatement.close();
		}
	}

	@Override
	public void updateNamedEntity(AbstractNamedEntity e) throws SQLException{
		e.updateDB(this);
	}
	
	public void updateParticipant(ParticipantBean b) throws SQLException{
		PreparedStatement updateStatement = 
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
		PreparedStatement taxonStatement = c.prepareStatement(TAXONROWQUERY);
		try{
			taxonStatement.setInt(1, id);
			ResultSet rawResults = taxonStatement.executeQuery();
			AbstractResults taxonResults = new DBResults(rawResults);
			if (taxonResults.next()){
				TaxonBean result = new TaxonBean();
				result.fill(taxonResults);
				return result;
			}
			else {
				return null;
			}
		}
		finally{
			taxonStatement.close();
		}
	}
	

	public Set<TaxonBean> getTaxa() throws SQLException{
		final Set<TaxonBean> result = new HashSet<TaxonBean>();
		Statement allTaxaStatement = c.createStatement();
		try{
			ResultSet rawResults = allTaxaStatement.executeQuery(TAXONTABLEQUERY);
			AbstractResults taxaResults = new DBResults(rawResults);
			while (taxaResults.next()){
				TaxonBean tax = new TaxonBean();
				tax.fill(taxaResults);
				result.add(tax);
			}
			return result;
		}
		finally{
			allTaxaStatement.close();
		}
	}

	
	public void updateTaxon(TaxonBean t) throws SQLException{
		PreparedStatement updateStatement = 
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
		PreparedStatement individualStatement = c.prepareStatement(INDIVIDUALROWQUERY);
		try{
			individualStatement.setInt(1, id);
			ResultSet individualSet = individualStatement.executeQuery();
			AbstractResults individualResults = new DBResults(individualSet);
			if (individualSet.next()){
				IndividualBean result = new IndividualBean();
				result.fill(individualResults);
				return result;
			}
			else {
				return null;
			}
		}
		finally{
			individualStatement.close();
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
		Statement sourceOntStatement = c.createStatement();
		try{
			ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYSOURCEQUERY);
			while (sourceOntSet.next()){
				final int domain_id = sourceOntSet.getInt("domain");
				final String domain = domainName(domain_id);
				final String source = sourceOntSet.getString("source_url");
				result.put(source,domain);
			}
			sourceOntStatement.close();
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
		Statement sourceOntStatement = c.createStatement();
		try{
			ResultSet sourceOntSet = sourceOntStatement.executeQuery(ONTOLOGYNAMEQUERY);
			while (sourceOntSet.next()){
				final String source = sourceOntSet.getString("source_url");
				final String name = sourceOntSet.getString("name");
				result.put(source,name);
			}
			sourceOntStatement.close();
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
		Statement countStatement = c.createStatement();
		try {
			ResultSet publicationSet = countStatement.executeQuery(PUBLICATIONIDCOUNTERQUERY);
			while(publicationSet.next()){
				final String source = publicationSet.getString("generated_id");
				int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			}
			ResultSet assertionSet = countStatement.executeQuery(CLAIMIDCOUNTERQUERY);
			while(assertionSet.next()){
				final String source = assertionSet.getString("generated_id");
				int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			}
			assertionSet.close();
			ResultSet individualSet = countStatement.executeQuery(INDIVIDUALIDCOUNTERQUERY);
			while(individualSet.next()){
				final String source = individualSet.getString("generated_id");
				int count = extractCount(source);
				if (count > maxid){
					maxid = count;
				}
			
			}
		}
		finally {
			countStatement.close();
		}
		return maxid;
	}
	
	private int extractCount(String id){
		if (id != null && id.startsWith(IRIManager.ARACHBPREFIX)){
			String raw = id.substring(IRIManager.ARACHBPREFIX.length());
			try{
				int result = Integer.parseInt(raw);
				return result;
				}
			catch(NumberFormatException e){
				log.warn("Bad id format: " + id);
				return -1;
			}
		}
		else {
			return -1;
		}
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
		PreparedStatement pElementStatement = c.prepareStatement(PELEMENTSFROMPARTICIPANTQUERY);
		final Set<PElementBean> result = new HashSet<PElementBean>();
		try{
			pElementStatement.setInt(1,p.getId());
			ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementsResults = new DBResults(rawResults);
			while (pElementsResults.next()){
				PElementBean bean = new PElementBean();
				bean.fill(pElementsResults);
				fillPElementParents(bean);
				fillPElementChildren(bean);
				result.add(bean);
			}
		}
		finally{
			pElementStatement.close();
		}
		return result;
	}
	
	
	@Override
	public PElementBean getPElement(int id) throws Exception{
		PreparedStatement pElementStatement = c.prepareStatement(PELEMENTQUERY);
		try{
			pElementStatement.setInt(1, id);
			ResultSet rawResults = pElementStatement.executeQuery();
			AbstractResults pElementResults = new DBResults(rawResults);
			if (pElementResults.next()){
				PElementBean result = new PElementBean();
				result.fill(pElementResults);
				fillPElementParents(result);
				fillPElementChildren(result);
				return result;
			}
			else {
				return null;
			}
		}
		finally{
			pElementStatement.close();
		}
	}
	
	
	public void fillPElementParents(PElementBean result) throws Exception{
		PreparedStatement pElementParentsStatement = c.prepareStatement(PELEMENTPARENTSQUERY);
		try{
			pElementParentsStatement.setInt(1, result.getId());
			ResultSet rawResults = pElementParentsStatement.executeQuery();
			AbstractResults parentResults = new DBResults(rawResults);
			result.fillParents(parentResults);
		}
		finally{
			pElementParentsStatement.close();
		}
	}
	
	public void fillPElementChildren(PElementBean result) throws Exception{
		PreparedStatement pElementChildrenStatement = c.prepareStatement(PELEMENTCHILDRENQUERY);
		try{
			pElementChildrenStatement.setInt(1, result.getId());
			ResultSet rawResults = pElementChildrenStatement.executeQuery();
			AbstractResults childrenResults = new DBResults(rawResults);
			result.fillChildren(childrenResults);			
		}
		finally{
			pElementChildrenStatement.close();
		}
	}

}

