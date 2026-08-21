package ugi.mc_fix_hardcoded_lava_level;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ugi.mc_fix_hardcoded_lava_level.config.Config;
import ugi.mc_fix_hardcoded_lava_level.config.ConfigHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FixHardCodedLavaLevel implements ModInitializer {
    public static final String MOD_ID = "mc_fix_hardcoded_lava_level";
    public static ConfigHandler CONFIG;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final int DEFAULT_SEA_LEVEL = 63; // Used for debug reasons
    private static final int DEFAULT_MINIMUM_HEIGHT = -64;
    public static final String JSON_LAVA_LEVEL_KEY = "lava_level";
    public static Integer overworldBottomLavaLevel; // Integer Object so null can be used

    ResourceManagerReloadListener lavaLevelReloadListener =resourceManager ->
    {
        // Iterate over all noise setting files
        for (Identifier dimensionNoiseSetting : resourceManager.listResources("worldgen/noise_settings", path -> path.toString().endsWith(".json")).keySet())
        {
            // Find the overworld noise setting file
            if (dimensionNoiseSetting.getPath().endsWith("overworld.json"))
            {
                // Open stream and parse via Gson library
	            InputStream dimensionNoiseSettingStream;
	            try
	            {
		            dimensionNoiseSettingStream = resourceManager.getResourceOrThrow(dimensionNoiseSetting).open();
                    JsonObject dimensionNoiseSettingJson = GsonHelper.parse(new InputStreamReader(dimensionNoiseSettingStream, StandardCharsets.UTF_8));
                    // Set value only if the key exists already
                    if (dimensionNoiseSettingJson.keySet().contains(JSON_LAVA_LEVEL_KEY))
                    {
                        overworldBottomLavaLevel = dimensionNoiseSettingJson.get(JSON_LAVA_LEVEL_KEY).getAsInt();
                        if (CONFIG.verbose_Mode)
                        {
                            LOGGER.info("Data-driven Lava Level set to y = {} for this world", overworldBottomLavaLevel);
                        }
                    }
                    else
                    {
                        overworldBottomLavaLevel = null;
                    }
	            }
	            catch (IOException e)
	            {
		            throw new RuntimeException(e);
	            }

            }
        }
    };

    @Override
    public void onInitialize() {
        //FixHardCodedLavaLevel.LOGGER.info("Loading Config for " + FixHardCodedLavaLevel.MOD_ID);

        // Register the datapack listener, requires the Fabric API
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(Identifier.fromNamespaceAndPath(MOD_ID, "resource_loader"), lavaLevelReloadListener);

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