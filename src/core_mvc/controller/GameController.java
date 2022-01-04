package core_mvc.controller;

import core_mvc.model.Domino;
import core_mvc.model.Game;
import core_mvc.model.Kingdom;
import core_mvc.model.Player;
import core_mvc.view.WalletObserver;

public class GameController
{

    private final Game game;
    private WalletObserver walletObserver;
    private Domino selectedDomino;
    private int clickedTileIndex;

    public GameController(Game game)
    {
        this.game = game;
        this.createWalletObserver();
        this.addWalletObserver();
    }

    private void createWalletObserver()
    {
        walletObserver = new WalletObserver(game.getWallet(), this);
    }

    private void addWalletObserver()
    {
        game.getWallet().addObserver(walletObserver);
    }

    public void flipLeft(Domino selectedDomino)
    {
        selectedDomino.flipLeft();
    }

    public void flipRight(Domino selectedDomino)
    {
        selectedDomino.flipRight();
    }

    public void flip180(Domino selectedDomino)
    {
        selectedDomino.flip180();
    }

    public void setSelectedDomino(Domino domino)
    {
        this.selectedDomino = domino;
    }

    public Domino getSelectedDomino()
    {
        return this.selectedDomino;
    }

    public void tryDominoPlacement(Kingdom kingdom, int finalI, int finalJ, Player player) 
    {
        kingdom.tryDominoPlacement(finalI, finalJ, this.selectedDomino, this.clickedTileIndex, player);
    }
}
