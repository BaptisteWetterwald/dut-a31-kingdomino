package core.model;

public class Domino
{
    private final int id;
    private boolean horizontal;
    /*
        1 à l'indice 0 et 3 à l'indice 1 :
        [1|3]
        ou
        1
        -
        3

        Inverse (3 à l'indice 0 et 1 à l'indice 1):
        [3|1]
        ou
        3
        -
        1

     */
    private final Tile[] tiles = new Tile[2];

    public Domino(int id, Tile t1, Tile t2)
    {
        this.id = id;
        this.tiles[0] = t1;
        this.tiles[1] = t2;
        this.horizontal = true;
    }

    public int getId()
    {
        return this.id;
    }

    public Tile[] getTiles()
    {
        return this.tiles;
    }

    @Override
    public String toString()
    {
        return this.getId() + " | " + this.tiles[0].toString() + " | " + this.tiles[1].toString();
    }

    public void flip180()
    {
        switchTiles();
    }

    public void flipRight()
    {
        if (!this.horizontal)
            switchTiles();
        this.horizontal = !this.horizontal;
    }

    public void flipLeft()
    {
        if (this.horizontal)
            switchTiles();
        this.horizontal = !this.horizontal;
    }

    private void switchTiles()
    {
        Tile temp = this.tiles[0];
        this.tiles[0] = this.tiles[1];
        this.tiles[1] = temp;
    }

    public boolean isHorizontal()
    {
        return this.horizontal;
    }

}
