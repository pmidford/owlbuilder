package org.arachb.arachadmin;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.IRI;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class IRIManager {

    private static final Logger log = Logger.getLogger(IRIManager.class);


    //TODO this should be configurable...
    public final static String ARACHBPREFIX = "http://arachb.org/arachb/ARACHB_";

    //TODO maybe this too
    final static String ncbiprefix = "http://purl.obolibrary.org/obo/NCBITaxon_";
    public IRI getNCBI_IRI(String ncbiID){
        return IRI.create(ncbiprefix+ncbiID);
    }


    final static String DOICHARENCODING = "UTF-8";


    private int extractCount(String id){
        if (id != null && id.startsWith(IRIManager.ARACHBPREFIX)){
            try{
                return Integer.parseInt(id.substring(IRIManager.ARACHBPREFIX.length()));
            }
            catch(NumberFormatException e){
                log.warn("Bad id format: " + id);
                return -1;
            }
        }
        return -1;
    }


    /**
     * This cleans up doi's (which tend to have lots of URI unfriendly characters) and returns a properly prefixed doi
     * @param doi
     * @return IRI using using doi prefix
     */
    public static String cleanupDoi(String doi){
        if (doi == null || doi.length() == 0){
            throw new RuntimeException("Invalid empty DOI in publication");
        }
        URL raw;
        String cleanpath;
        try {
            raw = new URL(doi);
            cleanpath = URLEncoder.encode(raw.getPath().substring(1),DOICHARENCODING);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("DOI could not be converted to URL: " + doi);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Claims that " + DOICHARENCODING + " is not supported");
        }

        if (log.isDebugEnabled()){
            log.debug("raw is " + raw);
        }
        if (log.isDebugEnabled()){
            log.debug("clean path is " + cleanpath);
        }
        return IRI.create("http://dx.doi.org/",cleanpath).toString();
    }
}
