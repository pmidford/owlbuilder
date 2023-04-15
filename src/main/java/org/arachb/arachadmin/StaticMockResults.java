package org.arachb.arachadmin;


public class StaticMockResults extends MockResults {

	
	public StaticMockResults(){
		super(1);
		count = 0;
	}

	
	@Override
	public boolean next(){
		return true;
	}
	
	public void setInteger(int f,Integer i){
		super.setInteger(0, f, i);
	}
	
	public void setString(int f, String s){
		super.setString(0, f, s);
	}

	@Override
	public void reset(){}
	//does nothing
}
