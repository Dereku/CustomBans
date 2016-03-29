package me.itzrex.cbans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Utils {
	// 1.5.2-1.9
	@SuppressWarnings("unchecked")
	public static Collection<Player> getOnlinePlayers() {
		Collection<Player> playersOnline = new ArrayList<>();
		try {
			if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class)
				playersOnline = ((Collection<Player>) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0])
						.invoke(null, new Object[0]));
			else
				playersOnline = Arrays.asList(((Player[]) Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0])
						.invoke(null, new Object[0])));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return playersOnline;

	}

}
