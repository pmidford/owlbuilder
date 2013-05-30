package org.arachb.owlbuilder;

/**
 * Main class - merges or generates fresh owl file from admindb
 *
 */

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.arachb.owlbuilder.lib.DBConnection;
import org.arachb.owlbuilder.lib.IRIManager;
import org.arachb.owlbuilder.lib.Publication;
import org.arachb.owlbuilder.lib.Taxon;
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
	private final IRIManager iriManager;
	private final DBConnection connection;
	
	private final List<IRI> taxonMiriotList = new ArrayList<IRI>();
	
	
	public static final String targetIRI = "http://arachb.org/arachb/arachb.owl";
	
	private static String temporaryOutput = "test.owl";
	
	private static Logger log = Logger.getLogger(Owlbuilder.class);

	public static void main( String[] args ) throws Exception {
		Owlbuilder builder = null;
		try{
			// Configure log4j
			PropertyConfigurator.configure(Owlbuilder.class.getClassLoader().getResource("log4j.properties"));
			log.info("Trying to start");
			builder = new Owlbuilder();
			builder.process();
		}
		finally{
			builder.shutdown();
		}
	}
	
	Owlbuilder() throws Exception{
		connection = new DBConnection();
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		iriManager = new IRIManager(connection);
	}

	void process() throws Exception{		
		OWLOntology ontology = manager.createOntology(IRI.create(targetIRI));
		processDatabase(ontology);
		log.info("Saving ontology");
		File f = new File(temporaryOutput);
		manager.saveOntology(ontology, IRI.create(f.toURI()));

	}
	
	void shutdown() throws Exception{
		connection.close();
	}

	void processDatabase(OWLOntology ontology) throws Exception{
		log.info("Processing publications");
		processPublications(ontology);
		log.info("Processing term usages");
		processTermUsages(ontology);
		log.info("Processing taxonomy");
		processTaxonomy(ontology);
	}
	
	void processPublications(OWLOntology ontology) throws Exception{
		OWLClass pubAboutInvestigationClass = factory.getOWLClass(IRIManager.pubAboutInvestigation);
		final Set<Publication> pubs = connection.getPublications();
		for (Publication pub : pubs){
			IRI pubID;
			if(pub.get_doi() != null && pub.get_doi() != ""){
				URL clean = cleanupDOI(pub.get_doi());
				pubID = IRI.create(clean);
				//TODO check for existing arachb id - generate owl:sameas
			}
			else{ //generate arachb IRI (maybe temporary)
				pubID = iriManager.getARACHB_IRI();
				pub.set_generated_id(pubID.toString());
				connection.updatePublication(pub);
				//TODO store back into db
			}
			OWLIndividual pub_ind = factory.getOWLNamedIndividual(pubID);
			OWLClassAssertionAxiom classAssertion = 
					factory.getOWLClassAssertionAxiom(pubAboutInvestigationClass, pub_ind); 
			manager.addAxiom(ontology, classAssertion);
		}
	}
	
	void processTermUsages(OWLOntology ontology) throws Exception{
		final Set<Usage> usages = connection.getUsages();
		for (Usage u : usages){
			OWLIndividual ind = factory.getOWLAnonymousIndividual();			
		}		
	}
	
	void processTaxonomy(OWLOntology ontology) throws Exception{
		final Set<Taxon> taxa = connection.getTaxa();
		for (Taxon t : taxa){
			IRI taxonID;
			if (t.get_ncbi_id() != null){
				taxonID = iriManager.getARACHB_IRI();
				t.set_generated_id(taxonID.toString());
				connection.updateTaxon(t);
			}
			else {
				
			}
		}
	}
	
	private void generateTaxonMiriotReport(){
		
	}
	
	private void generateMissinTaxReport(){
		
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
