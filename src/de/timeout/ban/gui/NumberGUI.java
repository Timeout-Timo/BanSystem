package de.timeout.ban.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.elements.Digit;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class NumberGUI extends GUI {
	
	private Digit ones, tens, hundreds, thousands, tenthousands;

	public NumberGUI(String title) {
		this.menu = Bukkit.createInventory(null, 9*5, title);
		for(int i = 0; i < menu.getSize(); i++)menu.setItem(i, n);
		
		ItemStack plus = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 5, 1, "§a+");
		ItemStack minus = ItemStackAPI.createItemStack(Materials.STAINED_GLASS_PANE, (short) 14, 1, "§c-");
		
		for(int i = 9; i < 18; i = i +2)menu.setItem(i, plus);
		for(int i = 27; i < 36; i = i +2)menu.setItem(i, minus);
		
		ones = new Digit();
		tens = new Digit();
		hundreds = new Digit();
		thousands = new Digit();
		tenthousands = new Digit();
		
		menu.setItem(18, tenthousands.getDigitDisplayItem());
		menu.setItem(20, thousands.getDigitDisplayItem());
		menu.setItem(22, hundreds.getDigitDisplayItem());
		menu.setItem(24, tens.getDigitDisplayItem());
		menu.setItem(26, ones.getDigitDisplayItem());
	}
	
	public NumberGUI(NumberGUI source) {
		this.menu = source.getMenu();
		this.ones = new Digit();
		this.tens = new Digit();
		this.hundreds = new Digit();
		this.thousands = new Digit();
		this.tenthousands = new Digit();
		
		menu.setItem(18, tenthousands.getDigitDisplayItem());
		menu.setItem(20, thousands.getDigitDisplayItem());
		menu.setItem(22, hundreds.getDigitDisplayItem());
		menu.setItem(24, tens.getDigitDisplayItem());
		menu.setItem(26, ones.getDigitDisplayItem());
	}
	
	public void updateDigits() {
		menu.setItem(18, tenthousands.getDigitDisplayItem());
		menu.setItem(20, thousands.getDigitDisplayItem());
		menu.setItem(22, hundreds.getDigitDisplayItem());
		menu.setItem(24, tens.getDigitDisplayItem());
		menu.setItem(26, ones.getDigitDisplayItem());
	}
	
	public void updateGUI() {
		updateDigits();
		player.updateInventory();
	}
	
	public void addOne() {
		ones.plus();
		updateGUI();
	}
	
	public void removeOne() {
		ones.minus();
		updateGUI();
	}
	
	public void addTen() {
		tens.plus();
		updateGUI();
	}
	
	public void removeTen() {
		tens.minus();
		updateGUI();
	}
	
	public void addHundred() {
		hundreds.plus();
		updateGUI();
	}
	
	public void removeHundred() {
		hundreds.minus();
		updateGUI();
	}
	
	public void addThousand() {
		thousands.plus();
		updateGUI();
	}
	
	public void removeThousand() {
		thousands.minus();
		updateGUI();
	}
	
	public void addTenThousand() {
		tenthousands.plus();
		updateGUI();
	}
	
	public void removeTenTousand() {
		tenthousands.minus();
		updateGUI();
	}
	
	public int getNumber() {
		return (tenthousands.getDigit() * 10000) + (thousands.getDigit() * 1000) + (hundreds.getDigit() * 100) + 
				(tens.getDigit() * 10) + ones.getDigit();
	}
}
