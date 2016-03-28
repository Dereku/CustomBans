package me.itzrex.cbans.inv;

import java.util.List;

import me.itzrex.cbans.CustomBans;
import me.itzrex.cbans.utils.Item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class InvCmd implements CommandExecutor {

	public static String prefix = CustomBans.prefix;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(!p.hasPermission("cbans.baninv")){
			p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', CustomBans.geInstance().getConfig().getString("messages.noperm")));
			return true;
		}
		if(args.length == 0){
			p.sendMessage(prefix + "§7Используйте: §6/baninv [ник]");
			return true;
		}
		if(args.length == 1){
			List<String> banlist = CustomBans.dconfig.getStringList("banlist");
			if(!banlist.contains(args[0])){
				p.sendMessage(prefix + "Игрок не был забанен");
				return true;
			}
			Inventory inv = Bukkit.createInventory(null, 9, "Информация");
			Item i = new Item(Material.SKULL_ITEM);
			i.setName("§c" + args[0]);
			i.setData(3);
			i.setSkullOwner(args[0]);
			i.addLore("§7Забанил: §c" + CustomBans.dconfig.getString(args[0] + ".bannedby"));
			i.addLore("§7Причина: §c" + CustomBans.dconfig.getString(args[0] + ".reason"));
			i.addLore("§7Дата бана: §c" + CustomBans.dconfig.getString(args[0] + ".time"));
			i.addLore("§7(Пермамент - " + (CustomBans.dconfig.getBoolean((args[0]) + ".permament") ? "§aДа§7)" : "§cНет§7)"));
			inv.setItem(4, i.getItem());
			p.openInventory(inv);
		}
		return false;
	}

}
