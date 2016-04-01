package me.itzrex.cbans.inv;

import java.util.ArrayList;
import java.util.List;

import me.itzrex.cbans.CustomBans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
			if(!banlist.contains(args[0].toLowerCase())){
				p.sendMessage(prefix + "Игрок не был забанен");
				return true;
			}
			Inventory inv = Bukkit.createInventory(null, 9, "Информация");
			ItemStack i2 = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) i2.getItemMeta();
			meta.setOwner(args[0]);
			meta.setDisplayName("§c" + args[0]);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7Забанил: §c" + CustomBans.dconfig.getString(args[0].toLowerCase() + ".bannedby"));
			lore.add("§7Причина: §c" + CustomBans.dconfig.getString(args[0].toLowerCase() + ".reason"));
			lore.add("§7Дата бана: §c" + CustomBans.dconfig.getString(args[0].toLowerCase() + ".time"));
			lore.add("§7(Пермамент - " + (CustomBans.dconfig.getBoolean((args[0].toLowerCase()) + ".permament") ? "§aДа§7)" : "§cНет§7)"));
			meta.setLore(lore);
			i2.setItemMeta(meta);
			inv.setItem(4, i2);
			p.openInventory(inv);
		}
		return false;
	}

}
