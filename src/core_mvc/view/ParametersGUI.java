package core_mvc.view;

import core_mvc.model.GameConstraint;
import core_mvc.model.Harmony;
import core_mvc.model.MiddleKingdom;
import core_mvc.model.Player;

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
    private JLabel label;
    private JTextField txt;
    private final JButton go = new JButton("Let's gooooo !");
    private final List<Player> players;
    private final List<GameConstraint> gameConstraints = new ArrayList<>();

    public ParametersGUI()
    {
        this.setTitle("\"Only kings play KingDomino\"");
        this.setSize(1000,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        players = new ArrayList<>();
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
        middleKingdom = new JCheckBox("Middle Empire");
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
            players.clear();
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
            if (harmonyMode.isSelected())
                gameConstraints.add(new Harmony());
            else
            {
                gameConstraints.removeIf(gc -> gc instanceof Harmony);
            }
        });

        middleKingdom.addActionListener(e -> {
            if (middleKingdom.isSelected())
                gameConstraints.add(new MiddleKingdom());
            else
            {
                gameConstraints.removeIf(gc -> gc instanceof MiddleKingdom);
            }
        });

    }


    public List<Player> getPlayers()
    {
        return this.players;
    }

    /*private void print(boolean[] tb)
    {
        for(int i=0;i<tb.length;i++)
        {
            System.out.println(i+" : " +tb[i]);
        }
    }*/

    public List<GameConstraint> getGameConstraints()
    {
        return this.gameConstraints;
    }
}
