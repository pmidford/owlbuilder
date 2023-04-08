package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.arachb.owlbuilder.OwlBuilder;
import org.arachb.owlbuilder.lib.Vocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.Map;

@Entity
@Table(name = "behavior_phenotype")
public class BehaviorPhenotype {
    @Id
    private String ref_id;
    private String publication_id;
    private String publication_behavior;
    private String behavior_term_id;
    private String evidence_id;
    private String bearer_term_id;


    public String getRef_id() {
        return ref_id;
    }


    public OWLIndividual generateOWL(OwlBuilder builder, Map<String, OWLObject> elements) {

        final OWLDataFactory factory = builder.getDataFactory();
        //final OWLClass bpClass =
        //        factory.getOWLClass(Vocabulary.behavior_phenotype_IRI);
        final OWLClass textEntityClass = factory.getOWLClass(Vocabulary.textualEntity);
        // need to build a full class expression here
        final OWLClass dispositionClass = factory.getOWLClass(Vocabulary.dispositionIRI);
        final OWLClass bearerClass = factory.getOWLClass(IRI.create(bearer_term_id));
        final OWLObjectProperty inheres_in = factory.getOWLObjectProperty(Vocabulary.inheresInProperty);
        final OWLObjectProperty realized_in = factory.getOWLObjectProperty(Vocabulary.realizedInProperty);
        final OWLClass behaviorClass = factory.getOWLClass(IRI.create(behavior_term_id));
        final OWLClassExpression realizedInExpression = factory.getOWLObjectSomeValuesFrom(realized_in, behaviorClass);
        final OWLClassExpression inheresInExpression = factory.getOWLObjectSomeValuesFrom(inheres_in,bearerClass);
        final OWLObjectIntersectionOf dispositionIntersection =
                factory.getOWLObjectIntersectionOf(inheresInExpression,
                        realizedInExpression,
                        dispositionClass);
        final OWLObjectProperty denotes_relation = factory.getOWLObjectProperty(Vocabulary.denotesProperty);
        OWLIndividual text_fragment = factory.getOWLAnonymousIndividual();
        builder.addClassAssertionAxiom(textEntityClass,text_fragment);

        final IRI bp_id = IRI.create(ref_id);
        OWLIndividual bp_ind = factory.getOWLNamedIndividual(bp_id);
        builder.addClassAssertionAxiom(dispositionIntersection, bp_ind);
        builder.addOWLObjectPropertyAssertion(denotes_relation, text_fragment, bp_ind);
        final String iComment = "Individual from behavior phenotype owlgeneration, id = " + ref_id;
        builder.addComment(bp_id, iComment);


        builder.addAxioms();
        elements.put(ref_id, bp_ind);

        return bp_ind;
    }

}
