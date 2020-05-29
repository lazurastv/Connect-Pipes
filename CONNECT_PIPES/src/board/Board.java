package board;

import static board.BoardLogic.CheckIfFinished;
import frames.Popup;
import generator.Generator;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import static javax.swing.SwingConstants.CENTER;
import menu.MenuGUI;
import static player.ActiveUser.loadActivePlayer;
import static player.ActiveUser.loadPreviousScore;
import player.Player;
import player.PlayersRanking;
import player.Saves;

public class Board extends JFrame {

    int w = 0, h = 0;   //width and height of board
    int[][] set;        //array containing information what is where on the board
    int now = 0;        //currently selected pipe
    Tile[][] tile;     //array with all tile
    Types type;
    int[] pipecount;    //how many pipes are available (pipe, turn)
    private int time = 0;       //how long it took to solve the level
    private int nextLevel;
    private boolean endless = false;
    public boolean viewOnly = false;
    private Generator gen;

    private JPanel grid;
    private JPanel select;
    private JPanel menu;
    private JLabel[] labels;
    private JLabel scoreCount;
    private JButton[][] squares;
    ButtonHandler buttonHandler = new ButtonHandler();

    public void setGenerator(Generator g) {
        gen = g;
    }
    
    private void LoadLevel(String f) {
        try {
            File in = new File(f);
            Scanner sc = new Scanner(in);
            String tmp = null;
            pipecount = new int[3];
            pipecount[0] = sc.nextInt();
            if (sc.hasNextInt()) {
                pipecount[1] = sc.nextInt();
            } else {
                pipecount[1] = 0;
            }
            if (sc.hasNextInt()) {
                pipecount[2] = sc.nextInt();
            } else {
                pipecount[2] = 0;
            }
            if (sc.hasNextInt()) {
                time = sc.nextInt();
            }
            if (in.getAbsolutePath().contains("src\\levels\\") && !in.getName().contains("rand")) {
                nextLevel = in.getName().charAt(5) - '0' + 1;
                String text = "";
                switch (nextLevel - 1) {
                    case 1: text = "<html>The most straightforward pipe in the game is the straight pipe!"
                            + " Depending on its orientation, it connects pipes to its left and right, or"
                            + " above and below it.<br>"
                            + " Select the pipe you wish to place on the right, and click on the field to place it."
                            + " To rotate a pipe, click on it again. To remove a pipe, select the eraser icon on"
                            + " the right and click on the pipe you want to remove. Connect the fish to the"
                            + " empty aquarium to beat the level!</html>"; break;
                    case 2: text = "<html>The second type of pipe is the turning pipe. It connects two pipes"
                            + " based on the direction it is currently facing. These pipes can lead to some very"
                            + " tricky level setups!</html>"; break;
                    case 3: text = "<html>The last type of pipe in the game is the teleporter pipe."
                            + " It is very similar to the regular straight pipe, but with one major difference."
                            + " A teleporter pipe connects to normal pipes the same way as others, but it also"
                            + " has a portal on one of its sides. This portal connects to ANY portal facing its direction."
                            + " The portal cannot however change the direction of traversal. If a portal faces"
                            + " upwards, after traveling through another portal it must still travel upwards."
                            + " Note: The portal can connect to any portal. If there are two portals it could"
                            + " connect to, it will connect to them both. The fish will pick the path that leads"
                            + " to the aquarium."
                            + " It may seem that these pipes are always better than straight ones, but teleporter"
                            + " pipes need at least two spaces between pipes to connect them."
                            + " The straight pipe can connect two pipes even if the space between them is one.</html>"; break;
                    case 4: text = "<html>This level introduces the final concept of this game: multiple aquariums!"
                            + " If all you had to do was connect one fish to its aquarium, the game would be pretty"
                            + " simple, don't you think? In this level, you must connect both fish to an empty aquarium."
                            + " Note that you decide which aquarium is optimal for which fish! Good luck!</html>"; break;
                    case 5: text = "<html>Now you know all there is to know about this game. This is the last level handcrafted"
                            + " by me for you so you get used to the concepts. Once you feel acustomed, go ahead and try"
                            + " endless mode, where time will be ticking down, and once the clock runs out, you must start again!"
                            + " The levels generated will be getting harder and harder, and your score will be compared to"
                            + " that of other players in the leaderboard. Can you be the best at connecting pipes?";
                }
                new Popup(text).setVisible(true);
            }
            sc.nextLine();
            while (sc.hasNext()) {
                h++;
                tmp = sc.nextLine();
            }
            String[] arr = tmp.split("\\s+");
            w = arr.length;
            if (h == 0) {
                System.out.println("Zapis jest pusty.");
            }
            sc = new Scanner(in);
            sc.nextLine();
            set = new int[h][w];
            for (int i = 0; i < h; i++) {
                tmp = sc.nextLine();
                arr = tmp.split("\\s+");
                for (int j = 0; j < w; j++) {
                    set[i][j] = type.GetIndex(arr[j]);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Level loading error.");
        }
    }

    public int SaveToFile(String s, boolean override) {
        try {
            if (s.isBlank() && override == false) {
                return 2;
            }
            File file;
            file = new File("src/saves/" + s + ".txt");
            if (file.exists() && override == false) {
                return 1;
            }
            file.delete();
            file.createNewFile();
            FileWriter save = new FileWriter(file.getAbsoluteFile());
            PrintWriter pw = new PrintWriter(save);
            pw.print(labels[0].getText() + " " + labels[1].getText() + " " + labels[2].getText() + " " + time + " " + nextLevel + "\n");
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pw.print(type.type[set[i][j]] + " ");
                    if (j + 1 == w) {
                        pw.print("\n");
                    }
                }
            }
            pw.close();
            save.close();
        } catch (IOException e) {
            System.out.println("The file couldn't be created.");
        }
        return 0;
    }

