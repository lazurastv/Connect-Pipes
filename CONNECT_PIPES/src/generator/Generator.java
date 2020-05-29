package generator;

import board.Board;
import static board.BoardLogic.CheckIfFinished;
import board.Tile;
import board.Types;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Generator {

    private int w = 0, h = 0;
    private int time = 0;   //in endless mode, time counts down
    private int[][] set;
    private int[][] solvedSet;
    private int[] pipecount;
    private static int level;       //denotes difficulty
    private Random rand = new Random();
    private Types types = new Types();

    public Generator(boolean reset) {
        if (reset == true) {
            level = 1;
        } else {
            level++;
        }
        int n = 0;
        do {
            selectSize();
            n = generateSet();
            generatePipecount();
        } while (pipecount[0] == 0 && pipecount[1] == 0 && pipecount[2] == 0
                || !CheckIfFinished(setToTiles()));
        selectTime(n);
        genObst();
        solvedSet = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                solvedSet[i][j] = set[i][j];
            }
        }
        hideSet();
        saveLevel(set, false);
        Board b = new Board("src/levels/rand.txt");
        b.setGenerator(this);
    }

    private Tile[][] setToTiles() {
        Tile[][] tile = new Tile[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tile[i][j] = new Tile(types.type[set[i][j]]);
            }
        }
        return tile;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void showSol() {
        saveLevel(solvedSet, true);
        Board b = new Board("src/levels/randSol.txt");
        b.setAlwaysOnTop(true);
    }

    //selects level size based on level
    private void selectSize() { //selection of size based on level
        if (level < 5) {
            w = level + 3;
            h = level + 3;
        } else if (level < 10) {
            w = 8 + level - 4;
            h = 8;
        } else {
            w = 14 + rand.nextInt(5);
            h = 8;
        }
        w += rand.nextInt(3);    //minimal size 4
        h += rand.nextInt(3);
    }
    
    //select how much time is given to beat the level
    private void selectTime(int n) {
        int placementTime = pipecount[0] + pipecount[1] + pipecount[2];
        time = n * 10 + placementTime/2 + rand.nextInt(5);
    }

    //how many fish will have to be connected to empty aquariums
    private int selectFishAmount() {
        if (level < 5) {
            return (1 + rand.nextInt(level));
        } else if (level < 10) {
            return (3 + rand.nextInt(level-3));
        } else {
            return (5 + rand.nextInt(4));
        }
    }

    //generates a map with at least one solution
    private int generateSet() {
        set = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                set[i][j] = types.GetIndex("empty");
            }
        }
        int amount = selectFishAmount();
        int copy = amount;
        while (amount-- > 0) {
            set = new GeneratePath(set).getSet();
        }
        return copy;
    }

    //counts how many of each pipe are needed to win the level
    private void generatePipecount() {
        pipecount = new int[3];
        for (int[] sup : set) {
            for (int n : sup) {
                String tile = types.type[n];
                if (tile.contains("pipe")) {
                    pipecount[0]++;
                } else if (tile.contains("turn")) {
                    pipecount[1]++;
                } else if (tile.contains("tele")) {
                    pipecount[2]++;
                }
            }
        }
    }

    //makes every tile on the map either an obstacle or empty
    private void hideSet() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (types.type[set[i][j]].contains("pipe")
                        || types.type[set[i][j]].contains("turn")
                        || types.type[set[i][j]].contains("tele")) {
                    set[i][j] = types.GetIndex("empty");
                }
            }
        }
    }

    private void genObst() {
        int prob = 20 + rand.nextInt(80);   //% of tiles not obstacles
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (types.type[set[i][j]].equals("empty") && rand.nextInt(100) > prob) {
                    set[i][j] = types.GetIndex("full");
                }
            }
        }
    }

    //saves the level for reading, removed immediately after
    private void saveLevel(int set[][], boolean sol) {
        File file = new File("src/levels/rand" + (sol ? "Sol.txt" : ".txt"));
        try (FileWriter fw = new FileWriter(file);
                PrintWriter pw = new PrintWriter(fw)) {
            file.createNewFile();
            pw.print(pipecount[0] + " " + pipecount[1] + " " + pipecount[2] + " " + time + " " + (level + 1) + "\n");
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pw.print(types.type[set[i][j]] + " ");
                    if (j == w - 1) {
                        pw.print("\n");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File does not exist.");
        }
    }

}
