package core_mvc.model;

public class Harmony implements GameConstraint
{
    @Override
    public void setNewScore(Player p)
    {
        if (respects(p.getKingdom().getGrid()))
            p.setScore(p.getScore() + 5);
    }

    @Override
    public boolean respects(Tile[][] grid)
    {
        boolean foundEmpty = false;
        for (int i = 0; i < grid.length && !foundEmpty; i++)
        {
            for (int j = 0; j < grid[0].length && !foundEmpty; j++)
            {
                if (grid[i][j] == null)
                    foundEmpty = true;
            }
        }
        return !foundEmpty;
    }
}
