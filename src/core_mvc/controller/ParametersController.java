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

    public void addMiddleKingdomConstraint()
    {
        game.addMiddleKingdomConstraint();
    }

    public void removeMiddleKingdomConstraint()
    {
        game.removeMiddleKingdomConstraint();
    }

    public void addHarmonyConstraint()
    {
        game.addHarmonyConstraint();
    }

    public void removeHarmonyConstraint()
    {
        game.removeHarmonyConstraint();
    }

    public void clearPlayers()
    {
        game.clearPlayers();
    }
}
