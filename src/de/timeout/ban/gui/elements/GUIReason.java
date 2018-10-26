package de.timeout.ban.gui.elements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.reason.Reason;
import de.timeout.utils.ItemStackAPI;

public class GUIReason extends Reason {
	
	private static final BukkitBan main = BukkitBan.plugin;
	
	private ItemStack item;

	public GUIReason(String name, ItemStack item) {
		super(name);
		this.item = item;
	}
	
	public static GUIReason getReasonFromItemStack(ItemStack item) {
		try(PreparedStatement ps = main.getMySQL().getConnection().prepareStatement("SELECT * FROM Reasons WHERE Item = ?")) {
			ps.setString(1, ItemStackAPI.encodeItemStack(item));
			try(ResultSet rs = ps.executeQuery()) {
				return new GUIReason(rs.getString("Name"), rs.getString("Display"), ReasonType.valueOf(rs.getString("Type")),
						rs.getLong("FirstStage"), rs.getLong("SecondStage"), rs.getLong("ThirdStage"), rs.getInt("FirstLine"),
						rs.getInt("SecondLine"), rs.getInt("Points"), item);
			}
		} catch(SQLException e) {
			main.getLogger().log(Level.SEVERE, "Could not connect to MySQL-Database", e);
		}
		return null;
	}

	public GUIReason(String name, String prefix, ReasonType type, long firstStage, long secondStage, long thirdStage,
			int firstLine, int secondLine, int points, ItemStack item) {
		super(name, prefix, type, firstStage, secondStage, thirdStage, firstLine, secondLine, points);
		setItem(item);
	}
	
	public void setItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display != null ? display : name);
		item.setItemMeta(meta);
		
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}
	
	@Override
	public void uploadToMySQL() {
		BukkitBan.plugin.getMySQL().insert("INSERT INTO Reasons VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", name, display, type.name(), 
				String.valueOf(firstStage), String.valueOf(secondStage), String.valueOf(thirdStage), String.valueOf(firstLine), String.valueOf(secondLine),
				String.valueOf(points), ItemStackAPI.encodeItemStack(item));
	}
}
