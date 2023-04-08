package org.arachb.arachadmin;

import org.apache.log4j.Logger;
import org.arachb.owlbuilder.OwlBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.arachb.owlbuilder.lib.Vocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name="publication")
public class Publication {

    public String getRef_id() {
        return ref_id;
    }

    @Id
    String ref_id;

    String publication_type;
    String dispensation;
    Date download_time;
    Date reviewed_time;

    public String getTitle() {
        return title;
    }

    String title;
    String alternate_title;

    public String getAuthor_list() {
        return author_list;
    }

    String author_list;
    String editor_list;
    String source_publication;
    String volume;
    String issue;
    String serial_identifier;
    String page_range;

    public Date getPublication_date() {
        return publication_date;
    }

    Date publication_date;
    String doi;
    String curation_status;
    Date curation_update;
    String format;
    String language;

    private static Logger log = Logger.getLogger(Publication.class);


    public OWLObject generateOWL(OwlBuilder builder, Map<String, OWLObject> elements) throws Exception {
        final OWLDataFactory factory = builder.getDataFactory();
        final OWLClass pubAboutInvestigationClass =
                factory.getOWLClass(Vocabulary.pubAboutInvestigation);
        final OWLClass narrativeClass = factory.getOWLClass(Vocabulary.narrative_IRI);
        final OWLClass bpClass = factory.getOWLClass(Vocabulary.behavior_phenotype_IRI);

        //TODO if no narratives or claims, don't generate a publication
        String clean_id;
        if (ref_id.startsWith("http://dx.doi.org")){
            clean_id = IRIManager.cleanupDoi(ref_id);
        }
        else {
            clean_id = ref_id;
        }
        final IRI publication_id = IRI.create(clean_id);
        OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
        builder.addClassAssertionAxiom(pubAboutInvestigationClass, pub_ind);
        final String iComment = "Individual from publication owlgeneration, id = " + clean_id;
        builder.addComment(publication_id, iComment);
        builder.addOWLAnnotationToIndividual(pub_ind, Vocabulary.dcTitle, title);
        builder.addLabel(publication_id, generateCitation());
        builder.addAxioms();
        elements.put(clean_id, pub_ind);
        // Process narratives
        Map<String, Narrative> narrativeTable = builder.getNarrativeTableForPublication(this);
        for(String key: narrativeTable.keySet()) {
              OWLIndividual nar = narrativeTable.get(key).generateOWL(builder, elements);
              builder.addClassAssertionAxiom(narrativeClass, nar);
        }

        // Process behavior phenotypes
        Map<String, BehaviorPhenotype> bpTable = builder.getBehaviorPhenotypeTableForPublication(this);
        for(String key: bpTable.keySet()){
            OWLIndividual phenoref = bpTable.get(key).generateOWL(builder, elements);
            log.info("About to add class assertion axiom for " + bpClass + " and " + phenoref.toString());
            builder.addClassAssertionAxiom(bpClass, phenoref);
        }
        builder.addAxioms();
        return pub_ind;
    }

    public String generateCitation(){
        String[] authors = getAuthor_list().split(";");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getPublication_date());
        int year = calendar.get(Calendar.YEAR);
        String result;
        String author;
        switch (authors.length) {
            case 0:
                throw new RuntimeException(String.format("Don't know how to generate citation for publication with no authors: %s",
                        this.getRef_id()));
            case 1:
                author = authors[0].trim();
                result = String.format("%s (%d)", author, year);
                break;

            case 2:
                String author1 = authors[0].trim();
                String author2 = authors[1].trim();
                result = String.format("%s and %s (%d)", author1, author2, year);
                break;

            default:
                author = authors[0].trim();
                result = String.format("%s et al. (%d)", author, year);
        }
        return result;
    }


}


