package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.ClaimBean;
import org.arachb.arachadmin.ParticipantBean;
import org.arachb.arachadmin.PublicationBean;
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

public class Claim implements AbstractNamedEntity {
	
	private final ClaimBean bean;

	private static Logger log = Logger.getLogger(Claim.class);

	public Claim(ClaimBean b){
		bean = b;
	}


	@Override
	public int getId() {
		return bean.getId();
	}

	@Override
	public void setGeneratedId(String id) {
		// TODO Auto-generated method stub

	}

	
	final static String NOASSERTIONGENID = "Assertion has no generated id; db id = %s";

	@Override
	public String getIriString(){
		final String genId = bean.getGeneratedId();
		if (genId == null){
			final String msg = String.format(NOASSERTIONGENID, bean.getId());
			throw new IllegalStateException(msg);
		}
		return genId;
	}

	//this seems a little dubious
	@Override
	public String checkIriString(){
		return bean.getGeneratedId();
	}

	@Override
	public String getGeneratedId() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub

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
    	OWLClass behaviorClass = factory.getOWLClass(IRI.create(bean.getBehaviorIri()));
		final QuantifiedParticipant primary = new QuantifiedParticipant(c.getPrimaryParticipant(bean));
    	builder.initializeMiscTermAndParents(behaviorClass);
		OWLObject owlPrimary = primary.generateOWL(builder);
		final Set<QuantifiedParticipant> otherParticipants = 
				QuantifiedParticipant.wrapSet(c.getParticipants(bean));
		final Set<OWLObject> otherOWLParticipants = new HashSet<OWLObject>();
		for (QuantifiedParticipant p : otherParticipants){
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
        else {  // probably a curation error; log this don't throw exception
        	final String msgStr = 
        			"Assertion primary %s of id %s is neither an individual nor a class expression";
			throw new RuntimeException(String.format(msgStr,owlPrimary,bean.getId()));
        }
        
 	    Publication pub = new Publication(c.getPublication(bean.getPublication()));
 	   
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
