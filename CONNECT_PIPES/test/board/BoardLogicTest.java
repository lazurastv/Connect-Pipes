/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import static board.BoardLogic.CheckIfFinished;
import static board.BoardLogic.ScanBoard;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class BoardLogicTest {
    
    Tile[][] test;
    
    public BoardLogicTest() {
        test = new Tile[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                test[i][j] = new Tile("empty");
        test[0][0] = new Tile("startR");
        test[0][1] = new Tile("pipeLR");
        test[0][2] = new Tile("pipeDL");
        test[1][2] = new Tile("pipeRU");
        test[1][3] = new Tile("finishL");
    }
    
    @Test
    public void testCheckIfFinished() {
        assertTrue(CheckIfFinished(test));
        int[][] t = ScanBoard(test);
        assertEquals(1, t.length);
        assertEquals(0, t[0][0]);
        assertEquals(0, t[0][1]);
    }
    
}
