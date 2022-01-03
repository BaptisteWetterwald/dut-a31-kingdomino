package core_mvc.model;

import core_mvc.view.Observable;

import java.util.ArrayList;
import java.util.List;

public class Kingdom extends Observable
{
    private final Tile[][] grid;

    public Kingdom(int height, int width)
    {
        this.grid = new Tile[height][width];
        grid[height/2][width/2] = new Castle();
        /*
             0 1 2 3 4
           0 x x x x x
           1 x x x x x
           2 x x C x x
           3 x x x x x
           4 x x x x x
       */
    }

    public Tile[][] getGrid()
    {
        return this.grid;
    }

    public void tryDominoPlacement(int lineIndex, int columnIndex, Domino domino, int clickedTileIndex, Player p)
    {
        boolean res = false;
        if (domino != null)
        {
            if (grid[lineIndex][columnIndex] == null && clickedTileIndex != -1)
            {
                boolean leftOrTopTileClicked = (clickedTileIndex == 0);
                boolean horizontal = domino.isHorizontal();
                if (horizontal)
                {
                    if (columnIndex + 1 <= grid[0].length - 1)
                        if (leftOrTopTileClicked && grid[lineIndex][columnIndex + 1] == null)
                            if (hasValidNeighbor(domino.getTiles()[0], lineIndex, columnIndex)
                                || hasValidNeighbor(domino.getTiles()[1], lineIndex, columnIndex+1))
                            {
                                res = true;
                                grid[lineIndex][columnIndex] = domino.getTiles()[0];
                                grid[lineIndex][columnIndex+1] = domino.getTiles()[1];
                            }
                    if (columnIndex - 1 >= 0)
                        if (!leftOrTopTileClicked && grid[lineIndex][columnIndex - 1] == null)
                            if (hasValidNeighbor(domino.getTiles()[0], lineIndex, columnIndex-1)
                                || hasValidNeighbor(domino.getTiles()[1], lineIndex, columnIndex))
                            {
                                res = true;
                                grid[lineIndex][columnIndex-1] = domino.getTiles()[0];
                                grid[lineIndex][columnIndex] = domino.getTiles()[1];
                            }

                }
                else
                {
                    if (lineIndex + 1 <= grid.length - 1)
                        if (leftOrTopTileClicked && grid[lineIndex + 1][columnIndex] == null)
                            if (hasValidNeighbor(domino.getTiles()[0], lineIndex, columnIndex)
                                || hasValidNeighbor(domino.getTiles()[1], lineIndex+1, columnIndex))
                            {
                                res = true;
                                grid[lineIndex][columnIndex] = domino.getTiles()[0];
                                grid[lineIndex+1][columnIndex] = domino.getTiles()[1];
                            }
                    if (lineIndex - 1 >= 0)
                        if (!leftOrTopTileClicked && grid[lineIndex - 1][columnIndex] == null)
                            if (hasValidNeighbor(domino.getTiles()[0], lineIndex-1, columnIndex)
                                || hasValidNeighbor(domino.getTiles()[1], lineIndex, columnIndex))
                            {
                                res = true;
                                grid[lineIndex-1][columnIndex] = domino.getTiles()[0];
                                grid[lineIndex][columnIndex] = domino.getTiles()[1];
                            }
                }
            }
        }

        if (res)
            placeDomino(domino, p);
    }

    public void placeDomino(Domino domino, Player p)
    {
        p.setPlayed(true);
        p.setLastPlayedDomino(domino);
        this.notifyObservers();
    }

    public boolean hasValidNeighbor(Tile t1, int l, int c)
    {
        List<Tile> neighbors = new ArrayList<>();
        if (l > 0)
            neighbors.add(grid[l-1][c]);
        if (l < grid.length - 1)
            neighbors.add(grid[l+1][c]);
        if (c > 0)
            neighbors.add(grid[l][c-1]);
        if (c < grid[0].length - 1)
            neighbors.add(grid[l][c+1]);

        boolean res = false;
        for (int i = 0; i < neighbors.size() && !res; i++)
            if (neighbors.get(i) != null)
                if (neighbors.get(i).getBiome() == t1.getBiome() || neighbors.get(i).getBiome() == Biome.CASTLE)
                    res = true;

        return res;
    }

    public int calculateScore()
    {
        List<List<Tile>> terrains = new ArrayList<>();

        int[] untreated = getFirstUntreatedTile(terrains);
        while (untreated[0] != -1)
        {
            List<Tile> terrain = new ArrayList<>();
            terrains.add(terrain);
            fillTerrainOfTile(terrain, untreated);
            untreated = getFirstUntreatedTile(terrains);
        }

        int score = 0;
        for (List<Tile> terrain : terrains)
        {
            int crowns = 0;
            for (Tile tile : terrain)
                crowns += tile.getCrowns();
            score += crowns * terrain.size();
        }
        return score;
    }

