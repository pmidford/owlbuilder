/**
 * 
 */
package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.NarrativeBean;
import org.arachb.arachadmin.PublicationBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;

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
		final IRI nar_iri = IRI.create(bean.getIRIString());
		final OWLIndividual nar_ind = factory.getOWLNamedIndividual(nar_iri);
		final OWLClass informationContentClass =
				factory.getOWLClass(Vocabulary.informationContentEntity);
		b.addClassAssertionAxiom(informationContentClass, nar_ind);
		PublicationBean pb = null;
		if (!PublicationBean.isCached(bean.getPublicationId())){
			pb = b.getConnection().getPublication(bean.getPublicationId());
			pb.cache();
		}
		else{
			pb = PublicationBean.getCached(bean.getPublicationId());
		}
		Publication pub = new Publication(pb);
		OWLIndividual pub_ind = (OWLIndividual)pub.generateOWL(b,elements);
		b.addIndividualPartOfAxiom(nar_ind, pub_ind);
		final String label = bean.getLabel();
		if (label != null){
			b.addLabel(nar_iri,label);
		}
		if (bean.getDescription() != null){
			final String iComment = String.format(bean.getDescription(),bean.getId());
			b.addComment(nar_iri, iComment);
		}
		b.addAxioms();
		elements.put(nar_iri.toString(), nar_ind);
		return nar_ind;
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
