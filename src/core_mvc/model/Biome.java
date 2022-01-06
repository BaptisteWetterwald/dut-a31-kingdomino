package core_mvc.model;

public enum Biome
{
    CASTLE, WHEAT, FOREST, WATER, GRASS, SWAMP, MINE;

    public static Biome parse(String s)
    {
        return Biome.valueOf(s.toUpperCase());
    }
}
