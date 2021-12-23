package core.view;

import core.model.Tile;
import core.model.Wallet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;

public class WalletObserver extends JFrame implements IObserver
{
    private final JPanel[] dominosPanel;
    private final HashMap<JPanel, JButton[]> map;

    public WalletObserver(Wallet wallet, Board board)
    {
        map = new HashMap<>();
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new GridLayout(0, 1));

        dominosPanel = new JPanel[wallet.getSize()];
        for (int i=0; i< wallet.getSize(); i++)
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
            map.put(dominosPanel[i], buttons);
            dominosPanel[i].add(buttons[0]);
            dominosPanel[i].add(buttons[1]);
            columnPanel.add(dominosPanel[i]);

            int finalI = i;
            buttons[0].addActionListener(e -> board.setSelectedDomino(wallet.getDominos().get(finalI)));
            buttons[1].addActionListener(e -> board.setSelectedDomino(wallet.getDominos().get(finalI)));
        }
        board.addWalletPanel(columnPanel);
        this.update(wallet);
    }

    @Override
    public void update(Observable object)
    {
        Wallet wallet = (Wallet)object;

        for (int i=0; i<wallet.getDominos().size(); i++)
        {
            for (int j=0; j<2; j++)
            {
                Tile t = wallet.getDominos().get(i).getTiles()[j];
                JButton b = map.get(dominosPanel[i])[j];
                //b.setForeground(t.getCrowns() == 0 ? b.getBackground() : Color.BLACK);
                b.setBackground(t.getBiome().getColor());
                b.setText(t.getCrownsAsString());
            }
            dominosPanel[i].setVisible(!wallet.hasBeenUsedAlready(wallet.getDominos().get(i)));
        }
    }
}
