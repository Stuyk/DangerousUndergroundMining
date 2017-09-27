package stuyk.mining;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		ErrorLogger("Started");
		Bukkit.getPluginManager().registerEvents(new MiningHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new StructureIntegrityHandler(this), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void ErrorLogger(String message) {
		System.out.println(String.format("Mining -> %s", message));
	}
}
