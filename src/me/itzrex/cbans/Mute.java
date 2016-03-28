package me.itzrex.cbans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mute implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.mute")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/mute [ник] [причина]");
				return true;
			}
			if(args.length == 1){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "§7Игрок защищён от мута.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", p.getName()).replace("%muted%", target.getName()).replace("%reason%", "Не указана")));
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(!mutelist.contains(target.getName().toLowerCase())){
							mutelist.add(target.getName().toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".reason", "Не указана");
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".permament", true);
						CustomBans.dconfig2.save(CustomBans.dataFile2);
						return true;
					}
				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0])){
						sender.sendMessage(prefix + "§7Защищён от мута");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", p.getName()).replace("%muted%", args[0]).replace("%reason%", "Не указана")));
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(!mutelist.contains(args[0].toLowerCase())){
							mutelist.add(args[0].toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".reason", "Не указана");
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", true);
						try {
							CustomBans.dconfig2.save(CustomBans.dataFile2);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(args.length < 2 ){
				return true;
			}
			String reason = "Не указана.";
			try {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
				Player target = Bukkit.getPlayer(args[0]);
				if(target.hasPermission("cbans.shield")){
					sender.sendMessage(prefix + "§7Игрок защищён от мута");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", p.getName()).replace("%muted%", target.getName()).replace("%reason%", reason)));
					List<String> mutelist = (List<String>) CustomBans.dconfig2.getStringList("mutelist");
					if(!mutelist.contains(target.getName().toLowerCase())){
						mutelist.add(target.getName().toLowerCase());
					CustomBans.dconfig2.set("mutelist", mutelist);
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".mutedby", p.getName());
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".reason", reason);
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig2.set(target.getName().toLowerCase() + ".permament", true);
					CustomBans.dconfig2.save(CustomBans.dataFile2);
					return true;
				}
			}
				
			} catch (NullPointerException e2){
				if(CustomBans.dplayers.getBoolean(args[0])){
					sender.sendMessage(prefix + "§7Игрок защищён от мута.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", p.getName()).replace("%muted%", args[0]).replace("%reason%", reason)));
					List<String> mutelist = (List<String>) CustomBans.dconfig2.getStringList("mutelist");
					if(!mutelist.contains(args[0].toLowerCase())){
						mutelist.add(args[0].toLowerCase());
					CustomBans.dconfig2.set("mutelist", mutelist);
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".mutedby", p.getName());
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".reason", reason);
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".time", getDateTime());
					CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", true);
					try {
						CustomBans.dconfig2.save(CustomBans.dataFile2);
					} catch (IOException e) {
						e.printStackTrace();
					}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		return true;
}
	  private static String getDateTime()
	  {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    return dateFormat.format(date);
	  }
}
