package core_mvc.model;

import java.util.ArrayList;
import java.util.List;

public class Wallet extends Observable
{
    private final List<Domino> dominos;
    private final List<Domino> used;
    private final int size;

    public Wallet(int size)
    {
        this.dominos = new ArrayList<>();
        this.used = new ArrayList<>();
        this.size = size;
    }

    public int getSize()
    {
        return this.size;
    }

    public List<Domino> getDominos()
    {
        return this.dominos;
    }

    public void declareAsUsed(Domino domino)
    {
        this.used.add(domino);
    }

    public boolean hasBeenUsedAlready(Domino domino)
    {
        return this.used.contains(domino);
    }

    public void clearUsedDominos()
    {
        this.used.clear();
    }

    public String getUsed()
    {
        return this.used.toString();
    }
}
