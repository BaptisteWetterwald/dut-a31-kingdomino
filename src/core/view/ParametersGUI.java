package core.view;

import core.model.GameConstraint;
import core.model.Harmony;
import core.model.MiddleKingdom;
import core.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParametersGUI extends JFrame
{
    private final JPanel names;
    private final JPanel p1;
    private final JPanel p2;
    private final JPanel p3;
    private final JPanel p4;
    private final JPanel p5;
    private final JCheckBox harmonyMode;
    private final JCheckBox middleKingdom;
    private JLabel label;
    private JTextField txt;
    private final JButton go = new JButton("Let's gooooo !");
    private final List<Player> players;
    private final List<GameConstraint> gameConstraints = new ArrayList<>();
    InputStream fileStream = this.getClass().getResourceAsStream("/kingdominoimg.png");
    InputStream img2 = this.getClass().getResourceAsStream("/Capture.png");

    public ParametersGUI()
    {
        JPanel bigPanel = new JPanel();
        p1=new JPanel();
        p2=new JPanel();
        p3=new JPanel();
        p4=new JPanel();
        p4.setLayout(new GridLayout(1,0));
        p5=new JPanel();

        try {
            Image img = ImageIO.read(fileStream);

            this.setContentPane(new JPanel(new BorderLayout()) {
                @Override public void paintComponent(Graphics g) {
                    g.drawImage(img, 0, 0, null);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setTitle("\"Only kings play KingDomino\"");
        this.setSize(750,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        players = new ArrayList<>();
        JPanel nbPlayersSelection = new JPanel();

        p1.setLayout(new GridLayout(1,0));
        p2.setLayout(new GridLayout(1,0));
        p1.setBorder(new EmptyBorder(40, 30, 20, 25));
        p2.setBorder(new EmptyBorder(25, 25, 35, 30));
        p4.setBorder(new EmptyBorder(0, 40, 30, 40));
        p5.setBorder(new EmptyBorder(25, 0, 0, 0));
        p4.setOpaque(false);
        p5.setOpaque(false);

        LineBorder roundedLineBorder = new LineBorder(Color.black, 1, true);
        TitledBorder nbPlayers = BorderFactory.createTitledBorder(roundedLineBorder,"How many players:");
        nbPlayersSelection.setBorder(nbPlayers);
        nbPlayersSelection.setLayout(new BoxLayout(nbPlayersSelection, BoxLayout.Y_AXIS));

        JRadioButton[] buttons = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        for(int i=0; i<buttons.length; i++)
        {
            buttons[i] = new JRadioButton((2+i) + " players");
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            nbPlayersSelection.add(buttons[i]);
            buttons[i].setOpaque(false);
            group.add(buttons[i]);
        }

        JPanel modeSelection = new JPanel();
        Border mode = BorderFactory.createTitledBorder(roundedLineBorder,"Choose a game mode:");
        modeSelection.setBorder(mode);
        harmonyMode = new JCheckBox("Harmony");
        middleKingdom = new JCheckBox("Middle Empire");
        modeSelection.add(harmonyMode);
        modeSelection.add(middleKingdom);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,0));

        try {
            Image img3 = ImageIO.read(img2);

            panel.add(new JPanel(new BorderLayout()) {
                @Override public void paintComponent(Graphics g) {
                    g.drawImage(img3, 0, 0, null);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        p4.add(nbPlayersSelection);
        p5.add(modeSelection);
        panel.add(p4);
        panel.add(p5);
        p1.add(panel);

        bigPanel.setLayout(new GridLayout(0,2));
        names = new JPanel();
        names.setLayout(new GridLayout(12,0));
        Border namesTitle = BorderFactory.createTitledBorder(roundedLineBorder,"Enter the players' names:");
        bigPanel.add(p1);
        p2.add(names);
        bigPanel.add(p2);
        names.setBorder(namesTitle);
        p3.add(go);

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
                            names.add(p3);
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
        nbPlayersSelection.setBackground(new Color(255,255,255,150));
        modeSelection.setBackground(new Color(255,255,255,150));
        panel.setOpaque(false);
        names.setBackground(new Color(255,255,255,150));
        p1.setOpaque(false);
        p2.setOpaque(false);
        p3.setOpaque(false);
        bigPanel.setOpaque(false);
        harmonyMode.setOpaque(false);
        middleKingdom.setOpaque(false);
        this.add(bigPanel);
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
}
