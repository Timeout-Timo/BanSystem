package de.timeout.ban.gui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.timeout.ban.gui.elements.Button;
import de.timeout.ban.gui.elements.GUIReason;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ViolenceGUI extends GUI {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private ReasonType type;
	private List<GUIReason> reasons;
	private Button customban;
	
	public ViolenceGUI(ReasonType type, String title, String customban) {
		this.type = type;
		this.reasons = getReasonsFromSQL();
		this.menu = Bukkit.createInventory(null, 9 * (reasons.size() / 9 + 1), title);
		for(int i = 0; i < menu.getSize(); i++)menu.setItem(i, n);
		for(int i = 0; i < reasons.size(); i++) menu.setItem(i, reasons.get(i).getItem());
		
		this.customban = new Button(ItemStackAPI.createItemStack(Materials.BOOK_AND_QUILL, 1, customban));
		this.menu.setItem(menu.getSize() -1, this.customban.getItem());
	}
	
	public ViolenceGUI(ViolenceGUI source) {
		this.type = source.getType();
		this.reasons = getReasonsFromSQL();
		
		this.menu = Bukkit.createInventory(null, 9 * (reasons.size() / 9 + 1), source.getMenu().getName());
		for(int i = 0; i < menu.getSize(); i++)menu.setItem(i, n);
		for(int i = 0; i < reasons.size(); i++) menu.setItem(i, reasons.get(i).getItem());
		
		this.customban = new Button(source.getMenu().getItem(source.getMenu().getSize() -1));
		this.menu.setItem(menu.getSize() -1, this.customban.getItem());
	}
	
	public ReasonType getType() {
		return type;
	}
	
	private List<GUIReason> getReasonsFromSQL() {
		List<GUIReason> list = new ArrayList<GUIReason>();
		try(PreparedStatement ps = main.getMySQL().getConnection().prepareStatement("SELECT * FROM Reasons WHERE Type = ?")) {
			ps.setString(1, type.name());
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					reasons.add(new GUIReason(rs.getString("Name"), rs.getString("Display"), type,
							rs.getLong("FirstStage"), rs.getLong("SecondStage"), rs.getLong("ThirdStage"), rs.getInt("FirstLine"), rs.getInt("SecondStage"), rs.getInt("Points"),
							ItemStackAPI.decodeItemStack(rs.getString("Item"))));
					
				}
			}
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not get data from MySQL-Database", e);
		}
		return list;
	}
}
