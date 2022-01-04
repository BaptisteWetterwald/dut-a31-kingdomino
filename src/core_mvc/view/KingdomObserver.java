package core_mvc.view;

import core_mvc.controller.GameController;
import core_mvc.model.Kingdom;
import core_mvc.model.Player;
import core_mvc.model.Tile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KingdomObserver extends JPanel implements IObserver
{
    private final JButton[][] buttons;
    private final int gridWidth;
    private final int gridHeight;
    private final JLabel label;
    private final Player player;
    private final JPanel slideElementsPanel;

    public KingdomObserver(Player player, GameController controller)
    {
        this.player = player;
        Kingdom kingdom = player.getKingdom();
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
                b.addActionListener(e -> controller.tryDominoPlacement(kingdom, finalI, finalJ, player));
                //b.addActionListener(e -> kingdom.tryDominoPlacement(finalI, finalJ, gameView.getSelectedDomino(), gameView.getClickedTileIndex(), player));
                gridPanel.add(b);
            }
        }

        JPanel kingdomGridWithButtons = new JPanel();
        kingdomGridWithButtons.setLayout(new BoxLayout(kingdomGridWithButtons, BoxLayout.Y_AXIS));
        slideElementsPanel = new JPanel();
        slideElementsPanel.setLayout(new BoxLayout(slideElementsPanel, BoxLayout.X_AXIS));

        for (int i=0; i<4; i++)
        {
            String s = "";
            switch(i)
            {
                case 0:
                    s = "◀";
                    break;
                case 1:
                    s = "▲";
                    break;
                case 2:
                    s = "▼";
                    break;
                case 3:
                    s = "▶";
            }
            JButton b = new JButton(s);
            int finalI = i;
            b.addActionListener(e -> {
                player.getKingdom().slideGridElements(finalI, player.hasPlayed());
            });
            b.setFocusable(false);
            slideElementsPanel.add(b);
        }

        kingdomGridWithButtons.add(slideElementsPanel);
        kingdomGridWithButtons.add(gridPanel);

        columnPanel.add(kingdomGridWithButtons);
        label = new JLabel();
        label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        label.setText("Kingdom of " + player + " [0]");
        columnPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameView.addKingdomPanel(columnPanel);
        //columnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        columnPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JButton skipTurnButton = gameView.getSkipTurnButton();
        skipTurnButton.addActionListener(e -> player.getKingdom().placeDomino(gameView.getSelectedDomino(), player));
        gameView.addSkipTurnButton(skipTurnButton);
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
                buttons[i][j].setEnabled(!player.hasPlayed());
                buttons[i][j].setText(tile != null ? tile.getCrownsAsString() : "");
            }
        }
        label.setText("Kingdom of " + player + " [" + player.getScore() + "]");
        for (Component c : slideElementsPanel.getComponents())
            c.setEnabled(!player.hasPlayed());
    }
}