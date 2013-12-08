package org.arachb.owlbuilder.lib;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MockResults implements AbstractResults {

	private final Map<String,Integer> intResults = new HashMap<String,Integer>();
	private final Map<String,String> stringResults = new HashMap<String,String>();
	private int count = 0;
	private int size = 0;
	
	@Override
	public int getInt(String field) throws SQLException {
		if (intResults.containsKey(field)){
			return intResults.get(field);
		}
		else {
			throw new SQLException("Bad integer field in MockResults: " + field);
		}
	}

	@Override
	public String getString(String field) throws SQLException {
		if (stringResults.containsKey(field)){
			return stringResults.get(field);
		}
		else {
			throw new SQLException("Bad string field in MockResults: " + field);
		}
	}

	@Override
	public boolean next() {
		if (count < size){
			count++;
			return true;
		}
		return false;
	}
	
	
	public void setInteger(String s,int i){
		intResults.put(s, i);
	}
	
	public void setString(String s, String s2){
		stringResults.put(s, s2);
	}

	public void setSize(int s){
		size = s;
	}
	
}
