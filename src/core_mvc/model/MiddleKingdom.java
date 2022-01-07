package core_mvc.model;

public class MiddleKingdom implements GameConstraint
{

    @Override
    public void setNewScore(Player p)
    {
        p.setScore(p.getScore() + 10);
    }

    @Override
    public boolean respects(Tile[][] grid)
    {
        int line = grid.length/2;
        int column = grid[0].length/2;
        return grid[line][column] != null && grid[line][column].getBiome() == Biome.CASTLE;
    }
}
