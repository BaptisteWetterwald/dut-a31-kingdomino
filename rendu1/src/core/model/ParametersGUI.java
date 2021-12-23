package core.model;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParametersGUI extends JFrame {

    JPanel panel;
    JPanel bigPanel;
    JPanel nbPlayersSelection;
    JPanel modeSelection;
    JPanel names;
    JCheckBox harmonyMode;
    JCheckBox middleEmpireMode;
    JLabel label;
    JTextField txt;
    JButton go = new JButton("Let's gooooo !");
    List<Player> players;
    boolean [] gameConstraint= new boolean[2];

    public ParametersGUI()
    {
        this.setTitle("\"Only kings play KingDomino\"");
        this.setSize(1000,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        players = new ArrayList<>();
        nbPlayersSelection = new JPanel();
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
        modeSelection = new JPanel();
        Border mode = BorderFactory.createTitledBorder("Choose a game mode:");
        modeSelection.setBorder(mode);
        harmonyMode = new JCheckBox("Harmony");
        middleEmpireMode = new JCheckBox("Middle Empire");
        modeSelection.add(harmonyMode);
        modeSelection.add(middleEmpireMode);
        panel = new JPanel();
        panel.setLayout(new GridLayout(2,0));
        panel.add(nbPlayersSelection);
        panel.add(modeSelection);

        bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(0,2));
        names = new JPanel();
        names.setLayout(new GridLayout(12,0));
        Border namesTitle = BorderFactory.createTitledBorder("Enter the players' names:");
        bigPanel.add(panel);
        bigPanel.add(names);
        names.setBorder(namesTitle);

        for (Component r : nbPlayersSelection.getComponents()) {
            if (r instanceof JRadioButton)
            {
                ((JRadioButton) r).addActionListener(e -> {
                    if(((JRadioButton) r).isSelected())
                    {
                        int id=Integer.parseInt(((JRadioButton) r).getText().substring(0,1));
                        names.removeAll();
                        for (int i = 0; i < id; i++) {
                            label = new JLabel("Player n°" + (i+1));
                            txt = new JTextField();
                            txt.setSize(10,2);
                            names.add(label);
                            names.add(txt);
                            names.add(go);
                        }
                        revalidate();
                        repaint();
                    }
                });
            }
        }

        go.addActionListener(e -> {
            players = new ArrayList<>();
            for (Component r : names.getComponents()) {
                if (r instanceof JTextField)
                {
                    players.add(new Player(((JTextField) r).getText()));
                }
            }
        });

        this.setContentPane(bigPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        harmonyMode.addActionListener(e -> {
            gameConstraint[1]= harmonyMode.isSelected();
            //print(gameConstraint);
        });

        middleEmpireMode.addActionListener(e -> {
            gameConstraint[1]= middleEmpireMode.isSelected();
                //print(gameConstraint);
        });

    }
    public List<Player> getPlayers()
    {
        return this.players;
    }

    private void print(boolean[] tb)
    {
        for(int i=0;i<tb.length;i++)
        {
            System.out.println(i+" : " +tb[i]);
        }
    }
}
