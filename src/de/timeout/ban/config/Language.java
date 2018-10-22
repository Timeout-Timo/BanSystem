package de.timeout.ban.config;

import de.timeout.ban.Ban;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public enum Language {
	
	PREFIX("prefix"),
	MYSQL_CONNECTED("mysql.connected"),
	MYSQL_DISCONNECTED("mysql.disconnected"),
	MYSQL_FAILED("mysql.failed");
	
	private static Ban main = Ban.plugin;
	private static final Configuration defaultFile = ConfigurationProvider.getProvider(YamlConfiguration.class)
			.load(Ban.plugin.getResourceAsStream("assets/ban/language/de_DE.yml"));

	private String path;
	
	private Language(String path) {
		this.path = path;
	}
	
	public static Language getLanguageByPath(String path) {
		for(Language lang : values()) {
			if(lang.getPath().equalsIgnoreCase(path)) return lang;
		}
		throw new NullPointerException("Languagepath not found");
	}
	
	public String getPath() {
		return path;
	}
	
	public String getMessage() {
		String message = main.getConfigManager().getLanguageConfiguration().getString(path);
		if(message != null) return ChatColor.translateAlternateColorCodes('&', message);
		else return ChatColor.translateAlternateColorCodes('&', defaultFile.getString(path));
	}
}
