package de.timeout.ban.manager;

import java.util.UUID;

import de.timeout.ban.Ban;
import de.timeout.ban.reason.Reason;
import de.timeout.ban.reason.Reason.ReasonType;

public class TrialManager {

	private static final Ban main = Ban.plugin;
	
	public TrialManager() {
		main.getProxy().getPluginManager().registerListener(main, new GUIHooker());
		main.getProxy().registerChannel("Ban");
	}
	
	public void ban(UUID uuid, String name, Reason reason) {
		
	}
	
	public void mute(UUID uuid, String name, Reason reason) {
		
	}
	
	public void addReason(String name, String prefix, long timestamp, ReasonType type, String itemstack) {
		main.getMySQL().insert("INSERT INTO Reason VALUES(?, ?, ?, ?, ?)", name, prefix, String.valueOf(timestamp), type.name(), itemstack);
	}
	
	public void removeReason(String reason) {
		main.getMySQL().delete("DELETE FROM Reason WHERE Name = ?", reason);
	}
}
