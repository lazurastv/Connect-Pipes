package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ActiveUser {

    public static Player loadActivePlayer() {
        File in = new File("src/player/active.txt");
        try (Scanner sc = new Scanner(in)) {
            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
            int id = sc.nextInt();
            for (Player p : ranking) {
                if (p.getId() == id) {
                    return p;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("active.txt not found");
        } catch (IOException e) {
            System.out.println("PlayerData.txt not found");
        }
        return null;
    }

    public static int loadPreviousScore() {
        File in = new File("src/player/active.txt");
        try (Scanner sc = new Scanner(in)) {
            sc.nextInt();
            return sc.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to find active.txt");
        }
        return -1;
    }

    public static void setScoreToZero() {
        Player p = loadActivePlayer();
        p.setScore(0);
        try {
            PlayersRanking ranking = Saves.readPlayersFromFile("src/player/PlayerData.txt");
            PlayersRanking newrank = new PlayersRanking();
            for (Player pl : ranking) {
                if (p.getId() == pl.getId()) {
                    newrank.addSort(p);
                } else {
                    newrank.addSort(pl);
                }
            }
            Saves.writePlayersToFile(newrank, "src/player/PlayerData.txt");
        } catch (IOException e) {}
    }

    public static void saveActivePlayer(Player p) {
        File active = new File("src/player/active.txt");
        try (FileWriter fw = new FileWriter(active);
                PrintWriter pw = new PrintWriter(fw)) {
            active.createNewFile();
            pw.print(p.getId());
            pw.print(" " + p.getScore());
        } catch (IOException e) {
            System.out.println("Saving player problem.");
        }
    }
}
