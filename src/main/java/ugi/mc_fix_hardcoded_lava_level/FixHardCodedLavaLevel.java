package ugi.mc_fix_hardcoded_lava_level;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ugi.mc_fix_hardcoded_lava_level.config.Config;
import ugi.mc_fix_hardcoded_lava_level.config.ConfigHandler;

public class FixHardCodedLavaLevel implements ModInitializer {
    public static final String MOD_ID = "mc_fix_hardcoded_lava_level";
    public static ConfigHandler CONFIG;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final int DEFAULT_SEA_LEVEL = 63; // Used for debug reasons
    private static final int DEFAULT_MINIMUM_HEIGHT = -64;

    @Override
    public void onInitialize() {
        //FixHardCodedLavaLevel.LOGGER.info("Loading Config for " + FixHardCodedLavaLevel.MOD_ID);
        Config.loadConfig();
        if (CONFIG.verbose_Mode)
        {
            LOGGER.info("Lava Level: {} blocks {}", CONFIG.vertical_Reference_To_Lava_Separation, CONFIG.vertical_Reference_Type);
            switch (CONFIG.vertical_Reference_Type)
            {
                case BELOW_SEA_LEVEL:
                    LOGGER.info("Lava will fill below y = {} if sea level is {}", DEFAULT_SEA_LEVEL - CONFIG.vertical_Reference_To_Lava_Separation, DEFAULT_SEA_LEVEL);
                    break;
                case ABOVE_BOTTOM:
                    LOGGER.info("Lava will fill below y= {} if world ends at y = {}", DEFAULT_MINIMUM_HEIGHT + CONFIG.vertical_Reference_To_Lava_Separation, DEFAULT_MINIMUM_HEIGHT);
                    break;
                default:
                    break;
            }

        }
        else
        {
            LOGGER.info("Lava Level is now smarter! Thank me later :)");
        }
    }
}