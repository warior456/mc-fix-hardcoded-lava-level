package ugi.mc_fix_hardcoded_lava_level.config;

public class ConfigHandler {
    public int config_Version = 2;
    // Default to 117, (+63 - -54, the vanilla distance)
    public int vertical_Reference_To_Lava_Separation = 117;
    public DistanceReferenceEnum vertical_Reference_Type = DistanceReferenceEnum.BELOW_SEA_LEVEL;
    // Output more detailed information
    public boolean verbose_Mode = false;
}