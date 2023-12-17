package useless.spawneggs.config;


import io.github.prospector.modmenu.ModMenu;
import io.github.prospector.modmenu.config.ModMenuConfig;
import net.fabricmc.loader.api.FabricLoader;
import useless.spawneggs.SpawnEggsMod;

import java.io.*;

public class SpawnEggsConfigManager {
	private static File file;
	private static SpawnEggsConfig config;

	private static void prepareBiomeConfigFile() {
		if (file != null) {
			return;
		}
		file = new File(FabricLoader.getInstance().getConfigDirectory(), SpawnEggsMod.MOD_ID + ".json");
	}

	public static SpawnEggsConfig initializeConfig() {
		if (config != null) {
			return config;
		}

		config = new SpawnEggsConfig();
		load();

		return config;
	}

	private static void load() {
		prepareBiomeConfigFile();

		try {
			if (!file.exists()) {
				save();
			}
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));

				config = ModMenu.GSON.fromJson(br, SpawnEggsConfig.class);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load Spawn Eggs configuration file; reverting to defaults");
			e.printStackTrace();
		}
	}

	public static void save() {
		prepareBiomeConfigFile();

		String jsonString = ModMenu.GSON.toJson(config);

		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save Mod Menu configuration file");
			e.printStackTrace();
		}
	}

	public static SpawnEggsConfig getConfig() {
		return config;
	}
}
