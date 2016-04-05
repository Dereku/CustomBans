package me.itzrex.cbans.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import me.itzrex.cbans.CustomBans;


public class Database{
	private DatabaseCore core;
	
	public Database(DatabaseCore core) throws ConnectionException{
		try{
			try{
				if(!core.getConnection().isValid(10)){
					throw new ConnectionException("Database doesn not appear to be valid!");
				}
			}
			catch(AbstractMethodError e){
				//You don't need to validate this core.
			}
		}
		catch(SQLException e){
			throw new ConnectionException(e.getMessage());
		}
		
		this.core = core;
	}
	
	public DatabaseCore getCore(){
		return core;
	}
	
	public Connection getConnection(){
		return core.getConnection();
	}
	
	public void execute(String query, Object...objs){
		BufferStatement bs = new BufferStatement(query, objs);
		core.queue(bs);
	}
	
	public boolean hasTable(String table) throws SQLException{
		ResultSet rs = getConnection().getMetaData().getTables(null, null, "%", null);
		while(rs.next()){
			if(table.equalsIgnoreCase(rs.getString("TABLE_NAME"))){
				rs.close();
				return true;
			}
		}
		rs.close();
		return false;
	}
	
	public void close(){
		this.core.close();
	}
	
	public boolean hasColumn(String table, String column) throws SQLException{
		if(!hasTable(table)) return false;
		
		String query = "SELECT * FROM " + table + " LIMIT 0,1";
		try{
			PreparedStatement ps = this.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				rs.getString(column); //Throws an exception if it can't find that column
				return true;
			}
		}
		catch(SQLException e){
			return false;
		}
		return false; //Uh, wtf.
	}
	
	public static class ConnectionException extends Exception{
		private static final long serialVersionUID = 8348749992936357317L;

		public ConnectionException(String msg){
			super(msg);
		}
	}
	
	public void copyTo(Database db) throws SQLException{
		ResultSet rs = getConnection().getMetaData().getTables(null, null, "%", null);
		List<String> tables = new LinkedList<String>();
		while(rs.next()){
			tables.add(rs.getString("TABLE_NAME"));
		}
		rs.close();
		
		core.flush();
		
		//For each table
		for(String table : tables){
			if(table.toLowerCase().startsWith("sqlite_autoindex_")) continue;
			System.out.println("Copying " + table);
			//Wipe the old records
			db.getConnection().prepareStatement("DELETE FROM " + table).execute();
			
			//Fetch all the data from the existing database
			rs = getConnection().prepareStatement("SELECT * FROM " + table).executeQuery();
			
			int n = 0;
			
			//Build the query
			String query = "INSERT INTO " + table + " VALUES (";
			//Append another placeholder for the value
			query += "?";
			for(int i = 2; i <= rs.getMetaData().getColumnCount(); i++){
				//Add the rest of the placeholders and values.  This is so we have (?, ?, ?) and not (?, ?, ?, ).
				query += ", ?";
			}
			//End the query
			query += ")";
			
			PreparedStatement ps = db.getConnection().prepareStatement(query);
			while(rs.next()){
				n++;
				
				for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
					ps.setObject(i, rs.getObject(i));
				}
				
				ps.addBatch();
				
				if(n % 100 == 0){
					ps.executeBatch();
					CustomBans.geInstance().getLogger().info(n + " records copied...");
				}
			}
			ps.executeBatch();
			rs.close();
		}
		db.getConnection().close();
		
		
		this.getConnection().close();
	}
}