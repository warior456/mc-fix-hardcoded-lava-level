package ugi.mc_fix_hardcoded_lava_level.mixin;



import net.minecraft.SharedConstants;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import ugi.mc_fix_hardcoded_lava_level.FixHardCodedLavaLevel;

@Mixin({NoiseBasedChunkGenerator.class})
public abstract class NoiseChunkGeneratorMixin {

    /**
     * @author Matteo_fey (@warior456)
     * @author mrburgerUS (@mrburger)
     * @reason Replace -54 Lava level with configuration
     */
    @Overwrite
    private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings settings) {
        int configuredBottomLevelSetting = getConfiguredBottomLevelSetting(settings);

        // Fluid Sampler for the bottom Lava fill (the purpose of this mod)
        Aquifer.FluidStatus bottomFluidLevel = new Aquifer.FluidStatus(configuredBottomLevelSetting, Blocks.LAVA.defaultBlockState());
        // Fluid Sampler for the Sea Level fill
        Aquifer.FluidStatus seaLevelFluid = new Aquifer.FluidStatus(settings.seaLevel(), settings.defaultFluid());
        // Fluid Sampler used when sea level and aquifers are disabled, I guess?
        Aquifer.FluidStatus disabledFluidLevel = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
        return (x, y, z) -> {
            if (SharedConstants.DEBUG_DISABLE_FLUID_GENERATION)
            {
                return disabledFluidLevel;
            }
            // If y-coordinate is below the bottom lava fill (or if the sea level is lower than that, use that)
            // Also restrict the value to within the world's minimum height
            else if (y < Math.max(Math.min(configuredBottomLevelSetting, settings.seaLevel()), DimensionType.MIN_Y)) {
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
    private static int getConfiguredBottomLevelSetting(NoiseGeneratorSettings settings)
    {
        return switch (FixHardCodedLavaLevel.CONFIG.vertical_Reference_Type)
        {
	        case BELOW_SEA_LEVEL ->
			        settings.seaLevel() - FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABOVE_BOTTOM ->
                    settings.noiseSettings().minY() + FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABSOLUTE -> FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
        }; // set to the old default
    }
}