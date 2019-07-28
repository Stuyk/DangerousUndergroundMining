package stuyk.mining.mining;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface StructureCalculator
{
    int getSafetyRank(Block block);
    int getValidBlocksAbovePlayer(int totalAbove, Player player);
    int getMiningSupportSafetyRank(Player player);
    int getRequiredRank(Player player);
}
