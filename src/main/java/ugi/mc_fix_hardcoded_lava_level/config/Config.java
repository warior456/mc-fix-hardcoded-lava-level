package ugi.mc_fix_hardcoded_lava_level.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import ugi.mc_fix_hardcoded_lava_level.FixHardCodedLavaLevel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Config
{
	public static final int CURRENT_CONFIG_VERSION = 2; // Used to validate version.

	public static void loadConfig()
	{
		File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), FixHardCodedLavaLevel.MOD_ID + "_config.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (configFile.exists())
		{
			try
			{
				FileReader fileReader = new FileReader(configFile);
				FixHardCodedLavaLevel.CONFIG = gson.fromJson(fileReader, ConfigHandler.class);
				fileReader.close();
				// if version outdated, do not load
				if (FixHardCodedLavaLevel.CONFIG.config_Version != CURRENT_CONFIG_VERSION)
				{
					FixHardCodedLavaLevel.LOGGER.error("Expected version: {}, Got: {}", CURRENT_CONFIG_VERSION, FixHardCodedLavaLevel.CONFIG.config_Version);

					if (FixHardCodedLavaLevel.CONFIG.config_Version < CURRENT_CONFIG_VERSION)
					{
						if (FixHardCodedLavaLevel.CONFIG.verbose_Mode)
						{
							FixHardCodedLavaLevel.LOGGER.info("Migrating config to version: {}", CURRENT_CONFIG_VERSION);
						}
						// Upgrade config file
						FixHardCodedLavaLevel.CONFIG = migrateConfiguration(FixHardCodedLavaLevel.CONFIG);
						saveConfig(); // save new one
					}
					else if (FixHardCodedLavaLevel.CONFIG.config_Version > CURRENT_CONFIG_VERSION)
					{
						// Brain rot
						if (FixHardCodedLavaLevel.CONFIG.verbose_Mode)
						{
							FixHardCodedLavaLevel.LOGGER.warn("The config version is somehow greater, that's sus. Ignoring Config...");
						}
						// Ignore the config and use the default instead. DO NOT SAVE IT OR IT WILL OVERWRITE
						FixHardCodedLavaLevel.CONFIG = new ConfigHandler();
					}

				}
				else
				{
					saveConfig(); // save config
				}
			}
			catch (IOException e)
			{
				FixHardCodedLavaLevel.LOGGER.warn("The config file: {} was not loaded", e.getLocalizedMessage());
			}
		}
		else
		{
			// Create the config
			FixHardCodedLavaLevel.CONFIG = new ConfigHandler();
			saveConfig();
		}
	}

	@SuppressWarnings ("ResultOfMethodCallIgnored")
	public static void saveConfig()
	{
		File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), FixHardCodedLavaLevel.MOD_ID + "_config.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (! configFile.getParentFile().exists())
		{
			// Make the directory
			configFile.getParentFile().mkdir();
		}
		try
		{
			FileWriter fileWriter = new FileWriter(configFile);
			fileWriter.write(gson.toJson(FixHardCodedLavaLevel.CONFIG));
			fileWriter.close();
		}
		catch (IOException e)
		{
			FixHardCodedLavaLevel.LOGGER.warn("Config was not saved to: {}", e.getLocalizedMessage());
		}
	}

	private static ConfigHandler migrateConfiguration(ConfigHandler parsedConfigFile)
	{
		ConfigHandler migratedConfig;
		migratedConfig = new ConfigHandler();
		migratedConfig.config_Version = CURRENT_CONFIG_VERSION; // Set version, should be redundant if the code is properly updated between versions.
		migratedConfig.verbose_Mode = parsedConfigFile.verbose_Mode; // set to default if it does not exist
		//migratedConfig.vertical_Sea_To_Lava_Separation = parsedConfigFile.distance_between_sea_and_lava_level;
		migratedConfig.vertical_Reference_To_Lava_Separation = parsedConfigFile.vertical_Reference_To_Lava_Separation;
		migratedConfig.vertical_Reference_Type = parsedConfigFile.vertical_Reference_Type;
		// In the future add all keys to transfer over.

		return migratedConfig;
	}
}
