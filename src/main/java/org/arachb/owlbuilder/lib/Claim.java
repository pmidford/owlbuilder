package org.arachb.owlbuilder.lib;

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

public class Claim implements AbstractNamedEntity{

	static final private String ROWQUERY = "SELECT a.id, a.publication, " +
	"a.publication_behavior, a.behavior_term, a.taxon," +
	"a.evidence, a.generated_id, pub.doi, " +
	"pub.generated_id, behavior.source_id, behavior.generated_id " +
    "FROM assertion AS a " +
	"LEFT JOIN publication AS pub ON (a.publication = pub.id) " +
	"LEFT JOIN term AS behavior ON (a.behavior_term = behavior.id) " +
	"LEFT JOIN term AS evidence ON (a.evidence = evidence.id) " +
    "WHERE a.id = ?";
	

	static final private String TABLEQUERY = "SELECT a.id, a.publication, " +
	"a.publication_behavior, a.behavior_term, a.taxon, " +
	"a.evidence, a.generated_id, pub.doi, " +
	"pub.generated_id, behavior.source_id, behavior.generated_id " +
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
	final static int DBTAXON = 5;
	final static int DBEVIDENCE = 6;
	final static int DBGENERATEDID = 7;
	final static int DBPUBDOI = 8;
	final static int DBPUBGENERATEDID = 9;
	final static int DBBEHAVIORSOURCEID = 10;
	final static int DBBEHAVIORGENERATEDID = 11;
	final static int DBEVIDENCESOURCEID = 12;
	final static int DBEVIDENCEGENERATEDID = 13;
	
	final static String BADPUBLICATIONIRI =
			"Publication without IRI referenced as assertion publication: assertion id = %s; publication id = %s";
	final static String BADBEHAVIORIRI =
			"Term without IRI referenced as assertion behavior: assertion id = %s; behavior id = %s";
	final static String BADEVIDENCEIRI =
			"Term without IRI referenced as assertion evidence; assertion id = %s; evidence id = %s";

	final static String NOASSERTIONGENID = 
			"Assertion has no generated id; db id = %s";
	
	private final static Logger log = Logger.getLogger(Claim.class);

	private int id;
	private int publication;
	private int behavior;
	private String publicationBehavior;
	private int taxon;
	private int evidence;
	private String generated_id = null;  //for validity checking
	private String behaviorIRI;
	private String publicationIRI;
	private String evidenceIRI;
	
	static final Claim dummy = new Claim(); 
	
	
	public static String getRowQuery(){
		return Claim.ROWQUERY;
	}
	
	public static String getTableQuery(){
		return Claim.TABLEQUERY;
	}
	
	@Override
	public String getUpdateStatement(){
		return Claim.ROWUPDATE;
	}
	
	
	//maybe make this a constructor
	@Override
	public void fill(AbstractResults record) throws Exception{
		id = record.getInt(DBID);
		publication = record.getInt(DBPUBLICATION);
		publicationBehavior = record.getString(DBPUBLICATIONBEHAVIOR);
		behavior= record.getInt(DBBEHAVIORTERM);
		taxon = record.getInt(DBTAXON);
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
		
	public int getTaxon(){
		return taxon;
	}
	
	
	public int getEvidence(){
		return evidence;
	}
	
	@Override
	public String getIriString(){
		if (generated_id == null){
			final String msg = String.format(NOASSERTIONGENID, id);
			throw new IllegalStateException(msg);
		}
		return generated_id;
	}

	@Override
	public String checkIriString(){
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
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass textualEntityClass = factory.getOWLClass(IRIManager.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(IRIManager.denotesProperty);
		final OWLObjectProperty partofProperty = factory.getOWLObjectProperty(IRIManager.partOfProperty);
    	final OWLIndividual assert_ind = factory.getOWLNamedIndividual(IRI.create(getIriString()));
    	OWLClass behaviorClass = factory.getOWLClass(IRI.create(behaviorIRI));
		final Participant primary = c.getPrimaryParticipant(this);
    	builder.initializeMiscTermAndParents(behaviorClass);
		OWLObject owlPrimary = primary.generateOWL(builder);
		final Set<Participant> otherParticipants = c.getParticipants(this);
		final Set<OWLObject> otherOWLParticipants = new HashSet<OWLObject>();
		for (Participant p : otherParticipants){
			otherOWLParticipants.add(p.generateOWL(builder));
		}
		OWLObjectProperty hasParticipant = factory.getOWLObjectProperty(IRIManager.hasParticipantProperty);
        if (owlPrimary instanceof OWLClassExpression){
        	Set<OWLClassExpression> supersets = new HashSet<OWLClassExpression>(); 
        	supersets.add(textualEntityClass);
        	OWLClassExpression hasParticipantPrimary = 
        			factory.getOWLObjectSomeValuesFrom(hasParticipant,(OWLClassExpression) owlPrimary);
        	OWLClassExpression behaviorWithParticipant =
        			factory.getOWLObjectIntersectionOf(behaviorClass,hasParticipantPrimary);
        	OWLClassExpression denotesExpr = 
        			factory.getOWLObjectSomeValuesFrom(denotesProp,behaviorWithParticipant); 
        	supersets.add(denotesExpr);
        	OWLClassExpression intersectExpr =
        			factory.getOWLObjectIntersectionOf(supersets);
            OWLClassAssertionAxiom textClassAssertion = 
        			factory.getOWLClassAssertionAxiom(intersectExpr, assert_ind); 
        	manager.addAxiom(target, textClassAssertion);
        }
        else if (owlPrimary instanceof OWLIndividual){
        	//TODO fill this in
        }
        else {
        	throw new RuntimeException("Assertion primary " + 
        							   owlPrimary + 
        							   " is neither an individual or a class expression");
        }
        
 	    Publication pub = c.getPublication(getPublication());
 	    
 	    OWLObject pub_obj = pub.generateOWL(builder);
 	    if (pub_obj instanceof OWLIndividual){
 	    	OWLIndividual pub_ind = (OWLIndividual)pub_obj;
    		OWLObjectPropertyAssertionAxiom partofAssertion = 
    				factory.getOWLObjectPropertyAssertionAxiom(partofProperty, assert_ind, pub_ind);
    		manager.addAxiom(target, partofAssertion);
    	}
        return assert_ind;
	}
	

}
