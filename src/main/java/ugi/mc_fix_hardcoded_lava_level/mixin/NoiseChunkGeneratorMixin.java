package ugi.mc_fix_hardcoded_lava_level.mixin;



import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.DimensionType;
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
        int seaLevelSetting = settings.seaLevel();
        int configuredBottomLevelSetting = getConfiguredBottomLevelSetting(seaLevelSetting);
        FixHardCodedLavaLevel.LOGGER.info("Dimension Min Height: {}", DimensionType.MIN_HEIGHT);

        // Fluid Sampler for the bottom Lava fill (the purpose of this mod)
        AquiferSampler.FluidLevel bottomFluidLevel = new AquiferSampler.FluidLevel(configuredBottomLevelSetting, Blocks.LAVA.getDefaultState());
        // Fluid Sampler for the Sea Level fill
        AquiferSampler.FluidLevel seaLevelFluid = new AquiferSampler.FluidLevel(seaLevelSetting, settings.defaultFluid());
        // Fluid Sampler used when sea level and aquifers are disabled, I guess?
        AquiferSampler.FluidLevel disabledFluidLevel = new AquiferSampler.FluidLevel(DimensionType.MIN_HEIGHT * 2, Blocks.AIR.getDefaultState());
        return (x, y, z) -> {
            if (SharedConstants.DISABLE_FLUID_GENERATION)
            {
                return disabledFluidLevel;
            }
            // If y-coordinate is below the bottom lava fill (or if the sea level is lower than that, use that)
            // Also restrict the value to within the world's minimum height
            else if (y < Math.max(Math.min(configuredBottomLevelSetting, seaLevelSetting), DimensionType.MIN_HEIGHT)) {
                return bottomFluidLevel;
            }
            else
            {
                return seaLevelFluid;
            }
        };
    }

    @Unique
    private static int getConfiguredBottomLevelSetting(int seaLevelSetting)
    {
        return switch (FixHardCodedLavaLevel.CONFIG.vertical_Reference_Type)
        {
	        case BELOW_SEA_LEVEL ->
			        seaLevelSetting - FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABOVE_BOTTOM ->
			        DimensionType.MIN_HEIGHT + FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
	        case ABSOLUTE -> FixHardCodedLavaLevel.CONFIG.vertical_Reference_To_Lava_Separation;
        }; // set to the old default
    }
}