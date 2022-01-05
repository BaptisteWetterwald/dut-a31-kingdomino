package core_mvc.view;

import core_mvc.model.Kingdom;
import core_mvc.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable
{
    private final List<IObserver> observers;

    public Observable()
    {
        observers = new ArrayList<>();
    }

    public void notifyObservers() {
        for (IObserver o : observers)
            if (this instanceof Kingdom)
                o.update((Kingdom) this);
            else if (this instanceof Wallet)
                o.update((Wallet) this);
    }

    public void addObserver(IObserver observer) {
        this.observers.add(observer);
    }
}
