package core.view;

import core.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KingdomObserver extends JFrame implements IObserver
{
    private final JButton[][] buttons;
    private final int gridWidth;
    private final int gridHeight;
    private final Kingdom kingdom;
    private final JLabel label;
    private final Player player;

    public KingdomObserver(Board board, Player player)
    {
        this.player = player;
        kingdom = player.getKingdom();
        gridHeight = kingdom.getGrid().length;
        gridWidth = kingdom.getGrid()[0].length;
        buttons = new JButton[gridWidth][gridHeight];
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridHeight, gridWidth));
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
        for (int i=0; i<gridHeight; i++)
        {
            for (int j=0; j<gridWidth; j++)
            {
                JButton b = new JButton();
                buttons[i][j] = b;
                b.setFont(new Font(Font.SERIF, Font.BOLD, 15));
                b.setFocusPainted(false);

                int finalI = i;
                int finalJ = j;
                b.addActionListener(e -> kingdom.tryDominoPlacement(finalI, finalJ, board.getSelectedDomino(), board.getClickedTileIndex(), player));
                gridPanel.add(b);
            }
        }
        columnPanel.add(gridPanel);
        label = new JLabel();
        label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        label.setText("Kingdom of " + player + " [0]");
        columnPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        board.addKingdomPanel(columnPanel);
        //columnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        columnPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JButton skipTurnButton = board.getSkipTurnButton();
        skipTurnButton.addActionListener(e -> player.getKingdom().placeDomino(board.getSelectedDomino(), player));
        board.addSkipTurnButton(skipTurnButton);
        this.update(kingdom);
    }

    @Override
    public void update(Observable object)
    {
        for (int i=0; i<gridHeight; i++)
        {
            for (int j=0; j<gridWidth; j++)
            {
                Tile tile = ((Kingdom)object).getGrid()[i][j];
                buttons[i][j].setBackground(tile != null ? tile.getBiome().getColor() : Color.WHITE);
                buttons[i][j].setEnabled(kingdom.isModifiable());
                buttons[i][j].setText(tile != null ? tile.getCrownsAsString() : "");
            }
        }
        label.setText("Kingdom of " + player + " [" + player.getScore() + "]");
    }
}