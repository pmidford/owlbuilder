package org.arachb.owlbuilder.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.IRIManager;
import org.arachb.arachadmin.PublicationBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;

public class Publication implements GeneratingEntity {

	public final static String DOIPREFIX = "http://dx.doi.org";
	public final static String ARACHBPREFIX = "http://arachb.org/arachb";

	private final PublicationBean bean;

	private String generatedLabel;

	private static Logger log = Logger.getLogger(Publication.class);

	public Publication(PublicationBean b){
		bean = b;
        generatedLabel = generateCitation();
	}



	public boolean hasGeneratedLabel(){
		return (generatedLabel != null);
	}

	public String getGeneratedLabel(){
		return generatedLabel;
	}

    /**
     * generate a citation; much like the python version in arachadmin
     * @return citation with first one or two authors and a year
     */
	public String generateCitation(){
		String authors[] = bean.getAuthorList().split(";");
		if (authors.length == 1){
			String author = authors[0].trim();
			return String.format("%s (%s)", author,bean.getPublicationYear());
		}
		else if (authors.length == 2){
			String author1 = authors[0].trim();
			String author2 = authors[1].trim();
			return String.format("%s and %s (%s)", author1, author2,bean.getPublicationYear());
		}
		else{
			String author = authors[0].trim();
			return String.format("%s et al. (%s)", author, bean.getPublicationYear());
		}
	}

	final private static Map<String,OWLObject> defaultElementMap = new HashMap<String,OWLObject>();

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws Exception {
		OWLObject result = generateOWL(builder, defaultElementMap);
		defaultElementMap.clear();
		return result;
	}


	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements)
			throws Exception {
		final OWLDataFactory factory = builder.getDataFactory();
		final OWLClass pubAboutInvestigationClass =
				factory.getOWLClass(Vocabulary.pubAboutInvestigation);
		String ref_id = get_ref_id(builder);

		final IRI publication_id = IRI.create(ref_id);
		log.info("Publication_id is " + publication_id + " uriset is " + bean.getuidset());
		assert(publication_id != null);
		OWLIndividual pub_ind = factory.getOWLNamedIndividual(publication_id);
		builder.addClassAssertionAxiom(pubAboutInvestigationClass, pub_ind);
		if (hasGeneratedLabel()){
			builder.addLabel(publication_id, getGeneratedLabel());
		}
		final String iComment = "Individual from publication owlgeneration, id = " + bean.getId();
		builder.addComment(publication_id, iComment);
		final String jsonComment = generateJSON(ref_id);
		builder.addComment(publication_id,jsonComment);
		builder.addAxioms();

		return pub_ind;
	}


	static final String HTMLSUFFIX = ".html";

	public String get_ref_id(Owlbuilder b) throws Exception{
		String ref_id = bean.checkIRIString(b.getIRIManager());
		if (ref_id.startsWith(DOIPREFIX)){
			ref_id = IRIManager.cleanupDoi(ref_id);
		}
		return ref_id;
	}

    /**
    Generates a label resembling a text citation
    should work like {@link "https://github.com/pmidford/arachadmin/blob/master/modules/publication_tools.py"}
	@return 'citation' string
    */
	public String make_citation(){
	    String[] authors = bean.getAuthorList().split(";");
	    String year = bean.getPublicationYear();
	    if (authors.length == 1){   // single author
	        String author = authors[0].trim();
	        return String.format("%s (%s)", author, year);
	    }
	    else if (authors.length == 2){
	        String author1 = authors[0].trim();
	        String author2 = authors[1].trim();
	        return String.format("%s and %s (%s)",author1, author2, year);
	    }
	    else{
	        String author = authors[0].trim();
	        return String.format("%s et al. (%s)",author, year);
	    }
	}

	final String JSONSUFFIX = ".json";
	final String STRINGPAIRFORMAT = "\"%s\" : \"%s\",";
	final String STRINGPAIRFORMATNC = "\"%s\" : \"%s\"";
	private String generateJSON(String refId) throws IOException{
		final StringBuilder b = new StringBuilder(200);
		final String nl = System.lineSeparator();
		b.append("{");
		b.append(nl);
		if (bean.getPublicationType() != null){
			b.append(String.format(STRINGPAIRFORMAT, "publicationType", bean.getPublicationType()));
			b.append(nl);
		}
		if (bean.getTitle() != null){
			b.append(String.format(STRINGPAIRFORMAT, "title", bean.getTitle()));
			b.append(nl);
		}
		if (bean.getAlternateTitle() != null){
			b.append(String.format(STRINGPAIRFORMAT, "alternateTitle", bean.getAlternateTitle()));
			b.append(nl);
		}
		if (bean.getAuthorList() != null){
			b.append(String.format(STRINGPAIRFORMAT, "authorList", bean.getAuthorList()));
			b.append(nl);
		}
		if (bean.getEditorList() != null){
			b.append(String.format(STRINGPAIRFORMAT, "editorList", bean.getEditorList()));
			b.append(nl);
		}
		if (bean.getSourcePublication() != null){
			b.append(String.format(STRINGPAIRFORMAT, "sourcePublication", bean.getSourcePublication()));
			b.append(nl);
		}
		if (bean.getVolume() != 0){
			b.append(String.format(STRINGPAIRFORMAT, "volume", Integer.toString(bean.getVolume())));
			b.append(nl);
		}
		if (bean.getIssue() != null){
			b.append(String.format(STRINGPAIRFORMAT, "issue", bean.getIssue()));
			b.append(nl);
		}
		if (bean.getSerialIdentifier() != null){
			b.append(String.format(STRINGPAIRFORMAT, "serialIdentifier", bean.getSerialIdentifier()));
			b.append(nl);
		}
		if (bean.getPageRange() != null){
			b.append(String.format(STRINGPAIRFORMAT, "pageRange", bean.getPageRange()));
			b.append(nl);
		}
		if (bean.getPublicationDate() != null){
			b.append(String.format(STRINGPAIRFORMAT, "publicationDate", bean.getPublicationDate()));
			b.append(nl);
		}
		if (bean.getPublicationYear() != null){
			b.append(String.format(STRINGPAIRFORMAT, "publicationYear", bean.getPublicationYear()));
			b.append(nl);
		}
		if (bean.getCurationStatus() != null){
			b.append(String.format(STRINGPAIRFORMAT, "curationStatus", bean.getCurationStatus()));
			b.append(nl);
		}
		if (bean.getCurationUpdate() != null){
			b.append(String.format(STRINGPAIRFORMAT, "curationUpdate", bean.getCurationUpdate()));
			b.append(nl);
		}
		if (refId.startsWith(DOIPREFIX)){
			b.append(String.format(STRINGPAIRFORMATNC, "doi", refId));
		}
		else{
			b.append(String.format(STRINGPAIRFORMATNC, "localIdentifier", refId));
		}
		b.append(nl);
		b.append("}");
		return b.toString();
	}


	/**
	 *
	 * @param b
	 * @param config
	 * @throws Exception
	 */
	public void generatePublicationPage(Owlbuilder b, Properties config) throws Exception {
		String refId = get_ref_id(b);
		if (refId.startsWith(DOIPREFIX)){
			refId = refId.substring(DOIPREFIX.length()+1);
		}
		else if ((refId.startsWith(ARACHBPREFIX))){
			refId = refId.substring(ARACHBPREFIX.length()+1);
		}
	}



}
