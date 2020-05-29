/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class TypesTest {
    
    Types types;
    
    public TypesTest() {
        types = new Types();
    }

    @Test
    public void testCorrectOrder() {
        assertTrue(types.type[3].equals("1teleL"));
    }
    
    @Test
    public void testGetIndex() {
        assertNotEquals(-1, types.GetIndex("empty"));
    }

    @Test
    public void testGetName() {
        File tmp = new File("src/board/tmp.txt");
        tmp.deleteOnExit();
        assertTrue(types.GetName(tmp).equals("tmp"));
    }
    
}
