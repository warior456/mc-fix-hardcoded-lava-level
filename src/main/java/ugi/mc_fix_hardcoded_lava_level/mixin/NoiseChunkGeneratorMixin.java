package ugi.mc_fix_hardcoded_lava_level.mixin;



import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import ugi.mc_fix_hardcoded_lava_level.FixHardCodedLavaLevel;

@Mixin({NoiseChunkGenerator.class})
public abstract class NoiseChunkGeneratorMixin {

    /**
     * @author Matteo_fey (@warior456)
     * @author mrburgerUS (@mrburger)
     * @reason Replace -54 Lava level with configuration
     */
    @Overwrite
    private static AquiferSampler.FluidLevelSampler createFluidLevelSampler(ChunkGeneratorSettings settings) {
        int configuredBottomLevelSetting = getConfiguredBottomLevelSetting(settings);

        // Fluid Sampler for the bottom Lava fill (the purpose of this mod)
        AquiferSampler.FluidLevel bottomFluidLevel = new AquiferSampler.FluidLevel(configuredBottomLevelSetting, Blocks.LAVA.getDefaultState());
        // Fluid Sampler for the Sea Level fill
        AquiferSampler.FluidLevel seaLevelFluid = new AquiferSampler.FluidLevel(settings.seaLevel(), settings.defaultFluid());
        // Fluid Sampler used when sea level and aquifers are disabled, I guess?
        AquiferSampler.FluidLevel disabledFluidLevel = new AquiferSampler.FluidLevel(DimensionType.MIN_HEIGHT * 2, Blocks.AIR.getDefaultState());
        return (x, y, z) -> {
            if (SharedConstants.DISABLE_FLUID_GENERATION)
            {
                return disabledFluidLevel;
            }
            // If y-coordinate is below the bottom lava fill (or if the sea level is lower than that, use that)
            // Also restrict the value to within the world's minimum height
            else if (y < Math.max(Math.min(configuredBottomLevelSetting, settings.seaLevel()), DimensionType.MIN_HEIGHT)) {
                return bottomFluidLevel;
            }
            else
            {
                return seaLevelFluid;
            }
        };
    }


    /**
     * @author mrburgerUS (@mrburger)
     *  Retrieve the correct y-level for the Aquifer of lava at the bottom of a world
     * @return an int for the y-level
     */
    @Unique
    private static int getConfiguredBottomLevelSetting(ChunkGeneratorSettings settings)
    {
        return switch (FixHardCodedLavaLevel.CONFIG.vertical_Reference_Type)
        {
	        case BELOW_SEA_LEVEL ->
			        settings.seaLevel() - FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABOVE_BOTTOM ->
                    settings.generationShapeConfig().minimumY() + FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABSOLUTE -> FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
        }; // set to the old default
    }
}