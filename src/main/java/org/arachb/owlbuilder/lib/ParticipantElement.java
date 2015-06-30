package org.arachb.owlbuilder.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.IndividualBean;
import org.arachb.arachadmin.PElementBean;
import org.arachb.arachadmin.PropertyBean;
import org.arachb.arachadmin.TermBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public class ParticipantElement implements GeneratingEntity {
	
	final static Map<Integer,ParticipantElement> directory = new HashMap<Integer,ParticipantElement>(); 
	
	
	final PElementBean bean;
	
	NamedGeneratingEntity entity;
	final private Map<Integer,Plink> parentLinks = new HashMap<Integer,Plink>();
	final private Map<Integer,Plink> childLinks = new HashMap<Integer,Plink>();

	private static Logger log = Logger.getLogger(ParticipantElement.class);

	
	final static String CONSTRUCTPEBADENTITY = 
			"Bean for constructing Participant Element has neither ClassExpression or Individual";

	/**
	 * Factory method for generating ParticipantElements from beans
	 * @param peb bean specifying the PElement
	 * @return new PElement
	 */
	public static ParticipantElement getElement(PElementBean peb){
		if (ParticipantElement.directory.containsKey(peb.getId())){
			return ParticipantElement.directory.get(peb.getId());
		}
		ParticipantElement newElement = new ParticipantElement(peb);
		ParticipantElement.directory.put(peb.getId(), newElement);
		return newElement;
	}

	private ParticipantElement(PElementBean peb){
		bean = peb;
		if (bean.getTermId() > 0){
			entity = new ClassTerm(TermBean.getCached(bean.getTermId()));
		}
		else if (bean.getIndividualId() > 0){
			log.info("Setting individual for participant element, id = " + bean.getId());
			entity = new Individual(IndividualBean.getCached(bean.getIndividualId()));
		}
		else {
			throw new RuntimeException(CONSTRUCTPEBADENTITY + bean);
		}
	}

	public boolean isIndividual(){
		return entity instanceof Individual;
	}

	/**
	 * Utility for making ParticipantElement Sets from sets of beans
	 */
	public static Set<ParticipantElement> wrapSet(Set<PElementBean> bset){
		final Set<ParticipantElement>result = new HashSet<ParticipantElement>();
		for(PElementBean b : bset){
			result.add(ParticipantElement.getElement(b));
		}
		return result;
	}

	/**
	 * Can't initialize
	 */
	public void resolveLinks(AbstractConnection c) throws Exception{
	for (Integer childIndex : bean.getChildren()){
		Integer childProp = bean.getChildProperty(childIndex);
		Plink newLink = new Plink(childIndex,childProp,c);   //TODO this feels like overdesign
		childLinks.put(childProp, newLink);
	}
	for (Integer parentIndex : bean.getParents()){
		Integer parentProp = bean.getParentProperty(parentIndex);
		Plink newLink = new Plink(parentIndex,parentProp,c);
		parentLinks.put(parentIndex, newLink);
	}
	}


	@Override
	public OWLObject generateOWL(Owlbuilder builder, Map<String, OWLObject> elements) throws Exception {
		if (log.isInfoEnabled()){
			generatingReport();
		}
		return entity.generateOWL(builder, elements);			
	}

	@Override
	public OWLObject generateOWL(Owlbuilder builder) throws Exception {
		if (log.isInfoEnabled()){
			generatingReport();
		}
		return entity.generateOWL(builder);
	}
		
	

	private void generatingReport(){
		if (entity instanceof Individual){
			String iriString = ((Individual)entity).getIRIString();
			log.info(String.format("Participant Element %d is generating code for Individual ",bean.getId(),iriString));
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
	
	public ParticipantElement getParentElement(Integer index){
		return parentLinks.get(index).getElement();
	}


	public PropertyTerm getParentProperty(Integer index){
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

	
	public ParticipantElement getChildElement(Integer index){
		return childLinks.get(index).getElement();
	}


	public PropertyTerm getChildProperty(Integer index){
		return childLinks.get(index).getProperty();
	}

	

	
	
	/* Bean interfaces */

	public int getId(){
		return bean.getId();
	}
	
	static private class Plink{
		private final PropertyTerm property;
		private final ParticipantElement element;	
		
		Plink(Integer elementid, Integer propertyid, AbstractConnection c) throws Exception{
			if (!PropertyBean.isCached(propertyid)){
				c.getProperty(propertyid).cache();
			}
			property = new PropertyTerm(PropertyBean.getCached(propertyid));
			if (PElementBean.isCached(elementid)){
				c.getPElement(elementid).cache();
			}
			element = ParticipantElement.getElement(PElementBean.getCached(elementid));
		}
		
		public PropertyTerm getProperty() {
			return property;
		}
		
		public ParticipantElement getElement() {
			return element;
		}
	}

	
}

