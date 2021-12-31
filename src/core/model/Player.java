package core.model;

public class Player
{
    private Kingdom kingdom;
    private final String name;
    private boolean played;
    private Domino lastPlayedDomino;
    private int score;

    public Player(String name)
    {
        this.name = name;
        this.score = 0;
        this.played = true;
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

    public boolean hasPlayed() {
        return played;
    }

    public void setPlayed(boolean hasPlayed) {
        this.played = hasPlayed;
    }
    
    public Domino getLastPlayedDomino()
    {
        return this.lastPlayedDomino;
    }

    public void setLastPlayedDomino(Domino domino)
    {
        this.lastPlayedDomino = domino;
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
