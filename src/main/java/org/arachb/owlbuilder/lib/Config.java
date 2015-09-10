package org.arachb.owlbuilder.lib;

public class Config {
	
	private String ontologyDir;
	private String importDir;
	private String mireotDir;
	private String cacheDir;
	private String targetDir;
	private String testDir;
	
	/**
	 * @param configFile will specify a path to the configuration file 
	 */
	public Config(String configFile){
		//will eventually process a configuration file
		ontologyDir = "//Users/pmidford/Projects/arachtools/website/arachb";
		importDir = "//Users/pmidford/Projects/arachtools/website/import";
		mireotDir = "//Users/pmidford/Projects/arachtools/mireot";
		cacheDir = "//Users/pmidford/Projects/arachadmin/private/ontology_cache";
		targetDir = "//Users/pmidford/Projects/Spider-Behavior/src/main/resources";
		testDir = "//Users/pmidford/Projects/arachtools/OwlBuilder";
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
	
	public String getTargetDir(){
		return targetDir;
	}

	public String getTestDir(){
		return testDir;
	}
	
}
