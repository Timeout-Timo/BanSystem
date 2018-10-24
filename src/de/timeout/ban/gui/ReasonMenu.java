package de.timeout.ban.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.elements.Button;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ReasonMenu extends GUI {
		
	private Button mute, ban;
	
	public ReasonMenu() {
		Inventory inv = Bukkit.createInventory(null, 9*1, GUILanguage.MENU_REASON_MENU_TITLE.getMessage());
		for(int i = 0; i < inv.getSize(); i++) inv.setItem(i, n);
		
		this.mute = new Button(ItemStackAPI.createItemStack(Materials.EMPTY_MAP, 1, GUILanguage.ITEMS_REASON_MENU_MUTE.getMessage()));
		this.ban = new Button(ItemStackAPI.createItemStack(Materials.BARRIER, 1, GUILanguage.ITEMS_REASON_MENU_BAN.getMessage()));
		
		inv.setItem(2, mute.getItem());
		inv.setItem(6, ban.getItem());
		this.menu = inv;
	}
	
	public ReasonMenu(ReasonMenu source) {
		this.menu = source.getMenu();
		this.ban = new Button(source.getMenu().getItem(6));
		this.mute = new Button(source.getMenu().getItem(2));
	}
	
	public boolean isMuteButton(ItemStack item) {
		return mute.getItem().getType() == item.getType();
	}
	
	public boolean isBanButton(ItemStack item) {
		return ban.getItem().getType() == item.getType();
	}
}
