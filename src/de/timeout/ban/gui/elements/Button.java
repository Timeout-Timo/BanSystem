package de.timeout.ban.gui.elements;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.timeout.utils.ItemStackAPI;

public class Button {

	private boolean activate;
	private ItemStack item;
	
	public Button(ItemStack item) {
		this.item = item;
		this.activate = false;
	}
	
	public void activate() {
		this.activate = true;
		ItemStackAPI.enchantItem(item, Enchantment.DURABILITY, 1, true);
	}
	
	public void deactivate() {
		this.activate = false;
		ItemStackAPI.removeEnchantments(item);
	}
	
	public void press() {
		activate = !activate;
	}
	
	public boolean isActivated() {
		return activate;
	}
	
	public ItemStack getItem() {
		return item;
	}
}
