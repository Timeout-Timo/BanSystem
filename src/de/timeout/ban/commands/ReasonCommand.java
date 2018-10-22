package de.timeout.ban.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.gui.ConfirmGUI;
import de.timeout.ban.gui.GUI;
import de.timeout.ban.gui.GUIReason;
import de.timeout.ban.gui.ReasonMenu;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ReasonCommand implements CommandExecutor, Listener {

	private static final BukkitBan main = BukkitBan.plugin;
	private static final String removeTitle = ChatColor.translateAlternateColorCodes('&', main.getLanguage().getString("menu.reason.remove.title"));
	private static final String removeYes = ChatColor.translateAlternateColorCodes('&', main.getLanguage().getString("items.reason.remove.yes"));
	private static final String removeNo = ChatColor.translateAlternateColorCodes('&', main.getLanguage().getString("menu.reason.remove.no"));
		
	private final HashMap<Player, GUIReason> newReasons = new HashMap<Player, GUIReason>();
	private final HashMap<Player, GUI> openGUIs = new HashMap<Player, GUI>();
	
	private ReasonMenu reasonMenu;
	
	public ReasonCommand() {
		this.reasonMenu = new ReasonMenu();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args.length > 2) {
				String name = args[1];
				switch(args[0].toLowerCase()) {
				case "add":
					if(newReasons.containsKey(p))newReasons.remove(p);
					reasonMenu.openGUI(p);
					newReasons.put(p, new GUIReason(name, p.getItemOnCursor() != null ? p.getItemOnCursor() : 
						ItemStackAPI.createItemStack(Materials.BARRIER, 1)));
					openGUIs.put(p, reasonMenu);

					break;
				case "remove":
					ConfirmGUI gui = new ConfirmGUI(ItemStackAPI.decodeItemStack((String) main.getMySQL().getValue("SELECT Item FROM Reason WHERE Name = ?", name)),
							removeTitle, removeYes, removeNo);
					gui.openGUI(p);
					newReasons.put(p, new GUIReason(name, gui.getMiddle()));
					openGUIs.put(p, gui);
					break;
				default:
				}
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("import")) {
					
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player && event.getClickedInventory() != null && event.getCurrentItem() != null) {
			Player p = (Player) event.getWhoClicked();
			Inventory inv = event.getClickedInventory();
			ItemStack item = event.getCurrentItem();
			if(reasonMenu.isOpen(p)) {
				GUIReason reason = newReasons.get(p);
				if(reasonMenu.isBanButton(item)) {
					reason.setType(ReasonType.BAN);
					
					//Nächstes Inv mit FirstStage öffnen.
				} else if(reasonMenu.isMuteButton(item)) {
					reason.setType(ReasonType.MUTE);
					p.closeInventory();
					//Nächstes Inv mit FirstStage öffnen
				}
			} else if(inv.getName().equalsIgnoreCase(removeTitle)) {
				event.setCancelled(true);
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase(removeYes)) {
					main.getMySQL().delete("DELETE FROM Reason WHERE Name = ?", newReasons.get(p).getName());
					p.closeInventory();
				} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(removeNo)) p.closeInventory();
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player p = (Player) event.getPlayer();
			GUI gui;
			if((gui = openGUIs.get(p)) != null) {
				gui.closeGUI(p);
				
			}
		}
	}
}
