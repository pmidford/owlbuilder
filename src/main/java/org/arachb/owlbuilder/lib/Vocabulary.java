package org.arachb.owlbuilder.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;

/**
 * Utility class for OWL IRI constants
 * @author pmidford
 *
 */


public class Vocabulary {


    private Vocabulary() {}  // prevent instantiation

    public static final IRI pubAboutInvestigation =
            IRI.create("http://purl.obolibrary.org/obo/IAO_0000312");
    public static final String pubAboutInvestigationName = "publication about an investigation";

    public static final IRI denotesProperty =
            IRI.create("http://purl.obolibrary.org/obo/IAO_0000219");
    public static final String denotesPropertyName = "denotes";

    public static final IRI informationContentEntity =
            IRI.create("http://purl.obolibrary.org/obo/IAO_0000030");
    public static final String informationContentEntityName = "information content entity";

    public static final IRI textualEntity =
            IRI.create("http://purl.obolibrary.org/obo/IAO_0000300");
    public static final String textualEntityName = "textual entity";

    public static final String dcTitleName = "Title";

    public static final IRI dcTitle = IRI.create("http://purl.org/dc/terms/title");

    private static final Map<IRI, String> iaoImports_mod = new HashMap<>();
    static {
        iaoImports_mod.put(pubAboutInvestigation, pubAboutInvestigationName);
        iaoImports_mod.put(denotesProperty, denotesPropertyName);
        iaoImports_mod.put(informationContentEntity, informationContentEntityName);
        iaoImports_mod.put(dcTitle, dcTitleName);
        iaoImports_mod.put(textualEntity, textualEntityName);
    }
    public static final Map<IRI, String> iaoImports = Collections.unmodifiableMap(iaoImports_mod);

    /* Arachb terms and properties */

    public static final String narrative_label = "narrative";
    public static final IRI narrative_IRI = IRI.create("http://arachb.org/arachb/ARACHB_000000009");

    public static final String behavior_phenotype_label = "behavior phenotype";
    public static final IRI behavior_phenotype_IRI = IRI.create("http://arachb.org/arachb/ARACHB_000000010");

    private static final Map<IRI, String> arachbTerms_mod = new HashMap<>();
    static {
        arachbTerms_mod.put(narrative_IRI, narrative_label);
        arachbTerms_mod.put(behavior_phenotype_IRI, behavior_phenotype_label);
    }

    public static final Map<IRI, String> arachbTerms = Collections.unmodifiableMap(arachbTerms_mod);

    /* BFO and RO properties */
    public static final IRI partOfProperty =
            IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
    public static final String partOfString = "part of";
    // Suppress hasPart - current ontology set allows some reasoners to actually
// equate this with its inverse.  Predictable stupidity ensues.
//	public static final IRI hasPartProperty =
//			IRI.create("http://purl.obolibrary.org/obo/BFO_0000051");
    public static final IRI participatesInProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0000056");
    public static final String participatesInString = "participates in";

    public static final IRI hasParticipantProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0000057");
    public static final String hasParticipantString = "has participant";

    public static final IRI hasActiveParticipantProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002218");
    public static final String hasActiveParticipantString = "has active participant";

    public static final IRI overlapsProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002131");
    public static final String overlapsString = "overlaps";

    public static final IRI activelyParticipatesInProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002217");
    public static final String activelyParticipateInString = "actively participates in";
    public static final IRI mereotopologicallyRelatedToProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002323");
    public static final String mereotopologicallyRelatedString = "mereotopologically related";
    public static final IRI hasMaterialContributionProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002507");
    public static final String hasMaterialContributionString = "has material contribution";
    public static final IRI determinesProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0002508");
    public static final String determinesString = "determines";

    public static final IRI inheresInProperty =
            IRI.create("http://purl.obolibrary.org/obo/RO_0000052");
    public static final String inheresInString = "inheres in";

    public static final IRI realizedInProperty =
            IRI.create("http://purl.obolibrary.org/obo/BFO_0000054");
    public static final String realizedInString = "realized in";

    public static final IRI dispositionIRI =
            IRI.create("http://purl.obolibrary.org/obo/BFO_0000016");
    public static final String dispositionString = "disposition";

    public static final IRI processIRI =
            IRI.create("http://purl.obolibrary.org/obo/BFO_0000015");
    public static final String processString = "process";

    private static final Map<IRI, String> bfoImports_mod = new HashMap<>();
    static {
        bfoImports_mod.put(partOfProperty, partOfString);
        bfoImports_mod.put(hasParticipantProperty, hasParticipantString);
        bfoImports_mod.put(dispositionIRI, dispositionString);
        bfoImports_mod.put(processIRI, processString);
        bfoImports_mod.put(realizedInProperty, realizedInString);
        bfoImports_mod.put(participatesInProperty, participatesInString);
    }
    public static final Map<IRI, String> bfoImports = Collections.unmodifiableMap(bfoImports_mod);

    // NCBI Taxonomy terms
    public static final IRI arachnidaTaxon =
            IRI.create("http://purl.obolibrary.org/obo/NCBITaxon_6854");



}
