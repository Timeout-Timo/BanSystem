package de.timeout.ban.config;

import java.io.File;

public abstract class ConfigCreator<T> {

	protected static final String RESSOURCE_LOADED = "§8[§aOut-Configuration§8]§a %file §fis §asucessful loaded";
	protected static final String FILE_NOT_EXIST = "§8[§aOut-Configuration§8]§a %file §7could not be found: §aGenerate...";
	
	protected String packagename;
	protected T main;
	
	public ConfigCreator(T main, String packagename) {
		this.main = main;
		this.packagename = packagename;
		createConfigurations();
	}
	
	public abstract void createConfigurations();
	
	protected abstract void loadResource(T plugin, String filepath);
	
	protected abstract File loadFile(T plugin, String filepath);
}
