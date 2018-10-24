package de.timeout.ban.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.timeout.ban.gui.config.GUILanguage;
import de.timeout.ban.gui.elements.Button;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ClockGUI extends GUI {
		
	private ItemStack days, hours, minutes;
	private Button perma, confirm;
	
	public ClockGUI(String title) {
		this.menu = Bukkit.createInventory(null, 9*5, title);
		this.days = ItemStackAPI.createItemStack(Materials.PAPER, 1, GUILanguage.ITEMS_CLOCK_DAYS.getMessage());
		this.hours = ItemStackAPI.createItemStack(Materials.PAPER, 1, GUILanguage.ITEMS_CLOCK_HOURS.getMessage());
		this.minutes = ItemStackAPI.createItemStack(Materials.PAPER, 1, GUILanguage.ITEMS_CLOCK_MINUTES.getMessage());
		this.perma = new Button(ItemStackAPI.createItemStack(Materials.BARRIER, 1, GUILanguage.ITEMS_CLOCK_PERMANENT.getMessage()));
		this.confirm = new Button(ItemStackAPI.createItemStack(Materials.WATCH, 1, GUILanguage.ITEMS_CLOCK_CONFIRM.getMessage()));
		for(int i = 0; i < menu.getSize(); i++) menu.setItem(i, n);
		
		ItemStackAPI.setLore(days, "§70");
		ItemStackAPI.setLore(hours, "§70");
		ItemStackAPI.setLore(minutes, "§71");
		
		ItemStack plus = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 5, 1, "§a+");
		ItemStack minus = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 14, 1, "§c-");
		
		for(int i = 10; i < 17; i = i + 3) menu.setItem(i, plus); //10D, 13H, 16M
		for(int i = 28; i < 35; i = i + 3) menu.setItem(i, minus); //28D, 31H, 34M 
		
		menu.setItem(19, days);
		menu.setItem(22, hours);
		menu.setItem(25, minutes);
		menu.setItem(42, perma.getItem());
		menu.setItem(38, confirm.getItem());
	}
	
	public ClockGUI(ClockGUI source) {
		this.menu = source.getMenu();
		this.days = source.getMenu().getItem(19);
		this.hours = source.getMenu().getItem(22);
		this.minutes = source.getMenu().getItem(25);
		this.perma = new Button(source.getMenu().getItem(42));
		this.confirm = new Button(source.getMenu().getItem(38));
	}
	
	public long getTimeMillis() {
		return (getTime(days) * 24L) * (getTime(hours) * 60L) * (getTime(minutes) * 60L) * 1000L;
	}
	
	private long getTime(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		return Integer.parseInt(ChatColor.stripColor(meta.getLore().get(0)));
	}
	
	public void addMinute() {
		ItemStackAPI.setLore(minutes, String.valueOf("§7" + (getTime(minutes) + 1)));
		updateGUI();
	}
	
	public void removeMinute() {
		if(getTimeMillis() > 60000) ItemStackAPI.setLore(minutes, String.valueOf("§7" + (getTime(minutes) -1)));
		updateGUI();
	}
	
	public void addHour() {
		ItemStackAPI.setLore(hours, String.valueOf("§7" + (getTime(hours) + 1)));
		updateGUI();
	}
	
	public void removeHour() {
		if(getTimeMillis() > 60000) ItemStackAPI.setLore(hours, String.valueOf("§7" + (getTime(hours) -1)));
		updateGUI();
	}
	
	public void addDay() {
		ItemStackAPI.setLore(days, String.valueOf("§7" + (getTime(days) + 1)));
		updateGUI();
	}
	
	public void removeDay() {
		if(getTimeMillis() > 60000) ItemStackAPI.setLore(hours, String.valueOf("§7" + (getTime(days) -1)));
		updateGUI();
	}
	
	public void pressPermaButton() {
		perma.press();
		updateGUI();
	}
	
	public void updateGUI() {
		player.updateInventory();
	}
}
