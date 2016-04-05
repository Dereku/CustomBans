package me.itzrex.cbans.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;





import java.util.HashSet;

import me.itzrex.cbans.Ban;
import me.itzrex.cbans.CustomBans;
import me.itzrex.cbans.db.Database;
import me.itzrex.cbans.db.DatabaseHelper;
import me.itzrex.cbans.utils.IPAddress;
import me.itzrex.cbans.utils.TrieSet;

public class BanManager {

	protected CustomBans plugin;
	public static HashMap<String, Banner> bans = new HashMap<String, Banner>();
	public static HashMap<String, Mutter> mutes  = new HashMap<String, Mutter>();
	private HashSet<String> whitelist = new HashSet<String>();
	public static TrieSet players = new TrieSet();
	public static String defaultreason = "Не указана";
	private Database db;
	
	public BanManager(CustomBans plugin){
		this.plugin = plugin;
		this.db = plugin.getDb();
		this.reloadAll();
		
	}
	
	public void reloadAll(){
		this.db = plugin.getDb();
		db.getCore().flush();
		this.bans.clear();
		this.players.clear();
		this.mutes.clear();
		plugin.reloadConfig();
		try{
			DatabaseHelper.setup(db);
		}catch(SQLException e){
			e.printStackTrace();
		}
		String query = "";
		plugin.getLogger().info("Loading database...");
		try {
			db.getConnection().close();
			
			boolean readOnly = plugin.getConfig().getBoolean("connect.readonly", false);
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				plugin.getLogger().info("Loading whitelist...");
				query = "SELECT * FROM whitelist";
				rs = db.getConnection().prepareStatement(query).executeQuery();
				
				while(rs.next()){
					String name = rs.getString("name");
					whitelist.add(name);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				if(!readOnly){
					ps = db.getConnection().prepareStatement("DELETE FROM bans WHERE expires <> 0 AND expires < ?");
					ps.setLong(1, System.currentTimeMillis());
					ps.execute(); 
				}
				plugin.getLogger().info("Loading bans data...");
				query = "SELECT * FROM bans";
				ps = db.getConnection().prepareStatement(query);
				rs = ps.executeQuery();
				while(rs.next()){
					String name = rs.getString("name");
					String reason = rs.getString("reason");
					String banner = rs.getString("banner");
					long expires = rs.getLong("expires");
					long time = rs.getLong("time");
					Banner ban = new Banner(name, reason, banner, time);
					this.bans.put(name.toLowerCase(), ban);
					}
				
			} catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(!readOnly){
					ps = db.getConnection().prepareStatement("DELETE FROM mutes WHERE expires <> 0 AND expires < ?");
					ps.setLong(1, System.currentTimeMillis());
					ps.execute();
				}
				
				plugin.getLogger().info("Loading mutes...");
				query = "SELECT * FROM mutes";
				ps = db.getConnection().prepareStatement(query);
				rs = ps.executeQuery();
				
				while(rs.next()){
					String name = rs.getString("name");
					String banner = rs.getString("muter");
					String reason = rs.getString("reason");
					
					long expires = rs.getLong("expires");
					long time = rs.getLong("time");
					Mutter mute = new Mutter(name, banner, reason, time);
					this.mutes.put(name.toLowerCase(), mute);
					}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
}
    public Mutter getMute(String name) {
    	name = name.toLowerCase();
    	
        Mutter mute = mutes.get(name);
        if (mute != null) {
            return mute;
        }
        return null;
    }
    public void ban(String name, String reason, String banner){
    	name = name.toLowerCase();
    	banner = banner.toLowerCase();
    	
    	this.unban(name); //Ensure they're unbanned first.
    	
    	Banner ban = new Banner(name, reason, banner, System.currentTimeMillis());
    	this.bans.put(name, ban);
    	
    	db.execute("INSERT INTO bans (name, reason, banner, time) VALUES (?, ?, ?, ?)", name, reason, banner, System.currentTimeMillis());
    }
    public void unban(String name){
    	name = name.toLowerCase();
    	Banner ban = this.bans.get(name);
    	
    	if(ban != null){
    		this.bans.remove(name);
    		db.execute("DELETE FROM bans WHERE name = ?", name);
    	}
    }
    public void unmute(String name){
    	name = name.toLowerCase();
    	
    	Mutter mute = this.mutes.get(name);
    	
    	//Escape it
    	if(mute != null){
    		this.mutes.remove(name);
    		db.execute("DELETE FROM mutes WHERE name = ?", name);
    	}
    }
    public Banner getBan(String name){
    	name = name.toLowerCase();
    	
    	Banner ban = bans.get(name);
    	if(ban != null){
     }
		return ban;
    }
    public void mute(String name, String banner, String reason){
    	name = name.toLowerCase();
    	
    	this.unmute(name); //Esnure they're unmuted first.
    	
    	Mutter mute = new Mutter(name, banner, reason, System.currentTimeMillis());
    	this.mutes.put(name, mute);
    	
    	db.execute("INSERT INTO mutes (name, muter, reason, time) VALUES (?, ?, ?, ?)", name, banner, reason, System.currentTimeMillis());
    }
	public boolean isWhitelisted(String name){
		name = name.toLowerCase();
		return whitelist.contains(name);
	}
	public void setWhitelisted(String name, boolean white){
		name = name.toLowerCase();
		if(white){
			whitelist.add(name);
			db.execute("INSERT INTO whitelist (name) VALUES (?)", name);
		}
		else{
			whitelist.remove(name);
			db.execute("DELETE FROM whitelist WHERE name = ?", name);
		}
	}
}
