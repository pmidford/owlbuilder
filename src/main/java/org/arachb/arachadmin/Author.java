package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Author {
    @Id
    String ref_id;

    String last_name;
    String given_names;
    String assigned_id;
    String generated_id;
    int merge_set_id;

}
