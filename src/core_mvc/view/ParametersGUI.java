package core_mvc.view;

import core_mvc.controller.ParametersController;
import core_mvc.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParametersGUI extends JFrame
{
    private final JPanel names;
    private final JCheckBox harmonyMode;
    private final JCheckBox middleKingdom;
    private int nbPlayers;
    private JLabel label;
    private JTextField txt;
    private final JButton startButton = new JButton("Let's gooooo !");

    public ParametersGUI(Game game, ParametersController controller)
    {
        this.setTitle("\"Only kings play KingDomino\"");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel nbPlayersSelection = new JPanel();
        Border nbPlayers = BorderFactory.createTitledBorder("How many players:");
        nbPlayersSelection.setBorder(nbPlayers);
        nbPlayersSelection.setLayout(new BoxLayout(nbPlayersSelection, BoxLayout.Y_AXIS));

        JRadioButton[] buttons = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        for(int i=0; i<buttons.length; i++)
        {
            buttons[i] = new JRadioButton((2+i) + " players");
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            nbPlayersSelection.add(buttons[i]);
            group.add(buttons[i]);
        }
        JPanel modeSelection = new JPanel();
        Border mode = BorderFactory.createTitledBorder("Choose a game mode:");
        modeSelection.setBorder(mode);
        harmonyMode = new JCheckBox("Harmony");
        middleKingdom = new JCheckBox("MiddleKingdom");
        modeSelection.add(harmonyMode);
        modeSelection.add(middleKingdom);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,0));
        panel.add(nbPlayersSelection);
        panel.add(modeSelection);

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(0,2));
        names = new JPanel();
        names.setLayout(new GridLayout(12,0));
        Border namesTitle = BorderFactory.createTitledBorder("Enter the players' names:");
        bigPanel.add(panel);
        bigPanel.add(names);
        names.setBorder(namesTitle);

        for (Component r : nbPlayersSelection.getComponents())
            if (r instanceof JRadioButton)
                ((JRadioButton) r).addActionListener(e -> {
                    if(((JRadioButton) r).isSelected())
                    {
                        int id = Integer.parseInt(((JRadioButton) r).getText().substring(0,1));
                        this.nbPlayers = Integer.parseInt(((JRadioButton) r).getText().substring(0,1));
                        names.removeAll();
                        for (int i = 0; i < id; i++)
                        {
                            label = new JLabel("Player n°" + (i+1));
                            txt = new JTextField();
                            txt.setSize(10,2);
                            names.add(label);
                            names.add(txt);
                            names.add(startButton);
                        }
                        revalidate();
                        repaint();
                    }
                });

        startButton.addActionListener(e -> {
            controller.clearPlayers();
            for (Component r : names.getComponents())
                if (r instanceof JTextField)
                    controller.addPlayer( ((JTextField) r).getText() );
            if (game.getPlayers().size() == this.nbPlayers)
            {
                controller.startGame();
                this.setVisible(false);
                this.dispose();
            }
        });

        harmonyMode.addActionListener(e -> {
            if (harmonyMode.isSelected())
                controller.addGameConstraint(Harmony.class);
            else
                controller.removeGameConstraint(Harmony.class);
        });

        middleKingdom.addActionListener(e -> {
            if (middleKingdom.isSelected())
                controller.addGameConstraint(MiddleKingdom.class);
            else
                controller.removeGameConstraint(MiddleKingdom.class);
        });

        this.setContentPane(bigPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
