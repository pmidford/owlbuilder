package org.arachb.owlbuilder.lib;

public interface AbstractNamedEntity extends AbstractEntity{
	
	public void setGeneratedId(String id);
	
	public String getUpdateStatement();
	
	public String getIriString();

	//need to save the original generated id so old generated id's can
	//be captured using SAMEAS
	public String getGeneratedId();

	
}
