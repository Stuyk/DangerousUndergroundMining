package stuyk.mining;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.md_5.bungee.api.ChatColor;
import stuyk.mining.mining.ClassicCalculator;

public class StructureIntegrityHandler implements Listener {
	private DangerousUndergroundMining instance;
	
	public StructureIntegrityHandler(DangerousUndergroundMining instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		// Check the player's height.
		if (event.getPlayer().getLocation().getBlockY() >= instance.getConfiguration().getDepth()) {
			return;
		}
		
		// Check if the block the player is placing is valid.
		if (!ClassicCalculator.isValidBlockType(instance.getConfiguration().getSupportMaterials(), event.getBlock())) {
			return;
		}
		
		// Tell the player what the current safety rating is for the structure he's building.
		int safetyRank = instance.getCalculator().getSafetyRank(event.getBlock());
		event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.WHITE + "Structure Rating: " + instance.getCalculator().getMiningSupportSafetyRank(event.getPlayer()) + "/" + instance.getCalculator().getRequiredRank(event.getPlayer())));
	}
}
