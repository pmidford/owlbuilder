package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Assertion implements AbstractNamedEntity{

	static final private String ROWQUERY = "SELECT a.id, a.publication, " +
	"a.publication_behavior, a.behavior_term, a.publication_taxon, a.taxon," +
	"a.publication_anatomy, a.evidence, a.generated_id, " +
	"pub.doi,pub.generated_id,behavior.source_id,behavior.generated_id " +
    "FROM assertion AS a " +
	"LEFT JOIN publication AS pub ON (a.publication = pub.id) " +
	"LEFT JOIN term AS behavior ON (a.behavior_term = behavior.id) " +
	"LEFT JOIN term AS evidence ON (a.evidence = evidence.id) " +
    "WHERE a.id = ?";
	

	static final private String TABLEQUERY = "SELECT a.id, a.publication, " +
	"a.publication_behavior, a.behavior_term, a.publication_taxon, a.taxon," +
	"a.publication_anatomy, a.evidence, a.generated_id, " +
	"pub.doi,pub.generated_id,behavior.source_id,behavior.generated_id " +
	"FROM assertion AS a " +
	"LEFT JOIN publication AS pub ON (a.publication = pub.id) " +
	"LEFT JOIN term AS behavior ON (a.behavior_term = behavior.id) " +
	"LEFT JOIN term AS evidence ON (a.evidence = evidence.id) ";
	
	static final private String ROWUPDATE = "UPDATE assertion " +
			"SET generated_id = ? WHERE id = ?";
	

	final static int DBID = 1;
	final static int DBPUBLICATION = 2;
	final static int DBPUBLICATIONBEHAVIOR = 3;
	final static int DBBEHAVIORTERM = 4;
	final static int DBPUBLICATIONTAXON = 5;
	final static int DBTAXON = 6;
	final static int DBPUBLICATIONANATOMY = 7;
	final static int DBEVIDENCE = 8;
	final static int DBGENERATEDID = 9;
	final static int DBPUBDOI = 10;
	final static int DBPUBGENERATEDID = 11;
	final static int DBBEHAVIORSOURCEID = 12;
	final static int DBBEHAVIORGENERATEDID = 13;
	final static int DBEVIDENCESOURCEID = 14;
	final static int DBEVIDENCEGENERATEDID = 15;
	
	final static String BADPUBLICATIONIRI =
			"Publication without IRI referenced as assertion publication: assertion id = %s; publication id = %s";
	final static String BADBEHAVIORIRI =
			"Term without IRI referenced as assertion behavior: assertion id = %s; behavior id = %s";
	final static String BADEVIDENCEIRI =
			"Term without IRI referenced as assertion evidence; assertion id = %s; evidence id = %s";

	
	private final static Logger log = Logger.getLogger(Assertion.class);

	private int id;
	private int publication;
	private int behavior;
	private String publicationBehavior;
	private int taxon;
	private String publicationTaxon;
	private String publicationAnatomy;
	private int evidence;
	private String generated_id = null;  //for validity checking
	private String publicationIRI;
	private String behaviorIRI;
	private String evidenceIRI;
	
	static final Assertion dummy = new Assertion(); 
	
	
	public static String getRowQuery(){
		return Assertion.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Assertion.TABLEQUERY;
	}
	
	@Override
	public String getUpdateStatement(){
		return Assertion.ROWUPDATE;
	}
	
	
	//maybe make this a constructor
	@Override
	public void fill(AbstractResults record) throws Exception{
		id = record.getInt(DBID);
		publication = record.getInt(DBPUBLICATION);
		publicationBehavior = record.getString(DBPUBLICATIONBEHAVIOR);
		behavior = record.getInt(DBBEHAVIORTERM);
		publicationTaxon = record.getString(DBPUBLICATIONTAXON);
		taxon = record.getInt(DBTAXON);
		publicationAnatomy = record.getString(DBPUBLICATIONANATOMY);
		evidence = record.getInt(DBEVIDENCE);
		generated_id = record.getString(DBGENERATEDID);
		if (publication != 0){
			updatePublicationIRI(record);
		}
		if (behavior != 0){
			updateBehaviorIRI(record);
		}
		if (evidence != 0){
			updateEvidenceIRI(record);
		}
	}
	
	private void updatePublicationIRI(AbstractResults record) throws Exception{
		if (record.getString(DBPUBDOI) != null){
			this.setPublicationIri(Publication.cleanupDoi(record.getString(DBPUBDOI)));
		}
		else if (record.getString(DBPUBGENERATEDID) != null){
			this.setPublicationIri(record.getString(DBPUBGENERATEDID));
		}
		else{
			throwBadState(BADPUBLICATIONIRI);
		}
	}

	private void updateBehaviorIRI(AbstractResults record) throws Exception{
		if (record.getString(DBBEHAVIORSOURCEID) != null){
			this.setBehaviorIri(record.getString(DBBEHAVIORSOURCEID));
		}
		else if (record.getString(DBBEHAVIORGENERATEDID) != null){
			this.setBehaviorIri(record.getString(DBBEHAVIORGENERATEDID));
		}
		else{
			throwBadState(BADBEHAVIORIRI);
		}
	}

	private void updateEvidenceIRI(AbstractResults record) throws Exception{
		if (record.getString(DBEVIDENCESOURCEID) != null){
			this.setEvidenceIri(record.getString(DBEVIDENCESOURCEID));
		}
		else if (record.getString(DBEVIDENCEGENERATEDID) != null){
			this.setEvidenceIri(record.getString(DBEVIDENCEGENERATEDID));
		}
		else{
			throwBadState(BADEVIDENCEIRI);
		}
	}
	
	private void throwBadState(String template){
		final String msg = String.format(BADEVIDENCEIRI, id, evidence);
		throw new IllegalStateException(msg);
	}

	@Override
	public int getId(){
		return id;
	}
	
	public int getPublication(){
		return publication;
	}
	
	public String getPublicationBehavior(){
		return publicationBehavior;
	}
	
	public int getBehavior(){
		return behavior;
	}
	
	public String getPublicationTaxon(){
		return publicationTaxon;
	}
	
	public int getTaxon(){
		return taxon;
	}
	
	public String getPublicationAnatomy(){
		return publicationAnatomy;
	}
	
	public int getEvidence(){
		return evidence;
	}
	
	@Override
	public String getIriString(){
		if (generated_id == null){
			throw new IllegalStateException("Publication has neither doi nor generated id");
		}
		return generated_id;
	}


	public void setPublicationIri(String s){
		publicationIRI = s;
	}

	public void setBehaviorIri(String s){
		behaviorIRI = s;
	}

	public void setEvidenceIri(String s){
		evidenceIRI = s;
	}

	
	@Override
	public void setGeneratedId(String id){
		generated_id = id;
	}
	
	@Override
	public String getGeneratedId(){
		return generated_id;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws Exception{		
		final AbstractConnection c = builder.getConnection();
		OWLOntology target = builder.getTarget();
		OWLOntologyManager manager = builder.getOntologyManager();
		OWLDataFactory factory = builder.getDataFactory();
		final OWLClass textualEntityClass = factory.getOWLClass(IRIManager.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(IRIManager.denotesProperty);
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
		final Participant primary = c.getPrimaryParticipant(this);
		OWLObject owlPrimary = primary.generateOWL(builder);
		final Set<Participant> otherParticipants = c.getParticipants(this);
		for (Participant p : otherParticipants){
			OWLObject owlRep = p.generateOWL(builder);
		}
		OWLObjectProperty hasParticipant = factory.getOWLObjectProperty(IRIManager.hasParticipantProperty);
        if (owlPrimary instanceof OWLClassExpression){
        	Set<OWLClassExpression> supersets = new HashSet<OWLClassExpression>(); 
        	supersets.add(textualEntityClass);
        	OWLClassExpression hasParticipantPrimary = 
        			factory.getOWLObjectSomeValuesFrom(hasParticipant,(OWLClassExpression) owlPrimary);
        	supersets.add(hasParticipantPrimary);

        	//find the publication id
        	Publication p = c.getPublication(getPublication());
        	IRI publication_id = IRI.create(publicationIRI);
        	OWLClass behaviorclass = factory.getOWLClass(IRI.create(behaviorIRI)); 
        	OWLClassExpression denotesExpr = 
        			factory.getOWLObjectSomeValuesFrom(denotesProp,behaviorclass); 
        	supersets.add(denotesExpr);
        	OWLClassExpression intersectExpr =
        			factory.getOWLObjectIntersectionOf(supersets);
        	OWLIndividual assert_ind = factory.getOWLNamedIndividual(IRI.create(getIriString()));
        	OWLClassAssertionAxiom textClassAssertion = 
        			factory.getOWLClassAssertionAxiom(intersectExpr, assert_ind); 
        	manager.addAxiom(target, textClassAssertion);
        	if (publication_id != null){
        		OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
        		OWLObjectPropertyAssertionAxiom partofAssertion = 
        				factory.getOWLObjectPropertyAssertionAxiom(partofProperty, assert_ind, pub_ind);
        		manager.addAxiom(target, partofAssertion);
        	}
		}


		// TODO Auto-generated method stub
		return null;
	}
}
