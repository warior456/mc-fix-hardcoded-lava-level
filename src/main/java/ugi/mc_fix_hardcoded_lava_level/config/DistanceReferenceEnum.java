package ugi.mc_fix_hardcoded_lava_level.config;

public enum DistanceReferenceEnum
{
	BELOW_SEA_LEVEL("Below Sea Level"),
	ABOVE_BOTTOM("Above World Bottom"),
	ABSOLUTE("Y-Coordinate");

	private final String humanName;

	DistanceReferenceEnum(String name)
	{
		this.humanName = name;
	}

	@Override
	public String toString()
	{
		return this.humanName;
	}
}
