package frames;

import board.Board;
import board.Types;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.SwingConstants.CENTER;
import menu.Menu;
import menu.MenuGUI;
import static player.ActiveUser.loadActivePlayer;
import static player.ActiveUser.saveActivePlayer;
import player.Player;
import player.PlayersRanking;
import player.Saves;

public class Scrollable extends JFrame {

    private JPanel main;
    private JPanel south;
    private JPanel east, west, north;
    private JButton[] buttons;
    private JButton cancel;
    private ButtonHandler buttonHandler;
    private JScrollPane list;
    private String type;

    public Scrollable() {
        main = new JPanel();
        south = new JPanel();
        east = new JPanel();
        west = new JPanel();
        north = new JPanel();
        north.setPreferredSize(new Dimension(400, 50));
        buttonHandler = new ButtonHandler();
        east.setPreferredSize(new Dimension(35, 400));
        west.setPreferredSize(new Dimension(50, 400));
        main.setLayout(new GridLayout(0, 1));
        cancel = new JButton("Cancel");
        cancel.setBackground(Color.CYAN);
        cancel.addActionListener(buttonHandler);
        list = new JScrollPane(main);
        list.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        south.add(cancel);
        getContentPane().add(list, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
        getContentPane().add(east, BorderLayout.EAST);
        getContentPane().add(west, BorderLayout.WEST);
        getContentPane().add(north, BorderLayout.NORTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setSize(400, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void loadScores() {
        try {
            list.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            buttons = new JButton[1];
            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
            main.setLayout(new GridLayout(0, 2));
            JLabel tmp1 = new JLabel("Gracz");
            tmp1.setHorizontalAlignment(CENTER);
            tmp1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            main.add(tmp1);
            JLabel tmp2 = new JLabel("Wynik");
            tmp2.setHorizontalAlignment(CENTER);
            tmp2.setBorder(tmp1.getBorder());
            main.add(tmp2);
            for (Player p : ranking) {
                JLabel tmp = new JLabel(p.getNick());
                tmp.setPreferredSize(new Dimension(100, 50));
                tmp.setBackground(Color.CYAN);
                tmp.setOpaque(true);
                tmp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                tmp.setHorizontalAlignment(CENTER);
                main.add(tmp);
                JLabel tmp0 = new JLabel(Integer.toString(p.getScore()));
                tmp0.setPreferredSize(new Dimension(100, 50));
                tmp0.setBackground(Color.CYAN);
                tmp0.setOpaque(true);
                tmp0.setBorder(tmp.getBorder());
                tmp0.setHorizontalAlignment(CENTER);
                main.add(tmp0);
            }
        } catch (IOException e) {
            return;
        }
        cancel.removeActionListener(buttonHandler);
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        this.revalidate();
        this.repaint();
    }

    public void loadSaves(boolean levels) {
        File[] loads = levels ? new File("src/levels/").listFiles() : new File("src/saves/").listFiles();
        if (loads.length == 0 && levels == false) {
            MenuGUI it = new MenuGUI();
            it.setVisible(true);
            Popup le = new Popup("There are no saves to load!");
            le.setVisible(true);
            le.setAlwaysOnTop(true);
            close();
            return;
        }
        type = levels ? "level" : "save";
        int len = type.equals("level") ? 5 : loads.length;
        buttons = new JButton[len];
        for (int i = 0; i < len; i++) {
            buttons[i] = new JButton(Types.GetName(loads[i]));
            buttons[i].addActionListener(buttonHandler);
            buttons[i].setPreferredSize(new Dimension(200, 50));
            if (levels && loadActivePlayer().getLevel() < Types.GetName(loads[i]).charAt(5) - '0')
                buttons[i].setBackground(Color.GRAY);
            else
                buttons[i].setBackground(Color.CYAN);
            main.add(buttons[i]);
        }
        this.revalidate();
        this.repaint();
    }

    public void loadPlayers() {
        try {
            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
            type = "player";
            int len = 0;
            for (Player p : ranking) {
                len++;
            }
            if (len == 0) {
                close();
                Popup pop = new Popup("There are no existing players!");
                pop.setVisible(true);
                pop.yes.removeActionListener(buttonHandler);
                pop.yes.addActionListener(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        Menu men = new Menu();
                        men.setVisible(true);
                        return;
                    }
                });
            }
            buttons = new JButton[len];
            int i = 0;
            for (Player p : ranking) {
                buttons[i] = new JButton(p.getNick());
                buttons[i].addActionListener(buttonHandler);
                buttons[i].setPreferredSize(new Dimension(200, 50));
                buttons[i].setBackground(Color.CYAN);
                main.add(buttons[i]);
                i++;
            }
            cancel.removeActionListener(buttonHandler);
            cancel.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    close();
                    Menu m = new Menu();
                    m.setVisible(true);
                } 
            });
        } catch (IOException err) {
            return;
        }
        this.revalidate();
        this.repaint();
    }

    private void close() {
        this.dispose();

    }

    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            int i = 0;
            for (JButton j : buttons) {
                if (j == source) {
                    switch (type) {
                        case "save":
                            new Board(new File("src/saves/").listFiles()[i].getPath());
                            break;
                        case "player":
                            try {
                            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
                            for (Player p : ranking) {
                                if (p.getNick().equals(j.getText())) {
                                    saveActivePlayer(p);
                                    break;
                                }
                            }
                            MenuGUI m = new MenuGUI();
                            m.setVisible(true);
                        } catch (IOException err) {
                            return;
                        }
                        break;
                        case "level":
                            if (loadActivePlayer().getLevel() < i + 1) {
                                new Popup("Your level is too low!").setVisible(true);
                                return;
                            }
                            new Board(new File("src/levels/").listFiles()[i].getPath());
                    }
                } else {
                    i++;
                }
            }
            if (source == cancel) {
                MenuGUI men = new MenuGUI();
                men.setVisible(true);
            }
            close();
        }
    }
}
