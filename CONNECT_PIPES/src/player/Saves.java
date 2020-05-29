package player;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Saves {

    public static PlayersRanking readPlayersFromFile(String s) throws IOException {
        PlayersRanking ranking;
        try (FileReader f = new FileReader(s);
                BufferedReader buff = new BufferedReader(f)) {
            ranking = new PlayersRanking();
            String line;
            while ((line = buff.readLine()) != null) {
                String[] dec = line.split("\\s+");
                if (dec.length == 4) {
                    try {
                        Player p = new Player(Integer.parseInt(dec[0]), dec[1], Integer.parseInt(dec[2]));
                        p.setLevel(Integer.parseInt(dec[3]));
                        ranking.addSort(p);
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }
        return ranking;
    }

    public static void writePlayersToFile(PlayersRanking ranking, String s) throws IOException {
        try (PrintWriter out = new PrintWriter(s)) {
            for (Player p : ranking) {
                out.println(p);
            }
        }
    }

}
