package core.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable
{
    private final List<IObserver> observers;

    public Observable()
    {
        observers = new ArrayList<>();
    }

    public void notifyObservers()
    {
        for (IObserver o : observers)
            o.update(this);
    }

    public void addObserver(IObserver observer)
    {
        this.observers.add(observer);
    }
}
