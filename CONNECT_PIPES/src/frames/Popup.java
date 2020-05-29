package frames;

import board.Board;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.CENTER;
import menu.Menu;
import menu.MenuGUI;
import static player.ActiveUser.saveActivePlayer;
import player.Player;
import player.PlayersRanking;
import player.Saves;

public class Popup extends JFrame {

    JPanel main;
    JPanel south;
    JPanel center;
    JTextField field;
    JLabel text;
    JButton yes;
    JButton no;
    String input;
    ActionListener buttonHandler;
    Board level;

    public Popup(String s) {
        main = new JPanel();
        south = new JPanel();
        buttonHandler = new CloseWhenClicked();
        text = new JLabel(s);
        int dif = 0;
        if (s.length() > 50) {
            dif = 100;
        }
        text.setPreferredSize(new Dimension(300 + dif, 100 + dif));
        text.setHorizontalAlignment(CENTER);
        text.setBackground(Color.YELLOW);
        text.setOpaque(true);
        yes = new JButton("Ok");
        yes.setPreferredSize(new Dimension(100, 50));
        yes.setBackground(Color.YELLOW);
        yes.addActionListener(buttonHandler);
        main.add(text);
        south.add(yes);

        getContentPane().add(main, BorderLayout.NORTH);
        getContentPane().add(south, BorderLayout.SOUTH);
        setAlwaysOnTop(true);
        setSize(400 + dif, 200 + dif);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addTextfield(String s) {
        center = new JPanel();
        field = new JTextField(s);
        field.setBackground(Color.WHITE);
        text.setPreferredSize(new Dimension(300, 50));
        field.setPreferredSize(new Dimension(300, 25));
        field.addActionListener(buttonHandler);
        field.setHorizontalAlignment(JTextField.CENTER);
        center.add(field);
        getContentPane().add(center, BorderLayout.CENTER);
    }

    public void addNo(String y, String n) {
        no = new JButton(n);
        yes.setText(y);
        no.setPreferredSize(yes.getPreferredSize());
        no.setBackground(yes.getBackground());
        no.addActionListener(buttonHandler);
        south.add(no);
    }

    public void SavePopup(Board b) {
        addTextfield("");
        addNo("Ok", "Cancel");
        yes.removeActionListener(buttonHandler);
        field.removeActionListener(buttonHandler);
        buttonHandler = new NameInput();
        yes.addActionListener(buttonHandler);
        field.addActionListener(buttonHandler);
        level = b;
        setVisible(true);
    }
    
    public void ConfirmSave() {
        addNo("Yes", "No");
        yes.removeActionListener(buttonHandler);
        no.removeActionListener(buttonHandler);
        buttonHandler = new ConfirmS();
        yes.addActionListener(buttonHandler);
        no.addActionListener(buttonHandler);
        setVisible(true);
    }

    public void CreatePlayer() {
        addTextfield("");
        yes.removeActionListener(buttonHandler);
        buttonHandler = new PlayerHandle();
        yes.addActionListener(buttonHandler);
        setVisible(true);
    }

    private class PlayerHandle implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == yes) {
                try {
                    PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
                    Player me = new Player().setNick(field.getText());
                    for (Player p : ranking) {
                        if (p.getNick().equals(me.getNick())) {
                            new Popup("This player name already exists!").setVisible(true);
                            Menu cur = new Menu();
                            cur.setAlwaysOnTop(false);
                            cur.setVisible(true);
                            Close();
                            return;
                        }
                    }
                    if (me.getNick().isBlank()) {
                        new Popup("The player name cannot be blank!").setVisible(true);
                        Menu cur = new Menu();
                        cur.setAlwaysOnTop(false);
                        cur.setVisible(true);
                        Close();
                        return;
                    }
                    ranking.addSort(me);
                    Saves.writePlayersToFile(ranking, "src/player/PlayerData.txt");
                    saveActivePlayer(me);
                    MenuGUI cur = new MenuGUI();
                    cur.setVisible(true);
                } catch (IOException err) {
                    return;
                }
                Close();
            }
        }
    }

    private class ConfirmS implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == yes) {
                level.SaveToFile(field.getText(), true);
            } else {
                new Popup("What should the savefile be called?").SavePopup(level);
            }
            Close();
        }
    }

    private class NameInput implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == yes) {
                int n = level.SaveToFile(field.getText(), false);
                Popup error;
                switch (n) {
                    case 1:
                        error = new Popup("This file already exists! Do you want to overwrite it?");
                        break;
                    case 2:
                        error = new Popup("Do you want to save without a name?");
                        break;
                    default:
                        Close();
                        return;
                }
                error.field = field;
                error.level = level;
                error.ConfirmSave();
            }
            Close();
        }
    }

    private class CloseWhenClicked implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Close();
        }
    }

    private void Close() {
        this.dispose();
    }
}
