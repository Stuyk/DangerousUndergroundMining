package stuyk.mining;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.md_5.bungee.api.ChatColor;

public class StructureIntegrityHandler implements Listener {
	Main main;
	
	public StructureIntegrityHandler(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		// Check the player's height.
		if (event.getPlayer().getLocation().getBlockY() >= SafetyMath.lowHeightPoint) {
			return;
		}
		
		// Check if the block the player is placing is valid.
		if (!SafetyMath.isValidBlockType(SafetyMath.validStructureMaterials, event.getBlock())) {
			return;
		}
		
		// Tell the player what the current safety rating is for the structure he's building.
		int safetyRank = SafetyMath.getSafetyRank(event.getBlock());
		event.getPlayer().sendMessage(ChatColor.YELLOW + "Structure Rating: " + safetyRank);
	}
}
