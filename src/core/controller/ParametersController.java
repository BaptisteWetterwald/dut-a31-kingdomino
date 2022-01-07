package core.controller;

import core.model.Game;
import core.view.GameView;

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

    public void setGameConstraints(boolean harmony, boolean middleKingdom)
    {
        game.setConstraints(harmony, middleKingdom);
    }

    public void clearPlayers()
    {
        game.clearPlayers();
    }
}
