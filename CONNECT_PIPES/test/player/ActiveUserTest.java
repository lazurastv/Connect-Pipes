/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;
import static player.ActiveUser.saveActivePlayer;

/**
 *
 * @author dominik
 */
public class ActiveUserTest {
    
    @Test
    public void testSaveActivePlayer() {
        saveActivePlayer(new Player("Osoba 1", 6));
        File tmp = new File("src/player/active.txt");
        try (Scanner sc = new Scanner(tmp)) {
            int n = 0;
            while (sc.hasNextInt()) {
                int id = sc.nextInt();
                n++;
                if (n == 1) {
                    assertEquals(1, id);
                } else {
                    assertEquals(6, id);
                }
            }
            assertEquals(2, n);
        } catch (FileNotFoundException ex) {
            fail("active.txt doesn't exist.");
        }
        new File("src/player/active.txt").delete();
    }

}
