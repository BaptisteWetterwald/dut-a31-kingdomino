package core;

import core.controller.ParametersController;
import core.model.Game;
import core.view.ParametersGUI;

public class App
{
    public static void main(String[] args)
    {
        Game game = new Game();
        ParametersController controller = new ParametersController(game);
        new ParametersGUI(game, controller);
    }
}