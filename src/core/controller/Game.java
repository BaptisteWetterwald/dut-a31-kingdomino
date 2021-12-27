package core.controller;

import core.model.*;
import core.view.Board;
import core.view.KingdomObserver;
import core.view.WalletObserver;

import javax.swing.*;
import java.util.*;

public class Game
{
    Random random = new Random();

    private final List<Domino> deck = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private Player[] newOrder;
    private final Board board;
    private final Wallet wallet;
    private boolean[] gameConstraints;

    public Game()
    {
        //quickSetup();
        slowSetup();

        int nbPlayers = this.players.size();
        wallet = new Wallet(nbPlayers%2 == 0 ? 4 : 3);
        CSVReader reader = new CSVReader();
        List<Domino> allDominos = reader.generateDominos();
        while (deck.size() < nbPlayers * 12)
        {
            int r = random.nextInt(allDominos.size());
            deck.add(allDominos.get(r));
            allDominos.remove(r);
        }

        for (Player p : this.players)
        {
            p.setKingdom(new Kingdom(5, 5));
        }
        board = new Board(players.size());
        WalletObserver walletObserver = new WalletObserver(wallet, board);
        wallet.addObserver(walletObserver);
        board.setVisible(true);
        board.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameProgress();
    }

    private void quickSetup()
    {
        players.add(new Player("Baptou"));
        players.add(new Player("Hamzouz"));
        players.add(new Player("JuL"));
        this.gameConstraints = new boolean[]{true, true};
    }

    private void slowSetup()
    {
        ParametersGUI parameters = new ParametersGUI();
        while (parameters.getPlayers().size() == 0)
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        this.players.addAll(parameters.getPlayers());
        this.gameConstraints = parameters.getGameConstraints();

        parameters.dispose();
    }

    private void gameProgress()
    {

        for (Player p : players)
        {
            KingdomObserver window = new KingdomObserver(board, p);
            p.getKingdom().addObserver(window);
        }

        //First round
        Player[] oldOrder = new Player[wallet.getSize()];
        for (int i=0; i<players.size(); i++)
        {
            oldOrder[i] = players.get(i);
            if (players.size() == 2)
                oldOrder[i+2] = players.get(i);
        }
        newOrder = new Player[wallet.getSize()];
        List<Player> temp = Arrays.asList(oldOrder);
        Collections.shuffle(temp);
        temp.toArray(oldOrder);

        while (deck.size() > 0)
        {
            nextRound();

            for (Player p : oldOrder)
            {
                this.turnOf(p);
            }

            for (int i=0; i<newOrder.length; i++)
            {
                oldOrder[i] = newOrder[i];
                newOrder[i] = null;
            }
        }

        for (Player p : players)
        {
            if (gameConstraints[0] && p.getKingdom().respectsMiddleKingdom())
            {
                p.setScore(p.getKingdom().getScore() + 10);
                p.getKingdom().notifyObservers();
            }
            if (gameConstraints[1] && p.getKingdom().respectsHarmony())
            {
                p.setScore(p.getKingdom().getScore() + 5);
                p.getKingdom().notifyObservers();
            }
        }

        board.displayEndScreen(players, gameConstraints);
        System.out.println("Adèle said \"This is the end... of the game!\"");
    }

    private void nextRound()
    {
        wallet.clearUsedDominos();
        wallet.getDominos().clear();
        while (wallet.getDominos().size() < wallet.getSize())
        {
            int r = random.nextInt(deck.size());
            wallet.getDominos().add(deck.get(r));
            deck.remove(r);
        }
        wallet.getDominos().sort(Comparator.comparingInt(Domino::getId));
        wallet.notifyObservers();
    }

    private void turnOf(Player p)
    {
        char lastLetter = p.getName().toLowerCase().charAt(p.getName().length()-1);
        board.writeInstructionTitle(p.getName() + (lastLetter != 's' && lastLetter != 'z' ? "'s" : "'") + " turn");
        p.getKingdom().setModifiable(true);
        p.setPlayed(false);

        while(!p.hasPlayed())
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        p.setScore(p.getKingdom().getScore());
        p.getKingdom().notifyObservers();

        int index = wallet.getDominos().indexOf(p.getLastPlayedDomino());
        newOrder[index] = p;

        p.getKingdom().setModifiable(false);
        board.setSelectedDomino(null);
        board.setClickedTileIndex(-1);
        board.getSkipTurnButton().setVisible(false);

        wallet.declareAsUsed(p.getLastPlayedDomino());
        wallet.notifyObservers();
    }

}
