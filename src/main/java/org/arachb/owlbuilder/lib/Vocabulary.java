package org.arachb.owlbuilder.lib;

import java.util.HashSet;
import java.util.Set;

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
	public static final IRI denotesProperty =
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000219");
	public static final IRI informationContentEntity = 
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000030");
	public static final IRI textualEntity =
			IRI.create("http://purl.obolibrary.org/obo/IAO_0000300");

	public static final Set<IRI> iaoImports = new HashSet<IRI>();
	static {
		iaoImports.add(pubAboutInvestigation);
		iaoImports.add(denotesProperty);
		iaoImports.add(informationContentEntity);
	}
	
	public static final IRI partOfProperty = 
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000050");
	public static final IRI hasPartProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000051");
	public static final IRI participatesInProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000056");
	public static final IRI hasParticipantProperty =
			IRI.create("http://purl.obolibrary.org/obo/RO_0000057");
	public static final IRI hasActiveParticipantProperty =
			IRI.create("http://purl.obolibrary.org/obo/RO_0002218");
	public static final IRI hasParticipantAtSomeTimeProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0000057");
	public static final IRI overlapsProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0002131");
	public static final IRI activelyParticipatesInProperty =
			IRI.create("http://purl.obolibrary.org/obo/RO_0002217");
	public static final IRI mereotopologicallyRelatedToProperty =
			IRI.create("http://purl.obolibrary.org/obo/BFO_0002323");
	public static final IRI hasMaterialContributionProperty =
			IRI.create("http://purl.obolibrary.org/obo/RO_0002507");
	public static final IRI determinesProperty =
			IRI.create("http://purl.obolibrary.org/obo/RO_0002508");



	public static final Set<IRI> bfoImports = new HashSet<IRI>();
	static {
		bfoImports.add(partOfProperty);
		bfoImports.add(hasParticipantProperty);
	}
	
	public static final IRI arachnidaTaxon = 
			IRI.create("http://purl.obolibrary.org/obo/NCBITaxon_6854");



}
