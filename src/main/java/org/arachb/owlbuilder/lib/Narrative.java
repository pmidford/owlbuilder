/**
 * 
 */
package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.ClaimBean;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.NarrativeBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author pmidford
 *
 */
public class Narrative implements NamedGeneratingEntity {
	
	private static Logger log = Logger.getLogger(Narrative.class);

	
	private final NarrativeBean bean;
	
	
	public Narrative(NarrativeBean nb){
		bean = nb;
	}
	
	public static Set<Narrative> wrapSet(Set<NarrativeBean> nset) {
		final Set<Narrative>result = new HashSet<>();
		for(NarrativeBean b : nset){
			result.add(new Narrative(b));
		}
		return result;
	}

	private final Map<String, OWLObject> localElements = new HashMap<>();
	


	/* (non-Javadoc)
	 * @see org.arachb.owlbuilder.lib.GeneratingEntity#generateOWL(org.arachb.owlbuilder.Owlbuilder, java.util.Map)
	 */
	@Override
	public OWLObject generateOWL(Owlbuilder b, Map<String, OWLObject> elements) throws Exception {
		final OWLDataFactory factory = b.getDataFactory();
		final OWLOntologyManager manager = b.getOntologyManager();
		final OWLOntology target = b.getTarget();
		final OWLIndividual nar_ind = factory.getOWLNamedIndividual(IRI.create(bean.getIRIString()));
		final OWLClass informationContentClass =
				factory.getOWLClass(Vocabulary.informationContentEntity);
		final Set<OWLAxiom> allaxioms = new HashSet<OWLAxiom>();
		allaxioms.add(factory.getOWLClassAssertionAxiom(informationContentClass, nar_ind));
		manager.addAxioms(target, allaxioms);
		IRI narrativeIRI = IRI.create(bean.getIRIString());
		OWLObject result = factory.getOWLNamedIndividual(narrativeIRI);
		elements.put(narrativeIRI.toString(), result);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.arachb.owlbuilder.lib.GeneratingEntity#generateOWL(org.arachb.owlbuilder.Owlbuilder)
	 */
	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception {
		OWLObject result = generateOWL(b,localElements);
		localElements.clear();
		return result;
	}


}
