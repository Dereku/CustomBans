package me.itzrex.cbans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerLoginEvent e) throws IOException{
		Player p = e.getPlayer();
		List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
		if(CustomBans.dplayers.getBoolean(p.getName())){
			return;
		}
		if(banlist.contains(p.getName())){
			if(CustomBans.dconfig.getBoolean(p.getName() + ".permament")){
				String reason = CustomBans.dconfig.getString(p.getName() + ".reason");
				String time = CustomBans.dconfig.getString(p.getName() + ".time");
				String bannedby = CustomBans.dconfig.getString(p.getName() + ".bannedby");
				e.getResult();
				e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cВы были заблокированы на этом сервере.\nЗабанил: §6" + bannedby + "\n§cВремя: §6" + time + "\n§cПричина: §6" + reason);
			} else if(!CustomBans.dconfig.getBoolean(e.getPlayer().getName() + ".permament")){
		        Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
		        Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
		        Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
		        Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
		        Integer banLasts = Integer.valueOf(CustomBans.dconfig.getInt(String.valueOf(e.getPlayer().getName()) + ".lasts"));
		        Integer bansTime = Integer.valueOf(CustomBans.dconfig.getInt(String.valueOf(e.getPlayer().getName()) + ".bans-time"));
		        Boolean unbanned = false;
		        if (currentMin.intValue() - banLasts.intValue() > bansTime.intValue()) {
		            unbanned = Boolean.valueOf(true);
		          }
		        Integer towait = Integer.valueOf(bansTime.intValue() - (currentMin.intValue() - banLasts.intValue()));
		        if (unbanned.booleanValue())
		        {
		        	banlist.remove(e.getPlayer().getName());
		        	CustomBans.dconfig.set("banlist", banlist);
		        	CustomBans.dconfig.save(CustomBans.dataFile);
		        } else {
					String reason = CustomBans.dconfig.getString(p.getName() + ".reason");
					String time = CustomBans.dconfig.getString(p.getName() + ".time");
					String bannedby = CustomBans.dconfig.getString(p.getName() + ".bannedby");
					e.getResult();
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cВы были временно заблокированы на этом сервере.\nЗабанил: §6" + bannedby + "\n§cВремя: §6" + time + "\n§cПричина: §6" + reason + "\n§cОкончание блокировки через §6" + towait + " §cминут." );
		        }
			}
		}
		if(p.hasPermission("cbans.shield")){
			CustomBans.dplayers.set(p.getName(), true);
			CustomBans.dplayers.save(CustomBans.players);
		} else {
			CustomBans.dplayers.set(p.getName(), false);
			CustomBans.dplayers.save(CustomBans.players);
		}
	}
}


