package stuyk.mining;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import stuyk.mining.config.MiningConfig;
import stuyk.mining.mining.ClassicCalculator;
import stuyk.mining.mining.StructureCalculator;

import java.io.File;

public class DangerousUndergroundMining extends JavaPlugin {

	private MiningConfig config;
	private StructureCalculator structureCalculator;
	private MessageHandler messageHandler;

	@Override
	public void onEnable()
	{
		ConfigurationSerialization.registerClass(MiningConfig.class);

		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists())
		{
			saveResource("config.yml", false);
		}
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
		config = (MiningConfig) configuration.get("config");

		getLogger().info("Loaded config with " + config.getSupportMaterials().size() + " support materials, and "
				+ config.getCollapseMaterials().size() + " collapsable materials.");

		this.structureCalculator = new ClassicCalculator(this);
		this.messageHandler = new MessageHandler(this);

		Bukkit.getPluginManager().registerEvents(new MiningHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new StructureIntegrityHandler(this), this);
	}

	@Override
	public void onDisable() {
		
	}

	public MiningConfig getConfiguration()
	{
		return config;
	}

	public StructureCalculator getCalculator()
	{
		return structureCalculator;
	}

	public MessageHandler getMessageHandler()
	{
		return messageHandler;
	}
}
