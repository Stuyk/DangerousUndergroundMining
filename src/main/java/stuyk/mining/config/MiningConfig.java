package stuyk.mining.config;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs("MiningConfig")
public class MiningConfig implements ConfigurationSerializable, Cloneable
{
    private int depth;
    private int supportRange;
    private boolean lightRequired;
    private int lightLevel;
    private int mineshaftTotalBlockCount;
    private int mineshaftBlockCount;
    private int safetyScale;
    private List<Material> supportMaterials;
    private List<String> unstableMessages;
    private List<Material> collapseMaterials;
    private List<String> collapseMessages;

    public MiningConfig(int depth, int supportRange, boolean lightRequired, int lightLevel,
                        int mineshaftTotalBlockCount, int mineshaftBlockCount, int safetyScale,
                        List<Material> supportMaterials, List<String> unstableMessages,
                        List<Material> collapseMaterials, List<String> collapseMessages)
    {
        this.depth = depth;
        this.supportRange = supportRange;
        this.lightRequired = lightRequired;
        this.lightLevel = lightLevel;
        this.mineshaftTotalBlockCount = mineshaftTotalBlockCount;
        this.mineshaftBlockCount = mineshaftBlockCount;
        this.safetyScale = safetyScale;
        this.supportMaterials = supportMaterials;
        this.unstableMessages = unstableMessages;
        this.collapseMaterials = collapseMaterials;
        this.collapseMessages = collapseMessages;
    }

    @Override
    public Map<String, Object> serialize()
    {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("depth", depth);
        map.put("support-range", supportRange);
        map.put("light-required", lightRequired);
        map.put("light-level", lightLevel);
        map.put("mineshaft-total-block-count", mineshaftTotalBlockCount);
        map.put("mineshaft-block-count", mineshaftBlockCount);
        map.put("safety-scale", safetyScale);
        List<String> supportMats = new LinkedList<>();
        for(Material m : supportMaterials)
        {
            supportMats.add(m.getKey().getKey());
        }
        map.put("support-materials", supportMats);
        List<String> collapseMats = new LinkedList<>();
        for(Material m : collapseMaterials)
        {
            collapseMats.add(m.getKey().getKey());
        }
        map.put("collapse-materials", collapseMats);
        map.put("unstable-messages", unstableMessages);
        map.put("collapse-messages", collapseMessages);
        return map;
    }

    public static MiningConfig deserialize(Map<String, Object> map)
    {
        // Load basic stuff.
        int depth = getIfValid(map, "depth", Integer.class, 60);
        int supportRange = getIfValid(map, "support-range", Integer.class, 5);
        boolean lightRequired = getIfValid(map, "light-required", Boolean.class, true);
        int lightLevel = getIfValid(map, "light-level", Integer.class, 6);
        int mineshaftTotalBlockCount = getIfValid(map, "mineshaft-total-count", Integer.class, 15);
        int mineshaftBlockCount = getIfValid(map, "mineshaft-block-count", Integer.class, 7);
        int safetyScale = getIfValid(map, "safety-scale", Integer.class, 5000);

        ArrayList<String> supportMaterialValues = getIfValid(map, "support-materials", ArrayList.class, new ArrayList());
        List<Material> supportMaterials = new LinkedList<>();
        for(String s : supportMaterialValues)
        {
            Material m = Material.getMaterial(s);
            if(m != null)
            {
                supportMaterials.add(m);
            }
        }

        ArrayList<String> unstableMessages = getIfValid(map, "unstable-messages", ArrayList.class, new ArrayList());

        ArrayList<String> collapseMaterialValues = getIfValid(map, "collapse-materials", ArrayList.class, new ArrayList());
        List<Material> collapseMaterials = new LinkedList<>();
        for(String s : collapseMaterialValues)
        {
            Material m = Material.getMaterial(s);
            if(m != null)
            {
                collapseMaterials.add(m);
            }
        }
        ArrayList<String> collapseMessages = getIfValid(map, "collapse-messages", ArrayList.class, new ArrayList());

        return new MiningConfig(depth, supportRange, lightRequired, lightLevel, mineshaftTotalBlockCount,
                mineshaftBlockCount, safetyScale, supportMaterials, unstableMessages, collapseMaterials,
                collapseMessages);
    }

    private static <T> T getIfValid(Map<String, Object> map, String key, Class<T> clazz, T def)
    {
        if(!map.containsKey(key))
        {
            return def;
        }
        Object obj = map.get(key);
        if(obj == null || !obj.getClass().equals(clazz))
        {
            return def;
        }
        return clazz.cast(map.get(key));
    }

    public boolean isLightRequired()
    {
        return lightRequired;
    }

    public int getDepth()
    {
        return depth;
    }

    public int getMineshaftTotalBlockCount()
    {
        return mineshaftTotalBlockCount;
    }

    public int getSafetyScale()
    {
        return safetyScale;
    }

    public List<Material> getSupportMaterials()
    {
        return supportMaterials;
    }

    public List<Material> getCollapseMaterials()
    {
        return collapseMaterials;
    }

    public int getMineshaftBlockCount()
    {
        return mineshaftBlockCount;
    }

    public int getLightLevel()
    {
        return lightLevel;
    }

    public List<String> getCollapseMessages()
    {
        return collapseMessages;
    }

    public List<String> getUnstableMessages()
    {
        return unstableMessages;
    }

    public int getSupportRange()
    {
        return supportRange;
    }
}
