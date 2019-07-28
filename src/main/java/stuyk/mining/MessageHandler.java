package stuyk.mining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageHandler {

	private final List<String> unstableMessages;
	private final List<String> collapseMessages;

	public MessageHandler(DangerousUndergroundMining instance)
	{
		this.collapseMessages = instance.getConfiguration().getCollapseMessages();
		this.unstableMessages = instance.getConfiguration().getUnstableMessages();
	}

	public void sendCollapseMessage(Player player) {
		int randomMessageID = new Random().nextInt(collapseMessages.size());
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(unstableMessages.get(randomMessageID), ChatColor.RED));
	}

	public void sendUnstableMessage(Player player)
	{
		int randomMessageID = new Random().nextInt(unstableMessages.size());
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(unstableMessages.get(randomMessageID), ChatColor.GRAY));
	}
}
