package core_mvc.controller;

import core_mvc.model.Game;
import core_mvc.model.GameConstraint;
import core_mvc.model.Player;
import core_mvc.view.GameView;

import java.util.List;

public class ParametersController
{

    public void startGame(List<Player> players, List<GameConstraint> gameConstraints)
    {
        System.out.println("SHOULD ARRIVE HERE");
        Game game = new Game(players, gameConstraints);
        GameController controller = new GameController(game);
        new GameView(game, controller);
    }
}
