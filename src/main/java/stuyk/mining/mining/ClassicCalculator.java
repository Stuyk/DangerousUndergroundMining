package stuyk.mining.mining;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import stuyk.mining.DangerousUndergroundMining;

import java.util.List;

public final class ClassicCalculator implements StructureCalculator
{
	private DangerousUndergroundMining instance;

	public ClassicCalculator(DangerousUndergroundMining instance)
	{
		this.instance = instance;
	}
	
	// How many blocks of X type are above the player.
	public int getValidBlocksAbovePlayer(int totalAbove, Player player) {
		// Declare an integer that will be our count.
		int totalCount = 0;
		// If we find air, loop and add 1.
		int airCount = 0;
		// Get the first most relevant block to the player.
		Block block = player.getLocation().getBlock();
		for (int i = block.getY(); i < block.getY() + totalAbove; i++) {
			Block currentLoopBlock = player.getWorld().getBlockAt(block.getX(), i, block.getZ());
			if (currentLoopBlock.getType() == Material.AIR) {
				airCount += 1;
			}
		}
		
		// Get the height of the block and loop it based on the height of the block + the total times we want to loop. Fancy way of going i = 0; i < totalCount
		for (int i = block.getY(); i < block.getY() + totalAbove + airCount; i++) {
			// Loop through a list of Materials in an array and compare the block type to them.
			Block currentLoopBlock = player.getWorld().getBlockAt(block.getX(), i, block.getZ());
			if(instance.getConfiguration().getCollapseMaterials().contains(currentLoopBlock.getType()))
			{
				totalCount += 1;
			}
		}
		return totalCount;
	}

	// Check each block above a specific block and ensure they match. Then return a total safety rank.
	public int getSafetyRank(Block block) {
		int totalSafetyThreshold = 0;
		int lowestStructurePoint = block.getY();
		boolean lastBlockValid = true;

		// Get our lowest structure point.
		Block lowestBlock = block;
		for (int i = 0; i < 10; i++) {
			if (lowestBlock.getRelative(BlockFace.DOWN).getType() == block.getType()) {
				lowestBlock = lowestBlock.getRelative(BlockFace.DOWN);
			} else {
				break;
			}
		}
		
		for (int i = lowestBlock.getY(); i < lowestBlock.getY() + 10; i++) {
			if (lastBlockValid) {
				if (block.getWorld().getBlockAt(block.getX(), i, block.getZ()).getType() == block.getType()) {
					lastBlockValid = true;
					totalSafetyThreshold += 5;
				} else if (isValidBlockType(instance.getConfiguration().getCollapseMaterials(), block.getWorld().getBlockAt(block.getX(), i, block.getZ()))) {
					return totalSafetyThreshold;
				}
			}
		}
		
		// Check the safety rank of our structure.
		for (int i = lowestStructurePoint; i < lowestStructurePoint + 10; i++) {
			if (lastBlockValid) {
				if (block.getRelative(0, i - lowestStructurePoint, 0).getType() == block.getType()) {
					lastBlockValid = true;
					totalSafetyThreshold += 5;
				} else if (isValidBlockType(instance.getConfiguration().getCollapseMaterials(), block.getRelative(0, i, 0))) {
					return totalSafetyThreshold;
				}
			}
		}
		return totalSafetyThreshold;
	}

	// Loop through our list to see if the block type is valid.
	public static boolean isValidBlockType(List<Material> materials, Block block) {
		return materials.contains(block.getType());
	}
	
	public static boolean isValidItemType(Material[] materialList, Material material) {
		for (int i = 0; i < materialList.length; i++) {
			if (materialList[i] == material) {
				return true;
			}
		}
		return false;
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

	@Override
	public int getRequiredRank(Player player)
	{
		return instance.getConfiguration().getSafetyScale() / (player.getLocation().getBlockY() / instance.getConfiguration().getHeightThreshold());
	}
}
