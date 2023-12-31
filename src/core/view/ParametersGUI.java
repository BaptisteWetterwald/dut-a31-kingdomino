package core.view;

import core.controller.ParametersController;
import core.model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParametersGUI extends JFrame
{
    private final JPanel names;
    private final JCheckBox harmonyMode;
    private final JCheckBox middleKingdom;
    private int nbPlayers;
    private JLabel label;
    private JTextField txt;
    private final Font smallButtonsFont = new Font("Arial", Font.PLAIN,15);
    private final Font smallTitlesFont = new Font("Arial", Font.BOLD,17);
    private final Font largeButtonsFont = new Font("Arial", Font.PLAIN, 35);
    private final Font largeTitlesFont = new Font("Arial", Font.BOLD, 45);

    private final ArrayList<TitledBorder> titles = new ArrayList<>();
    private final ArrayList<Component> buttonsAndLabels = new ArrayList<>();
    private boolean smallResolution = true;
    private final JButton changeFont;

    public ParametersGUI(Game game, ParametersController controller)
    {
        this.setTitle("\"Only kings play KingDomino\"");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1150, 677));

        JPanel bigPanel = new JPanel();
        JPanel p1 = new JPanel();
        p1.setOpaque(false);
        JPanel p2 = new JPanel();
        p2.setOpaque(false);
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        p4.setLayout(new GridLayout(1, 0));
        JPanel p5 = new JPanel();
        p5.setOpaque(false);
        p5.setLayout(new GridLayout(1, 0));
        JPanel p6 = new JPanel();
        p6.setLayout(new GridLayout(1, 0));
        p6.setOpaque(false);

        try {
            InputStream fileStream = this.getClass().getResourceAsStream("/kingdominoimg.png");
            Image img = ImageIO.read(fileStream);
            this.setContentPane(new JPanel(new BorderLayout()) {
                @Override
                public void paintComponent(Graphics g) {
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        p1.setLayout(new GridLayout(1, 0));
        p2.setLayout(new GridLayout(1, 0));
        p1.setBorder(new EmptyBorder(10, 250, 180, 10));
        p2.setBorder(new EmptyBorder(180, 10, 160, 350));
        p4.setBorder(new EmptyBorder(30, 100, 10, 10));
        p5.setBorder(new EmptyBorder(10, 100, 30, 10));
        p6.setBorder(new EmptyBorder(10, 0, 0, 10));

        JPanel nbPlayersSelection = new JPanel();
        TitledBorder nbPlayersBorder = BorderFactory.createTitledBorder("How many players?");
        titles.add(nbPlayersBorder);
        nbPlayersBorder.setTitleJustification(TitledBorder.CENTER);
        nbPlayersSelection.setBorder(nbPlayersBorder);
        nbPlayersSelection.setLayout(new BoxLayout(nbPlayersSelection, BoxLayout.Y_AXIS));
        nbPlayersSelection.setBackground(new Color(255, 255, 255, 150));

        JRadioButton[] buttons = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JRadioButton((2 + i) + " players");
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons[i].setOpaque(false);
            buttons[i].setRolloverEnabled(false);
            buttons[i].setFocusable(false);
            this.buttonsAndLabels.add(buttons[i]);

            nbPlayersSelection.add(buttons[i]);
            group.add(buttons[i]);
        }
        JPanel modeSelection = new JPanel();
        modeSelection.setBackground(new Color(255, 255, 255, 150));
        modeSelection.setLayout(new BoxLayout(modeSelection, BoxLayout.Y_AXIS));

        TitledBorder mode = BorderFactory.createTitledBorder("Choose a game mode");
        this.titles.add(mode);
        mode.setTitleJustification(TitledBorder.CENTER);
        modeSelection.setBorder(mode);

        harmonyMode = new JCheckBox("Harmony");
        harmonyMode.setOpaque(false);
        harmonyMode.setRolloverEnabled(false);
        harmonyMode.setFocusable(false);
        buttonsAndLabels.add(harmonyMode);

        middleKingdom = new JCheckBox("MiddleKingdom");
        middleKingdom.setOpaque(false);
        middleKingdom.setRolloverEnabled(false);
        middleKingdom.setFocusable(false);
        buttonsAndLabels.add(middleKingdom);

        changeFont = new JButton("Titles are too large? Click here!");
        changeFont.setFocusable(false);
        changeFont.addActionListener(e -> {
            changeFont();
        });
        changeFont.setFont(smallButtonsFont);

        modeSelection.add(harmonyMode);
        modeSelection.add(middleKingdom);

        harmonyMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        middleKingdom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        try {
            InputStream img2 = this.getClass().getResourceAsStream("/Capture.png");
            Image img3 = ImageIO.read(img2);
            p6.add(new JPanel(new BorderLayout()) {
                @Override
                public void paintComponent(Graphics g) {
                    g.drawImage(img3, 0, 0, null);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        p4.add(nbPlayersSelection);
        p5.add(modeSelection);

        p4.setOpaque(false);

        panel.add(p6);
        panel.add(p4);
        panel.add(p5);
        panel.add(changeFont);
        changeFont.setAlignmentX(Component.CENTER_ALIGNMENT);
        p1.add(panel);

        bigPanel.setOpaque(false);
        bigPanel.setLayout(new GridLayout(0, 2));
        names = new JPanel();
        names.setLayout(new GridLayout(9, 0));
        TitledBorder namesTitle = BorderFactory.createTitledBorder("Enter the players names");
        titles.add(namesTitle);
        namesTitle.setTitleJustification(TitledBorder.CENTER);
        bigPanel.add(p1);
        p2.add(names);
        bigPanel.add(p2);
        names.setBorder(namesTitle);
        names.setBackground(new Color(255, 255, 255, 150));
        JButton startButton = new JButton("Let's gooooo !");
        buttonsAndLabels.add(startButton);
        startButton.setBackground(new Color(255, 195, 0));
        p3.setOpaque(false);
        p3.add(startButton);

        for (Component r : nbPlayersSelection.getComponents())
            if (r instanceof JRadioButton)
                ((JRadioButton) r).addActionListener(e -> {
                    if (((JRadioButton) r).isSelected())
                    {
                        int id = Integer.parseInt(((JRadioButton) r).getText().substring(0, 1));
                        this.nbPlayers = Integer.parseInt(((JRadioButton) r).getText().substring(0, 1));
                        names.removeAll();
                        for (int i = 0; i < id; i++)
                        {
                            label = new JLabel("Player n°" + (i + 1));
                            label.setFont(this.smallResolution ? this.smallButtonsFont : this.largeButtonsFont);
                            buttonsAndLabels.add(label);
                            txt = new JTextField();
                            buttonsAndLabels.add(txt);
                            txt.setSize(10, 2);
                            txt.setFont(this.smallResolution ? this.smallButtonsFont : this.largeButtonsFont);
                            names.add(label);
                            names.add(txt);
                            names.add(p3);
                        }
                        revalidate();
                        repaint();
                    }
                });

        startButton.addActionListener(e -> {
            controller.clearPlayers();
            for (Component r : names.getComponents())
                if (r instanceof JTextField)
                    controller.addPlayer(((JTextField) r).getText());
            if (game.getPlayers().size() == this.nbPlayers) {
                controller.startGame();
                this.setVisible(false);
                this.dispose();
            }
        });

        ActionListener a = e -> controller.setGameConstraints(harmonyMode.isSelected(), middleKingdom.isSelected());
        harmonyMode.addActionListener(a);
        middleKingdom.addActionListener(a);

        this.add(bigPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.changeFont();
    }

    private void changeFont()
    {
        this.smallResolution = !this.smallResolution;
        for (TitledBorder tb : titles)
            tb.setTitleFont(this.smallResolution ? this.smallTitlesFont : this.largeTitlesFont);
        for (Component c : buttonsAndLabels)
            c.setFont(this.smallResolution ? this.smallButtonsFont : this.largeButtonsFont);

        this.changeFont.setText("Titles are too " + (this.smallResolution ? "small" : "large") + "? Click here!");
        repaint();
    }

}
