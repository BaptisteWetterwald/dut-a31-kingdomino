package core_mvc.model;

public class Tile
{
    private final int crowns;
    private final Biome biome;

    public Tile(Biome biome, int crowns)
    {
        this.biome = biome;
        this.crowns = crowns;
    }

    @Override
    public String toString()
    {
        return this.biome.toString() + " (" + this.crowns + ")";
    }

    public Biome getBiome()
    {
        return this.biome;
    }

    public int getCrowns()
    {
        return this.crowns;
    }

    public String getCrownsAsString()
    {
        String s = "";
        for (int i=0; i<this.crowns; i++)
        {
            s += "\uD83D\uDC51";
            if (i < this.crowns-1)
                s += " ";
        }
        return s;
    }
}
