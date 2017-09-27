package stuyk.mining;

import java.util.Random;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageHandler {
	public static String[] collapseMessages = new String[] {
		"The ceiling suddenly collapses around you.",
		"The ceiling breaks above you and falls down on top of you."
	};

	public static void sendCollapseMessage(Player player) {
		double randomMessageID = new Random().nextInt(collapseMessages.length - 1);
		player.sendMessage(ChatColor.RED + collapseMessages[(int) randomMessageID]);
	}
}
