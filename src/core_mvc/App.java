package core_mvc;

import core_mvc.controller.ParametersController;
import core_mvc.view.ParametersGUI;

public class App
{
    public static void main(String[] args)
    {
        ParametersController controller = new ParametersController();
        new ParametersGUI(controller);
    }
}