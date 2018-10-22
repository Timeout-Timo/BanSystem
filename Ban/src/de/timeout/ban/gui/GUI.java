package de.timeout.ban.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class GUI {
	
	protected Inventory menu;
	protected final List<Player> openPlayers = new ArrayList<Player>();
	protected final ItemStack n = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 7, 1, "§5");
	
	public GUI(Inventory design) {
		this.menu = design;
	}
	
	public GUI() { /* Empty for other classes */ }
	
	public void openGUI(Player player) {
		player.openInventory(menu);
		openPlayers.add(player);
	}
	
	public void closeGUI(Player player) {
		player.closeInventory();
		openPlayers.remove(player);
	}
	
	public boolean isOpen(Player player) {
		return openPlayers.contains(player);
	}
	
	public boolean isGUI(Inventory inv) {
		if(menu.getSize() == inv.getSize()) {
			for(int i = 0; i < menu.getSize(); i++) 
				if(!menu.getItem(i).isSimilar(inv.getItem(i)))return false;
			return true;
		}
		return false;
	}
}
