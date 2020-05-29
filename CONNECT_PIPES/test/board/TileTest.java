/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class TileTest {
    
    @Test
    public void testTeleport() {
        Tile t = new Tile("1teleR");
        t.x = 4;
        t.y = 1;
        Tile t2 = new Tile("1teleL");
        t2.x = 1;
        t2.y = 1;
        assertTrue(t.IsConnectedTo(t2));
        t2.x = 6;
        assertTrue(!t2.IsConnectedTo(t));
    }

    @Test
    public void testCheckConnections() {
        Tile t = new Tile("pipeDR");
        assertTrue(t.right);
        assertTrue(t.down);
    }

    @Test
    public void testIsConnectedTo() {
        Tile t = new Tile("pipeLR");
        t.x = 2;
        t.y = 2;
        Tile t2 = new Tile("pipeDL");
        t2.x = 3;
        t2.y = 2;
        assertTrue(t.IsConnectedTo(t2));
        Tile t3 = new Tile("pipeDU");
        t3.x = 3;
        t3.y = 3;
        assertTrue(t2.IsConnectedTo(t3));
        assertTrue(!t.IsConnectedTo(t3));
    }
}
