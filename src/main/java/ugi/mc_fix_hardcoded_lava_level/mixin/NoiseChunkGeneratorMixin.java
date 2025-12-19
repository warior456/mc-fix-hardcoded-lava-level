package ugi.mc_fix_hardcoded_lava_level.mixin;



import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
        int configuredBottomLevelSetting = seaLevelSetting - FixHardCodedLavaLevel.CONFIG.vertical_Sea_To_Lava_Separation;

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
            else if (y < Math.min(configuredBottomLevelSetting, seaLevelSetting)) {
                return bottomFluidLevel;
            }
            else
            {
                return seaLevelFluid;
            }
        };
    }
}