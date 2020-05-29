/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static player.Saves.readPlayersFromFile;
import static player.Saves.writePlayersToFile;

/**
 *
 * @author dominik
 */
public class SavesTest {


    public SavesTest() {
    }

    @Test
    public void testWritePlayersToFile() throws Exception {
        PlayersRanking instance = new PlayersRanking();
        instance.addSort(new Player("Player 1", 0));
        instance.addSort(new Player("Player 2", 2));
        writePlayersToFile(instance, "src/player/test.txt");
        File tmp = new File("src/player/test.txt");
        assertTrue(tmp.exists());
        try (Scanner sc = new Scanner(tmp)) {
            assertTrue(sc.nextLine().contains("Player 2 2 1"));
            assertTrue(sc.nextLine().contains("Player 1 0 1"));
        }
        tmp.delete();
    }

    @Test
    public void testReadPlayersFromFile() throws Exception {
        File tmp = new File("src/player/testr.txt");
        tmp.createNewFile();
        try (FileWriter fw = new FileWriter(tmp);
                PrintWriter pw = new PrintWriter(fw)) {
            pw.println("1 Player1 2 1");
            pw.println("2 Player2 1 1");
        }
        PlayersRanking instance = readPlayersFromFile("src/player/testr.txt");
        int count = 0;
        for (Player p : instance) {
            assertEquals(p.getLevel(), 1);
            count++;
        }
        tmp.delete();
        assertEquals(2, count);
    }

}
