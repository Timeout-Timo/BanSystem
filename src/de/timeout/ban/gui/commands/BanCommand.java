package de.timeout.ban.gui.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissibleBase;

import de.timeout.ban.gui.BukkitBan;
import de.timeout.ban.gui.GUI;
import de.timeout.ban.gui.ViolenceGUI;
import de.timeout.ban.gui.config.GUILanguage;
import de.timeout.ban.reason.Reason.ReasonType;
import de.timeout.utils.Sounds;

public class BanCommand implements CommandExecutor, Listener {

	private static final BukkitBan main = BukkitBan.plugin;
	
	private final HashMap<Player, GUI> openGUIs = new HashMap<Player, GUI>();
	private final HashMap<Player, String> targets = new HashMap<Player, String>();
	
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
						targets.put(p, ot.getName());
						openGUIs.put(p, gui);
						gui.openGUI(p);
						p.playSound(p.getLocation(), Sounds.CHEST_OPEN.bukkitSound(), 1F, 1F);
					} else p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_BAN_IGNORE.getMessage());
				}
			} else p.sendMessage(GUILanguage.PREFIX.getMessage() + GUILanguage.MESSAGE_ERROR_PERMISSIONS.getMessage());
		}
		return false;
	}
}
