package org.arachb.owlbuilder.lib;

public class Config {

    private String ontologyDir;
    private String importDir;
    private String mireotDir;
    private String cacheDir;

    /**
     * @param configFile will specify a path to the configuration file
     */
    public Config(String configFile){
        //will eventually process a configuration file
        ontologyDir = "//home/midford/Projects/arachtools/website/arachb";
        importDir = "//home/midford/Projects/arachtools/website/import";
        mireotDir = "//home/midford/Projects/arachtools/mireot";
        cacheDir = "//home/midford/Projects/arachcurator/arachcurator/private/ontology_cache";
    }

    public String getOntologyDir(){
        return ontologyDir;
    }

    public String getImportDir(){
        return importDir;
    }

    public String getMireotDir(){
        return mireotDir;
    }

    public String getCacheDir(){
        return cacheDir;
    }

}
