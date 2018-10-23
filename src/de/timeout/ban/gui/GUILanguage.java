package de.timeout.ban.gui;

import de.timeout.ban.config.Messageable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public enum GUILanguage implements Messageable {
	
	PREFIX("prefix"),
	MESSAGE_REASON_IMPORT("message.reason.import"),
	MESSAGE_REASON_REMOVE_NOTFOUND("message.reason.remove.notfound"),
	MESSAGE_REASON_REMOVE_SUCCESS("message.reason.remove.success"),
	MENU_REASON_MENU_TITLE("menu.reason.menu.title"),
	MENU_REASON_REMOVE_TITLE("menu.reason.remove.title"),
	MENU_REASON_FIRSTSTAGE_TITLE("menu.reason.firststage.title"),
	MENU_REASON_SECONDSTAGE_TITLE("menu.reason.secondstage.title"),
	MENU_REASON_THIRDSTAGE_TITLE("menu.reason.thirdstage.title"),
	ITEMS_REASON_MENU_MUTE("items.reason.menu.mute"),
	ITEMS_REASON_MENU_BAN("items.reason.menu.ban"),
	ITEMS_REASON_REMOVE_YES("items.reason.remove.yes"),
	ITEMS_REASON_REMOVE_NO("items.reason.remove.no");
	

	private static BukkitBan main = BukkitBan.plugin;
	private static final Configuration defaultFile = ConfigurationProvider.getProvider(YamlConfiguration.class)
			.load(main.getResource("assets/ban/gui/language/de_DE.yml"));
	
	private String path;
	private String message;
	
	private GUILanguage(String path) {
		this.path = path;
		this.message = getMessageFromYaml();
	}
	
	public static GUILanguage getLanguageByPath(String path) {
		for(GUILanguage lang : values()) {
			if(lang.getPath().equalsIgnoreCase(path)) return lang;
		}
		throw new NullPointerException("Languagepath not found");
	}
	
	private String getMessageFromYaml() {
		String yamlmessage = main.getConfigManager().getLanguageConfig().getString(path);
		if(yamlmessage != null) return ChatColor.translateAlternateColorCodes('&', yamlmessage);
		else return ChatColor.translateAlternateColorCodes('&', defaultFile.getString(path));
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public String getPath() {
		return path;
	}

}
