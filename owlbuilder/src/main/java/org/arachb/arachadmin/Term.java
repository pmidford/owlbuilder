package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Term {
    @Id
    private String ref_id;

    private String authority;
    private String domain;
    private String label;
    private String comment;
    //private int dummy_id;

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }
}
