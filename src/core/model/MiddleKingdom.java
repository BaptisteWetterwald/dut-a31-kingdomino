package core.model;

public class MiddleKingdom extends GameConstraint
{
    public MiddleKingdom()
    {
        super(10);
    }

    @Override
    public boolean respects(Tile[][] grid)
    {
        int line = grid.length/2;
        int column = grid[0].length/2;
        return grid[line][column] != null && grid[line][column].getBiome() == Biome.CASTLE;
    }
}
