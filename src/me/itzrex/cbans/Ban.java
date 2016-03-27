package me.itzrex.cbans;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mysql.jdbc.StringUtils;

public class Ban implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			Player p = (Player) sender;
			if(!p.hasPermission("cbans.ban")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(prefix + "§7Используйте: §6/ban [ник] [причина]");
				return true;
			}
			if(args.length == 1){
				try {
					Player target = Bukkit.getPlayer(args[0]);
					if(target.hasPermission("cbans.shield")){
						sender.sendMessage(prefix + "§7Игрок защищён от бана.");
						return true;
					}
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", "Не указана")));
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg").replace("%admin%", p.getName()).replace("%reason%", "Не указана")));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(target.getName())){
							banlist.add(target.getName());
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(target.getName() + ".bannedby", p.getName());
						CustomBans.dconfig.set(target.getName() + ".reason", "Не указана");
						CustomBans.dconfig.set(target.getName() + ".time", getDateTime());
						CustomBans.dconfig.set(target.getName() + ".permament", true);
						CustomBans.dconfig.save(CustomBans.dataFile);
						return true;
					}
				}
						
				} catch (NullPointerException e){
					if(CustomBans.dplayers.getBoolean(args[0])){
						sender.sendMessage(prefix + "§7Игрок защищён от бана.");
						return true;
					}
					sender.sendMessage(prefix + "§7Игрок не найден, блокировка в оффлайн.");
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", "Не указана")));
						List<String> banlist = (List<String>)CustomBans.dconfig.getStringList("banlist");
						if(!banlist.contains(args[0])){
							banlist.add(args[0]);
						CustomBans.dconfig.set("banlist", banlist);
						CustomBans.dconfig.set(args[0] + ".bannedby", p.getName());
						CustomBans.dconfig.set(args[0] + ".reason", "Не указана");
						CustomBans.dconfig.set(args[0] + ".time", getDateTime());
						CustomBans.dconfig.set(args[0] + ".permament", true);
						try {
							CustomBans.dconfig.save(CustomBans.dataFile);
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
					sender.sendMessage(prefix + "§7Игрок защищён от бана.");
					return true;
				}
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", target.getName()).replace("%reason%", reason)));
					target.kickPlayer(ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.targetmsg").replace("%admin%", p.getName()).replace("%reason%", reason)));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
					if(!banlist.contains(target.getName())){
						banlist.add(target.getName());
					CustomBans.dconfig.set("banlist", banlist);
					CustomBans.dconfig.set(target.getName() + ".bannedby", p.getName());
					CustomBans.dconfig.set(target.getName() + ".reason", reason);
					CustomBans.dconfig.set(target.getName() + ".time", getDateTime());
					CustomBans.dconfig.set(target.getName() + ".permament", true);
					CustomBans.dconfig.save(CustomBans.dataFile);
					return true;
				}
			}
				
			} catch (NullPointerException e2){
				if(CustomBans.dplayers.getBoolean(args[0])){
					sender.sendMessage(prefix + "§7Игрок защищён от бана.");
					return true;
				}
				sender.sendMessage(prefix + "§7Игрок не найден, блокировка в оффлайн.");
				for(Player pl : Bukkit.getOnlinePlayers()){
					pl.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.banned").replace("%admin%", p.getName()).replace("%banned%", args[0]).replace("%reason%", reason)));
					List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
					if(!banlist.contains(args[0])){
						banlist.add(args[0]);
					CustomBans.dconfig.set("banlist", banlist);
					CustomBans.dconfig.set(args[0] + ".bannedby", p.getName());
					CustomBans.dconfig.set(args[0] + ".reason", reason);
					CustomBans.dconfig.set(args[0] + ".time", getDateTime());
					CustomBans.dconfig.set(args[0] + ".permament", true);
					try {
						CustomBans.dconfig.save(CustomBans.dataFile);
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
