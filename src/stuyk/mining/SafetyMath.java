package stuyk.mining;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class SafetyMath {
	// Valid materials for supporting a mine.
	public static Material[] validStructureMaterials = new Material[] {
			Material.LOG, 
			Material.LOG_2, 
			Material.WOOD, 
			Material.FENCE, 
			Material.NETHER_BRICK, 
			Material.CLAY_BRICK, 
			Material.BRICK, 
			Material.SMOOTH_BRICK
	};
	
	// Materials that count as valid ceiling materials for ceiling collapses.
	public static Material[] validCeilingMaterials = new Material[] {
			Material.STONE,
			Material.COBBLE_WALL,
			Material.COBBLESTONE,
			Material.SANDSTONE,
			Material.NETHER_BRICK,
			Material.SMOOTH_BRICK,
			Material.TORCH,
			Material.DIRT,
			Material.IRON_ORE,
			Material.COAL_ORE,
			Material.DIAMOND_ORE,
			Material.LAPIS_ORE,
			Material.EMERALD_ORE,
			Material.GOLD_ORE,
			Material.QUARTZ_ORE,
			Material.REDSTONE_ORE
	};
	
	public static Material[] validMiningItems = new Material[] {
			Material.DIAMOND_PICKAXE,
			Material.GOLD_PICKAXE,
			Material.IRON_PICKAXE,
			Material.STONE_PICKAXE,
			Material.WOOD_PICKAXE
	};
	
	// How low the light has to prevent players from mining.
	public static int lowLightPoint = 6;
	
	// How far down below ground do players have to be before mining support must be implemented.
	public static int lowHeightPoint = 60;
	
	// Our basic safety scale. 2500 / 60 = 41. 41 safety rating is required to build.
	public static int safetyScale = 2500;
	
	// How many blocks above the player's head should be necessary to count as a mine shaft.
	public static int mineshaftBlockCount = 7;
	
	// How many blocks should we check for above the player's head.
	public static int mineshaftBlockTotalCount = 15;
	
	// How many blocks of X type are above the player.
	public static int getValidBlocksAbovePlayer(int totalAbove, Material[] listOfValidBlocks, Player player) {
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
			for (int b = 0; b < validCeilingMaterials.length; b++) {
				// If the block type matches add 1 to the totalCount.
				if (currentLoopBlock.getType() == validCeilingMaterials[b]) {
					// BAM!
					totalCount += 1;
				}
			}
		}
		return totalCount;
	}

	// Check each block above a specific block and ensure they match. Then return a total safety rank.
	public static int getSafetyRank(Block block) {
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
				} else if (isValidBlockType(SafetyMath.validCeilingMaterials, block.getWorld().getBlockAt(block.getX(), i, block.getZ()))) {
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
				} else if (isValidBlockType(SafetyMath.validCeilingMaterials, block.getRelative(0, i, 0))) {
					return totalSafetyThreshold;
				}
			}
		}
		return totalSafetyThreshold;
	}
	
	public static int getDepthScale(Player player) {
		return 0;
	}
	
	// Loop through our list to see if the block type is valid.
	public static boolean isValidBlockType(Material[] materialList, Block block) {
		for (int i = 0; i < materialList.length; i++) {
			if (materialList[i] == block.getType()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidItemType(Material[] materialList, Material material) {
		for (int i = 0; i < materialList.length; i++) {
			if (materialList[i] == material) {
				return true;
			}
		}
		return false;
	}
}
