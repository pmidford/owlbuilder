package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.arachb.owlbuilder.OwlBuilder;
import org.arachb.owlbuilder.lib.Vocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.Map;

//import static org.arachb.owlbuilder.OwlBuilder.log;

@Entity
public class Narrative {

    private String ref_id;
    private String publication_id;
    private String label;

    private String description;

    public String getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(String publication_id) {
        this.publication_id = publication_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getBehavior_annotation_id() {
        return behavior_annotation_id;
    }

    public void setBehavior_annotation_id(String behavior_annotation_id) {
        this.behavior_annotation_id = behavior_annotation_id;
    }

    private String behavior_annotation_id;

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    @Id
    public String getRef_id() {
        return ref_id;
    }


    public OWLIndividual generateOWL(OwlBuilder builder, Map<String, OWLObject> elements) throws Exception {

        final OWLDataFactory factory = builder.getDataFactory();
        final OWLClass narrativeClass =
                factory.getOWLClass(Vocabulary.narrative_IRI);


        final IRI narrative_id = IRI.create(ref_id);
        OWLIndividual nar_ind = factory.getOWLNamedIndividual(narrative_id);
        builder.addClassAssertionAxiom(narrativeClass, nar_ind);
        OWLIndividual process_ind = factory.getOWLAnonymousIndividual();
        final String iComment = "Individual from narrative owlgeneration, id = " + ref_id;
        builder.addComment(narrative_id, iComment);

        OWLObjectProperty part_of = factory.getOWLObjectProperty(Vocabulary.partOfProperty);
        OWLObjectProperty denotes = factory.getOWLObjectProperty(Vocabulary.denotesProperty);

        // Process events
        Map<Integer, Event> eventTable = builder.getEventTableForNarrative(this);
        for(Integer key: eventTable.keySet()){
            OWLIndividual e = eventTable.get(key).generateOWL(builder, elements);
            factory.getOWLObjectPropertyAssertionAxiom(part_of,e,nar_ind);  // should be process_ind
        }

        builder.addAxioms();
        elements.put(ref_id, nar_ind);

        return nar_ind;
    }



}
