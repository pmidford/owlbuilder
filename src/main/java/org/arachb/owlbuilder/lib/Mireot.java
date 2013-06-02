package org.arachb.owlbuilder.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;

public class Mireot {
	
	final static String ONTOFOXSERVICEURL = "";
	
	final static String OUTPUTURI = "[URI of the OWL(RDF/XML) output file]";
	final static String SOURCEONTOLOGY = "[Source ontology]";
	final static String LOWLEVELTERMS = "[Low level source term URIs]";
	final static String TOPLEVELTERMS = 
			"[Top level source term URIs and target direct superclass URIs]";
	final static String SOURCERETRIEVAL = "[Source term retrieval setting]";
	final static String ANNOTATIONURIS = "[Source annotation URIs]";
	
	final static String SUBCLASSSPECIFIER = "subClassOf";
	final static String ALLINTERMEDIATESOPTION = "includeAllIntermediates";
	final static String ALLANNOTATIONSOPTION = "includeAllAnnotationProperties";
	
	//TODO this should be in a configuration file!
	final static String SPIDERROOT = 
			" #Araneae (NCBITaxon)";
	
	
	final static String LINESEPARATOR = System.getProperty("line.separator");
	final static String FILESEPARATOR = System.getProperty("file.separator");
	
	//pulled from configuration
	private String mireotDir = null;
	private String importDir = null;
	private String sourceOntology = null;  //full IRI not required by ontofox
	
	private IRI targetOntology = null;
	private IRI superclassIRI = null;
	private Map<IRI,String> sourceTerms = null; 
	private String topIRI = "http://purl.obolibrary.org/obo/NCBITaxon_6893";
	private String topName = "Araneae";
	
	
	
	public void setTargetOntology(IRI t){
		targetOntology = t;
	}
	
	public void setSuperclassIRI(IRI s){
		superclassIRI = s;
	}
	
	public void setMireotDir(String md){
		mireotDir = md;
	}
	
	public void setImportDir(String id){
		importDir = id;
	}
	
	public void setSourceTerms(Map<IRI,String> st){
		sourceTerms = st;
	}
	
	public void setSourceOntology(String s){
		sourceOntology = s;
	}
	
	public void setTop(String i, String name){
		topIRI = i;
		topName = name;
	}
	
	public void generateRequest() throws Exception{
		writeRequestFile();
		processRequest();
		writeImportFile();
	}
	
	
	
	private void writeRequestFile() throws Exception{
		String requestFileStr = 
				mireotDir + FILESEPARATOR + "ontofox" + sourceOntology + "input.txt";
		File requestFile = new File(requestFileStr);
		BufferedWriter requestWriter = new BufferedWriter(new FileWriter(requestFile));
		try {
			requestWriter.write(OUTPUTURI);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(targetOntology.toString());
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(SOURCEONTOLOGY);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(sourceOntology);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(LOWLEVELTERMS);
			requestWriter.write(LINESEPARATOR);			
			for (Map.Entry<IRI, String> e : sourceTerms.entrySet()){
				String s = String.format("%s #%s (%s)",
						e.getKey().toString(), 
						e.getValue(), 
						sourceOntology);
				requestWriter.write(s);
				requestWriter.write(LINESEPARATOR);
			}
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(TOPLEVELTERMS);
			requestWriter.write(LINESEPARATOR);
			String s = String.format("%s #%s (%s)",topIRI,topName,sourceOntology);
			requestWriter.write(s);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(SUBCLASSSPECIFIER);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(SOURCERETRIEVAL);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(ALLINTERMEDIATESOPTION);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(ANNOTATIONURIS);
			requestWriter.write(LINESEPARATOR);
			requestWriter.write(ALLANNOTATIONSOPTION);
			requestWriter.write(LINESEPARATOR);
		}
		finally{
			requestWriter.close();
		}
	}
	
	public void processRequest(){
		
	}
	
	public void writeImportFile(){
		
	}

}
