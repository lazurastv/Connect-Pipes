package player;

import java.util.Iterator;

public class PlayersRanking implements Iterable<Player> {

    private Node head;
    private int size;

    public PlayersRanking() {
        head = null;
        size = 0;
    }

    public void addSort(Player p) {
        head = recAddSort(head, p);
        size++;
    }

    @Override
    public Iterator<Player> iterator() {
        return new Iterator<Player>() {
            Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Player next() {
                Player p = current.elem;
                current = current.next;
                return p;
            }
        };
    }

    private class Node {

        public Player elem;
        public Node next;
    }

    private Node recAddSort(Node n, Player p) {
        if (n == null || n.elem.getScore() < p.getScore()) {
            Node temp = new Node();
            temp.elem = p;
            temp.next = n;
            return temp;
        } else {
            n.next = recAddSort(n.next, p);
            return n;
        }
    }

}
