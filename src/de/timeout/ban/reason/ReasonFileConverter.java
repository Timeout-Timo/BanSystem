package de.timeout.ban.reason;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.gui.elements.GUIReason;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;
import de.timeout.utils.UTFConfig;

public class ReasonFileConverter {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private final UTFConfig reasonsYML = new UTFConfig(new File(main.getDataFolder(), "reasons.yml"));
	private final List<GUIReason> reasons = new ArrayList<GUIReason>();
	
	public ReasonFileConverter() {
		loadReasonsFromConfig();
	}
	
	public void loadReasonsFromConfig() {
		ConfigurationSection section = reasonsYML.getConfigurationSection("reasons");
		section.getKeys(false).forEach(name -> {
			String display = section.getString(name + ".display");
			long firstStage = getTimeFromConfigSection(section.getConfigurationSection(name + ".firststage"));
			long secondStage = getTimeFromConfigSection(section.getConfigurationSection(name + ".secondstage"));
			long thirdStage = getTimeFromConfigSection(section.getConfigurationSection(name + "thirdstage"));
			int firstLine = section.getInt(name + ".firstline");
			int secondline = section.getInt(name + ".secondline");
			int points = section.getInt(name + ".points");
			ReasonType type = ReasonType.valueOf(section.getString(name + ".type"));
			ItemStack item = getItemFromConfigSection(section.getConfigurationSection(name + ".gui"), display);
			
			if(display != null && type != null && item != null && firstStage >= 60000 && secondStage >= 60000 && thirdStage >= 60000 && 
					firstLine > 0 && secondline > 0 && points > 0)
				reasons.add(new GUIReason(name, display, type, firstStage, secondStage, thirdStage, firstLine, secondline, points, item));
		});
	}
	
	private ItemStack getItemFromConfigSection(ConfigurationSection section, String displayname) {
		return ItemStackAPI.createItemStack(Materials.valueOf(section.getString("material")), (short) section.getInt("subid"), 1, displayname);
	}
	
	private long getTimeFromConfigSection(ConfigurationSection section) {
		return !section.getBoolean("permanent") ? 
				(section.getLong("days") * 24) * (section.getLong("hours") * 60) * (section.getLong("minutes") * 60) * 1000L : -1L;
	}
	
	public void uploadToMySQL() {
		main.getMySQL().delete("DELETE FROM Reason WHERE 1");
		reasons.forEach(reason -> main.getMySQL().insert("INSERT INTO Reason VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					reason.getName(),
					reason.getDisplay(),
					reason.getType().name(),
					String.valueOf(reason.getFirstStage()),
					String.valueOf(reason.getSecondStage()),
					String.valueOf(reason.getThirdStage()),
					String.valueOf(reason.getFirstLine()),
					String.valueOf(reason.getSecondLine()),
					String.valueOf(reason.getPoints()),
					ItemStackAPI.encodeItemStack(reason.getItem()))
		);
	}
}
