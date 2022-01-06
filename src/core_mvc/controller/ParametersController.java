package core_mvc.controller;

import core_mvc.model.Game;
import core_mvc.view.GameView;

public class ParametersController
{
    private final Game game;

    public ParametersController(Game game)
    {
        this.game = game;
    }

    public void startGame()
    {
        new GameView(game, new GameController(game));
        game.start();
    }

    public void addPlayer(String s)
    {
        game.createPlayer(s);
    }

    public void addGameConstraint(Class c)
    {
        game.addGameConstraint(c);
    }

    public void removeGameConstraint(Class c)
    {
        game.removeGameConstraint(c);
    }

    public void clearPlayers()
    {
        game.clearPlayers();
    }
}
