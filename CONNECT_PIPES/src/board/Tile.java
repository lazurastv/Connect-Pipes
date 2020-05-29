package board;

public class Tile {

    public int x, y;           //location of this tile
    public String type;         //type of tile, int instead of enum so it can be loaded automatically
    public boolean up = false, down = false, left = false, right = false;  //which sides this pipe connects to
    
    public Tile(String s) {
        type = s;
        CheckConnections();
    }

    public void CheckConnections() {
        int i = type.length();
        while (--i >= 0) {
            switch (type.charAt(i)) {
                case 'L':
                    left = true;
                    break;
                case 'R':
                    right = true;
                    break;
                case 'D':
                    down = true;
                    break;
                case 'U':
                    up = true;
                    break;
                default:
                    return;
            }
        }
    }

    private boolean teleport(Tile t) {
        return (type.contains("U") && t.type.contains("D") && y < t.y
                || type.contains("D") && t.type.contains("U") && y > t.y
                || type.contains("L") && t.type.contains("R") && x < t.x
                || type.contains("R") && t.type.contains("L") && x > t.x);
    }

//checks if the given tile is connected to tile at location x,y
    public boolean IsConnectedTo(Tile t) {
        if (type.contains("tele") && t.type.contains("tele")) {
            return teleport(t);
        } else {
            return (y == t.y + 1 && up == true && t.down == true
                    || x == t.x - 1 && right == true && t.left == true
                    || x == t.x + 1 && left == true && t.right == true
                    || y == t.y - 1 && down == true && t.up == true);
        }
    }
}
