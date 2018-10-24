package de.timeout.ban.command;

import de.timeout.ban.Ban;

import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompleter implements Listener {

	public static final Ban main = Ban.plugin;
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {
		if(event.getCursor().toLowerCase().startsWith("/mute") || event.getCursor().toLowerCase().startsWith("/ban")) {
			event.getSuggestions().clear();
			main.getProxy().getPlayers().forEach(p -> event.getSuggestions().add(p.getName()));
		}
	}
}
