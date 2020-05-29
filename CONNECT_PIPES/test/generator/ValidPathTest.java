/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import board.Tile;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class ValidPathTest {
    
    ValidPath valp;
    
    public ValidPathTest() {
        valp = new ValidPath();
        valp.addTile(new Tile("empty"));
        valp.addTile(new Tile("full"));
    }

    @Test
    public void testAddTile() {
        valp.addTile(new Tile("pipeLR"));
        int count = 0;
        for (Tile t : valp)
            count++;
        assertEquals(3, count);
    }

    @Test
    public void testLength() {
        assertEquals(2, valp.length());
    }

    @Test
    public void testContains() {
        Tile t = new Tile("pipeDU");
        t.x = 2;
        t.y = 2;
        valp.addTile(t);
        assertTrue(valp.contains(t));
    }
}
