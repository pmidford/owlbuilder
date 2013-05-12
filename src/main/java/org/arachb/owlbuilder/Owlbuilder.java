package org.arachb.owlbuilder;

/**
 * Hello world!
 *
 */

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.arachb.owlbuilder.lib.DBConnection;

public class Owlbuilder {

	
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private final DBConnection connection;
	
	public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
	
	private static String temporaryOutput = "test.owl";
	
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	public static void main( String[] args ) throws Exception {
		// Configure log4j
		PropertyConfigurator.configure(Owlbuilder.class.getClassLoader().getResource("log4j.properties"));
		log.info("Trying to start");
		Owlbuilder builder = new Owlbuilder();
		builder.process();
		builder.shutdown();
	}
	
	Owlbuilder() throws Exception{
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		connection = new DBConnection();
	}

	void process() throws Exception{		
		OWLOntology ontology = manager.createOntology(IRI.create(targetIRI));
		File f = new File(temporaryOutput);
		manager.saveOntology(ontology, IRI.create(f.toURI()));

	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase(){
		
	}
	
	
	OWLOntologyManager getOntologyManager(){
		return manager;
	}
	
	OWLDataFactory getDataFactory(){
		return factory;
	}
	
	DBConnection getConnection(){
		return connection;
	}
	
}
