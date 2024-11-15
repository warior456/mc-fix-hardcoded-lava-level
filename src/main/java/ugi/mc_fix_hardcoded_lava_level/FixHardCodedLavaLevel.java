package ugi.mc_fix_hardcoded_lava_level;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ugi.mc_fix_hardcoded_lava_level.config.Config;
import ugi.mc_fix_hardcoded_lava_level.config.ConfigHandler;

public class FixHardCodedLavaLevel implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "mc_fix_hardcoded_lava_level";
    public static ConfigHandler CONFIG;
    public static final Logger LOGGER = LoggerFactory.getLogger("mc_fix_hardcoded_lava_level");

    @Override
    public void onInitialize() {
        FixHardCodedLavaLevel.LOGGER.info("Loading Config for " + FixHardCodedLavaLevel.MOD_ID);
        Config.loadConfig();

        LOGGER.info(FixHardCodedLavaLevel.MOD_ID + " has loaded");
    }
}