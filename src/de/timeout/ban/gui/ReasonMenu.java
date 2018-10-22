package de.timeout.ban.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.elements.Button;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;
import net.md_5.bungee.api.ChatColor;

public class ReasonMenu extends GUI {
	
	private static final BukkitBan main = BukkitBan.plugin;
	private static final String mainmenuTitle = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("menu.reason.menu.title"));
	private static final String muteName = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("items.reason.menu.mute"));
	private static final String banName = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("items.reason.menu.ban"));
	
	private Button mute, ban;
	
	public ReasonMenu() {
		Inventory inv = Bukkit.createInventory(null, 9*1, mainmenuTitle);
		for(int i = 0; i < inv.getSize(); i++) inv.setItem(i, n);
		
		this.mute = new Button(ItemStackAPI.createItemStack(Materials.EMPTY_MAP, 1, muteName));
		this.ban = new Button(ItemStackAPI.createItemStack(Materials.BARRIER, 1, banName));
		
		inv.setItem(2, mute.getItem());
		inv.setItem(6, ban.getItem());
		this.menu = inv;
	}
	
	public boolean isMuteButton(ItemStack item) {
		return mute.getItem().getType() == item.getType();
	}
	
	public boolean isBanButton(ItemStack item) {
		return ban.getItem().getType() == item.getType();
	}
}
