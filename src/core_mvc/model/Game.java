package core_mvc.model;

import core_mvc.utilities.CSVReader;

import java.util.*;

public class Game extends Observable
{
    Random random = new Random();

    private final List<Domino> deck = new ArrayList<>();
    private final List<Player> players;
    private final Player[] newOrder;
    private final Wallet wallet;
    private final List<GameConstraint> gameConstraints;
    private final Player[] oldOrder;
    private int clickedTileIndex;
    private Domino selectedDomino;
    private Player currentPlayer;
    private int currentRound;
    private final int numberOfRounds;
    private int playedTurnsInRound;
    private boolean finished = false;

    public Game(List<Player> players, List<GameConstraint> gameConstraints)
    {
        this.players = players;
        this.gameConstraints = gameConstraints;
        this.currentRound = 0;
        //quickSetup();

        this.clickedTileIndex = -1;
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

        this.numberOfRounds = deck.size()/wallet.getSize();

        for (Player p : this.players)
        {
            p.setKingdom(new Kingdom(5, 5));
        }

        //First round
        oldOrder = new Player[wallet.getSize()];
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

        nextRound();
        this.currentPlayer = oldOrder[0];
    }

    private void nextRound()
    {
        this.currentRound++;
        this.playedTurnsInRound = 0;
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

    public int getCurrentRound()
    {
        return this.currentRound;
    }

    public int getNumberOfRounds()
    {
        return this.numberOfRounds;
    }

    public Player getCurrentPlayer()
    {
        return this.currentPlayer;
    }

    public void playedTurn()
    {
        this.clickedTileIndex = -1;

        int indexCurrentPlayer = playedTurnsInRound;
        Player p = oldOrder[indexCurrentPlayer];

        if (indexCurrentPlayer < oldOrder.length-1)
            this.currentPlayer = oldOrder[indexCurrentPlayer + 1];

        p.getKingdom().placedDomino(this.getSelectedDomino(), p);
        p.setScore(p.getKingdom().calculateScore());
        p.getKingdom().notifyObservers();
        p.setLastPlayedDomino(this.selectedDomino);
        int index = wallet.getDominos().indexOf(this.selectedDomino);
        newOrder[index] = p;

        wallet.declareAsUsed(this.selectedDomino);
        this.selectedDomino = null;
        this.playedTurnsInRound++;

        if (playedTurnsInRound == oldOrder.length) //Everyone has played
        {
            if (currentRound < numberOfRounds)
            {
                nextRound();
                for (int i=0; i<newOrder.length; i++)
                {
                    oldOrder[i] = newOrder[i];
                    newOrder[i] = null;
                }
                this.currentPlayer = oldOrder[0];
            }
            else
            {
                //System.out.println("Adèle said \"This is the end... of the game!\"");
                this.finished = true;
                this.calculateFinalScores();
                for (Player p2 : this.players)
                    p2.getKingdom().notifyObservers();
                this.notifyObservers();
            }
        }
    }

    private void calculateFinalScores()
    {
        for (Player p : this.players)
            for (GameConstraint gc : gameConstraints)
                gc.setNewScore(p);
    }

    public List<GameConstraint> getGameConstraints()
    {
        return this.gameConstraints;
    }

    public List<Player> getPlayers()
    {
        return this.players;
    }

    public Wallet getWallet()
    {
        return this.wallet;
    }

    public void setClickedTileIndex(int clickedTileIndex) 
    {
        this.clickedTileIndex = clickedTileIndex;
    }

    public int getClickedTileIndex()
    {
        return this.clickedTileIndex;
    }

    public Domino getSelectedDomino()
    {
        return this.selectedDomino;
    }

    public void setSelectedDomino(Domino selectedDomino)
    {
        this.selectedDomino = selectedDomino;
    }

    public boolean isFinished()
    {
        return this.finished;
    }
}
