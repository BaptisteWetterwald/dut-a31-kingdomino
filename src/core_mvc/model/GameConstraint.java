package core_mvc.model;

public interface GameConstraint
{
    void setNewScore(Player p);
    boolean respects(Tile[][] grid);
}
