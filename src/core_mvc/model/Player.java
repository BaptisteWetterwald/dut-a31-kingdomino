package core_mvc.model;

public class Player
{
    private Kingdom kingdom;
    private final String name;
    private int score;

    public Player(String name)
    {
        this.name = name;
        this.score = 0;
    }

    public String getName()
    {
        return this.name;
    }

    public void setKingdom(Kingdom kingdom)
    {
        this.kingdom = kingdom;
    }

    public Kingdom getKingdom()
    {
        return this.kingdom;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public int getScore()
    {
        return this.score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
