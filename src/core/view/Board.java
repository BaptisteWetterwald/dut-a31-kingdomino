package core.view;

import core.model.Constants;
import core.model.Domino;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

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

    public Board()
    {
        this.setTitle(Constants.WINDOW_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));

        clickedTileIndex = -1;
        kingdomsGridPanel = new JPanel();
        kingdomsGridPanel.setLayout(new GridLayout(0, 2));
        JPanel selectedDominoPanel = new JPanel();
        selectedDominoPanel.setLayout(new GridLayout(2, 2));
        //selectedDominoPanel.setBorder(new LineBorder(Color.MAGENTA, 2));
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
        buttonsFlip[0] = new JButton(Constants.FLIP_LEFT);
        buttonsFlip[0].addActionListener(e -> {
            selectedDomino.flipLeft();
            //selectedDominoPanel.setLayout(selectedDomino.isHorizontal() ? horizontalLayout : verticalLayout);
            paintButtons(selectedDomino);
            //System.out.println("Flipped to left. Tile 0 : " + selectedDomino.getTiles()[0].getBiome() + ", tile 1 : " + selectedDomino.getTiles()[1].getBiome());
        });

        buttonsFlip[1] = new JButton(Constants.FLIP_180);
        buttonsFlip[1].addActionListener(e -> {
            selectedDomino.flip180();
            paintButtons(selectedDomino);
            //System.out.println("Flipped 180°. Tile 0 : " + selectedDomino.getTiles()[0].getBiome() + ", tile 1 : " + selectedDomino.getTiles()[1].getBiome());
        });

        buttonsFlip[2] = new JButton(Constants.FLIP_RIGHT);
        buttonsFlip[2].addActionListener(e -> {
            selectedDomino.flipRight();
            //selectedDominoPanel.setLayout(selectedDomino.isHorizontal() ? horizontalLayout : verticalLayout);
            paintButtons(selectedDomino);
            //System.out.println("Flipped to right. Tile 0 : " + selectedDomino.getTiles()[0].getBiome() + ", tile 1 : " + selectedDomino.getTiles()[1].getBiome());
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
        modifyDominoPanel.setBorder(new EmptyBorder(Constants.KINGDOM_PADDING, Constants.KINGDOM_PADDING, Constants.KINGDOM_PADDING, Constants.KINGDOM_PADDING));

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

        JPanel instructionsAndModifyPanel = new JPanel();
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
        }
        else
        {
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

}