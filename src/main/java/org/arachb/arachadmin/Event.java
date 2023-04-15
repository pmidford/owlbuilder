package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.arachb.owlbuilder.OwlBuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Map;

@Entity
public class Event {
    @Id int local_id;
    private String narrative_id;
    private String publication_behavior;
    private String behavior_term_id;
    private String evidence_id;

    public Integer get_local_id() {
        return local_id;
    }

    public OWLIndividual generateOWL(OwlBuilder builder, Map<String, OWLObject> elements){
        OWLIndividual e = builder.getDataFactory().getOWLAnonymousIndividual();
        OWLClass eventClass = builder.getDataFactory().getOWLClass(IRI.create(behavior_term_id));
        builder.addClassAssertionAxiom(eventClass,e);
        OwlBuilder.log.info("Adding event " + publication_behavior);
        return e;
    }
}
