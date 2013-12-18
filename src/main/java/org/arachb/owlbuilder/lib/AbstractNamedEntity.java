package org.arachb.owlbuilder.lib;

public interface AbstractNamedEntity extends AbstractEntity{
	
	public void setGeneratedID(String id);
	
	public String getUpdateStatement();
	
	public String getIRI_String();

	
}
