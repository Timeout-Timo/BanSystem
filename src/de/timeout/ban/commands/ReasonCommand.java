package de.timeout.ban.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.gui.ClockGUI;
import de.timeout.ban.gui.ConfirmGUI;
import de.timeout.ban.gui.GUI;
import de.timeout.ban.gui.GUILanguage;
import de.timeout.ban.gui.GUIReason;
import de.timeout.ban.gui.NumberGUI;
import de.timeout.ban.gui.ReasonMenu;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.ban.reason.ReasonFileConverter;
import de.timeout.utils.ItemStackAPI;
import de.timeout.utils.Materials;
import de.timeout.utils.Sounds;

public class ReasonCommand implements CommandExecutor, Listener {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private final List<Player> displayNeeded = new ArrayList<Player>();
	private final HashMap<Player, GUIReason> newReasons = new HashMap<Player, GUIReason>();
	private final HashMap<Player, GUI> openGUIs = new HashMap<Player, GUI>();
	
	private ReasonMenu typeMenu;
	private ClockGUI firstStageMenu, secondStageMenu, thirdStageMenu;
	private NumberGUI firstLineMenu, secondLineMenu, pointsMenu;
		
	public ReasonCommand() {
		this.typeMenu = new ReasonMenu();
		this.firstStageMenu = new ClockGUI(GUILanguage.MENU_REASON_FIRSTSTAGE_TITLE.getMessage());
		this.secondStageMenu = new ClockGUI(GUILanguage.MENU_REASON_SECONDSTAGE_TITLE.getMessage());
		this.thirdStageMenu = new ClockGUI(GUILanguage.MENU_REASON_THIRDSTAGE_TITLE.getMessage());
		this.firstLineMenu = new NumberGUI(GUILanguage.MENU_REASON_FIRSTLINE_TITLE.getMessage());
		this.secondLineMenu = new NumberGUI(GUILanguage.MENU_REASON_SECONDLINE_TITLE.getMessage());
		this.pointsMenu = new NumberGUI(GUILanguage.MENU_REASON_POINTS_TITLE.getMessage());
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
					displayNeeded.remove(p);
					newReasons.put(p, new GUIReason(name, p.getItemOnCursor() != null ? p.getItemOnCursor() : 
						ItemStackAPI.createItemStack(Materials.BARRIER, 1)));
					openGUIs.put(p, new ReasonMenu(typeMenu));
					p.playSound(p.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1F, 1F);
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
					p.playSound(p.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1F, 1F);
					break;
				default:
				}
			} else if(args.length == 1) {
				switch(args[0].toLowerCase()) {
				case "import":
					new ReasonFileConverter().uploadToMySQL();
					p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_REASON_IMPORT.getMessage());
					break;
				case "continue":
					break;
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
			
			GUI openGUI = openGUIs.get(p);
			if(openGUI != null) {
				event.setCancelled(true);
				if(openGUI instanceof ConfirmGUI) {
					//RemoveMenu
					if(item.getType() == Materials.STAINED_GLASS_PANE.material()) {
						if(((ConfirmGUI)openGUI).getResult(item)) 
							main.getMySQL().delete("DELETE FROM Reasons WHERE Name = ?", newReasons.get(p).getName());
						openGUI.closeGUI(p);
						openGUIs.remove(p);
					}
				} else if(typeMenu.isGUI(inv)) {
					if(typeMenu.isBanButton(item)) {
						newReasons.get(p).setType(ReasonType.BAN);
						typeMenu.closeGUI(p);	
					} else if(typeMenu.isMuteButton(item)) {
						newReasons.get(p).setType(ReasonType.MUTE);
						typeMenu.closeGUI(p);
					}
					
					ClockGUI cGUI = new ClockGUI(firstStageMenu);
					cGUI.openGUI(p);
					openGUIs.put(p, cGUI);
				} else if(openGUI instanceof ClockGUI) {
					//Manage ClockGUI
					ClockGUI cGUI = (ClockGUI)openGUI;
					
					switch(event.getSlot()) {
						case 1:
							cGUI.addDay();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 4:
							cGUI.addHour();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 7:
							cGUI.addMinute();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 19:
							cGUI.removeDay();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 22:
							cGUI.removeHour();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 25:
							cGUI.removeMinute();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 42:
							cGUI.pressPermaButton();
							p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							break;
						case 38:
							long millis = cGUI.getTimeMillis();
							cGUI.closeGUI(p);
							p.playSound(p.getLocation(), Sounds.NOTE_PIANO.bukkitSound(), 1F, 1F);
							if(firstStageMenu.isGUI(inv)) {
								newReasons.get(p).setFirstStage(millis);
								ClockGUI second = new ClockGUI(secondStageMenu);
								openGUIs.replace(p, second);
								second.openGUI(p);
							} else if(secondStageMenu.isGUI(inv)) {
								newReasons.get(p).setSecondStage(millis);
								ClockGUI third = new ClockGUI(thirdStageMenu);
								openGUIs.replace(p, third);
								third.openGUI(p);
							}
					}
				}
			}
		}
	}
	
	public void continueGUI(Player player) {
		GUIReason reason = newReasons.get(player);
		if(reason != null) {
			GUI gui = null;
			if(reason.getType() == null) {
				gui = new ReasonMenu(typeMenu);
			} else if(reason.getFirstStage() < 1) {
				gui = new ClockGUI(firstStageMenu);
			} else if(reason.getSecondStage() < 1) {
				gui = new ClockGUI(secondStageMenu);
			} else if(reason.getThirdStage() < 1) {
				gui = new ClockGUI(thirdStageMenu);
			} else if(reason.getFirstLine() < 1) {
				gui = new NumberGUI(firstLineMenu);
			} else if(reason.getSecondLine() < 1) {
				gui = new NumberGUI(secondLineMenu);
			} else if(reason.getPoints() < 1) {
				gui = new NumberGUI(pointsMenu);
			} else {	
				player.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_REASON_ADD_DISPLAY.getMessage());
				return;
			} 
			gui.openGUI(player);
			openGUIs.put(player, gui);
			player.playSound(player.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1F, 1F);
		}
	}
	
	@EventHandler
	public void onDisplayCheck(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if(displayNeeded.contains(p)) {
			event.setCancelled(true);
			newReasons.get(p).setDisplay(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
			displayNeeded.remove(p);
			
			p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_REASON_ADD_CONFIRM.getMessage());
			p.playSound(p.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 1F, 1F);
		}
	}
}
