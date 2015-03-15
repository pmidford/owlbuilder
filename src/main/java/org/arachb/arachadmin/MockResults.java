package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MockResults implements AbstractResults {

	private final Map<Integer,Integer> intResults = new HashMap<Integer,Integer>();
	private final Map<Integer,String> stringResults = new HashMap<Integer,String>();
	private final Map<Integer,Boolean> booleanResults = new HashMap<Integer,Boolean>();
	private final Set<Integer> nullFields = new HashSet<Integer>();
	private int count = 0;
	private int size = 0;
	
	@Override
	public int getInt(int field) throws SQLException {
		if (intResults.containsKey(field)){
			return intResults.get(field);
		}
		else if (nullFields.contains(field)){
			return 0;
		}
		else {
			throw new SQLException("Bad integer field in MockResults: " + field);
		}
	}

	@Override
	public String getString(int field) throws SQLException {
		if (stringResults.containsKey(field)){
			return stringResults.get(field);
		}
		else {
			throw new SQLException("Bad string field in MockResults: " + field);
		}
	}
	
	@Override
	public boolean getBoolean(int field) throws SQLException {
		if (booleanResults.containsKey(field)){
			return booleanResults.get(field);
		}
		else {
			throw new SQLException("Bad boolean field in MockResults: " + field);
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
	
	
	public void setInteger(int f,Integer i){
		if (i != null){
			intResults.put(f, i);
		}
		else {
			nullFields.add(f);
		}
	}
	
	public void setString(int f, String s){
		stringResults.put(f, s);
	}

	public void setSize(int s){
		size = s;
	}
	
}
