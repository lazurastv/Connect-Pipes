package generator;

import board.Tile;
import java.util.Iterator;

public class ValidPath implements Iterable<Tile> {

    private Node head;
    private int size;

    private class Node {
        public Tile tile;
        public Node next;
    }
    
    public ValidPath() {
        head = null;
        size = 0;
    }
    
    public int length() {
        return size;
    }
    
    public void addTile(Tile t) {
        head = recAddTile(head, t);
        size++;
    }
    
    private Node recAddTile(Node n, Tile t) {
        if (n == null) {
            Node tmp = new Node();
            tmp.tile = t;
            tmp.next = n;
            return tmp;
        } else {
            n.next = recAddTile(n.next, t);
            return n;
        }
    }

    public boolean contains(Tile t) {
        for (Tile m : this) {
            if (t.x == m.x && t.y == m.y)
                return true;
        }
        return false;
    }
    
    @Override
    public Iterator<Tile> iterator() {
        return new Iterator<Tile>() {
            Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Tile next() {
                Tile t = current.tile;
                current = current.next;
                return t;
            }
        };
    }
}
