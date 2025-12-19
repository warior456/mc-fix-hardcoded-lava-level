package ugi.mc_fix_hardcoded_lava_level.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import ugi.mc_fix_hardcoded_lava_level.FixHardCodedLavaLevel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Config {
    public static final int CURRENT_CONFIG_VERSION = 2; // Used to validate version.

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), FixHardCodedLavaLevel.MOD_ID + "_config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (configFile.exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);
                FixHardCodedLavaLevel.CONFIG = gson.fromJson(fileReader, ConfigHandler.class);
                fileReader.close();
                // if version outdated, do not load
                if (FixHardCodedLavaLevel.CONFIG.config_Version != CURRENT_CONFIG_VERSION)
                {
                    FixHardCodedLavaLevel.LOGGER.error("Ignoring Config. Expected version: {}, Got: {}", CURRENT_CONFIG_VERSION, FixHardCodedLavaLevel.CONFIG.config_Version);
                    if (FixHardCodedLavaLevel.CONFIG.verbose_Mode)
                    {
                        if (FixHardCodedLavaLevel.CONFIG.config_Version < CURRENT_CONFIG_VERSION)
                        {
                            FixHardCodedLavaLevel.LOGGER.info("Try recreating the config, remember your settings!");
                        }
                        else if (FixHardCodedLavaLevel.CONFIG.config_Version > CURRENT_CONFIG_VERSION)
                        {
                            // Brain rot
                            FixHardCodedLavaLevel.LOGGER.warn("The config version is somehow greater, that's sus.");
                        }
                    }
                    // Ignore the config and use the default instead. DO NOT SAVE IT OR IT WILL OVERWRITE
                    FixHardCodedLavaLevel.CONFIG = new ConfigHandler();
                }
                else
                {
                    saveConfig(); // Update config
                }
            } catch (IOException e) {
	            FixHardCodedLavaLevel.LOGGER.warn("The config file: {} was not loaded", e.getLocalizedMessage());
            }
        } else {
            // Create the config
            FixHardCodedLavaLevel.CONFIG = new ConfigHandler();
            saveConfig();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), FixHardCodedLavaLevel.MOD_ID + "_config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!configFile.getParentFile().exists()) {
            // Make the directory
            configFile.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(gson.toJson(FixHardCodedLavaLevel.CONFIG));
            fileWriter.close();
        } catch (IOException e) {
	        FixHardCodedLavaLevel.LOGGER.warn("Config was not saved to: {}", e.getLocalizedMessage());
        }
    }
}
