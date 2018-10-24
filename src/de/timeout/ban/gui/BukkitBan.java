package de.timeout.ban.gui;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import de.timeout.ban.gui.commands.ReasonCommand;
import de.timeout.ban.gui.config.BukkitConfigCreator;
import de.timeout.ban.gui.config.BukkitConfigManager;
import de.timeout.ban.sql.MySQL;
import de.timeout.utils.UTFConfig;

public class BukkitBan extends JavaPlugin {
	
	public static BukkitBan plugin;
	
	private UTFConfig config;
	private MySQL mysql;
	private BukkitConfigManager configmanager;
	
	@Override
	public void onEnable() {
		plugin = this;
		new BukkitConfigCreator(this, "assets/ban/gui/");
		this.config = new UTFConfig(new File(getDataFolder(), "config.yml"));
		this.configmanager = new BukkitConfigManager(getConfig().getString("language"));
		this.mysql = new MySQL(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"),
				getConfig().getString("mysql.username"), getConfig().getString("mysql.password"));
		registerCommands();
	}	

	@Override
	public UTFConfig getConfig() {
		return config;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public BukkitConfigManager getConfigManager() {
		return configmanager;
	}
	
	public UTFConfig getLanguage() {
		return configmanager.getLanguageConfig();
	}
	
	private void registerCommands() {
		this.getCommand("reason").setExecutor(new ReasonCommand());
	}
 }
