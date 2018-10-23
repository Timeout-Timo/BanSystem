package de.timeout.ban;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import de.timeout.ban.config.ProxyConfigCreator;
import de.timeout.ban.config.ProxyConfigManager;
import de.timeout.ban.sql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Ban extends Plugin {

	private static final String CONFIG_NAME = "config.yml";
	
	public static Ban plugin;
	
	private ProxyConfigManager configManager;
	private Configuration config;
	private MySQL mysql;
	
	@Override
	public void onEnable() {
		plugin = this;
		new ProxyConfigCreator();
		loadConfig();
		this.configManager = new ProxyConfigManager();
		this.mysql = new MySQL(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"),
				config.getString("mysql.username"), config.getString("mysql.password"));
		createTables();
	}
	
	public Configuration getConfig() {
		return config;
	}
	
	public boolean saveIP() {
		return config.getBoolean("ip");
	}
	
	public ProxyConfigManager getConfigManager() {
		return configManager;
	}

	public void loadConfig() {
		try {
			this.config =  ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), CONFIG_NAME));
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Cannot load config.yml", e);			
		}
	}
	
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), CONFIG_NAME));
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Cannot save config.yml", e);
		}
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public void createTables() {
		try {
			//Create Bantable
			try(PreparedStatement ps = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bans(UUID VARCHAR(36), Name VARCHAR(16), Judge VARCHAR(100), Reason VARCHAR(100), Repeal BIGINT)")) {
				ps.execute();
			}
			//Create Mutetable
			try(PreparedStatement ps = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Mutes(UUID VARCHAR(36), Name VARCHAR(16), Judge VARCHAR(100), Reason VARCHAR(100), Repeal BIGINT)")) {
				ps.execute();
			}
			//Create History
			try(PreparedStatement ps = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS History(UUID VARCHAR(36), Mutes TEXT, Bans TEXT)")) {
				ps.execute();
			}
			//Create Reason
			try(PreparedStatement ps = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Reason(Name VARCHAR(100), Display VARCHAR(100), Type VARCHAR(4), FirstStage BIGINT, SecondStage BIGINT, ThirdStage BIGINT, FirstLine INT, SecondLine INT, Points INT, Item TEXT)")) {
				ps.execute();
			}
		} catch(SQLException e) {
			getLogger().log(Level.SEVERE, "Coult not create Databases", e);
		}
	}
}
