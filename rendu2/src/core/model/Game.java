package core.model;

import core.utilities.CSVReader;

import java.util.*;

public class Game extends Observable
{
    private final Random random = new Random();

    private final List<Domino> deck = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final boolean[] gameConstraints = new boolean[2];
    private final List<GameConstraint> constraints = new ArrayList<>();
    private Player[] oldOrder;
    private Player[] newOrder;
    private Domino selectedDomino;
    private Player currentPlayer;
    private Wallet wallet;
    private int clickedTileIndex;
    private int currentRound;
    private int numberOfRounds;
    private int playedTurnsInRound;

    public void start()
    {
        this.currentRound = 0;
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

        this.notifyObservers();
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

        p.setScore(p.getKingdom().calculateScore());
        p.getKingdom().notifyObservers();
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
                this.calculateFinalScores();
                for (Player p2 : this.players)
                    p2.getKingdom().notifyObservers();
                this.notifyObservers();
            }
        }
    }

    private void calculateFinalScores()
    {
        if (gameConstraints[0])
            constraints.add(new Harmony());
        if (gameConstraints[1])
            constraints.add(new MiddleKingdom());
        for (Player p : this.players)
            for (GameConstraint gc : constraints)
                if (gc.respects(p.getKingdom().getGrid()))
                    gc.setNewScore(p);
    }

    public void createPlayer(String name)
    {
        boolean done = numberOfRounds == 0;
        if (done)
            this.players.add(new Player(name.length() > 0 ? name : "Anon " + (this.players.size()+1)));
    }

    public void clearPlayers()
    {
        boolean done = numberOfRounds == 0;
        if (done)
            this.players.clear();
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

    public void setConstraints(boolean harmony, boolean middleKingdom)
    {
        this.gameConstraints[0] = harmony;
        this.gameConstraints[1] = middleKingdom;
    }

    public List<GameConstraint> getGameConstraints()
    {
        return this.constraints;
    }
}
