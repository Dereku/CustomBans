package me.itzrex.cbans;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class unMute implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	YamlConfiguration config;
	File dataFile;
	
	public unMute(){
		this.config = CustomBans.dconfig2;
		this.dataFile = CustomBans.dataFile2;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.unmute")){
				sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			config = CustomBans.dconfig2;
			dataFile = CustomBans.dataFile2;
			if(args.length == 0){
				sender.sendMessage(prefix + "Используйте: §6/unmute [ник]");
				return true;
			}
			if(args.length == 1){
				List<String> mutelist = (List<String>) config.getStringList("mutelist");
				if(!mutelist.contains(args[0].toLowerCase())){
					sender.sendMessage(prefix + "Данный игрок не был замучен.");
					return true;
				}
				mutelist.remove(args[0].toLowerCase());
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.unmuted").replace("%admin%", p.getName()).replace("%unmuted%", args[0])));
				}
				config.set("mutelist", mutelist);
				try {
					config.save(dataFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return true;
	}

}
