package de.timeout.ban.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

public class BukkitConfigCreator extends ConfigCreator<JavaPlugin> {
	
	public BukkitConfigCreator(JavaPlugin main, String packagename) {
		super(main, packagename);
	}

	@Override
	public void createConfigurations() {
		loadResource(main, "config.yml");
	}

	@Override
	protected void loadResource(JavaPlugin plugin, String filepath) {
		try {
			File f = loadFile(plugin, filepath);
			String[] folders = filepath.contains("/") ? filepath.split("/") : new String[] {filepath};
			if(f != null && f.length() == 0L) {
				try(InputStream in = plugin.getResource(packagename + String.join("/", folders));
					OutputStream out = new FileOutputStream(f)) {
					ByteStreams.copy(in, out);
				}
			}
			Bukkit.getConsoleSender().sendMessage(RESSOURCE_LOADED.replace("%file", (f != null ? f.getName() : folders[folders.length -1])));
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Fatal Error by creating File " + filepath, e);
		}
	}

	@Override
	protected File loadFile(JavaPlugin plugin, String filepath) {
		try {
			String[] folders = new String[] {filepath};
			File f = plugin.getDataFolder();
			if(!f.exists())f.mkdirs();
			if(filepath.contains("/")) {
				folders = filepath.split("/");
				for(int i = 0; i < folders.length -1; i++)if(!(f = new File(f, folders[i])).exists())f.mkdirs();			
			}
			f = new File(f, folders[folders.length -1]);
			if(!f.exists()) {
				Bukkit.getConsoleSender().sendMessage(FILE_NOT_EXIST.replace("%file", f.getName()));
				f.createNewFile();
			}
			return f;
		} catch (IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not load " + filepath, e);
		}
		return null;
	}

}
