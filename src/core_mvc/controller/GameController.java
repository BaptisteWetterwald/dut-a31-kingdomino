package core_mvc.controller;

import core_mvc.model.Domino;
import core_mvc.model.Game;
import core_mvc.model.Kingdom;
import core_mvc.model.Player;
import core_mvc.view.GameView;
import core_mvc.view.IObserver;
import core_mvc.view.Observable;

import java.util.Comparator;

public class GameController
{

    private final Game game;

    public GameController(Game game)
    {
        this.game = game;
    }

    public void flipLeft()
    {
        game.getSelectedDomino().flipLeft();
    }

    public void flipRight()
    {
        game.getSelectedDomino().flipRight();
    }

    public void flip180()
    {
        game.getSelectedDomino().flip180();
    }

    public boolean tryDominoPlacement(int finalI, int finalJ)
    {
        boolean done = game.getCurrentPlayer().getKingdom().tryDominoPlacement(finalI, finalJ, game.getSelectedDomino(), game.getClickedTileIndex(), game.getCurrentPlayer());
        if (done)
            game.playedTurn();
        return done;
    }

    public void makeLeaderboard()
    {
        game.getPlayers().sort(Comparator.comparingInt(Player::getScore).reversed());
    }

    public void slideKingdom(Kingdom kingdom, int finalI)
    {
        kingdom.slideGridElements(finalI);
    }

    public void setSelectedDomino(Domino domino)
    {
        game.setSelectedDomino(domino);
    }

    public void setClickedTileIndex(int clickedTileIndex)
    {
        game.setClickedTileIndex(clickedTileIndex);
    }

    public void skipTurn()
    {
        game.playedTurn();
    }

    public void addObserver(Observable observable, IObserver observer)
    {
        observable.addObserver(observer);
    }
}
