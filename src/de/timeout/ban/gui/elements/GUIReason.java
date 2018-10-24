package de.timeout.ban.gui.elements;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.reason.Reason;
import de.timeout.utils.ItemStackAPI;

public class GUIReason extends Reason {
	
	private ItemStack item;

	public GUIReason(String name, ItemStack item) {
		super(name);
		this.item = item;
	}

	public GUIReason(String name, String prefix, ReasonType type, long firstStage, long secondStage, long thirdStage,
			int firstLine, int secondLine, int points, ItemStack item) {
		super(name, prefix, type, firstStage, secondStage, thirdStage, firstLine, secondLine, points);
		setItem(item);
	}
	
	public void setItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display != null ? display : name);
		item.setItemMeta(meta);
		
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}
	
	@Override
	public void uploadToMySQL() {
		BukkitBan.plugin.getMySQL().insert("INSERT INTO Reasons VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", name, display, type.name(), 
				String.valueOf(firstStage), String.valueOf(secondStage), String.valueOf(thirdStage), String.valueOf(firstLine), String.valueOf(secondLine),
				String.valueOf(points), ItemStackAPI.encodeItemStack(item));
	}
}
