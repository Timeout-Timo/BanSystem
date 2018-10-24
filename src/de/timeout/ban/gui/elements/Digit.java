package de.timeout.ban.gui.elements;

import org.bukkit.inventory.ItemStack;

import de.timeout.utils.Skull;

public class Digit {

	private static final ItemStack ZERO = Skull.loadSkullFromGameprofileValue("§50", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIyMmI3OWI3MmFjZWY4NTkxMGRiOWU5YWU4YTdiYWU4ZWJjZTgzNDJjMThlMzQ3MjUyNzNlNzdjMWFhNCJ9fX0=");
	private static final ItemStack ONE = Skull.loadSkullFromGameprofileValue("§51", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBjNDg3M2U0YTIxOTE0NWViNGUzZjY2YzA5ZTJjZThiMjdkZTJmZDgxZGViNDhlM2M3ZTA0ZTk3NGU1ZWFhIn19fQ==");
	private static final ItemStack TWO = Skull.loadSkullFromGameprofileValue("§52", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM1ZjhmNTZlMWEyMzNmY2Q5Nzc3Mjg1ZTRiNWFiMTNmYWEwYzZkODJmYTYzNTQyNWMxNzQ1ZDQxMWU4NTcxZCJ9fX0=");
	private static final ItemStack THREE = Skull.loadSkullFromGameprofileValue("§53", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ0NjM1ZDZiYjhmOWFhNDZkYjYwODc1YmI1ZGIzOTQwYWIwYzRmMGViNjg0YzZiODg4NjQ3YmIzMzkyN2UifX19");
	private static final ItemStack FOUR = Skull.loadSkullFromGameprofileValue("§54", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjlhOWYzZDc5YTJkOGRiOGQ2NmI1OTc1MjU0MmFkNmUxMzQ5ODUzMjU4ZmFkOTg0ODc4OTI3NzM2YmQ1YjkxIn19fQ==");
	private static final ItemStack FIVE = Skull.loadSkullFromGameprofileValue("§55", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjllOGQyMjhlNTMwNzlkMDBiMmRmYzU3OTkzZDUyZmY3OGU2YTdlYTI3NDJiYmRmYjNjNGY0ZTkzY2QxOWI2In19fQ==");
	private static final ItemStack SIX = Skull.loadSkullFromGameprofileValue("§56", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiYmI3ZWJkNWQ5ZGViZjcyYTQyY2FiZjMwZDI1NTc0MzI0Y2VkYWM4ZjdmOGE1NTAxY2Q4MWU3MTZmOSJ9fX0=");
	private static final ItemStack SEVEN = Skull.loadSkullFromGameprofileValue("§57", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiYmI3ZWJkNWQ5ZGViZjcyYTQyY2FiZjMwZDI1NTc0MzI0Y2VkYWM4ZjdmOGE1NTAxY2Q4MWU3MTZmOSJ9fX0=");
	private static final ItemStack EIGHT = Skull.loadSkullFromGameprofileValue("§58", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhlZmI2Y2QxZjhlNTkxZWU1M2MxOTJmNjQ3NmU4M2UyNWI5NWJiZDI3ZWVjMWM2NDRjMTE1MjVmMDc2MjIifX19");
	private static final ItemStack NINE = Skull.loadSkullFromGameprofileValue("§59", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAyZjYwMjNiNWI5MGY2ZTJiMGIyZTNhNzIyOTc2MzQ3MTlkNTc0YmQ5ZmY2NDc2N2Y1NGQxOWJlZDNmYSJ9fX0=");
		
	private int digit;
	
	public Digit(int digit) {
		this.digit = digit;
	}
	
	public Digit() {
		this.digit = 0;
	}
	
	public ItemStack getDigitDisplayItem() {
		switch(digit) {
		case 1:
			return ONE;
		case 2:
			return TWO;
		case 3:
			return THREE;
		case 4:
			return FOUR;
		case 5:
			return FIVE;
		case 6:
			return SIX;
		case 7: 
			return SEVEN;
		case 8:
			return EIGHT;
		case 9:
			return NINE;
		default:
			return ZERO;
		}
	}
	
	public int getDigit() {
		return digit;
	}
	
	public void plus() {
		if(digit < 9) digit++;
		else digit = 0;
	}
	
	public void minus() {
		if(digit > 0) digit--;
		else digit = 9;
	}
}
