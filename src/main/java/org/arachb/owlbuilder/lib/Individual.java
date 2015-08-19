package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.NarrativeBean;
import org.arachb.arachadmin.PublicationBean;
import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;

public class Individual implements NamedGeneratingEntity{

	private static Logger log = Logger.getLogger(Individual.class);

	
	private final IndividualBean bean;
	
	
	
	public Individual(IndividualBean ib){
		bean = ib;
	}
	
	
	private final Map<String, OWLObject> localElements = new HashMap<>();
	
	
	@Override	
	public OWLObject generateOWL(Owlbuilder b) throws Exception{
		OWLObject result = generateOWL(b,localElements);
		localElements.clear();
		return result;
	}

	private static final String NOCACHEDTERM = "Term %s for individual %d is not cached";			
	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements) throws Exception {
		final OWLDataFactory factory = builder.getDataFactory();
		IRI individualIRI;
		String indString = bean.checkIRIString(builder.getIRIManager());
		if (elements.containsKey(indString)){
			return elements.get(indString);
		}
		individualIRI = IRI.create(indString);
		OWLIndividual namedIndividual = factory.getOWLNamedIndividual(individualIRI);
		builder.initializeMiscIndividual(namedIndividual);
		final String label = getLabelFromNarrativeSet();
		if (label != null){
			log.info("Individual Bean " + indString + " has label " + label);
			builder.addLabel(individualIRI, label);
		}
		final String iComment = "Individual from individual owlgeneration, id = " + bean.getId();
		builder.addComment(individualIRI, iComment);
		if (!TermBean.isCached(bean.getTerm())){
			throw new RuntimeException(String.format(NOCACHEDTERM, bean.getTerm(), bean.getId()));
		}
		ClassTerm ct =  new ClassTerm(TermBean.getCached(bean.getTerm()));
		OWLClass cl = (OWLClass)ct.generateOWL(builder, elements);
		builder.addClassAssertionAxiom(cl, namedIndividual);
		builder.addAxioms();
		builder.initializeMiscIndividual(namedIndividual);
		elements.put(indString, namedIndividual);
		return namedIndividual;
	}
	
	private String getLabelFromNarrativeSet(){
		switch (bean.getNaratives().size()){
		case 0: return bean.getLabel();
		case 1: Set<Integer>nSet = bean.getNaratives();
				int narrativeId = nSet.iterator().next();
				NarrativeBean narrative = NarrativeBean.getCached(narrativeId);
				return labelFromNarrative(narrative);
		default:
			return bean.getLabel() + " in multiple narratives";
		}
	}
	
	private String labelFromNarrative(NarrativeBean n){
		log.info("publication cache size = " + PublicationBean.cacheSize());
		PublicationBean p = PublicationBean.getCached(n.getPublicationId());
		String pubStr = p.getAuthorList()+"("+p.getPublicationYear()+")";
		return bean.getLabel() + " in " + n.getLabel() + " contained in " + pubStr;
	}
	

	/* methods to expose bean fields */

	public String getIRIString(){
		return bean.getIRIString();
	}
	
}
