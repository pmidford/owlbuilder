package org.arachb.owlbuilder;

/**
 * Hello world!
 *
 */

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
	
	public static final String targetIRI = "http://arachb.org/arachb/spider_behavior";
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	public static void main( String[] args ) throws Exception {
		// Configure log4j
		PropertyConfigurator.configure(Owlbuilder.class.getClassLoader().getResource("log4j.properties"));
		log.info("Trying to start");
		Owlbuilder builder = new Owlbuilder();
		builder.process();
	}
	
	Owlbuilder(){
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();		
	}

	void process() throws Exception{
		DBConnection c = new DBConnection();
		
		IRI iri = IRI.create("http://www.semanticweb.org/owlapi/ontologies/ontology#A");
		OWLClass clsAMethodA = factory.getOWLClass(iri);
		PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/owlapi/ontologies/ontology#");
		@SuppressWarnings("unused")
		OWLClass clsAMethodB = factory.getOWLClass(":A", pm);
		OWLOntology ontology = manager.createOntology(IRI.create(targetIRI));
		log.info("Initial signature size: " + ontology.getSignature().size());
		OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(clsAMethodA);
		manager.addAxiom(ontology, declarationAxiom);
		log.info("Final signature size: " + ontology.getSignature().size());
		c.close();
	}

}
