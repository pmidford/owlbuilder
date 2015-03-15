package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class PElementBean implements BeanBase {
	
	final static int DBID = 1;
	final static int DBTYPE = 2;
	final static int DBPARTICIPANT = 3;
	
	// these have their own results types
	final static int DBTERM = 1;
	final static int DBINDIVIDUAL = 1;
	
	final static int DBPARENTID = 1;
	final static int DBPARENTPROPERTY = 2;
	
	final static int DBCHILDID = 1;
	final static int DBCHILDPROPERTY = 2;
	
	private int id;
	private int eletype;
	private int participant;
	final private Map<Integer,Plink> parentLinks = new HashMap<Integer,Plink>();
	final private Map<Integer,Plink> childLinks = new HashMap<Integer,Plink>();
	
	private ParticipantBean part = null;
	private IndividualBean individual = null;
	private TermBean term;
	
	private static Logger log = Logger.getLogger(PElementBean.class);
	
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
	
	
	public TermBean getTerm(){
		return term;
	}

	
	public IndividualBean getIndividual(){
		return individual;
	}
	
	
	@Override
	public void fill(AbstractResults record) throws Exception {
		id = record.getInt(DBID);
		log.info("Filling Element: " + this.toString() + " with id: " + id);
		eletype = record.getInt(DBTYPE);
		participant = record.getInt(DBPARTICIPANT);
	}

	public void fillTerm(AbstractResults record, AbstractConnection c) throws SQLException{
		int termId = record.getInt(DBTERM);
		term = c.getTerm(termId);		
	}

	
	public void fillIndividual(AbstractResults record, AbstractConnection c) throws SQLException{
		int individualId = record.getInt(DBINDIVIDUAL);
		individual = c.getIndividual(individualId);
	}

	
	public void fillParents(AbstractResults parentResults, AbstractConnection c) throws Exception{		
		while (parentResults.next()){
			Plink pl = new Plink();
			pl.element_id = parentResults.getInt(DBPARENTID);
			pl.property_id = parentResults.getInt(DBPARENTPROPERTY);
			parentLinks.put(pl.element_id,pl);
		}
	}


	public void fillChildren(AbstractResults childrenResults, AbstractConnection c) throws Exception{
		while (childrenResults.next()){
			Plink pl = new Plink();
			pl.element_id = childrenResults.getInt(DBCHILDID);
			pl.property_id = childrenResults.getInt(DBCHILDPROPERTY);
			childLinks.put(pl.element_id, pl);			
		}
	}
	

	public void resolveParents(ParticipantBean pb, AbstractConnection c) throws Exception{
		resolveDependents(pb, parentLinks, c);
	}
	
	public void resolveChildren (ParticipantBean pb, AbstractConnection c) throws Exception{
		resolveDependents(pb, childLinks,c );
	}
	
	private void resolveDependents(ParticipantBean participantBean, Map<Integer, Plink> links, AbstractConnection c) throws Exception{
		for (Plink pl : links.values()){
			pl.element = participantBean.getElementBean(pl.element_id);
			pl.property = c.getProperty(pl.property_id);
		}
	}



	public Set<Integer> getParents(){
		return parentLinks.keySet();
	}
	
	public PElementBean getParentElement(Integer index){
		return parentLinks.get(index).getElement();
	}


	public PropertyBean getParentProperty(Integer index){
		return parentLinks.get(index).getProperty();
	}

	public Set<Integer> getChildren(){
		return childLinks.keySet();
	}
	
	public PElementBean getChildElement(Integer index){
		return childLinks.get(index).getElement();
	}


	public PropertyBean getChildProperty(Integer index){
		return childLinks.get(index).getProperty();
	}

	static private class Plink{
		private int property_id;
		private int element_id;
		private PropertyBean property;
		private PElementBean element;		
		
		public PropertyBean getProperty() {
			return property;
		}
		
		public PElementBean getElement() {
			return element;
		}
	}
	
	
	
}
