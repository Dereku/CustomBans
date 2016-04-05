package me.itzrex.cbans.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class BufferStatement{
	private Object[] values;
	private String query;
	private Exception stacktrace;
	public BufferStatement(String query, Object... values){
		this.query = query;
		this.values = values;
		this.stacktrace = new Exception(); 
		this.stacktrace.fillInStackTrace(); 
	}
	public PreparedStatement prepareStatement(Connection con) throws SQLException{
		PreparedStatement ps;
		ps = con.prepareStatement(query);
		for(int i = 1; i <= values.length; i++){
			ps.setObject(i, String.valueOf(values[i-1]));
		}
		return ps;
	}
	
	public StackTraceElement[] getStackTrace(){
		return stacktrace.getStackTrace();
	}
	
	@Override
	public String toString(){
		return "Query: " + query + ", values: " + Arrays.toString(values);
	}
}