package stuyk.mining.mining;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface StructureCalculator
{
    int getSafetyRank(Block block);
    int getValidBlocksAbovePlayer(int totalAbove, Player player);
}
