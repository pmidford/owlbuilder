package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Property {
    public String getRef_id() {
        return ref_id;
    }

    @Id
    String ref_id;
    String authority;

    public String getLabel() {
        return label;
    }

    String label;
    String comment;

}
