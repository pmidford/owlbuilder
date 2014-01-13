package org.arachb.owlbuilder.lib;

public interface AbstractNamedEntity extends AbstractEntity{
	
	public void setGeneratedId(String id);
	
	public String getUpdateStatement();
	
	/**
	 * need to save the original generated id so old generated id's can
	 * be captured using SAMEAS
	 * @return raw value of generatedId field
	 **/
	public String getGeneratedId();

	public String getIriString();

	/**
	 * Non exception throwing version for use when generating IRI's
	 * probably better than a catch in the appropriate place
	 * @return string for making IRI's or null (means needs to be generated)
	 **/
	public Object checkIriString();

	
}
