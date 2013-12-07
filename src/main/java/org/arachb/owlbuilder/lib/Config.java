package org.arachb.owlbuilder.lib;

public class Config {
	
	private String ontologyDir;
	private String importDir;
	private String mireotDir;
	private String cacheDir;
	private String taxonomyTarget;
	
	public Config(String configFile){
		//will eventually process a configuration file
		ontologyDir = "//Users/pmidford/Projects/arachtools/website/arachb";
		importDir = "//Users/pmidford/Projects/arachtools/website/import";
		mireotDir = "//Users/pmidford/Projects/arachtools/mireot";
		cacheDir = "//Users/pmidford/Projects/arachadmin/private/ontology_cache";
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
