package de.timeout.ban.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import de.timeout.ban.Ban;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ProxyConfigManager implements ConfigManager<Configuration> {
	
	private static Ban main = Ban.plugin;
	
	private Configuration langCfg;
	
	public ProxyConfigManager() {
		try {
			loadLanguage(main.getConfig().getString("language"));
		} catch (IOException e) {
			main.getLogger().log(Level.SEVERE, "Cannot load Configurations", e);
		}
	}
	
	@Override
	public void saveCustomConfig(File file, Configuration config) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
		} catch (IOException e) {
			
		}
	}

	public void loadLanguage(String name) throws IOException {
		File langFile = new File(main.getDataFolder() + "/language", name + ".yml");
		langCfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(langFile);
	}
	
	public Configuration getLanguageConfiguration() {
		return langCfg;
	}
}
