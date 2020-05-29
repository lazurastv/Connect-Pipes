/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class PlayersRankingTest {

    PlayersRanking instance;

    public PlayersRankingTest() {
        instance = new PlayersRanking();
        instance.addSort(new Player("Osoba 1", 0));
        instance.addSort(new Player("Osoba 2", 2));
        instance.addSort(new Player("Osoba 3", 1));
    }

    @Test
    public void testIfCorrectPlayers() {
        System.out.println("addSort");
        int count = -1;
        for (Player p : instance) {
            switch (++count) {
                case 0:
                    assertTrue(p.getScore() == 2);
                    break;
                case 1:
                    assertTrue(p.getNick().equals("Osoba 3"));
                    break;
                default: assertEquals(count, 2);
            }
        }
    }
}
