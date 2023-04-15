package org.arachb.owlbuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.arachb.arachadmin.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBSource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");



    // TODO probably should retrieve the source_url from the uid_set

    public Map<String,String> loadImportSourceMap(EntityManager em){
        final Map<String,String> result = new HashMap<>();
        em.getTransaction().begin();
        List<OntologySource> sources = em.createQuery("SELECT os FROM OntologySource os", OntologySource.class).getResultList();
        for ( OntologySource os  : sources ) {
            System.out.println( "domain: " + os.getDomain() + ";  ref_id:" + os.getRef_id() );
            result.put(os.getRef_id(), os.getDomain());
        }
        em.getTransaction().commit();
        return result;

    }

    static final String TERMDOMAINQUERY =
            "SELECT ref_id FROM term where term.domain = ?";

    public Set<String> getTermIdsByDomain(String domainId) throws SQLException {
        final EntityManager entityManager = emf.createEntityManager();
        final Set<String>idSet = new HashSet<>();
        entityManager.getTransaction().begin();
        List<Term> terms = entityManager.createQuery("SELECT t FROM Term AS t WHERE t.domain like :domain", Term.class)
                .setParameter("domain", domainId)
                .getResultList();
        for (Term t : terms) {
                idSet.add(t.getRef_id());
        }
        System.out.println("Domain " + domainId + " has " + idSet.size() + " terms");
        return idSet;
    }

    public List<Publication> getPublications(EntityManager em){
        final Set<String>idSet = new HashSet<>();
        em.getTransaction().begin();
        List<Publication> result =
                em.createQuery("SELECT p from Publication as p", Publication.class).getResultList();
        em.getTransaction().commit();
        return result;
    }

    public List<Narrative> getNarrativesForPublication(String pub_id, EntityManager em){
        final Set<String>idSet = new HashSet<>();
        em.getTransaction().begin();
        List<Narrative> result =
                em.createQuery("SELECT n FROM Narrative as n WHERE n.publication_id = :pub_id", Narrative.class)
                        .setParameter("pub_id", pub_id)
                        .getResultList();
        em.getTransaction().commit();
        return result;
    }

    public List<BehaviorPhenotype> getBehaviorPhenotypesForPublication(String pub_id, EntityManager em){
        final Set<String>idSet = new HashSet<>();
        em.getTransaction().begin();
        List<BehaviorPhenotype> result = em.createQuery("SELECT p FROM BehaviorPhenotype as p WHERE p.publication_id = :pub_id",BehaviorPhenotype.class)
                .setParameter("pub_id", pub_id)
                .getResultList();
        em.getTransaction().commit();
        return result;
    }

    public List<Event> getEventsForNarrative(String narrative_id, EntityManager em){
        final Set<String>idSet = new HashSet<>();
        em.getTransaction().begin();
        List<Event> result = em.createQuery("SELECT e FROM Event  as e WHERE e.narrative_id = :narrative_id", Event.class)
                .setParameter("narrative_id", narrative_id)
                .getResultList();
        em.getTransaction().commit();
        return result;
    }

    public List<Property> getProperties() {
        final EntityManager entityManager = emf.createEntityManager();
        final Set<String> idSet = new HashSet<>();
        entityManager.getTransaction().begin();
        return entityManager.createQuery("SELECT p from Property as p", Property.class).getResultList();
    }

}
