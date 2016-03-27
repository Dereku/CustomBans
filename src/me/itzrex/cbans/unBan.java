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

public class unBan implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	YamlConfiguration config;
	File dataFile;
	
	public unBan(){
		this.config = CustomBans.dconfig;
		this.dataFile = CustomBans.dataFile;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.unban")){
				sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return false;
			}
			config = CustomBans.dconfig;
			dataFile = CustomBans.dataFile;
			if(args.length == 0){
				sender.sendMessage(prefix + "�����������: �6/unban [���]");
				return false;
			}
			if(args.length == 1){
				List<String> banlist = (List<String>) config.getStringList("banlist");
				if(!banlist.contains(args[0])){
					sender.sendMessage(prefix + "������ ����� �� ��� �������.");
					return false;
				}
				banlist.remove(args[0]);
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.unbanned").replace("%admin%", p.getName()).replace("%unbanned%", args[0])));
				}
				config.set("banlist", banlist);
				try {
					config.save(dataFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return false;
	}

}