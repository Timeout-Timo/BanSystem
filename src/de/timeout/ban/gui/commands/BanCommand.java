package de.timeout.ban.gui.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissibleBase;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.gui.ClockGUI;
import de.timeout.ban.gui.GUI;
import de.timeout.ban.gui.ViolenceGUI;
import de.timeout.ban.gui.config.GUILanguage;
import de.timeout.ban.gui.elements.GUIReason;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.utils.Sounds;

public class BanCommand implements CommandExecutor, Listener {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private final HashMap<Player, GUI> openGUIs = new HashMap<Player, GUI>();
	private final HashMap<Player, OfflinePlayer> targets = new HashMap<Player, OfflinePlayer>();
	
	private ViolenceGUI violenceMenu;
	
	public BanCommand() {
		this.violenceMenu = new ViolenceGUI(ReasonType.BAN, GUILanguage.MENU_BAN_TITLE.getMessage(), GUILanguage.ITEMS_BAN_CUSTOMBAN_ITEM.getMessage());
		
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("ban.ban")) {
				if(args.length > 0) {
					OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(args[0]);
					PermissibleBase ob = new PermissibleBase(ot);
					if(!ob.hasPermission("ban.ignore")) {
						ViolenceGUI gui = new ViolenceGUI(violenceMenu);
						targets.put(p, ot);
						openGUIs.put(p, gui);
						gui.openGUI(p);
						p.playSound(p.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1F, 1F);
					} else p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_BAN_IGNORE.getMessage().replaceAll("%p", ot.getName()));
				}
			} else p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_ERROR_PERMISSIONS.getMessage());
		}
		return false;
	}
	
	private void sendBanMessage(Player sender, UUID bannedPerson, long time, String reasonName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Ban");
		out.writeUTF(bannedPerson.toString());
		out.writeLong(time);
		out.writeUTF(reasonName);
		
		sender.sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	@EventHandler
	public void onGUIManagement(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player && event.getClickedInventory() != null && event.getCurrentItem() != null) {
			Player p = (Player) event.getWhoClicked();
			Inventory inv = event.getClickedInventory();
			GUI openGUI = openGUIs.get(p);
			if(openGUI != null && openGUI.isGUI(inv)) {
				event.setCancelled(true);
				if(openGUI instanceof ClockGUI) {
					ClockGUI cGUI = (ClockGUI)openGUIs.get(p);
					
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
							if(p.hasPermission("ban.ban.perma")) {
								cGUI.pressPermaButton();
								p.playSound(p.getLocation(), Sounds.CLICK.bukkitSound(), 1F, 1F);
							} else p.playSound(p.getLocation(), Sounds.NOTE_BASS.bukkitSound(), 1F, 1F);
							break;
						case 38:
							cGUI.closeGUI(p);
							p.playSound(p.getLocation(), Sounds.NOTE_PIANO.bukkitSound(), 1F, 1F);							
							sendBanMessage(p, targets.get(p).getUniqueId(), cGUI.getTimeMillis(), "Custom");
							openGUIs.remove(p);
							targets.remove(p);
					}
				} else if(openGUI instanceof ViolenceGUI) {
					GUIReason reason;
					if(event.getSlot() == openGUI.getMenu().getSize() -1) {
						//Presses Custom-Menu
						if(p.hasPermission("ban.ban.custom")) {
							ClockGUI customGUI = new ClockGUI(GUILanguage.MENU_BAN_CUSTOMBAN.getMessage().replaceAll("%p", targets.get(p).getName()));
							customGUI.openGUI(p);
							openGUIs.replace(p, customGUI);
						} else p.playSound(p.getLocation(), Sounds.NOTE_BASS.bukkitSound(), 1F, 1F);
					} else if((reason = GUIReason.getReasonFromItemStack(event.getCurrentItem())) != null) {
						sendBanMessage(p, targets.get(p).getUniqueId(), 0L, reason.getName());
						p.playSound(p.getLocation(), Sounds.NOTE_PIANO.bukkitSound(), 1F, 1F);
						openGUI.closeGUI(p);
						openGUIs.remove(p);
						targets.remove(p);
					}
				}
			} else openGUIs.remove(p);
		}
	}
}
