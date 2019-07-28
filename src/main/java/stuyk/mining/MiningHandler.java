package stuyk.mining;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import stuyk.mining.mining.ClassicCalculator;

public class MiningHandler implements Listener {
	// Create an instance of the instance class.
	
	DangerousUndergroundMining instance;
	
	// Construct our class with an instance of the instance class.
	public MiningHandler(DangerousUndergroundMining instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		if (event.getPlayer().getInventory().getItemInMainHand().getType().getKey().getKey().endsWith("PICKAXE")) {
			return;
		}

		if(instance.getConfiguration().isLightRequired())
		{
			// Check if the player has enough light to work with.
			verifyLightLevel(event);
		}

		// Check if the player has a certain amount of blocks above their head. This will determine if they receive mining events.
		if (instance.getCalculator().getValidBlocksAbovePlayer(instance.getConfiguration().getMineshaftTotalBlockCount(), event.getPlayer()) <= instance.getConfiguration().getMineshaftBlockCount()) {

			return;
		}

		// Attempt a ceiling collapse.
		attemptCeilingAccident(event);
	}
	
	private void attemptCeilingAccident(BlockBreakEvent event) {
		// Check the blocks around the player and verify they're solid supports.
		if(getMiningSupportSafetyRank(event.getPlayer()) >= instance.getConfiguration().getSafetyScale() / event.getPlayer().getHeight() / 5) {
			return;
		}

		// Check the probability of a collapse event.
		if (rollProbability(95)) {
			createCeilingCollapse(event.getPlayer());
			return;
		}

		instance.getMessageHandler().sendUnstableMessage(event.getPlayer());
	}
	
	// Check if the player has enough light to work with.
	private void verifyLightLevel(BlockBreakEvent event) {
		instance.getLogger().info("Ignoring mining due to invalid light level.");
		if (event.getPlayer().getLocation().getBlock().getRelative(0, 1, 0).getLightLevel() <= instance.getConfiguration().getLightLevel()) {
			event.setCancelled(true);
		}
	}

	public int getMiningSupportSafetyRank(Player player) {
		// Get first block player is near.
		Location relativeBlock = player.getLocation();
		
		int safetyThreshold = 0;
		
		// Gather all of the blocks around the player.
		for (int x = relativeBlock.getBlockX() - instance.getConfiguration().getSupportRange(); x <= relativeBlock.getBlockX() + instance.getConfiguration().getSupportRange(); x++) {
			for (int z = relativeBlock.getBlockZ() - instance.getConfiguration().getSupportRange(); z <= relativeBlock.getBlockZ() + instance.getConfiguration().getSupportRange(); z++) {
				for (int y = relativeBlock.getBlockY() - instance.getConfiguration().getSupportRange(); y <= relativeBlock.getBlockY() + instance.getConfiguration().getSupportRange(); y++) {
					// Check the type of each block around the player.
					World w = relativeBlock.getWorld();
					if(w == null)
					{
						continue;
					}
					Block targetBlock = relativeBlock.getWorld().getBlockAt(x, y, z);
					// Check if the block is of the supported type. If not, skip this block.
					if (!ClassicCalculator.isValidBlockType(instance.getConfiguration().getSupportMaterials(), targetBlock)) {
						continue;
					}
					// If it is...
					// Add to our safety threshold
					safetyThreshold += instance.getCalculator().getSafetyRank(targetBlock);
					// If there is not enough safety continue searching for more supports.
					// If there is enough safety. Finish the loop and break out.
				}
			}
		}
		
		return safetyThreshold;
	}

	// The higher the probability the less often it'll happen.
	private boolean rollProbability(double number) {
		double chance = Math.random() * 100;
		if (chance <= number) {
			return false;
		} else {
			return true;
		}
	}
	
	// This creates a ceiling collapse above the player.
	private void createCeilingCollapse(Player player) {
		Location relativeBlock = player.getLocation();
		for (int height = relativeBlock.getBlockY(); height <= relativeBlock.getBlockY() + 4; height++) {
			for (int x = relativeBlock.getBlockX() - 3; x <= relativeBlock.getBlockX() + 3; x++) {
				for (int z = relativeBlock.getBlockZ() - 3; z <= relativeBlock.getBlockZ() + 3; z++) {
					World w = relativeBlock.getWorld();
					if(w == null)
					{
						continue;
					}
					Block targetBlock = relativeBlock.getWorld().getBlockAt(x, height, z);
					// If the block does not belong to a valid falling ceiling material, continue...
					if (!ClassicCalculator.isValidBlockType(instance.getConfiguration().getCollapseMaterials(), targetBlock)) {
						continue;
					}
					
					// Check if there is air below a specific amount of blocks. 5. If there is make the ceiling collapse for this row of blocks.
					for (int relativeHeight = targetBlock.getY(); relativeHeight > targetBlock.getY() - 5; relativeHeight--) {
						Block nextClosestBlock = targetBlock.getWorld().getBlockAt(targetBlock.getX(), relativeHeight, targetBlock.getZ());
						if (nextClosestBlock.getType() == Material.AIR) {
							createFallingBlock(targetBlock);
						}
					}
				}
			}
		}
		
		instance.getMessageHandler().sendCollapseMessage(player);
	}
	
	private void createFallingBlock(Block targetBlock) {
		targetBlock.setType(Material.GRAVEL);
	}
}
