package org.arachb.arachadmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class MockResults implements AbstractResults, ResetableResults{

	protected int count = -1;
	final private int size;
	final private RowResult[] rows;
	
    private static Logger log = Logger.getLogger(MockResults.class);

	public MockResults(int rowCount){
		size = rowCount;
		rows = new RowResult[size];
		for (int i=0;i< size;i++){
			rows[i] = new RowResult();
		}
	}
	
	final public static MockResults NullResults = new MockResults(0);
	
	@Override
	public int getInt(int field) throws SQLException {
		if (rows[count].intResults.containsKey(field)){
			return rows[count].intResults.get(field);
		}
		else if (rows[count].nullFields.contains(field)){
			return 0;
		}
		else {
			throw new SQLException("Bad integer field in MockResults: " + field);
		}
	}

	@Override
	public String getString(int field) throws SQLException {
		if (rows[count].stringResults.containsKey(field)){
			return rows[count].stringResults.get(field);
		}
		else {
			throw new SQLException("Bad string field in MockResults: " + field);
		}
	}
	
	@Override
	public boolean getBoolean(int field) throws SQLException {
		if (rows[count].booleanResults.containsKey(field)){
			return rows[count].booleanResults.get(field);
		}
		else {
			throw new SQLException("Bad boolean field in MockResults: " + field);
		}
	}
	

	@Override
	public boolean next() {
		if (count < size-1){
			count++;
			return true;
		}
		return false;
	}
	
	public void reset(){
		count = -1;
	}
	
	public void setInteger(int row,int f,Integer i){
		if (i != null){
			rows[row].intResults.put(f, i);
		}
		else {
			rows[row].nullFields.add(f);
		}
	}
	
	public void setString(int row, int f, String s){
		rows[row].stringResults.put(f, s);
	}

	
	
	private static class RowResult{
		private final Map<Integer,Integer> intResults = new HashMap<Integer,Integer>();
		private final Map<Integer,String> stringResults = new HashMap<Integer,String>();
		private final Map<Integer,Boolean> booleanResults = new HashMap<Integer,Boolean>();
		private final Set<Integer> nullFields = new HashSet<Integer>();

	}
}