    private void fillTerrainOfTile(List<Tile> terrain, int[] untreated)
    {
        int l = untreated[0];
        int c = untreated[1];
        if (!terrain.contains(grid[l][c]))
            terrain.add(grid[l][c]);

        List<int[]> neighbors = new ArrayList<>();

        if (l > 0)
            addNeighbor(neighbors, terrain, l, c, l-1, c); //up
        if (l < grid.length - 1)
            addNeighbor(neighbors, terrain, l, c, l+1, c); //down
        if (c > 0)
            addNeighbor(neighbors, terrain, l, c, l, c-1); //left
        if (c < grid[0].length - 1)
            addNeighbor(neighbors, terrain, l, c, l, c+1); //right

        for (int[] neighbor : neighbors)
        {
            fillTerrainOfTile(terrain, neighbor);
        }
    }

    private void addNeighbor(List<int[]> neighbors, List<Tile> terrain, int l, int c, int l2, int c2)
    {
        boolean res = true;
        if (grid[l2][c2] == null)
            res = false;
        else if (grid[l2][c2].getBiome() != grid[l][c].getBiome())
            res = false;
        else if (terrain.contains(grid[l2][c2]))
            res = false;
        if (res)
            neighbors.add(new int[]{l2, c2});
    }

    private int[] getFirstUntreatedTile(List<List<Tile>> terrains)
    {
        int[] coords = new int[]{-1, -1};
        boolean isFound = false;

        for (int i=0; i<grid.length && !isFound; i++)
        {
            for (int j=0; j<grid[0].length && !isFound; j++)
            {
                if (grid[i][j] != null)
                {
                    boolean isTreated = false;
                    for (int k = 0; k<terrains.size(); k++)
                    {
                        List<Tile> terrain = terrains.get(k);
                        for (int l = 0; l<terrain.size(); l++)
                        {
                            if (terrain.contains(grid[i][j]))
                            {
                                isTreated = true;
                            }
                        }
                    }
                    if (!isTreated)
                    {
                        isFound = true;
                        coords[0] = i;
                        coords[1] = j;
                    }
                }
            }
        }
        return coords;
    }

    public void slideGridElements(final int direction, final boolean hasPlayed)
    {
        if (!hasPlayed)
        {
            boolean empty = true;

            switch(direction)
            {
                case 0: //Left

                    for (int i=0; i<grid.length; i++)
                    {
                        if (grid[i][0] != null)
                        {
                            empty = false;
                            break;
                        }
                    }

                    if (empty)
                    {

                        for (int i=0; i<grid.length; i++)
                        {
                            for (int j=0; j<grid[0].length-1; j++)
                            {
                                grid[i][j] = grid[i][j+1];
                            }
                            grid[i][grid[0].length-1] = null;
                        }
                        this.notifyObservers();
                    }
                    break;

                case 1: //Up

                    for (int i=0; i<grid[0].length; i++)
                    {
                        if (grid[0][i] != null)
                        {
                            empty = false;
                            break;
                        }
                    }

                    if (empty)
                    {
                        for (int j=0; j<grid[0].length; j++)
                        {
                            for (int i=0; i<grid.length-1; i++)
                            {
                                grid[i][j] = grid[i + 1][j];
                            }
                            grid[grid.length-1][j] = null;
                        }
                        this.notifyObservers();
                    }
                    break;

                case 2: //Down

                    for (int i=0; i<grid[0].length; i++)
                    {
                        if (grid[grid.length-1][i] != null)
                        {
                            empty = false;
                            break;
                        }
                    }

                    if (empty)
                    {
                        for (int j=0; j<grid[0].length; j++)
                        {
                            for (int i=grid.length-1; i>0; i--)
                            {
                                grid[i][j] = grid[i-1][j];
                            }
                            grid[0][j] = null;
                        }
                        this.notifyObservers();
                    }
                    break;

                case 3: //Right

                    for (int i=0; i<grid.length; i++)
                    {
                        if (grid[i][grid[0].length-1] != null)
                        {
                            empty = false;
                            break;
                        }
                    }

                    if (empty)
                    {
                        for (int i=0; i<grid.length; i++)
                        {
                            for (int j=grid[0].length-1; j>0; j--)
                            {
                                grid[i][j] = grid[i][j-1];
                            }
                            grid[i][0] = null;
                        }
                        this.notifyObservers();
                    }
                    break;
            }
        }

    }
}