package core_mvc;

import core_mvc.controller.ParametersController;
import core_mvc.model.Game;
import core_mvc.view.ParametersGUI;

public class App
{
    public static void main(String[] args)
    {
        Game game = new Game();
        ParametersController controller = new ParametersController(game);
        new ParametersGUI(game, controller);
    }
}