package me.itzrex.cbans;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.itzrex.cbans.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class unMute implements CommandExecutor {

	/*
	 * Класс, отвечающий за разблокировку мута.
	 */
	public static String prefix = CustomBans.prefix;
	YamlConfiguration config;
	File dataFile;
	
	public unMute(){
		this.config = CustomBans.dconfig2;
		this.dataFile = CustomBans.dataFile2;
	}
	@Override
	//Тоже самое)
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p = sender;
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
				if(CustomBans.geInstance().isMySQL){
					if(!CustomBans.geInstance().getBanManager().mutes.containsKey(args[0].toLowerCase())){
						sender.sendMessage(prefix + "Игрок не был в муте.");
						return true;
					}
					CustomBans.geInstance().getBanManager().unmute(args[0].toLowerCase());
					for(Player pl : Utils.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.unmuted").replace("%admin%", p.getName()).replace("%unmuted%", args[0])));
					}
					CustomBans.geInstance().getLogger().info("Player " + args[0] + " unmuted.");
					return true;
				} else {
					List<String> mutelist = (List<String>) config.getStringList("mutelist");
					if(!mutelist.contains(args[0].toLowerCase())){
						sender.sendMessage(prefix + "Игрок не был в муте.");
						return true;
					}
					mutelist.remove(args[0].toLowerCase());
					for(Player pl : Utils.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.unmuted").replace("%admin%", p.getName()).replace("%unmuted%", args[0])));
					}
					CustomBans.geInstance().getLogger().info("Player " + args[0] + " unmuted.");

					config.set("mutelist", mutelist);
					try {
						config.save(dataFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
		return true;
	}

}
