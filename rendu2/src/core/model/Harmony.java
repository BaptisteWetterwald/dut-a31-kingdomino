package core.model;

public class Harmony extends GameConstraint
{

    public Harmony()
    {
        super(5);
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
