package de.timeout.ban.config;

import java.io.File;

@FunctionalInterface
public interface ConfigManager<T> {
	
	public void saveCustomConfig(File file, T config);
}
