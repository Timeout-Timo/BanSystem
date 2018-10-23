package de.timeout.ban.commands;

import java.util.HashMap;

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
import de.timeout.ban.gui.ClockGUI;
import de.timeout.ban.gui.ConfirmGUI;
import de.timeout.ban.gui.GUI;
import de.timeout.ban.gui.GUILanguage;
import de.timeout.ban.gui.GUIReason;
import de.timeout.ban.gui.ReasonMenu;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.ban.reason.ReasonFileConverter;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;

public class ReasonCommand implements CommandExecutor, Listener {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private final HashMap<Player, GUIReason> newReasons = new HashMap<Player, GUIReason>();
	private final HashMap<Player, GUI> openGUIs = new HashMap<Player, GUI>();
	
	private ReasonMenu typeMenu;
	private ClockGUI firstStageMenu, secondStageMenu, thirdStageMenu;
		
	public ReasonCommand() {
		this.typeMenu = new ReasonMenu();
		this.firstStageMenu = new ClockGUI(GUILanguage.MENU_REASON_FIRSTSTAGE_TITLE.getMessage());
		this.secondStageMenu = new ClockGUI(GUILanguage.MENU_REASON_SECONDSTAGE_TITLE.getMessage());
		this.thirdStageMenu = new ClockGUI(GUILanguage.MENU_REASON_THIRDSTAGE_TITLE.getMessage());
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
					typeMenu.openGUI(p);
					newReasons.put(p, new GUIReason(name, p.getItemOnCursor() != null ? p.getItemOnCursor() : 
						ItemStackAPI.createItemStack(Materials.BARRIER, 1)));
					openGUIs.put(p, typeMenu);

					break;
				case "remove":
					ItemStack middle = ItemStackAPI.decodeItemStack((String) main.getMySQL().getValue("SELECT Item FROM Reason WHERE Name = ?", name));
					if(middle != null) {
						ConfirmGUI gui = new ConfirmGUI(middle, GUILanguage.MENU_REASON_MENU_TITLE.getMessage(),
								GUILanguage.ITEMS_REASON_REMOVE_YES.getMessage(), GUILanguage.ITEMS_REASON_REMOVE_YES.getMessage());
						gui.openGUI(p);
						newReasons.put(p, new GUIReason(name, gui.getMiddle()));
						openGUIs.put(p, gui);
					} else p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_REASON_REMOVE_NOTFOUND.getMessage());

					break;
				default:
				}
			} else if(args.length == 1 && args[0].equalsIgnoreCase("import")) {
				new ReasonFileConverter().uploadToMySQL();
				p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_REASON_IMPORT.getMessage());
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
			
			GUI openGUI;
			if((openGUI = openGUIs.get(p)) != null) {
				if(openGUI instanceof ConfirmGUI) {
					event.setCancelled(true);
					//RemoveMenu
					if(item.getType() == Materials.STAINED_GLASS_PANE.material()) {
						if(((ConfirmGUI)openGUI).getResult(item)) 
							main.getMySQL().delete("DELETE FROM Reasons WHERE Name = ?", newReasons.get(p).getName());
						openGUI.closeGUI(p);
						openGUIs.remove(p);
					}
				} else if(typeMenu.isGUI(inv)) {
					event.setCancelled(true);
					if(typeMenu.isBanButton(item)) {
						newReasons.get(p).setType(ReasonType.BAN);
						typeMenu.closeGUI(p);
						
					} else if(typeMenu.isMuteButton(item)) {
						newReasons.get(p).setType(ReasonType.MUTE);
						typeMenu.closeGUI(p);
					}
				} else if(openGUI instanceof ClockGUI) {
					//Manage ClockGUI
					ClockGUI cGUI = (ClockGUI)openGUI;
					event.setCancelled(true);
					
					switch(event.getSlot()) {
						case 1:
							cGUI.addDay();
							break;
						case 4:
							cGUI.addHour();
							break;
						case 7:
							cGUI.addMinute();
							break;
						case 19:
							cGUI.removeDay();
							break;
						case 22:
							cGUI.removeHour();
							break;
						case 25:
							cGUI.removeMinute();
							break;
						case 26
					}
				}
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
				openGUIs.remove(p);
			}
		}
	}
}
