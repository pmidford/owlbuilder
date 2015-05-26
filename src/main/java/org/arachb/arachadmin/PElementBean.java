package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.arachb.owlbuilder.lib.Individual;



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
	
	private IndividualBean individual = null;
	private TermBean term = null;
	
	
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
	public void fill(AbstractResults record) throws SQLException {
		id = record.getInt(DBID);
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

	
	/**
	 * Current only tests for a term - no need for owlgenerating Term class at present,
	 * so just test and return true if no term (so resolve Individual gets called)
	 * @param c connection that might be used to load beans for a term (if needed)
	 * @return true if no term present
	 */
	public boolean resolveTerm(AbstractConnection c){
		return term == null;
	}
	
	public boolean resolveIndividual(AbstractConnection c){
		if (individual != null){
			generatingIndividual = new Individual(individual);
			return false;
		}
		throw new RuntimeException(
				String.format("Participant Element %s seems to have neither a term or an individual",this));
	}
	
	public void fillParents(AbstractResults parentResults) throws Exception{		
		while (parentResults.next()){
			Plink pl = new Plink();
			pl.element_id = parentResults.getInt(DBPARENTID);
			pl.property_id = parentResults.getInt(DBPARENTPROPERTY);
			parentLinks.put(pl.element_id,pl);
		}
	}


	public void fillChildren(AbstractResults childrenResults) throws Exception{
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
	
	public Integer getSingletonParent(){
		if (parentLinks.size() != 1){
			throw new RuntimeException("PElementBean " + toString() + " has number of parents other than 1");
		}
		return parentLinks.keySet().iterator().next();
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
	
	/**
	 * Convenience method for checking and returning the link to the pelement's child
	 * @return id of plink linking this pelement and its child
	 * @throws RuntimeException - if PElement has more than one child
	 */
	public Integer getSingletonChild(){
		if (childLinks.size() != 1){
			throw new RuntimeException("PElementBean " + toString() + " has number of children other than 1");
		}
		return childLinks.keySet().iterator().next();
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