    private ImageIcon ChooseIcon(String s) {
        if (s == null) {
            return null;
        } else if (s.equals("empty")) {
            return null;
        }
        return new ImageIcon("src/icons/" + s + ".png");
    }

    private JLabel initLabel(String ch) {   //standard label format
        JLabel init = new JLabel();
        init.setBackground(Color.WHITE);
        init.setPreferredSize(new Dimension(60, 60));
        init.setText(ch);
        init.setOpaque(true);
        init.setHorizontalAlignment(CENTER);
        init.setBorder(BorderFactory.createLineBorder(new JButton().getBackground(), 1, true));
        return init;
    }

    String timeString;
    Timer timer;

    private void close() {
        this.dispose();
    }

    private JLabel initTimer(boolean countDown) {
        JLabel Stopwatch = initLabel("0");
        timer = new Timer(true);
        int advance = countDown ? -1 : 1;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                time += advance;
                if (time < 0) {
                    close();
                    gen.showSol();
                    saveScore(true);
                    MenuGUI men = new MenuGUI();
                    men.setVisible(true);
                    men.setAlwaysOnTop(false);
                    timer.cancel();
                }
                int minutes = time / 60;
                int seconds = time % 60;

                if (seconds > 59) {
                    seconds %= 60;
                    minutes++;
                }
                if (minutes > 59) {
                    timer.cancel();
                } else if (minutes < 10 && seconds < 10) {
                    timeString = ("0" + minutes + ":0" + seconds);
                } else if (minutes < 10) {
                    timeString = ("0" + minutes + ":" + seconds);
                } else if (seconds < 10) {
                    timeString = (minutes + ":0" + seconds);
                } else {
                    timeString = (minutes + ":" + seconds);
                }
                Stopwatch.setText(timeString);
                if (time < 10 && time % 2 == 1 && advance == -1) {
                    Stopwatch.setBackground(Color.RED);
                } else {
                    Stopwatch.setBackground(Color.WHITE);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
        return Stopwatch;
    }

    private void GenerateTiles() {
        tile = new Tile[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tile[i][j] = new Tile(type.type[set[i][j]]);
                tile[i][j].x = j;
                tile[i][j].y = i;
            }
        }
    }
    
