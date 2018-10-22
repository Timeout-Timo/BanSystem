package de.timeout.ban.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.utils.UTFConfig;

public class BukkitConfigManager implements ConfigManager<UTFConfig> {
	
	private static BukkitBan main = BukkitBan.plugin;
	
	private File langFile;
	private UTFConfig langCfg;
	
	public BukkitConfigManager(String config) {
		loadLanguage(config);
	}

	@Override
	public void saveCustomConfig(File file, UTFConfig config) {
		try {
			config.save(file);
		} catch (IOException e) {
			main.getLogger().log(Level.WARNING, "Could not save", e);
		}
	}

	public void loadLanguage(String config) {
		if(langFile == null)
			langFile = new File(main.getDataFolder().getPath() + "/language", config + ".yml");
		langCfg = new UTFConfig(langFile);
	}
	
	public UTFConfig getLanguageConfig() {
		langCfg = new UTFConfig(langFile);
		return langCfg;
	}
	
	public void saveLanguageFile() {
		saveCustomConfig(langFile, langCfg);
	}
}
