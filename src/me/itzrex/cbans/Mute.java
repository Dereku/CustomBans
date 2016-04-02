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

	/*
	 * Класс, отвечающий за мут
	 */
	public static String prefix = CustomBans.prefix;
	static String reason;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSender p = sender;
			if(!p.hasPermission("cbans.mute")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/mute [ник] [причина]");
				return true;
			}
			if(args.length == 1){
				reason = "Не указана";
			} else if(args.length < 2 ){
				return true;
			} else {
				reason = org.apache.commons.lang.StringUtils.join(args, ' ', 1, args.length);
			}
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						if (sender.hasPermission("cbans.shieldbypass")) {
							sender.sendMessage(" ");
								} else {
									sender.sendMessage(prefix + "§7Игрок защищён от мута.");
									return true;
								}
						}
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(mutelist.contains(target.getName().toLowerCase())){
							sender.sendMessage(ChatColor.RED + "Игрок уже замутен.");
							return true;
						} else {
							mutelist.add(target.getName().toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".reason", reason);
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(target.getName().toLowerCase() + ".permament", true);
						CustomBans.dconfig2.save(CustomBans.dataFile2);
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", sender.getName()).replace("%muted%", target.getName()).replace("%reason%", reason)));
						}
						CustomBans.geInstance().getLogger().info("Player " + target.getName() + " muted.");
						return true;

				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0].toLowerCase())){
						if (sender.hasPermission("cbans.shieldbypass")) {
							sender.sendMessage(" ");
								} else {
									sender.sendMessage(prefix + "§7Игрок защищён от мута.");
									return true;
								}
						}
						List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
						if(!mutelist.contains(args[0].toLowerCase())){
							mutelist.add(args[0].toLowerCase());
						CustomBans.dconfig2.set("mutelist", mutelist);
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".mutedby", p.getName());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".reason", reason);
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".time", getDateTime());
						CustomBans.dconfig2.set(args[0].toLowerCase() + ".permament", true);
						CustomBans.geInstance().getLogger().info("Player " + args[0] + " muted.");
						for(Player pl : Utils.getOnlinePlayers()){
							pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.muted").replace("%admin%", p.getName()).replace("%muted%", args[0]).replace("%reason%", reason)));
						}
						try {
							CustomBans.dconfig2.save(CustomBans.dataFile2);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}  else {
						sender.sendMessage(ChatColor.RED + "Игрок уже замутен.");
						return true;
					}

				} catch (Exception e) {
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
