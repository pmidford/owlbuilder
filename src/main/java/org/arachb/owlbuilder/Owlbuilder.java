package org.arachb.owlbuilder;

/**
 * Hello world!
 *
 */

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.Publication;

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
		processDatabase(ontology);
		File f = new File(temporaryOutput);
		manager.saveOntology(ontology, IRI.create(f.toURI()));

	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase(OWLOntology ontology) throws Exception{
		IRI pubAboutInvestigation = IRI.create("http://purl.obolibrary.org/obo/IAO_0000312");
		OWLClass IAO312 = factory.getOWLClass(pubAboutInvestigation);
		final Set<Publication> pubs = connection.getPublications();
		for (Publication pub : pubs){
			if(pub.get_doi() != null && pub.get_doi() != ""){
				URL raw = new URL(pub.get_doi());
				String cleanpath = URLEncoder.encode(raw.getPath().substring(1),"UTF-8");
				System.out.println("raw is " + raw);
				System.out.println("clean path is " + cleanpath);
				URL clean = new URL(raw.getProtocol()+"://"+raw.getHost()+'/'+cleanpath);
				OWLIndividual pub_ind = factory.getOWLNamedIndividual(IRI.create(clean));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(
						 IAO312, pub_ind); 
				manager.addAxiom(ontology, classAssertion);
			}
		}
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
