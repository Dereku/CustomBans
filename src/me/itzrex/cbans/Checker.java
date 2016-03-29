package me.itzrex.cbans;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Checker implements CommandExecutor {
    //TODO send.sendMessage("Игрок был забанен, замучен, и тд(Для отображения в консоли)"
	/*
	 * Класс, отвечающий за проверку бана.
	 */
	public static String prefix = CustomBans.prefix;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((sender instanceof Player)){
			CommandSender p = sender;
			if(!p.hasPermission("cbans.check")){
				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
				return false;
			}
			if(args.length == 0){
				p.sendMessage(prefix + "§7Используйте: §6/checkban [ник]");
				return false;
			}
			if(args.length == 1){
		        FileConfiguration configuration = CustomBans.dconfig;
		        List<String> banlist = configuration.getStringList("banlist");
		        //Проверяем наличие игрока в банлисте.
		        if (banlist.contains(args[0].toLowerCase())){
		        	//Вывод данных.
					String reason = CustomBans.dconfig.getString(args[0].toLowerCase() + ".reason");
					String time = CustomBans.dconfig.getString(args[0].toLowerCase() + ".time");
					String bannedby = CustomBans.dconfig.getString(args[0].toLowerCase() + ".bannedby");
					p.sendMessage("§7Информация о игроке: §c" + args[0]);
					p.sendMessage("§7Причина: §c" + reason);
					p.sendMessage("§7Время бана: §c" + time);
					p.sendMessage("§7Забанил §c" + bannedby);
					return false;
		        } else {
		        	p.sendMessage(prefix + "§7Игрок не забанен.");
		        	return false;
		        }
			}
		}
		return false;
	}

}