    public Board(String f) {
        super("Pipe Game");
        type = new Types();
        if (f.equals("src/levels/rand.txt")) {
            endless = true;
        } else if (f.equals("src/levels/randSol.txt")) {
            endless = true;
            viewOnly = true;
        }
        LoadLevel(f);
        GenerateTiles();
        squares = new JButton[h + 1][w + 1];
        labels = new JLabel[h];
        select = new JPanel();
        select.setLayout(new GridLayout(h, 2));
        select.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, Color.BLACK));
        grid = new JPanel();
        grid.setLayout(new GridLayout(h, w));
        grid.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new JButton().getBackground()));
        menu = new JPanel();
        menu.setLayout(new GridLayout(1, 3));
        menu.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));

        for (int i = 0; i < h + 1; i++) {
            for (int j = 0; j < w + 1; j++) {
                String name = null;
                squares[i][j] = new JButton();
                squares[i][j].setBackground(Color.WHITE);
                if (!viewOnly)
                    squares[i][j].addActionListener(buttonHandler);
                else if (i == h && j == 2)
                    squares[i][j].addActionListener(new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            close();
                        }
                    });
                else
                    squares[i][j].addActionListener(new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            return;
                        }
                    });
                squares[i][j].setPreferredSize(new Dimension(60, 60));
                if (i == h) {
                    if (j == 0 && endless) {
                        scoreCount = initLabel("Your score: " + loadActivePlayer().getScore());
                        menu.add(scoreCount);
                    } else {
                        menu.add(squares[i][j]);
                        squares[i][j].setPreferredSize(new Dimension(0, 20));
                        switch (j) {
                            case 0:
                                squares[i][j].setText("Save Game");
                                break;
                            case 1:
                                menu.remove(squares[i][j]);
                                if (!viewOnly)
                                    menu.add(initTimer(endless));
                                else
                                    menu.add(initLabel("The solution"));
                                break;
                            case 2:
                                squares[i][j].setText("Quit to menu");
                                j = w;
                        }
                    }
                    continue;
                } else if (j < w) {
                    grid.add(squares[i][j]);
                    name = type.type[set[i][j]];
                } else if (j == w) {
                    if (i == 3) {
                        labels[i] = initLabel(Character.toString('X'));
                    } else if (i < 3) {
                        labels[i] = initLabel(Integer.toString(pipecount[i]));
                    } else {
                        labels[i] = initLabel(null);
                        labels[i].setOpaque(false);
                        select.add(labels[i]);
                        labels[i] = initLabel(null);
                        labels[i].setOpaque(false);
                        select.add(labels[i]);
                        continue;
                    }
                    select.add(labels[i]);
                    select.add(squares[i][j]);
                    switch (i) {
                        case 0:
                            name = "1pipeLR";
                            break;
                        case 1:
                            name = "1turnLU";
                            break;
                        case 2:
                            name = "1teleD";
                            break;
                        case 3:
                            name = "eraser";
                    }
                }
                squares[i][j].setIcon(ChooseIcon(name));
            }
        }

        checkIfNull();
        getContentPane().add(grid, BorderLayout.CENTER);
        getContentPane().add(menu, BorderLayout.SOUTH);
        getContentPane().add(select, BorderLayout.EAST);
        setSize(60 * (w + 3), 60 * (h + 1));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void GoToMenu() {
        this.dispose();
        MenuGUI l = new menu.MenuGUI();
        l.setVisible(true);
    }

    private void Rotate(Tile t) {
        String s = null;
        switch (t.type) {
            case "1pipeLR":
                s = "1pipeDU";
                break;
            case "1pipeDU":
                s = "1pipeLR";
                break;
            case "1turnDL":
                s = "1turnLU";
                break;
            case "1turnLU":
                s = "1turnRU";
                break;
            case "1turnRU":
                s = "1turnDR";
                break;
            case "1turnDR":
                s = "1turnDL";
                break;
            case "1teleD":
                s = "1teleL";
                break;
            case "1teleL":
                s = "1teleU";
                break;
            case "1teleU":
                s = "1teleR";
                break;
            case "1teleR":
                s = "1teleD";
        }
        int x = t.x, y = t.y;
        t = new Tile(s);
        t.x = x;
        t.y = y;
        tile[y][x] = t;
        squares[y][x].setIcon(ChooseIcon(s));
        set[y][x] = type.GetIndex(t.type);
    }

    private Board ReturnBoard() {
        return this;
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            for (int i = 0; i < h + 1; i++) {
                for (int j = 0; j < w + 1; j++) {
                    if (source == squares[i][j]) {
                        if (i == h) {
                            if (j == 0) {
                                new Popup("What should the savefile be called?").SavePopup(ReturnBoard());
                            } else {
                                timer.cancel();
                                if (endless && !viewOnly) {
                                    //new Popup("Your score: " + loadActivePlayer().getScore()).setVisible(true);
                                    saveScore(true);
                                }
                                GoToMenu();
                            }
                            return;
                        } else if (j < w) {
                            processClick(i, j);
                            return;
                        } else {
                            for (int k = 0; k < h; k++) {
                                if (k == i) {
                                    if (squares[k][j].getBackground() == Color.WHITE) {
                                        squares[k][j].setBackground(Color.YELLOW);
                                        switch (k) {
                                            case 0:
                                                now = type.GetIndex("1pipeLR");
                                                break;
                                            case 1:
                                                now = type.GetIndex("1turnLU");
                                                break;
                                            case 2:
                                                now = type.GetIndex("1teleD");
                                                break;
                                            case 3:
                                                now = type.GetIndex("eraser");
                                                break;
                                            default:
                                                now = type.GetIndex("empty");
                                                break;
                                        }
                                    } else {
                                        squares[k][j].setBackground(Color.WHITE);
                                        now = type.GetIndex("empty");
                                        return;
                                    }
                                } else {
                                    squares[k][j].setBackground(Color.WHITE);
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private void updateSet(String s, int i, int j) {
        int n = type.GetIndex(s);
        set[i][j] = n;
        squares[i][j].setIcon(ChooseIcon(type.type[n]));
        tile[i][j] = new Tile(type.type[n]);
        tile[i][j].x = j;
        tile[i][j].y = i;
    }

    private void checkIfNull() {
        if (pipecount[0] == 0) {
            squares[0][w].setIcon(null);
        } else {
            squares[0][w].setIcon(ChooseIcon("1pipeLR"));
        }

        if (pipecount[1] == 0) {
            squares[1][w].setIcon(null);
        } else {
            squares[1][w].setIcon(ChooseIcon("1turnLU"));
        }

        if (pipecount[2] == 0) {
            squares[2][w].setIcon(null);
        } else {
            squares[2][w].setIcon(ChooseIcon("1teleD"));
        }
    }

    private void saveScore(boolean finished) {
        try {
            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
            Player me = loadActivePlayer();
            if (me.getLevel() < nextLevel && !endless) {
                me.setLevel(nextLevel);
            }
            if (endless && !viewOnly) {
                me.addToScore(finished ? 0 : time + gen.getLevel()*10);
            }
            if (finished && loadPreviousScore() > me.getScore()) {
                me.setScore(loadPreviousScore());
            }
            PlayersRanking newrank = new PlayersRanking();
            for (Player p : ranking) {
                if (p.getId() == me.getId()) {
                    newrank.addSort(me);
                } else {
                    newrank.addSort(p);
                }
            }
            Saves.writePlayersToFile(newrank, "src/player/PlayerData.txt");
        } catch (IOException err) {
            return;
        }
    }

    private void processClick(int i, int j) {
        if (type.type[now].equals("eraser") && tile[i][j].type.contains("1") == true) {
            if (tile[i][j].type.contains("pipe")) {
                pipecount[0]++;
            } else if (tile[i][j].type.contains("turn")) {
                pipecount[1]++;
            } else if (tile[i][j].type.contains("tele")) {
                pipecount[2]++;
            }
            updateSet("empty", i, j);
        }
        for (int tmp = 0; tmp < pipecount.length; tmp++) {
            labels[tmp].setText(Integer.toString(pipecount[tmp]));
        }
        checkIfNull();

        if (tile[i][j].type.charAt(0) == '1' && now != type.GetIndex("eraser")) {
            Rotate(tile[i][j]);
        } else if (set[i][j] == type.GetIndex("empty") && now != type.GetIndex("empty")) {
            int tmp;
            if (pipecount[0] > 0 && type.type[now].contains("pipe")) {
                tmp = 0;
            } else if (pipecount[1] > 0 && type.type[now].contains("turn")) {
                tmp = 1;
            } else if (pipecount[2] > 0 && type.type[now].contains("tele")) {
                tmp = 2;
            } else {
                return;
            }
            if (tmp != -1) {
                pipecount[tmp]--;
                labels[tmp].setText(Integer.toString(pipecount[tmp]));
            }
            updateSet(type.type[now], i, j);
        }
        checkIfNull();

        if (CheckIfFinished(tile)) {
            timer.cancel();
            saveScore(false);
            if (endless) {
                gen = new Generator(false);
            } else {
                frames.Scrollable menu = new frames.Scrollable();
                menu.loadSaves(true);
                Popup tmp = new Popup("Congratulations! You have completed the level!");
                tmp.setVisible(true);
                tmp.setAlwaysOnTop(true);
            }
            this.dispose();
        }
    }
}
