package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.NarrativeBean;
import org.arachb.arachadmin.PublicationBean;
import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
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

	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements) throws Exception {
		final OWLDataFactory factory = builder.getDataFactory();
		IRI individualIRI;
			try {
				String indString = bean.checkIRIString(builder.getIRIManager());
				if (elements.containsKey(indString)){
					return elements.get(indString);
				}
				individualIRI = IRI.create(indString);
				OWLIndividual namedIndividual = factory.getOWLNamedIndividual(individualIRI);
				final String label = getLabelFromNarrativeSet();
				if (label != null){
					log.info("Individual Bean " + indString + " has label " + label);
					OWLAnnotation labelAnno = factory.getOWLAnnotation(factory.getRDFSLabel(),
																	   factory.getOWLLiteral(label));
					OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(individualIRI, labelAnno);
					// Add the axiom to the ontology
					builder.getOntologyManager().addAxiom(builder.getTarget(),ax);
				}
				ClassTerm ct;
				if (TermBean.isCached(bean.getTerm())){
					ct =  new ClassTerm(TermBean.getCached(bean.getTerm()));
				}
				else {
					throw new RuntimeException("Term " + bean.getTerm() + " for individual " + bean.getId() + " is not cached");
				}
				OWLClass cl = (OWLClass)ct.generateOWL(builder, elements);
				OWLClassAssertionAxiom clAssertion = factory.getOWLClassAssertionAxiom(cl, namedIndividual);
				builder.getOntologyManager().addAxiom(builder.getTarget(), clAssertion);
				builder.initializeMiscIndividual(namedIndividual);
				elements.put(indString, namedIndividual);
				return namedIndividual;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
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
	
}
