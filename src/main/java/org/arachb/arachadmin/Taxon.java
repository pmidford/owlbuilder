package org.arachb.arachadmin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Taxon {

    @Id
    private String ref_id;
    private String name;
    private String author;
    private String year;
    private String authority;
    private String parent;
    private String parent_term;
    private boolean merged;
    private String merge_status;
    private String external_id;

    public Taxon() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent_term() {
        return parent_term;
    }

    public void setParent_term(String parent_term) {
        this.parent_term = parent_term;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public String getMerge_status() {
        return merge_status;
    }

    public void setMerge_status(String merge_status) {
        this.merge_status = merge_status;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }


    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

}
