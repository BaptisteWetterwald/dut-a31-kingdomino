package core.model;

public abstract class GameConstraint
{

    private final int bonus;

    public GameConstraint(int bonus)
    {
        this.bonus = bonus;
    }

    void setNewScore(Player p)
    {
        p.setScore(p.getScore() + this.bonus);
    }
    
    public abstract boolean respects(Tile[][] grid);
}
