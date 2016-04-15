package me.itzrex.custombans.managers;

import me.itzrex.custombans.CustomBans;
import me.itzrex.custombans.db.Database;
import me.itzrex.custombans.db.DatabaseHelper;
import me.itzrex.custombans.util.Formatter;
import me.itzrex.custombans.util.TrieSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс создан itzRex. Дата: 06.04.2016.
 */
public class BanManager {
    //TODO: Изменить все e.printStackTrace(); на CustomBans.getInstance().getLogger().log(Level.WARNING, "Сообщение ошибки", e);

    private final CustomBans plugin;
    private final TrieSet players = new TrieSet();
    private final HashMap<String, Ban> bans = new HashMap<String, Ban>();
    private final HashMap<String, TempBan> tempbans = new HashMap<String, TempBan>();
    private final HashMap<String, BanIP> ipbans = new HashMap<String, BanIP>();
    private final HashMap<String, TempBanIP> tempipbans = new HashMap<String, TempBanIP>();
    private final HashSet<String> whitelist = new HashSet<String>();
    private final HashMap<String, Mute> mutes = new HashMap<String, Mute>();
    private final HashMap<String, TempMute> tempmutes = new HashMap<String, TempMute>();
    //private HashMap<String, String> actualNames = new HashMap<String, String>();
    private final HashMap<String, String> recentips = new HashMap<String, String>();
    private final HashMap<String, HashSet<String>> iplookup = new HashMap<String, HashSet<String>>();
    private Database db;

