package me.itzrex.cbans;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerListener implements Listener {
    //TODO send.sendMessage("Игрок был забанен, замучен, и тд(Для отображения в консоли)"

	/*
	 * Слушатель игроков на: баны, временые баны, муты, временный муты
	 */
	@EventHandler
	public void onJoin(PlayerLoginEvent e) throws IOException{
		Player p = e.getPlayer();
		List<String> banlist = (List<String>) CustomBans.dconfig.getStringList("banlist");
		if(CustomBans.dplayers.getBoolean(p.getName())){
			return;
		}
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
		//Проверка игрока на OP или право "cbans.shield"
		//Если он имеет это право.
		if(p.hasPermission("cbans.shield")){
			CustomBans.dplayers.set(p.getName(), true);
			CustomBans.dplayers.save(CustomBans.players);
			//В другом случае:
		} else {
			CustomBans.dplayers.set(p.getName(), false);
			CustomBans.dplayers.save(CustomBans.players);
		}
	}
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
    	//Слушатель чата, спасибо за это  @Slavik123123123
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
            else {
                final Integer day_of_year = Calendar.getInstance().get(6);
                final Integer hour = Calendar.getInstance().get(11);
                final Integer minute = Calendar.getInstance().get(12);
                final Integer currentMin = day_of_year * 1440 + hour * 60 + minute;
                final Integer banLasts = CustomBans.dconfig2.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".lasts");
                final Integer bansTime = CustomBans.dconfig2.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time");
                Boolean unbanned = false;
                if (currentMin - banLasts >= bansTime) {
                    unbanned = true;
                }
                final Integer towait = bansTime - (currentMin - banLasts);
                if (unbanned) {
                    final List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
                    mutelist.remove(e.getPlayer().getName().toLowerCase());
                    CustomBans.dconfig2.set("mutelist", mutelist);
                    try {
                        CustomBans.dconfig2.save(CustomBans.dataFile2);
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Ваш чат разблокирован.");
                }
                else {
                    e.getPlayer().sendMessage(ChatColor.RED + "Вы были временно заглушены");
                    e.getPlayer().sendMessage("Вас заблокировал " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutedby"));
                    e.getPlayer().sendMessage("Причина: " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".reason"));
                    e.getPlayer().sendMessage("Время мута: " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time"));
                    e.getPlayer().sendMessage("До конца: " + ChatColor.RED + towait + "минут.");
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onCommandWhileMuted(final PlayerCommandPreprocessEvent e) {
    	//Запрет команд во время мута, которые запрещённые в конфиге.
                if (CustomBans.dconfig2.getStringList("mutelist").contains(e.getPlayer().getName().toLowerCase())) {
                    final String message = e.getMessage();
                    final String[] split = message.split(" ");
                    if (!CustomBans.geInstance().getConfig().getStringList("mute-commands").contains(split[0])) {
                        return;
                    }
                    final Boolean permanent = CustomBans.dconfig2.getBoolean(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".permament");
                    if (permanent) {
                        e.getPlayer().sendMessage(ChatColor.RED + "Вы заглушены.");
                        e.getPlayer().sendMessage("Вас заблокировал:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutedby"));
                        e.getPlayer().sendMessage("Причина:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".reason"));
                        e.getPlayer().sendMessage("Время мута:" + ChatColor.RED + " " + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time"));
                        e.getPlayer().sendMessage("До конца: " + ChatColor.RED + "неогр.");
                        e.setCancelled(true);
                    }
                    else {
                        final Integer day_of_year = Calendar.getInstance().get(6);
                        final Integer hour = Calendar.getInstance().get(11);
                        final Integer minute = Calendar.getInstance().get(12);
                        final Integer currentMin = day_of_year * 1440 + hour * 60 + minute;
                        final Integer banLasts = CustomBans.dconfig2.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".lasts");
                        final Integer bansTime = CustomBans.dconfig2.getInt(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time");
                        Boolean unbanned = false;
                        if (currentMin - banLasts >= bansTime) {
                            unbanned = true;
                        }
                        final Integer towait = bansTime - (currentMin - banLasts);
                        if (unbanned) {
                            final List<String> mutelist = (List<String>)CustomBans.dconfig2.getStringList("mutelist");
                            mutelist.remove(e.getPlayer().getName().toLowerCase());
                            CustomBans.dconfig2.set("mutelist", (Object)mutelist);
                            try {
                                CustomBans.dconfig2.save(CustomBans.dataFile2);
                            }
                            catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.getPlayer().sendMessage(ChatColor.GREEN + "Ваш чат разблокирован.");
                        }
                        else {
                            e.getPlayer().sendMessage(ChatColor.RED + "Вы были временно заглушены");
                            e.getPlayer().sendMessage("Вас заблокировал " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutedby"));
                            e.getPlayer().sendMessage("Причина: " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".reason"));
                            e.getPlayer().sendMessage("Время мута: " + ChatColor.RED + CustomBans.dconfig2.getString(String.valueOf(e.getPlayer().getName().toLowerCase()) + ".mutes-time"));
                            e.getPlayer().sendMessage("До конца: " + ChatColor.RED + towait + "минут.");
                            e.setCancelled(true);
                        }
                    }
                }
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e){
    	if(e.getInventory().getTitle().equals("Информация")){
    		e.setCancelled(true);
    	}
    }
}


