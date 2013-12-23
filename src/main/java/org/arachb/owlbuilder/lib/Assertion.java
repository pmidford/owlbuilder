package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Assertion implements AbstractNamedEntity{
	
	private int id;
	private int publication;
	private int behavior_term;
	private String publication_behavior;
	private int taxon;
	private String publication_taxon;
	private String publication_anatomy;
	private int evidence;
	private String generated_id = null;  //for validity checking
	private String publicationIRI;
	private String behaviorIRI;
	private String evidenceIRI;
	
	static final Assertion dummy = new Assertion(); 
	
	static final private String ROWQUERY = "SELECT id, publication, " +
	"publication_behavior, behavior_term, publication_taxon, taxon," +
	"publication_anatomy, evidence, generated_id " +
    "FROM assertion where assertion.id = ?";

	static final private String TABLEQUERY = "SELECT id, publication, " +
	"publication_behavior, behavior_term, publication_taxon, taxon, " +
    "publication_anatomy, evidence, generated_id " +
	"FROM assertion";
	
	static final private String ROWUPDATE = "UPDATE assertion " +
			"SET generated_id = ? WHERE id = ?";
	
	
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
	public void fill(AbstractResults record) throws SQLException{
		id = record.getInt("id");
		publication = record.getInt("publication");
		publication_behavior = record.getString("publication_behavior");
		behavior_term = record.getInt("behavior_term");
		publication_taxon = record.getString("publication_taxon");
		taxon = record.getInt("taxon");
		publication_anatomy = record.getString("publication_anatomy");
		evidence = record.getInt("evidence");
		generated_id = record.getString("generated_id");
	}

	@Override
	public int get_id(){
		return id;
	}
	
	public int get_publication(){
		return publication;
	}
	
	public String get_publication_behavior(){
		return publication_behavior;
	}
	
	public int get_behavior_term(){
		return behavior_term;
	}
	
	public String get_publication_taxon(){
		return publication_taxon;
	}
	
	public int get_taxon(){
		return taxon;
	}
	
	public String get_publication_anatomy(){
		return publication_anatomy;
	}
	
	public int get_evidence(){
		return evidence;
	}
	
	@Override
	public String getIRI_String(){
		if (generated_id == null){
			throw new IllegalStateException("Publication has neither doi nor generated id");
		}
		return generated_id;
	}

	private String get_publicationIRI(){
		return publicationIRI;
	}
	
	private void set_publicationIRI(String s){
		publicationIRI = s;
	}
	
	private String get_behaviorIRI(){
		return behaviorIRI;
	}
	
	private void set_behaviorIRI(String s){
		behaviorIRI = s;
	}
	
	private String get_evidenceIRI(){
		return evidenceIRI;
	}

	private void set_evidenceIRI(String s){
		evidenceIRI = s;
	}

	@Override
	public void setGeneratedID(String id){
		generated_id = id;
	}

	@Override
	public OWLObject generateOWL(OWLOntology o, OWLOntologyManager manager,	IRIManager iriManager) {
		// TODO Auto-generated method stub
		return null;
	}
}
