package core.view;

import core.model.Domino;
import core.model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Board extends JFrame
{
    private final JPanel kingdomsGridPanel;
    private final JPanel rightPanel;
    private final JButton[][] selectedDominoButtons;
    private Domino selectedDomino;
    private int clickedTileIndex;
    private final JPanel modifyDominoPanel;
    private final JButton skipTurnButton;
    private final JLabel instructionsTitleLabel;
    private final JPanel instructionsAndModifyPanel;

    public Board()
    {
        this.setTitle("\"Only kings play KingDomino\"");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));

        clickedTileIndex = -1;
        kingdomsGridPanel = new JPanel();
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

                    final Board board = this;
                    int finalJ = j;
                    int finalI = i;
                    selectedDominoButtons[i][j].addActionListener(e -> {
                        if (finalI == 0 && finalJ == 0)
                            board.clickedTileIndex = 0;
                        else
                            board.clickedTileIndex = 1;
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
            selectedDomino.flipLeft();
            //selectedDominoPanel.setLayout(selectedDomino.isHorizontal() ? horizontalLayout : verticalLayout);
            paintButtons(selectedDomino);
            //System.out.println("Flipped to left. Tile 0 : " + selectedDomino.getTiles()[0].getBiome() + ", tile 1 : " + selectedDomino.getTiles()[1].getBiome());
        });

        buttonsFlip[1] = new JButton("\uD83D\uDDD8");
        buttonsFlip[1].addActionListener(e -> {
            selectedDomino.flip180();
            paintButtons(selectedDomino);
        });

        buttonsFlip[2] = new JButton("↷");
        buttonsFlip[2].addActionListener(e -> {
            selectedDomino.flipRight();
            paintButtons(selectedDomino);
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

        flipDominoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flipDominoPanel.setBorder(new LineBorder(Color.BLACK, 3, true));

        skipTurnButton = new JButton("Skip your turn");
        skipTurnButton.setFont(new Font(Font.SERIF, Font.PLAIN, 25));
        skipTurnButton.setBorder(new LineBorder(Color.BLACK, 2, true));
        skipTurnButton.setVisible(false);
        skipTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modifyDominoPanel = new JPanel();
        modifyDominoPanel.setLayout(new BoxLayout(modifyDominoPanel, BoxLayout.Y_AXIS));
        modifyDominoPanel.add(selectedDominoPanel);
        modifyDominoPanel.add(flipDominoPanel);
        modifyDominoPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        modifyDominoPanel.setVisible(false);

        /*modifyDominoPanel.setLayout(new GridLayout(2, 1));
        modifyDominoPanel.add(selectedDominoPanel);
        modifyDominoPanel.add(flipDominoPanel);*/

        /*modifyDominoPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridheight = 2;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        modifyDominoPanel.add(selectedDominoPanel, constraints);
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.gridy = 2;
        modifyDominoPanel.add(flipDominoPanel, constraints);*/

        //modifyDominoPanel.setBorder(new LineBorder(Color.BLACK, 2));

        instructionsAndModifyPanel = new JPanel();
        instructionsAndModifyPanel.setLayout(new GridLayout(2, 1));
        instructionsAndModifyPanel.add(modifyDominoPanel);
        instructionsAndModifyPanel.add(instructionsPanel);
        //instructionsAndModifyPanel.setBorder(new LineBorder(Color.BLACK, 2));

        JPanel linePanel = new JPanel();
        linePanel.setLayout(new GridLayout(1, 2));
        linePanel.add(kingdomsGridPanel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(1, 2));
        rightPanel.add(instructionsAndModifyPanel);

        linePanel.add(rightPanel);
        this.setContentPane(linePanel);
    }

    public void writeInstructionTitle(String s)
    {
        this.instructionsTitleLabel.setText(s);
    }

    public void addSkipTurnButton(JButton skipTurnButton)
    {
        modifyDominoPanel.add(skipTurnButton);
    }

    public void addKingdomPanel(JPanel panel)
    {


        this.kingdomsGridPanel.add(panel);

    }

    public void addWalletPanel(JPanel panel)
    {
        rightPanel.add(panel);
    }

    public void setSelectedDomino(Domino domino)
    {
        this.selectedDomino = domino;
        paintButtons(domino);
        if (domino != null)
            this.skipTurnButton.setVisible(true);
    }

    private void paintButtons(Domino domino)
    {
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
            selectedDominoButtons[0][0].setBackground(domino.getTiles()[0].getBiome().getColor());
            selectedDominoButtons[0][0].setText(domino.getTiles()[0].getCrownsAsString());
            if (domino.isHorizontal())
            {
                selectedDominoButtons[0][1].setVisible(true);
                selectedDominoButtons[1][0].setVisible(false);
                selectedDominoButtons[0][1].setBackground(domino.getTiles()[1].getBiome().getColor());
                selectedDominoButtons[0][1].setText(domino.getTiles()[1].getCrownsAsString());
            }
            else
            {
                selectedDominoButtons[0][1].setVisible(false);
                selectedDominoButtons[1][0].setVisible(true);
                selectedDominoButtons[1][0].setBackground(domino.getTiles()[1].getBiome().getColor());
                selectedDominoButtons[1][0].setText(domino.getTiles()[1].getCrownsAsString());
            }
        }
    }

    public Domino getSelectedDomino()
    {
        return this.selectedDomino;
    }

    public int getClickedTileIndex()
    {
        return this.clickedTileIndex;
    }

    public void setClickedTileIndex(int clickedTileIndex)
    {
        this.clickedTileIndex = clickedTileIndex;
    }

    public JButton getSkipTurnButton()
    {
        return skipTurnButton;
    }

    public void displayEndScreen(List<Player> players, boolean[] gameConstraints)
    {
        instructionsAndModifyPanel.removeAll();
        instructionsAndModifyPanel.setLayout(new GridLayout(players.size(), 1));
        players.sort(Comparator.comparingInt(Player::getScore).reversed());
        for (int i = 0; i < players.size(); i++)
        {
            Player p = players.get(i);
            JPanel playerStats = new JPanel();
            playerStats.setLayout(new BoxLayout(playerStats, BoxLayout.Y_AXIS));
            JLabel name = new JLabel("#" + (i+1) + " - " + p);
            name.setFont(new Font(Font.SERIF, Font.BOLD, 60));
            playerStats.add(name);
            JLabel score = new JLabel(p.getScore() + " points");
            score.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            playerStats.add(score);
            if (gameConstraints[0]) //MiddleKingdom
            {
                JLabel middleKingdom = new JLabel("MiddleKingdom:" + (p.getKingdom().respectsMiddleKingdom() ? "✓" : "✗"));
                middleKingdom.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
                playerStats.add(middleKingdom);
            }
            if (gameConstraints[1]) //Harmony
            {
                JLabel harmony = new JLabel("Harmony:" + (p.getKingdom().respectsHarmony() ? "✓" : "✗"));
                harmony.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
                playerStats.add(harmony);
            }
            playerStats.setBorder(new EmptyBorder(15, 15, 15, 15));
            instructionsAndModifyPanel.add(playerStats);
            playerStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        /*rightPanel.remove(1);
        String s = "";
        JLabel pixelArt = new JLabel(s);
        pixelArt.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        rightPanel.add(pixelArt);*/
    }
}