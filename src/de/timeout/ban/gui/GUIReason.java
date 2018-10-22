package de.timeout.ban.gui;

import org.bukkit.inventory.ItemStack;

import de.timeout.ban.reason.Reason;

public class GUIReason extends Reason {
	
	private ItemStack item;

	public GUIReason(String name, ItemStack item) {
		super(name);
		this.item = item;
	}

	public GUIReason(String name, String prefix, ReasonType type, long firstStage, long secondStage, long thirdStage,
			int firstLine, int secondLine, int points, ItemStack item) {
		super(name, prefix, type, firstStage, secondStage, thirdStage, firstLine, secondLine, points);
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}
}
