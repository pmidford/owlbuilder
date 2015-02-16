package org.arachb.arachadmin;

import java.util.HashSet;
import java.util.Set;

import org.arachb.owlbuilder.lib.AbstractResults;

public class PElementBean implements BeanBase {
	
	final static int DBID = 1;
	final static int DBTYPE = 2;
	final static int DBPARTICIPANT = 3;
	final static int DBTERM = 4;
	final static int DBINDIVIDUAL = 5;
	
	final static int DBPARENTID = 1;
	final static int DBPARENTPROPERTY = 2;
	
	final static int DBCHILDID = 1;
	final static int DBCHILDPROPERTY = 2;
	
	private int id;
	private int eletype;
	private int participant;
	final private Set<Plink> parentLinks = new HashSet<Plink>();
	final private Set<Plink> childLinks = new HashSet<Plink>();
	
	private ParticipantBean part;
	private IndividualBean ind;
	private TermBean term;
	
	
	
	@Override
	public int getId() {
		return id;
	}

	public int getType(){
		return eletype;
	}
	
	public int getParticipant(){
		return participant;
	}
	


	
	@Override
	public void fill(AbstractResults record) throws Exception {
		id = record.getInt(DBID);
		eletype = record.getInt(DBTYPE);
		participant = record.getInt(DBPARTICIPANT);
	}

	public void fillParents(AbstractResults parentResults) throws Exception{		
		while (parentResults.next()){
			int parentid = parentResults.getInt(DBPARENTID);
			int propertyid = parentResults.getInt(DBCHILDID);
			Plink r = new Plink();
			r.element_id = parentid;
			r.property_id = propertyid;
			r.element = null;
			parentLinks.add(r);
		}
	}

	public void fillChildren(AbstractResults childrenResults) throws Exception{
		while (childrenResults.next()){
			int childid = childrenResults.getInt(DBCHILDID);
			int propertyid = childrenResults.getInt(DBCHILDID);
			Plink r = new Plink();
			r.element_id = childid;
			r.property_id = propertyid;
			r.element = null;
			childLinks.add(r);
			
		}
		
	}

	public Set<Integer[]> getParents(){
		Set<Integer[]> result = new HashSet<Integer[]>();
		for (Plink pl: parentLinks){
			Integer[] l = new Integer[2];
			l[0] = pl.element_id;
			l[1] = pl.property_id;
			result.add(l);
		}
		return result;
	}
	

	public Set<Integer[]> getChildren(){
		Set<Integer[]> result = new HashSet<Integer[]>();
		for (Plink pl: childLinks){
			Integer[] l = new Integer[2];
			l[0] = pl.element_id;
			l[1] = pl.property_id;
			result.add(l);
		}
		return result;
	}

	static private class Plink{
		int element_id;
		int property_id;
		PElementBean element;
	}
}
