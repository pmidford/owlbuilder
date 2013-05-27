package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.Publication;
import org.arachb.owlbuilder.lib.Usage;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

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
				URL clean = cleanupDOI(pub.get_doi());
				OWLIndividual pub_ind = factory.getOWLNamedIndividual(IRI.create(clean));
				OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(
						 IAO312, pub_ind); 
				manager.addAxiom(ontology, classAssertion);
			}
		}
		final Set<Usage> usages = connection.getUsages();
		for (Usage u : usages){
			OWLIndividual usage_ind = factory.getOWLAnonymousIndividual();			
		}
	}
	
	private URL cleanupDOI(String doi) throws Exception{
		URL raw = new URL(doi);
		String cleanpath = URLEncoder.encode(raw.getPath().substring(1),"UTF-8");
		if (log.isDebugEnabled()){
			log.debug("raw is " + raw);
		}
		if (log.isDebugEnabled()){
			log.debug("clean path is " + cleanpath);
		}
		return new URL(raw.getProtocol()+"://"+raw.getHost()+'/'+cleanpath);
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
