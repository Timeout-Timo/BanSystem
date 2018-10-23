package de.timeout.ban.config;

import de.timeout.ban.Ban;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public enum Language implements Messageable {
	
	PREFIX("prefix"),
	MYSQL_CONNECTED("mysql.connected"),
	MYSQL_DISCONNECTED("mysql.disconnected"),
	MYSQL_FAILED("mysql.failed");
	
	private static Ban main = Ban.plugin;
	private static final Configuration defaultFile = ConfigurationProvider.getProvider(YamlConfiguration.class)
			.load(Ban.plugin.getResourceAsStream("assets/ban/language/de_DE.yml"));

	private String path;
	private String message;
	
	private Language(String path) {
		this.path = path;
		this.message = getMessageFromYaml();
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
	
	private String getMessageFromYaml() {
		String yamlmessage = main.getConfigManager().getLanguageConfiguration().getString(path);
		if(yamlmessage != null) return ChatColor.translateAlternateColorCodes('&', yamlmessage);
		else return ChatColor.translateAlternateColorCodes('&', defaultFile.getString(path));
	}
	
	public String getMessage() {
		return message;
	}
}
