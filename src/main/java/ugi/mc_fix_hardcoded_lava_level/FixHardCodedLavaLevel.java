package ugi.mc_fix_hardcoded_lava_level;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ugi.mc_fix_hardcoded_lava_level.config.Config;
import ugi.mc_fix_hardcoded_lava_level.config.ConfigHandler;

public class FixHardCodedLavaLevel implements ModInitializer {
    public static final String MOD_ID = "mc_fix_hardcoded_lava_level";
    public static ConfigHandler CONFIG;
    public static final Logger LOGGER = LoggerFactory.getLogger("Fix Hardcoded Lava Level");
    private static final int DEFAULT_SEA_LEVEL = 63; // Used for debug reasons

    @Override
    public void onInitialize() {
        //FixHardCodedLavaLevel.LOGGER.info("Loading Config for " + FixHardCodedLavaLevel.MOD_ID);
        Config.loadConfig();
        LOGGER.info("Lava Level: {} blocks below sea level", CONFIG.vertical_Sea_To_Lava_Separation);
        LOGGER.info("Lava will fill below y = {} if sea level is {}", DEFAULT_SEA_LEVEL - CONFIG.vertical_Sea_To_Lava_Separation, DEFAULT_SEA_LEVEL);
        //LOGGER.info(FixHardCodedLavaLevel.MOD_ID + " has loaded successfully");
    }
}