package core_mvc.model;

import java.awt.*;

import static java.awt.Color.*;

public enum Biome
{
    CASTLE(PINK), WHEAT(YELLOW), FOREST(new Color(0, 100, 0)), WATER(CYAN), GRASS(GREEN), SWAMP(new Color(205, 133, 63)), MINE(GRAY);

    private final Color color;

    Biome(Color color)
    {
        this.color = color;
    }

    public static Biome parse(String s)
    {
        return Biome.valueOf(s.toUpperCase());
    }

    public Color getColor()
    {
        return this.color;
    }

}
