package core_mvc.view;

import core_mvc.controller.GameController;
import core_mvc.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import static java.awt.Color.*;

public class GameView extends JFrame implements IObserver
{
    private final JButton[][] selectedDominoButtons;
    private final JPanel modifyDominoPanel;
    private final JButton skipTurnButton;
    private final JLabel instructionsTitleLabel;
    private final JLabel roundCounterLabel;
    private final JPanel instructionsAndModifyPanel;
    private final JPanel linePanel;

    private final Game game;
    private final GameController controller;

    //WALLET
    private final JPanel[] dominosPanel;
    private final HashMap<JPanel, JButton[]> walletMap;
    //FIN WALLET
    private JLabel label;
    private final HashMap<Kingdom, JButton[][]> kingdomButtons = new HashMap<>();
    private final HashMap<Kingdom, Player> kingdomsOwners = new HashMap<>();
    private final HashMap<Kingdom, JPanel> kingdomsSlideElementsPanel = new HashMap<>();
    private final HashMap<Kingdom, JLabel> kingdomsLabels = new HashMap<>();
    //FIN KINGDOM

    public GameView(Game game, GameController controller)
    {
        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));

        //Début swing pour board de base
        this.game = game;
        this.controller = controller;
        //KINGDOM

        this.setTitle("\"Only kings play KingDomino\"");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel kingdomsGridPanel = new JPanel();
        kingdomsGridPanel.setLayout(new GridLayout(0, 2));

        JPanel selectedDominoPanel = new JPanel();
        selectedDominoPanel.setLayout(new GridLayout(2, 2));
        selectedDominoButtons = new JButton[2][2];
        for (int i=0; i<selectedDominoButtons.length; i++)
        {
            for (int j=0; j<selectedDominoButtons[0].length; j++)
            {
                if ( !(i==selectedDominoButtons.length-1 && j==selectedDominoButtons[0].length-1) )
                {
                    selectedDominoButtons[i][j] = new JButton();
                    selectedDominoButtons[i][j].setFont(new Font(Font.SERIF, Font.BOLD, 50));
                    selectedDominoButtons[i][j].setBorder(new LineBorder(Color.BLACK, 3));
                    selectedDominoButtons[i][j].setFocusPainted(false);

                    int finalJ = j;
                    int finalI = i;
                    selectedDominoButtons[i][j].addActionListener(e -> {
                        controller.setClickedTileIndex((finalI == 0 && finalJ == 0) ? 0 : 1);
                    });
                    selectedDominoPanel.add(selectedDominoButtons[i][j]);
                }
            }
        }
        selectedDominoButtons[1][0].setVisible(false);

        JPanel flipDominoPanel = new JPanel();
        flipDominoPanel.setLayout(new BoxLayout(flipDominoPanel, BoxLayout.X_AXIS));

        JButton[] buttonsFlip = new JButton[3];
        buttonsFlip[0] = new JButton("↶");
        buttonsFlip[0].addActionListener(e -> {
            controller.flipLeft();
            paintSelectedDomino();
        });

        buttonsFlip[1] = new JButton("\uD83D\uDDD8");
        buttonsFlip[1].addActionListener(e -> {
            controller.flip180();
            paintSelectedDomino();
        });

        buttonsFlip[2] = new JButton("↷");
        buttonsFlip[2].addActionListener(e -> {
            controller.flipRight();
            paintSelectedDomino();
        });

        Font font = new Font(Font.SERIF, Font.BOLD, 40);
        for (JButton b : buttonsFlip)
        {
            b.setFont(font);
            b.setFocusPainted(false);
            flipDominoPanel.add(b);
        }

        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));

        instructionsTitleLabel = new JLabel("Instructions title here");
        instructionsTitleLabel.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        instructionsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        instructionsPanel.add(instructionsTitleLabel);

        JLabel[] instructionsLabels = new JLabel[5];
        font = new Font(Font.SERIF, Font.PLAIN, 30);
        for (int i = 0; i< instructionsLabels.length; i++)
        {
            instructionsLabels[i] = new JLabel("Instructions here");
            instructionsLabels[i].setFont(font);
            instructionsPanel.add(instructionsLabels[i]);
            instructionsLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        instructionsLabels[0].setText("1) Select a domino on the right");
        instructionsLabels[1].setText("2) Modify it");
        instructionsLabels[2].setText("3) Place it on your kingdom");
        instructionsLabels[3].setText("or skip your turn");
        instructionsLabels[4].setText("(this will ban the domino)");

        roundCounterLabel = new JLabel();
        updateRoundCounter();
        roundCounterLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        roundCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsPanel.add(roundCounterLabel);

        flipDominoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flipDominoPanel.setBorder(new LineBorder(Color.BLACK, 3, true));

        skipTurnButton = new JButton("Skip your turn");
        skipTurnButton.setFont(new Font(Font.SERIF, Font.PLAIN, 25));
        skipTurnButton.setBorder(new LineBorder(Color.BLACK, 2, true));
        skipTurnButton.setVisible(false);
        skipTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        skipTurnButton.addActionListener(e -> {
            disableButtonsFor(game.getCurrentPlayer().getKingdom());
            controller.skipTurn();
            playedTurn();
            if (!game.isFinished())
                enableButtonsFor(game.getCurrentPlayer().getKingdom());
        });

        modifyDominoPanel = new JPanel();
        modifyDominoPanel.setLayout(new BoxLayout(modifyDominoPanel, BoxLayout.Y_AXIS));
        modifyDominoPanel.add(selectedDominoPanel);
        modifyDominoPanel.add(flipDominoPanel);
        modifyDominoPanel.add(skipTurnButton);
        modifyDominoPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        modifyDominoPanel.setVisible(false);

        instructionsAndModifyPanel = new JPanel();
        instructionsAndModifyPanel.setLayout(new GridLayout(2, 1));
        instructionsAndModifyPanel.add(modifyDominoPanel);
        instructionsAndModifyPanel.add(instructionsPanel);

        linePanel = new JPanel();
        linePanel.setLayout(new GridLayout(1, 2));
        if (game.getPlayers().size() == 2)
        {
            JPanel kingdomsColumnPanel = new JPanel();
            kingdomsColumnPanel.setLayout(new GridLayout(2, 1));
            kingdomsColumnPanel.add(kingdomsGridPanel);
            linePanel.add(kingdomsColumnPanel);
        }
        else
            linePanel.add(kingdomsGridPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(1, 2));
        rightPanel.add(instructionsAndModifyPanel);

        linePanel.add(rightPanel);
        this.setContentPane(linePanel);
        this.setVisible(true);
        //Fin swing board de base

        //Début swing wallet
        walletMap = new HashMap<>();
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new GridLayout(4, 1));

        dominosPanel = new JPanel[game.getWallet().getSize()];
        for (int i=0; i<game.getWallet().getSize(); i++)
        {
            dominosPanel[i] = new JPanel();
            dominosPanel[i].setLayout(new GridLayout(1, 2));
            //dominosPanel[i].setBorder(new EmptyBorder(50, 10, 50, 10));
            dominosPanel[i].setBorder(new EmptyBorder(12, 12, 12, 12));
            JButton[] buttons = new JButton[2];
            buttons[0] = new JButton();
            buttons[1] = new JButton();
            buttons[0].setFont(new Font(Font.SERIF, Font.BOLD, 50));
            buttons[1].setFont(new Font(Font.SERIF, Font.BOLD, 50));

            buttons[0].setForeground(Color.BLACK);
            buttons[1].setForeground(Color.BLACK);

            buttons[0].setBorder(new LineBorder(Color.BLACK, 3));
            buttons[1].setBorder(new LineBorder(Color.BLACK, 3));

            buttons[0].setFocusPainted(false);
            buttons[1].setFocusPainted(false);

            //buttons[1].setBorder(new EmptyBorder(5, 5, 5, 5));
            walletMap.put(dominosPanel[i], buttons);
            dominosPanel[i].add(buttons[0]);
            dominosPanel[i].add(buttons[1]);
            columnPanel.add(dominosPanel[i]);

            int finalI = i;
            ActionListener onSelectedDomino = e -> {
                controller.setSelectedDomino(game.getWallet().getDominos().get(finalI));
                paintSelectedDomino();
                skipTurnButton.setVisible(true);
            };
            buttons[0].addActionListener(onSelectedDomino);
            buttons[1].addActionListener(onSelectedDomino);
            //buttons[1].addActionListener(e -> gameView.setSelectedDomino(wallet.getDominos().get(finalI)));
        }
        //gameView.addWalletPanel(columnPanel);
        rightPanel.add(columnPanel);

        //Fin swing wallet

        //Début swing kingdom

        int gridWidth = game.getPlayers().get(0).getKingdom().getGrid()[0].length;
        int gridHeight = game.getPlayers().get(0).getKingdom().getGrid().length;

        for (Player p : game.getPlayers())
        {
            JButton[][] buttons = new JButton[gridHeight][gridWidth];
            kingdomButtons.put(p.getKingdom(), buttons);
            kingdomsOwners.put(p.getKingdom(), p);
            JPanel gridPanel = new JPanel();
            gridPanel.setLayout(new GridLayout(gridHeight, gridWidth));
            JPanel columnKingdomPanel = new JPanel();
            columnKingdomPanel.setLayout(new BoxLayout(columnKingdomPanel, BoxLayout.Y_AXIS));

            JPanel kingdomGridWithButtons = new JPanel();
            kingdomGridWithButtons.setLayout(new BoxLayout(kingdomGridWithButtons, BoxLayout.Y_AXIS));
            JPanel slideElementsPanel = new JPanel();
            slideElementsPanel.setLayout(new BoxLayout(slideElementsPanel, BoxLayout.X_AXIS));

            for (int i=0; i<4; i++)
            {
                String s = switch (i)
                {
                    case 0 -> "◀";
                    case 1 -> "▲";
                    case 2 -> "▼";
                    case 3 -> "▶";
                    default -> "";
                };
                JButton b = new JButton(s);
                int finalI = i;

                b.addActionListener(e -> controller.slideKingdom(p.getKingdom(), finalI));
                b.setFocusable(false);
                b.setEnabled(false);
                slideElementsPanel.add(b);
            }

            kingdomsSlideElementsPanel.put(p.getKingdom(), slideElementsPanel);
            kingdomGridWithButtons.add(slideElementsPanel);
            kingdomGridWithButtons.add(gridPanel);

            for (int i=0; i<gridHeight; i++)
            {
                for (int j=0; j<gridWidth; j++)
                {
                    JButton b = new JButton();
                    buttons[i][j] = b;
                    b.setFont(new Font(Font.SERIF, Font.BOLD, 15));
                    b.setFocusPainted(false);
                    b.setEnabled(false);

                    int finalI = i;
                    int finalJ = j;
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Player old = game.getCurrentPlayer();
                            if (controller.tryDominoPlacement(finalI, finalJ))
                            {
                                disableButtonsFor(old.getKingdom());
                                playedTurn();
                                if (!game.isFinished())
                                    enableButtonsFor(game.getCurrentPlayer().getKingdom());
                            }
                        }
                    });
                    //b.addActionListener(e -> kingdom.tryDominoPlacement(finalI, finalJ, gameView.getSelectedDomino(), gameView.getClickedTileIndex(), player));
                    gridPanel.add(b);
                }
            }

            columnKingdomPanel.add(kingdomGridWithButtons);
            label = new JLabel();
            label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            label.setText("Kingdom of " + p + " [0]");
            columnKingdomPanel.add(label);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            kingdomsLabels.put(p.getKingdom(), label);

            //gameView.addKingdomPanel(columnPanel);
            //columnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            columnKingdomPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

            kingdomsGridPanel.add(columnKingdomPanel);
            controller.addObserver(p.getKingdom(), this);
            p.getKingdom().notifyObservers();
        }
        controller.addObserver(game.getWallet(), this);
        game.getWallet().notifyObservers();
        controller.addObserver(game, this);

        for (Component c : kingdomsSlideElementsPanel.get(game.getCurrentPlayer().getKingdom()).getComponents())
            c.setEnabled(true);
        for (JButton[] b : kingdomButtons.get(game.getCurrentPlayer().getKingdom()))
            for (JButton b2 : b)
                b2.setEnabled(true);

        this.writeInstructionTitle();
    }

    private void disableButtonsFor(Kingdom kingdom)
    {
        for (Component c : kingdomsSlideElementsPanel.get(kingdom).getComponents())
            c.setEnabled(false);
        for (JButton[] b : kingdomButtons.get(kingdom))
            for (JButton b2 : b)
                b2.setEnabled(false);
    }

    private void enableButtonsFor(Kingdom kingdom)
    {
        for (Component c : kingdomsSlideElementsPanel.get(kingdom).getComponents())
            c.setEnabled(true);
        for (JButton[] b : kingdomButtons.get(kingdom))
            for (JButton b2 : b)
                b2.setEnabled(true);
    }

    private void updateRoundCounter()
    {
        this.roundCounterLabel.setText("Round " + game.getCurrentRound() + "/" + game.getNumberOfRounds());
    }

    private void playedTurn()
    {
        controller.setSelectedDomino(null);
        writeInstructionTitle();
        paintSelectedDomino();
        this.skipTurnButton.setVisible(false);
        updateRoundCounter();
        game.getWallet().notifyObservers();
    }

    private void writeInstructionTitle()
    {
        Player p = game.getCurrentPlayer();
        char lastLetter = p.getName().toLowerCase().charAt(p.getName().length()-1);
        this.instructionsTitleLabel.setText(p.getName() + (lastLetter != 's' && lastLetter != 'z' ? "'s" : "'") + " turn");
    }

    private Color getColorOfTile(Tile tile)
    {
        Color color;
        if (tile != null)
        {
            color = switch (tile.getBiome())
            {
                case CASTLE -> PINK;
                case WHEAT -> new Color(240, 207, 3);
                case FOREST -> new Color(0, 100, 0);
                case WATER -> CYAN;
                case GRASS -> GREEN;
                case SWAMP -> new Color(205, 133, 63);
                case MINE -> GRAY;
            };
        }
        else
            color = Color.WHITE;
        return color;
    }

    private void paintSelectedDomino()
    {
        Domino domino = game.getSelectedDomino();
        if (domino == null)
        {
            selectedDominoButtons[0][0].setVisible(false);
            selectedDominoButtons[0][1].setVisible(false);
            selectedDominoButtons[1][0].setVisible(false);
            modifyDominoPanel.setVisible(false);
        }
        else
        {
            modifyDominoPanel.setVisible(true);
            selectedDominoButtons[0][0].setVisible(true);
            selectedDominoButtons[0][0].setBackground(this.getColorOfTile(domino.getTiles()[0]));
            selectedDominoButtons[0][0].setText(domino.getTiles()[0].getCrownsAsString());
            if (domino.isHorizontal())
            {
                selectedDominoButtons[0][1].setVisible(true);
                selectedDominoButtons[1][0].setVisible(false);
                selectedDominoButtons[0][1].setBackground(this.getColorOfTile(domino.getTiles()[1]));
                selectedDominoButtons[0][1].setText(domino.getTiles()[1].getCrownsAsString());
            }
            else
            {
                selectedDominoButtons[0][1].setVisible(false);
                selectedDominoButtons[1][0].setVisible(true);
                selectedDominoButtons[1][0].setBackground(this.getColorOfTile(domino.getTiles()[1]));
                selectedDominoButtons[1][0].setText(domino.getTiles()[1].getCrownsAsString());
            }
        }
    }

    public void displayEndScreen(List<GameConstraint> gameConstraints)
    {

        /*rightPanel.remove(1);
        String s = "";
        JLabel pixelArt = new JLabel(s);
        pixelArt.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        rightPanel.add(pixelArt);*/
    }

    @Override
    public void update(Observable o)
    {
        if (o instanceof Kingdom)
            update((Kingdom) o);
        else if (o instanceof Wallet)
            update((Wallet) o);
        else if (o instanceof Game)
            update((Game) o);
}

    public void update(Kingdom kingdom)
    {
        int gridWidth = game.getPlayers().get(0).getKingdom().getGrid()[0].length;
        int gridHeight = game.getPlayers().get(0).getKingdom().getGrid().length;
        for (int i=0; i<gridHeight; i++)
        {
            for (int j=0; j<gridWidth; j++)
            {
                Tile tile = kingdom.getGrid()[i][j];
                JButton[][] buttons = kingdomButtons.get(kingdom);
                buttons[i][j].setBackground(this.getColorOfTile(tile));
                buttons[i][j].setText(tile != null ? tile.getCrownsAsString() : "");
            }
        }
        Player owner = kingdomsOwners.get(kingdom);
        kingdomsLabels.get(owner.getKingdom()).setText("Kingdom of " + owner + " [" + owner.getScore() + "]");
    }

    public void update(Wallet wallet)
    {
        for (int i=0; i<wallet.getDominos().size(); i++)
        {
            for (int j=0; j<2; j++)
            {
                Tile t = wallet.getDominos().get(i).getTiles()[j];
                JButton b = walletMap.get(dominosPanel[i])[j];
                //b.setForeground(t.getCrowns() == 0 ? b.getBackground() : Color.BLACK);
                b.setBackground(this.getColorOfTile(t));
                b.setText(t.getCrownsAsString());
            }
            dominosPanel[i].setVisible(!game.getWallet().hasBeenUsedAlready(wallet.getDominos().get(i)));
        }
    }

    public void update(Game game)
    {
        for (Player p : game.getPlayers())
            disableButtonsFor(p.getKingdom());

        for (JPanel p : kingdomsSlideElementsPanel.values())
            for (Component c : p.getComponents())
                c.setVisible(false);

        instructionsAndModifyPanel.removeAll();
        instructionsAndModifyPanel.setLayout(new GridLayout(game.getPlayers().size(), 1));
        instructionsAndModifyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controller.makeLeaderboard();

        for (int i=0; i<game.getPlayers().size(); i++)
        {
            Player p = game.getPlayers().get(i);
            JPanel playerStats = new JPanel();
            playerStats.setLayout(new BoxLayout(playerStats, BoxLayout.Y_AXIS));
            JLabel name = new JLabel("#" + (i+1) + " - " + p);
            name.setFont(new Font(Font.SERIF, Font.BOLD, 60));
            playerStats.add(name);
            JLabel score = new JLabel(p.getScore() + " points");
            score.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            playerStats.add(score);
            for (GameConstraint gc : game.getGameConstraints())
            {
                JLabel label = new JLabel(gc.getClass().getSimpleName() + ": " + (gc.respects(p.getKingdom().getGrid()) ? "✓" : "✗"));
                label.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
                playerStats.add(label);
            }
            playerStats.setBorder(new EmptyBorder(15, 40, 15, 15));
            instructionsAndModifyPanel.add(playerStats);
            playerStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }
}