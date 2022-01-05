package core_mvc.controller;

import core_mvc.model.Domino;
import core_mvc.model.Game;
import core_mvc.model.Kingdom;
import core_mvc.model.Player;

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

    public void tryDominoPlacement(int finalI, int finalJ)
    {
        game.getCurrentPlayer().getKingdom().tryDominoPlacement(finalI, finalJ, game.getSelectedDomino(), game.getClickedTileIndex(), game.getCurrentPlayer());
        if (game.getSelectedDomino() == null)
            game.playedTurn();
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
}