    public BanManager(CustomBans plugin){
        this.plugin = plugin;
        this.db = plugin.getDb();
        reload();
    }
    public HashMap<String, Ban> getBans(){
        return bans;
    }
    public HashMap<String, TempBan> getTempBans(){
        return tempbans;
    }
    public HashSet<String> getWhitelist() {
        return this.whitelist;
    }
    public final void reload(){
        this.db = plugin.getDb();
        db.getCore().flush();
        this.bans.clear();
        this.mutes.clear();
        this.recentips.clear();
        this.tempbans.clear();
        this.tempmutes.clear();
        this.ipbans.clear();
        this.tempipbans.clear();
        plugin.reloadConfig();
        try {

            DatabaseHelper.setup(db);
        }catch(SQLException e){
            e.printStackTrace();
        }
        plugin.getLogger().info("Loading DB..");
        String query;
        try {

            db.getConnection().close();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                //Phase 5 loading: Load IP history
                plugin.getLogger().info("Loading ips...");
                query = "SELECT * FROM ips";
                ps = db.getConnection().prepareStatement(query);
                rs = ps.executeQuery();

                while(rs.next()){
                    String name = rs.getString("name").toLowerCase();
                    String ip = rs.getString("ip");

                    this.recentips.put(name, ip); //So we don't need it here
                    HashSet<String> list = this.iplookup.get(ip);
                    if(list == null){
                        list = new HashSet<String>(2);
                        this.iplookup.put(ip, list);
                    }
                    list.add(name); //Or here.
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            try{
                plugin.getLogger().info("Loading ipbans...");
                query = "SELECT * FROM ipbans";
                ps = db.getConnection().prepareStatement(query);
                rs = ps.executeQuery();

                while(rs.next()){
                    String ip = rs.getString("ip");
                    String reason = rs.getString("reason");
                    String banner = rs.getString("banner");

                    long expires = rs.getLong("expires");
                    long time = rs.getLong("time");

                    if(expires != 0){
                        TempBanIP tib = new TempBanIP(ip, reason, banner, time, expires);
                        this.tempipbans.put(ip, tib);
                    }
                    else{
                        BanIP ipban = new BanIP(ip, reason, banner, time);
                        this.ipbans.put(ip, ipban);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            try {
                plugin.getLogger().info("Loading bans...");
                query = "SELECT * FROM bans";
                ps = db.getConnection().prepareStatement(query);
                rs = ps.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    String reason = rs.getString("reason");
                    String banner = rs.getString("banner");
                    long expires = rs.getLong("expires");
                    long time = rs.getLong("time");
                    players.add(name);

                    if(expires != 0){
                        TempBan tb = new TempBan(name, reason, banner, time, expires);
                        this.tempbans.put(name.toLowerCase(), tb);
                    }
                    else {
                        Ban ban = new Ban(name, reason, banner, time);
                        this.bans.put(name.toLowerCase(), ban);
                    }
                }

            } catch(SQLException e){
                e.printStackTrace();
            }

            try {
                plugin.getLogger().info("Loading mutes...");
                query = "SELECT * FROM mutes";
                ps = db.getConnection().prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    String banner = rs.getString("muter");
                    String reason = rs.getString("reason");

                    long expires = rs.getLong("expires");
                    long time = rs.getLong("time");

                    if (expires != 0) {
                        TempMute tmute = new TempMute(name, banner, reason, time, expires);
                        this.tempmutes.put(name.toLowerCase(), tmute);
                    } else {
                        Mute mute = new Mute(name, banner, reason, time);
                        this.mutes.put(name.toLowerCase(), mute);
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

            try {
                plugin.getLogger().info("Loading whitelist...");
                query = "SELECT * FROM whitelist";
                rs = db.getConnection().prepareStatement(query).executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    whitelist.add(name);
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
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
    public boolean isWhitelisted(String name){
        name = name.toLowerCase();
        return whitelist.contains(name);
    }
    public Mute getMute(String name) {
        name = name.toLowerCase();

        Mute mute = mutes.get(name);
        if (mute != null) {
            return mute;
        }
        TempMute tempm = tempmutes.get(name);
        if (tempm !=null) {
            if(!tempm.hasExpired()) {
                return tempm;
            }
            else{
                tempmutes.remove(name);

                db.execute("DELETE FROM mutes WHERE name = ? AND expires <> 0", name);
            }
        }
        return null;
    }
    public BanIP getIPBan(String ip){
        BanIP ipBan = ipbans.get(ip);
        if(ipBan != null){
            return ipBan;
        }

        TempBanIP tempIPBan = tempipbans.get(ip);
        if(tempIPBan != null){
            if(!tempIPBan.hasExpired()){
                return tempIPBan;
            }
            else{
                tempipbans.remove(ip);
                db.execute("DELETE FROM ipbans WHERE ip = ? AND expires <> 0", ip);
            }
        }
        return null;
    }
    public BanIP getIPBan(InetAddress addr){
        return this.getIPBan(addr.getHostAddress());
    }
    public Ban getBan(String name){
        name = name.toLowerCase();

        Ban ban = bans.get(name);
        if(ban != null){
            return ban;
        }

        TempBan tempBan = tempbans.get(name);
        if(tempBan != null){
            if(!tempBan.hasExpired()){
                return tempBan;
            }
            else{
                tempbans.remove(name);
                db.execute("DELETE FROM bans WHERE name = ? AND expires <> 0", name);
            }
        }

        return null;
    }
    public void unbanip(String ip){
        BanIP ipBan = this.ipbans.get(ip);
        TempBanIP tipBan = this.tempipbans.get(ip);

        if(ipBan != null){
            this.ipbans.remove(ip);
            db.execute("DELETE FROM ipbans WHERE ip = ?", ip);
        }
        if(tipBan != null){
            this.tempipbans.remove(ip);
            if(ipBan == null){
                //We still need to delete it from the database
                db.execute("DELETE FROM ipbans WHERE ip = ?", ip);
            }
        }
    }
    public String match(String partial, boolean excludeOnline){
        partial = partial.toLowerCase();
        //Check the name isn't already complete
        String ip = this.recentips.get(partial);
        if(ip != null) return partial; // it's already complete.

        //Check the player and if they're online
        if(excludeOnline == false){
            Player p = Bukkit.getPlayer(partial);
            if(p != null) return p.getName().toLowerCase();
        }

        //Scan the map for the match. Iff one is found, return it.
        String nearestMap = players.nearestKey(partial); // Note that checking the nearest match to an exact name will return the same exact name

        if(nearestMap != null) return nearestMap;

        //We can't help you. Maybe you can not be lazy.
        return partial;
    }
    public void kickIP(final String ip, final String msg){
        Runnable r = () -> {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(isWhitelisted(p.getName()) == false){
                    String pip = getIP(p.getName()); //The players IP, Don't use player.getIP(), incase we use bungee it could be wrong!
                    if(ip.equals(pip)){
                        p.kickPlayer(msg);
                    }
                }
            }
        };


        if(Bukkit.isPrimaryThread()){
            r.run();
        }
        else{
            Bukkit.getScheduler().runTask(CustomBans.getInstance(), r);
        }
    }
    public String getIP(String user){
        if(user == null) return null;
        return this.recentips.get(user.toLowerCase());
    }
    public void ipban(String ip, String reason, String banner){
        banner = banner.toLowerCase();

        this.unbanip(ip); //Ensure it's unbanned first.

        BanIP ipban = new BanIP(ip, reason, banner, System.currentTimeMillis());
        this.ipbans.put(ip, ipban);

        db.execute("INSERT INTO ipbans (ip, reason, banner, time) VALUES (?, ?, ?, ?)", ip, reason, banner, System.currentTimeMillis());
        kickIP(ip, ipban.getKickMessage());
    }
    public void tempipban(String ip, String reason, String banner, long expires){
        banner = banner.toLowerCase();

        this.unbanip(ip); //Ensure it's unbanned first.

        TempBanIP tib = new TempBanIP(ip, reason, banner, System.currentTimeMillis(), expires);
        this.tempipbans.put(ip, tib);

        db.execute("INSERT INTO ipbans (ip, reason, banner, time, expires) VALUES (?, ?, ?, ?, ?)", ip, reason, banner, System.currentTimeMillis(), expires);
        kickIP(ip, tib.getKickMessage());
    }
    public void ban(String name, String reason, String banner){
        name = name.toLowerCase();
        banner = banner.toLowerCase();

        this.unban(name); //Ensure they're unbanned first.

        Ban ban = new Ban(name, reason, banner, System.currentTimeMillis());
        this.bans.put(name, ban);

        db.execute("INSERT INTO bans ('name', 'reason', 'banner', 'time') VALUES ('?', '?', '?', '?')", name, reason, banner, System.currentTimeMillis());
        kick(name, ban.getKickMessage());
    }
    public void kick(final String user, final String msg){
        Runnable r = () -> {
            Player p = Bukkit.getPlayerExact(user);
            if(p != null && p.isOnline()){
                p.kickPlayer(msg);
            }
        };

        if(Bukkit.isPrimaryThread()){
            r.run();
        }
        else{
            Bukkit.getScheduler().runTask(CustomBans.getInstance(), r);
        }
    }
    public void unban(String name){
        name = name.toLowerCase();
        Ban ban = this.bans.get(name);
        TempBan tBan = this.tempbans.get(name);

        if(ban != null){
            this.bans.remove(name);
            db.execute("DELETE FROM bans WHERE name = ?", name);
        }
        if(tBan != null){
            this.tempbans.remove(name);
            if(ban == null){
                //We still need to run this query then.
                db.execute("DELETE FROM bans WHERE name = ?", name);
            }
        }
    }
    public boolean logIP(String name, String ip){
        name = name.toLowerCase();
        String oldIP = this.recentips.get(name);
        if(oldIP != null && ip.equals(oldIP)){
            return false; //Nothing has changed.
        }

        boolean isNew = this.recentips.put(name, ip) == null;
        if(isNew == false){
            HashSet<String> usersFromOldIP = this.iplookup.get(oldIP);
            usersFromOldIP.remove(name);
        }
        else{
            players.add(name); //You're new! Add to autocomplete.
        }

        HashSet<String> usersFromNewIP = this.iplookup.get(ip);
        if(usersFromNewIP == null){
            usersFromNewIP = new HashSet<String>();
            this.iplookup.put(ip, usersFromNewIP);
        }
        usersFromNewIP.add(name);

        if(isNew == false){
            db.execute("UPDATE ips SET ip = ? WHERE name = ?", ip, name);
        }
        else{
            db.execute("INSERT INTO ips (name, ip) VALUES (?, ?)", name, ip);
        }

        return true;
    }
    public void unmute(String name){
        name = name.toLowerCase();

        Mute mute = this.mutes.get(name);
        TempMute tMute = this.tempmutes.get(name);

        //Escape it
        if(mute != null){
            this.mutes.remove(name);
            db.execute("DELETE FROM mutes WHERE name = ?", name);
        }
        if(tMute != null){
            this.tempmutes.remove(name);
            if(mute == null){
                //We still need to delete the mute from the database
                db.execute("DELETE FROM mutes WHERE name = ?", name);
            }
        }
    }
    public void tempban(String name, String reason, String banner, long expires){
        name = name.toLowerCase();
        banner = banner.toLowerCase();

        TempBan ban = new TempBan(name, reason, banner, System.currentTimeMillis(), expires);
        this.tempbans.put(name, ban);

        db.execute("INSERT INTO bans (name, reason, banner, time, expires) VALUES (?, ?, ?, ?, ?)", name, reason, banner, System.currentTimeMillis(), expires);
        kick(name, ban.getKickMessage());
    }
    public void mute(String name, String banner, String reason){
        name = name.toLowerCase();

        this.unmute(name); //Esnure they're unmuted first.

        Mute mute = new Mute(name, banner, reason, System.currentTimeMillis());
        this.mutes.put(name, mute);

        db.execute("INSERT INTO mutes (name, muter, time, reason) VALUES (?, ?, ?)", name, banner, System.currentTimeMillis(), reason);
    }
    public HashMap<String, String> getIPS(){
        return this.recentips;
    }
    public void tempmute(String name, String banner, String reason, long expires){
        name = name.toLowerCase();

        this.unmute(name); //Esnure they're unmuted first.

        TempMute tmute = new TempMute(name, banner, reason, System.currentTimeMillis(), expires);
        this.tempmutes.put(name, tmute);

        db.execute("INSERT INTO mutes (name, muter, time, expires, reason) VALUES (?, ?, ?, ?)", name, banner, System.currentTimeMillis(), expires, reason);
    }
    public void announce(String s){
        announce(s, false, null);
    }
    public void announce(String s, boolean silent, CommandSender sender){
        if(silent){
            s = Formatter.primary + "[Silent] " + s;

            for(Player p : Bukkit.getOnlinePlayers()){
                if(!p.hasPermission("custombans.silent")){
                    return;
                }
                p.sendMessage(s);
            }
            if(sender != null){
                sender.sendMessage(s);
            }
        }
        else{
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(s);
            }
        }
        Bukkit.getConsoleSender().sendMessage(s);
    }
}
