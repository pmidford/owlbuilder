package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.ClaimBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Claim implements GeneratingEntity {

	enum Profile {
	    UNKNOWN, INDIVIDUALONLY, CLASSONLY, MIXED
	}

	private final ClaimBean bean;
	private Profile claimProfile = Profile.UNKNOWN;

	private static Logger log = Logger.getLogger(Claim.class);

	public Claim(ClaimBean b){
		bean = b;
	}

	/**
	 * Utility for making Claim Sets from sets of beans
	 */
	public static Set<Claim> wrapSet(Set<ClaimBean> bset){
		final Set<Claim>result = new HashSet<Claim>();
		for(ClaimBean b : bset){
			result.add(new Claim(b));
		}
		return result;
	}

	final static String FORMATTEDINDIVIDUALCOMMENT = "Individual from claim owlgeneration, id = %d";
	/**
	 * generates all the owl objects and assertions required to represent the claim
	 * @param builder
	 * @return individual behavior corresponding to the claim
	 */
	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements) throws Exception{
		final AbstractConnection c = builder.getConnection();
		final OWLDataFactory factory = builder.getDataFactory();
		final IRI claim_iri = IRI.create(bean.getIRIString());
		final OWLIndividual claim_ind = factory.getOWLNamedIndividual(claim_iri);
		final OWLClass informationContentClass =
				factory.getOWLClass(Vocabulary.informationContentEntity);
		builder.addClassAssertionAxiom(informationContentClass, claim_ind);
		builder.addComment(claim_iri, String.format(FORMATTEDINDIVIDUALCOMMENT,bean.getId()));
		OWLClass behaviorClass = factory.getOWLClass(IRI.create(bean.getBehaviorIri()));
		builder.initializeMiscTermAndParents(behaviorClass);
		final Set<Participant> participants =
				Participant.wrapSet(c.getParticipantTable(getId()));
		// publication processing
		Publication pub = new Publication(c.getPublication(bean.getPublication()));
		OWLObject pub_obj = pub.generateOWL(builder);
		if (pub_obj instanceof OWLIndividual){
			OWLIndividual pub_ind = (OWLIndividual)pub_obj;
			builder.addIndividualPartOfAxiom(claim_ind, pub_ind);
		}
		builder.addAxioms();
		// load participant elements
		for (Participant p : participants){
			p.loadElements(c);
		}
		for (Participant p : participants){
			p.resolveElements();
		}
		claimProfile = setProfile(participants);
		log.info(String.format("Claim profile for %d is %s",getId(),claimProfile.toString()));
		switch (claimProfile){
		case INDIVIDUALONLY:
			generateOWLForIndividualOnlyProfile(builder,elements,participants,claim_ind);
			break;
		case CLASSONLY:
			generateOWLForClassOnlyProfile(builder,elements,participants,claim_ind);
			break;
		case MIXED:
			String msg = "Mixed claim profile encountered, time to implement? (id = %d)";
			throw new RuntimeException(String.format(msg,getId()));
		case UNKNOWN:
			msg = "Claim Profile is UNKNOWN - something is wrong (id = %d)";
			throw new RuntimeException(String.format(msg,getId()));
		default:
			break;
		}
		return claim_ind;
	}

	/**
	 * generates all the owl objects and assertions required to represent the claim
	 * @param builder
	 * @param participants
	 * @param claim_ind
	 */
	public void generateOWLForClassOnlyProfile(Owlbuilder builder,
			                                        Map<String, OWLObject> elements,
			                                        Set<Participant> participants,
			                                        OWLIndividual claim_ind) throws Exception{
		final OWLOntology target = builder.getTarget();
		final OWLDataFactory factory = builder.getDataFactory();
		OWLClass behaviorClass = factory.getOWLClass(IRI.create(bean.getBehaviorIri()));
		builder.initializeMiscTermAndParents(behaviorClass);

		final Map<OWLClassExpression,PropertyTerm> owlParticipants = new HashMap<OWLClassExpression,PropertyTerm>();
		for (Participant p : participants){
			OWLClassExpression op = (OWLClassExpression)p.generateOWL(builder);
			PropertyTerm pt = p.getProperty();
			assert op != null;
			owlParticipants.put(op, pt);
		}
		log.info(String.format("After processing claim participants: %d, target class count = %d",
				               bean.getId(),
				               target.getClassesInSignature().size()));


		for (Entry<OWLClassExpression, PropertyTerm> ePair : owlParticipants.entrySet()){
			connectClassParticipant(builder, ePair, claim_ind, behaviorClass);
		}
		log.info(String.format("After connecting claim participants: %d, target class count = %d",
				                bean.getId(),
				                target.getClassesInSignature().size()));

	}
	/**
	 * generates all the owl objects and assertions required to represent the claim
	 * @param builder
	 * @param elements
	 * @param participants
	 */
	public void generateOWLForIndividualOnlyProfile(Owlbuilder builder,
				   								    Map<String,OWLObject> elements,
                                                    Set<Participant> participants,
                                                    OWLIndividual claim_ind) throws Exception{
		final OWLOntology target = builder.getTarget();
		final OWLDataFactory factory = builder.getDataFactory();
		OWLClass behaviorClass = factory.getOWLClass(IRI.create(bean.getBehaviorIri()));
		builder.initializeMiscTermAndParents(behaviorClass);
		final Map<OWLIndividual,PropertyTerm> owlParticipants = new HashMap<OWLIndividual,PropertyTerm>();
		for (Participant p : participants){
			OWLIndividual op = (OWLIndividual)p.generateOWL(builder);
			PropertyTerm pt = p.getProperty();
			assert op != null;
			owlParticipants.put(op, pt);
		}
		log.info(String.format("After processing claim participants: %d, target class count = %d",
				               bean.getId(),
				               target.getClassesInSignature().size()));
		final String iComment = String.format("Individual from claim owlgeneration, id = %d",bean.getId());
		builder.addComment(IRI.create(bean.getIRIString()), iComment);
		IRI eventIRI = IRI.create(bean.getIRIString()+"_event");  //TODO not OBO compliant...
		OWLIndividual event_ind = factory.getOWLNamedIndividual(eventIRI);
		builder.addIndividualDenotesAxiom(claim_ind, event_ind);
		builder.addClassAssertionAxiom(behaviorClass, event_ind);
		builder.addAxioms();
		for (Entry<OWLIndividual, PropertyTerm> ePair : owlParticipants.entrySet()){
			connectIndividualParticipant(builder, ePair, event_ind, behaviorClass);
		}
		log.info(String.format("After connecting claim participants: %d, target class count = %d",
				                bean.getId(),
				                target.getClassesInSignature().size()));
	}



	final Map<String,OWLObject> defaultElementTable = new HashMap<String,OWLObject>();

	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception{
		defaultElementTable.clear();
		OWLObject result = null;
		try{
			result = generateOWL(b,defaultElementTable);
		}
		finally{
			defaultElementTable.clear();
		}
		return result;
	}

	/**
	 * returns a profile enum that reflects whether the participants are all individual,
	 * all classterm or a mixed set.  Hope to put off dealing with mixed set for the time
	 * being.
	 * @param participants
	 * @return profile state based on type of each participant
	 */
	public Profile setProfile(Iterable<Participant> participants){
		Profile result = Profile.UNKNOWN;
		for (Participant p : participants){
			if (p.isIndividual()){
				switch (result){
				case UNKNOWN:
				case INDIVIDUALONLY:
					result = Profile.INDIVIDUALONLY;
					break;
				case CLASSONLY:
					result = Profile.MIXED;
					break;
				default:
					result = Profile.UNKNOWN;
					break;
				}
			}
			else{
				switch (result){
				case UNKNOWN:
				case CLASSONLY:
					result = Profile.CLASSONLY;
					break;
				case INDIVIDUALONLY:
					result = Profile.MIXED;
					break;
				default:
					result = Profile.UNKNOWN;
					break;
				}
			}
		}
		return result;
	}


	/**
	 *
	 * @param builder
	 * @param owl_part owl representation of participant, either class or individual
	 * @param claim_ind owl individual representing the claim
	 * @param behaviorClass
	 */
	private void connectClassParticipant(Owlbuilder builder,
								    Entry<OWLClassExpression, PropertyTerm> ePair,
								    //OWLObject owl_part,
								    OWLIndividual claim_ind,
								    OWLClass behaviorClass){
		OWLOntology target = builder.getTarget();
		OWLOntologyManager manager = builder.getOntologyManager();
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass textualEntityClass = factory.getOWLClass(Vocabulary.textualEntity);
		final OWLObjectProperty denotesProp = factory.getOWLObjectProperty(Vocabulary.denotesProperty);
		final OWLClassExpression owl_participant = ePair.getKey();
		PropertyTerm headProperty = ePair.getValue();
		OWLObjectProperty pExpr = factory.getOWLObjectProperty(IRI.create(headProperty.getSourceId()));
		Set<OWLClassExpression> supersets = new HashSet<OWLClassExpression>();
		supersets.add(textualEntityClass);
		OWLClassExpression someParticipantExpr =
				factory.getOWLObjectSomeValuesFrom(pExpr,owl_participant);
		OWLClassExpression behaviorWithParticipant =
				factory.getOWLObjectIntersectionOf(behaviorClass,someParticipantExpr);
		OWLClassExpression denotesExpr =
				factory.getOWLObjectSomeValuesFrom(denotesProp,behaviorWithParticipant);
		supersets.add(denotesExpr);
		OWLClassExpression intersectExpr =
				factory.getOWLObjectIntersectionOf(supersets);
		OWLClassAssertionAxiom textClassAssertion =
				factory.getOWLClassAssertionAxiom(intersectExpr, claim_ind);
		manager.addAxiom(target, textClassAssertion);
	}

	/**
	 *
	 * @param builder
	 * @param owl_part owl representation of participant, either class or individual
	 * @param claim_ind owl individual representing the claim
	 * @param behaviorClass
	 */
	private void connectIndividualParticipant(Owlbuilder builder,
			                                  Entry<OWLIndividual, PropertyTerm> ePair,
								              OWLIndividual event_ind,
								              OWLClass behaviorClass) throws Exception{
		OWLIndividual headIndividual = ePair.getKey();
		PropertyTerm headProperty = ePair.getValue();
		OWLObjectProperty pExpr = (OWLObjectProperty)headProperty.generateOWL(builder);
		builder.addIndividualAxiom(pExpr, event_ind, headIndividual);
		builder.addAxioms();
	}



	public int getId(){
		return bean.getId();
	}


}
