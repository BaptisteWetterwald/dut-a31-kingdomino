package core_mvc.view;

import core_mvc.model.Game;
import core_mvc.model.Kingdom;
import core_mvc.model.Wallet;

public interface IObserver
{
    void update(Observable o);
}