package de.timeout.ban.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ConfirmGUI extends GUI {
	
	private ItemStack middle;
	
	public ConfirmGUI(ItemStack middle, String title, String yes, String no) {
		this.middle = middle;
		this.menu = createInventory(title, yes, no);
		setMiddle(middle);
	}
	
	private Inventory createInventory(String title, String yes, String no) {
		Inventory inv = Bukkit.createInventory(null, 9*1, title);
		for(int i = 0; i < inv.getSize(); i++)inv.setItem(i, n);
		
		inv.setItem(2, ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 5, 1, yes));
		inv.setItem(6, ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 14, 1, no));
		return inv;
	}
	
	public void setMiddle(ItemStack item) {
		this.middle = item;
		this.menu.setItem(4, item);
	}
	
	public ItemStack getMiddle() {
		return middle;
	}
	
	public boolean getResult(ItemStack click) {
		return menu.getItem(2).getItemMeta().getDisplayName().equalsIgnoreCase(click.getItemMeta().getDisplayName());
	}
}
