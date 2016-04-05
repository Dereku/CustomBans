package me.itzrex.cbans;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import me.itzrex.cbans.managers.Banner;
import me.itzrex.cbans.managers.Mutter;
import me.itzrex.cbans.utils.RexUtil;
import me.itzrex.cbans.utils.Temporary;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerListener implements Listener {
	private static CustomBans plugin;
    //TODO send.sendMessage("Игрок был забанен, замучен, и тд(Для отображения в консоли)"

	/*
	 * Слушатель игроков на: баны, временые баны, муты, временный муты
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(final PlayerLoginEvent e) throws IOException{
		final Player p = e.getPlayer();
		if(CustomBans.geInstance().isMySQL){
			if(p.hasPermission("cbans.shield")){
				CustomBans.geInstance().getBanManager().setWhitelisted(p.getName(), true);
			} else {
				CustomBans.geInstance().getBanManager().setWhitelisted(p.getName(), false);
			}
		} else {
			if(p.hasPermission("cbans.shield")){
				CustomBans.dplayers.set(p.getName().toLowerCase(), true);
				CustomBans.dplayers.save(CustomBans.players);
				//В другом случае:
			} else {
				CustomBans.dplayers.set(p.getName().toLowerCase(), false);
				CustomBans.dplayers.save(CustomBans.players);
			}
		}
		if(CustomBans.geInstance().isMySQL){
			Banner ban = CustomBans.geInstance().getBanManager().getBan(p.getName().toLowerCase());
			if(ban == null){
				return;
			}
			     e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			     e.setKickMessage(ban.getKickMessage()); 
		} else {
			List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
			//Проверяем, есть-ли игрок в банлисте.
			if(banlist.contains(p.getName().toLowerCase())){
				//Проверяем, забанен-ли игрок навсегда (пермамент).
				if(CustomBans.dconfig.getBoolean(p.getName().toLowerCase() + ".permament")){
					//Достаём причину бана
					String reason = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".reason");
					//Достаём время бана.
					String time = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".time");
					//Достаём ник игрока который забанил.
					String bannedby = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".bannedby");
					e.getResult();
					//Кикаем его с сообщением:
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cВы были забанены.\nЗабанил: §6" + bannedby + "\n§cВремя: §6" + time + "\n§cПричина: §6" + reason);
					//Если-же игрок временно забанен, пишем:
				} else if(!CustomBans.dconfig.getBoolean(e.getPlayer().getName().toLowerCase() + ".permament")){
					//Достаём календарные данные.
			        Integer day_of_year = Integer.valueOf(Calendar.getInstance().get(6));
			        Integer hour = Integer.valueOf(Calendar.getInstance().get(11));
			        Integer minute = Integer.valueOf(Calendar.getInstance().get(12));
			        Integer currentMin = Integer.valueOf(day_of_year.intValue() * 1440 + hour.intValue() * 60 + minute.intValue());
			        Integer banLasts = Integer.valueOf(CustomBans.dconfig.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".lasts"));
			        Integer bansTime = Integer.valueOf(CustomBans.dconfig.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".bans-time"));
			        Boolean unbanned = false;
			        //Если, время бана истекло, то ставим булевое значение "unbanned" на "true"
			        if (currentMin.intValue() - banLasts.intValue() > bansTime.intValue()) {
			            unbanned = Boolean.valueOf(true);
			          }
			        //Получаем минуты, через истечение какой, будет разбанен игрок
			        Integer towait = Integer.valueOf(bansTime.intValue() - (currentMin.intValue() - banLasts.intValue()));
			        //Если время истекло:
			        if (unbanned.booleanValue())
			        {
			        	//Удаляем его с банлиста
			        	banlist.remove(e.getPlayer().getName().toLowerCase());
			        	CustomBans.dconfig.set("banlist", banlist);
			        	CustomBans.dconfig.save(CustomBans.dataFile);
			        	//В другом случае:
			        } else {
						String reason = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".reason");
						String time = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".time");
						String bannedby = CustomBans.dconfig.getString(p.getName().toLowerCase() + ".bannedby");
						e.getResult();
						e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cВы временно забанены.\nЗабанил: §6" + bannedby + "\n§cВремя: §6" + time + "\n§cПричина: §6" + reason + "\n§cОсталось до конца §6" + towait + " §cминут." );
			        }
				}
			}
		}
	}
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
    	Player p = e.getPlayer();
    	if(CustomBans.geInstance().isMySQL){
    		Mutter mute = CustomBans.geInstance().getBanManager().getMute(p.getName());
    		if(mute != null){
    			if(CustomBans.geInstance().getBanManager().isWhitelisted(p.getName())){
    				return;
    			}
    			p.sendMessage("§cВы были заглушены.");
    			e.setCancelled(true);
    		}
    	} else {
            if (CustomBans.dconfig2.getStringList("mutelist").contains(e.getPlayer().getName().toLowerCase())) {
                final Boolean permanent = CustomBans.dconfig2.getBoolean(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".permament");
                if (permanent) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Вы заглушены.");
                    e.getPlayer().sendMessage("Вас заблокировал:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutedby"));
                    e.getPlayer().sendMessage("Причина:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".reason"));
                    e.getPlayer().sendMessage("Время мута:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time"));
                    e.getPlayer().sendMessage("До конца: " + ChatColor.RED + "неогр.");
                    e.setCancelled(true);
                }
            }
    	}
    }
}


