package core_mvc.model;

import core_mvc.utilities.CSVReader;

import java.util.*;

public class Game extends Observable
{
    Random random = new Random();

    private List<Domino> deck = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<GameConstraint> gameConstraints = new ArrayList<>();
    private Player[] oldOrder;
    private Player[] newOrder;
    private Domino selectedDomino;
    private Player currentPlayer;
    private Wallet wallet;
    private int clickedTileIndex;
    private int currentRound;
    private int numberOfRounds;
    private int playedTurnsInRound;
    private boolean finished = true;

    public void start()
    {
        System.out.println("GAME STARTED");
        this.currentRound = 0;

        this.clickedTileIndex = -1;
        int nbPlayers = this.players.size();
        wallet = new Wallet(nbPlayers%2 == 0 ? 4 : 3);
        CSVReader reader = CSVReader.getInstance();
        List<Domino> allDominos = reader.generateDominos();

        while (deck.size() < 2 * nbPlayers)
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
                //System.out.println("Adèle said \"This is the end... of the game!\"");

                this.calculateFinalScores();
                for (Player p2 : this.players)
                    p2.getKingdom().notifyObservers();
                this.notifyObservers();
                this.finished = true;
                this.deck = new ArrayList<>();
                this.players = new ArrayList<>();
                this.gameConstraints = new ArrayList<>();
            }
        }
    }

    private void calculateFinalScores()
    {
        for (Player p : this.players)
            for (GameConstraint gc : gameConstraints)
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

    public void addHarmonyConstraint()
    {
        if (numberOfRounds == 0)
            this.gameConstraints.add(new Harmony());
    }

    public void removeHarmonyConstraint()
    {
        if (numberOfRounds == 0)
            this.gameConstraints.removeIf(gc -> gc instanceof Harmony);
    }

    public void addMiddleKingdomConstraint()
    {
        if (numberOfRounds == 0)
            this.gameConstraints.add(new MiddleKingdom());
    }

    public void removeMiddleKingdomConstraint()
    {
        if (numberOfRounds == 0)
            this.gameConstraints.removeIf(gc -> gc instanceof MiddleKingdom);
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
