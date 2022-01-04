package core_mvc.model;

import core_mvc.model.*;
import core_mvc.utilities.CSVReader;
import core_mvc.view.GameView;
import core_mvc.view.KingdomObserver;
import core_mvc.view.ParametersGUI;
import core_mvc.view.WalletObserver;

import javax.swing.*;
import java.util.*;

public class Game
{
    Random random = new Random();

    private final List<Domino> deck = new ArrayList<>();
    private final List<Player> players;
    private Player[] newOrder;
    private final Wallet wallet;
    private List<GameConstraint> gameConstraints;

    public Game(List<Player> players, List<GameConstraint> gameConstraints)
    {
        this.players = players;
        this.gameConstraints = gameConstraints;
        //quickSetup();

        int nbPlayers = this.players.size();
        wallet = new Wallet(nbPlayers%2 == 0 ? 4 : 3);
        CSVReader reader = CSVReader.getInstance();
        List<Domino> allDominos = reader.generateDominos();
        while (deck.size() < 12 * nbPlayers)
        {
            int r = random.nextInt(allDominos.size());
            deck.add(allDominos.get(r));
            allDominos.remove(r);
        }

        for (Player p : this.players)
        {
            p.setKingdom(new Kingdom(5, 5));
        }

       /* WalletObserver walletObserver = new WalletObserver(wallet, gameView);
        wallet.addObserver(walletObserver);
        gameView.setVisible(true);
        gameView.setExtendedState(JFrame.MAXIMIZED_BOTH);*/
        gameProgress();
    }

    private void quickSetup()
    {
        players.add(new Player("Baptou"));
        players.add(new Player("Hamzouz"));
        players.add(new Player("JuL"));
        this.gameConstraints = new ArrayList<>();
        this.gameConstraints.add(new Harmony());
        this.gameConstraints.add(new MiddleKingdom());
    }

    private void gameProgress()
    {
        /*
        for (Player p : players)
        {
            KingdomObserver window = new KingdomObserver(gameView, p);
            p.getKingdom().addObserver(window);
        }*/

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
            p.setScore(p.getKingdom().calculateScore());
            for (GameConstraint gc : gameConstraints)
                gc.setNewScore(p);
        }

        //gameView.displayEndScreen(players, gameConstraints);
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
        //char lastLetter = p.getName().toLowerCase().charAt(p.getName().length()-1);
        //gameView.writeInstructionTitle(p.getName() + (lastLetter != 's' && lastLetter != 'z' ? "'s" : "'") + " turn");

        /*p.setPlayed(false);
        p.getKingdom().notifyObservers();

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
        }*/

        p.setScore(p.getKingdom().calculateScore());
        p.getKingdom().notifyObservers();

        int index = wallet.getDominos().indexOf(p.getLastPlayedDomino());
        newOrder[index] = p;

        /*gameView.setSelectedDomino(null);
        gameView.setClickedTileIndex(-1);
        gameView.getSkipTurnButton().setVisible(false);*/

        wallet.declareAsUsed(p.getLastPlayedDomino());
        wallet.notifyObservers();
    }

    public int getNbPlayers()
    {
        return this.players.size();
    }

    public Wallet getWallet()
    {
        return wallet;
    }
}
