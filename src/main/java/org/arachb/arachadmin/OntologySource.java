package org.arachb.arachadmin;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "ontology_source")
public class OntologySource {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OntologySource() {
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    @Id
    private String ref_id;

    private String name;
    private String processing;
    private Date last_update;
    private String authority;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String domain;
    private String subdomain;



}
