/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import static board.BoardLogic.CheckIfFinished;
import board.Tile;
import board.Types;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class GeneratePathTest {

    int[][] set;
    Types types;

    public GeneratePathTest() {
        types = new Types();
        set = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                set[i][j] = types.GetIndex("empty");
            }
        }
    }

    @Test
    public void testGetSet() {
        int[][] copyset = new int[5][5];
        System.arraycopy(set, 0, copyset, 0, 5);
        copyset = new GeneratePath(copyset).getSet();
        boolean confirm = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!types.type[copyset[i][j]].equals("empty")) {
                    confirm = true;
                }
            }
        }
        assertTrue(confirm);
    }

    @Test
    public void testCompleted() {
        int[][] copyset = new int[5][5];
        System.arraycopy(set, 0, copyset, 0, 5);
        int n = 3;
        while (n-- > 0) {
            copyset = new GeneratePath(copyset).getSet();
        }
        Tile[][] tile = new Tile[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tile[i][j] = new Tile(types.type[copyset[i][j]]);
            }
        }
        assertTrue(CheckIfFinished(tile));
    }
}
