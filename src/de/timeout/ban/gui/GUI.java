package de.timeout.ban.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class GUI {
	
	protected Inventory menu;
	protected Player player;
	protected final ItemStack n = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 7, 1, "§5");
	
	public GUI(Inventory design) {
		this.menu = design;
	}
	
	public GUI() { /* Empty for other classes */ }
	
	public void openGUI(Player player) {
		player.openInventory(menu);
	}
	
	public void closeGUI(Player player) {
		player.closeInventory();
		this.player = null;
		menu = null;
	}
	
	public boolean isOpen(Player player) {
		return this.player.equals(player);
	}
	
	public boolean isGUI(Inventory inv) {
		if(menu.getSize() == inv.getSize()) {
			for(int i = 0; i < menu.getSize(); i++) 
				if(!menu.getItem(i).isSimilar(inv.getItem(i)))return false;
			return true;
		}
		return false;
	}
	
	public Inventory getMenu() {
		return menu;
	}
}
