package me.itzrex.cbans.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.itzrex.cbans.utils.RexUtil;

import org.bukkit.ChatColor;

public class DatabaseHelper{
	public static void setup(Database db) throws SQLException{
		createTables(db);
	}
	public static void createTables(Database db) throws SQLException{
		if(!db.hasTable("bans")){
			createBanTable(db);
		}
		if(!db.hasTable("mutes")){
			createMuteTable(db);
		}
		if(!db.hasTable("whitelist")){
			createWhitelistTable(db);
		}
		if(!db.hasColumn("mutes", "reason")){
			try{
				db.getConnection().prepareStatement("ALTER TABLE mutes ADD COLUMN reason TEXT(100)").execute();
				System.out.println("Updating mutes table (Adding reason column)");
			}
			catch(SQLException e){}
		}
	}
	public static void createWhitelistTable(Database db){
		String query = "CREATE TABLE whitelist (name TEXT(30) NOT NULL)";
		try {
			Statement st = db.getConnection().createStatement();
			st.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Could not create whitelist table.");
		}
	}
	public static void createBanTable(Database db){
		String query = "CREATE TABLE bans ( name  TEXT(30) NOT NULL, reason  TEXT(100), banner  TEXT(30), time  BIGINT NOT NULL DEFAULT 0, expires  BIGINT NOT NULL DEFAULT 0 );";
		try {
			Statement st = db.getConnection().createStatement();
			st.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Could not create bans table.");
		}
	}
	public static void createMuteTable(Database db){
		String query = "CREATE TABLE mutes ( name  TEXT(30) NOT NULL, muter  TEXT(30), reason TEXT(100) NOT NULL, time  BIGINT DEFAULT 0, expires  BIGINT DEFAULT 0);";
		try {
			Statement st = db.getConnection().createStatement();
			st.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(ChatColor.RED + "Could not create mutes table.");
		}
	}
}