package me.itzrex.custombans.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.itzrex.custombans.util.Util;
import org.bukkit.ChatColor;

public class DatabaseHelper{
    public static void setup(Database db) throws SQLException{
        createTables(db);
    }
    public static void createTables(Database db) throws SQLException{
        if(!db.hasTable("ips")){
            createIPHistoryTable(db);
        }
        if(!db.hasTable("bans")){
            createBanTable(db);
        }
        if(!db.hasTable("ipbans")){
            createIPBanTable(db);
        }
        if(!db.hasTable("whitelist")){
            createWhitelistTable(db);
        }
        if(!db.hasTable("mutes")){
            createMuteTable(db);
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
        String query = "CREATE TABLE bans ( name  TEXT(30) NOT NULL, reason  TEXT(100) NOT NULL, banner  TEXT(30) NOT NULL, time  BIGINT NOT NULL DEFAULT 0, expires  BIGINT NOT NULL DEFAULT 0 );";
        try {
            Statement st = db.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ChatColor.RED + "Could not create bans table.");
        }
    }
    public static void createIPBanTable(Database db){
        String query = "CREATE TABLE ipbans ( ip  TEXT(20) NOT NULL, reason  TEXT(100) NOT NULL, banner  TEXT(30) NOT NULL, time  BIGINT NOT NULL DEFAULT 0, expires  BIGINT NOT NULL DEFAULT 0 );";
        try {
            Statement st = db.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ChatColor.RED + "Could not create ipbans table.");
        }
    }
    public static void createIPHistoryTable(Database db){
        String query = "CREATE TABLE ips ( name  TEXT(30) NOT NULL, ip  TEXT(20) NOT NULL);";
        try {
            Statement st = db.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ChatColor.RED + "Could not create ips table.");
        }
    }
    public static void createMuteTable(Database db){
        String query = "CREATE TABLE mutes ( name  TEXT(30) NOT NULL, muter  TEXT(30), time  BIGINT DEFAULT 0, expires  BIGINT DEFAULT 0, reason  TEXT(100) NOT NULL );";
        try {
            Statement st = db.getConnection().createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ChatColor.RED + "Could not create mutes table.");
        }
    }
}