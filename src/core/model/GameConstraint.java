package core.model;

public interface GameConstraint
{
    void setNewScore(Player p);
    boolean respects(Tile[][] grid);
}
