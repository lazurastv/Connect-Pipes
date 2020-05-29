package generator;

import static board.BoardLogic.CheckIfFinished;
import board.Tile;
import board.Types;
import java.util.Random;

public class GeneratePath {

    private int[][] set;
    private final Types types = new Types();
    private final ValidPath valp = new ValidPath();
    private final Random rand = new Random();
    private Tile[][] tile;
    private int w, h;

    public GeneratePath(int[][] set) {    //creates a random path starting from (x,y) to a random spot
        this.set = set;
        h = set.length;
        w = set[0].length;
        setToTiles();
        int[] tmp = randomEmpty();
        int y = tmp[0];
        int x = tmp[1];
        int px = x, py = y;
        int len = 3 + rand.nextInt(w/2 + h/2);
        while (len-- > 0 || valp.length() < 3) {
            tile[y][x].y = y;
            tile[y][x].x = x;
            valp.addTile(tile[y][x]);
            boolean left = true, right = true, up = true, down = true;
            if (x == 0 || !tile[y][x - 1].type.equals("empty")
                    || x - px > 0 || valp.contains(tile[y][x - 1])) {
                left = false;
            }
            if (x == w - 1 || !tile[y][x + 1].type.equals("empty")
                    || x - px < 0 || valp.contains(tile[y][x + 1])) {
                right = false;
            }
            if (y == 0 || !tile[y - 1][x].type.equals("empty")
                    || y - py > 0 || valp.contains(tile[y - 1][x])) {
                up = false;
            }
            if (y == h - 1 || !tile[y + 1][x].type.equals("empty")
                    || y - py < 0 || valp.contains(tile[y + 1][x])) {
                down = false;
            }
            if (!(up || down || left || right)) {
                break;
            }
            px = x;
            py = y;
            do {
                x = px;
                y = py;
                switch (rand.nextInt(4)) {
                    case 0:
                        if (up) {
                            y--;
                        }
                        break;
                    case 1:
                        if (down) {
                            y++;
                        }
                        break;
                    case 2:
                        if (right) {
                            x++;
                        }
                        break;
                    case 3:
                        if (left) {
                            x--;
                        }
                        break;
                }
            } while (px == x && py == y);
            tmp = goLine(py, px, y, x);
            y = tmp[0];
            x = tmp[1];
        }
        validateTypes();
    }

    private void validateTypes() {
        int len = valp.length();
        if (len < 3)
            return;
        int count = 0;
        Tile[] tmp = new Tile[len];
        for (Tile t : valp) {
            tmp[count++] = t;
        }
        for (int i = 0; i < len; i++) {
            StringBuilder sb = new StringBuilder();
            if (i == 0 || i == len - 1) {
                int add = i == 0 ? 1 : -1;
                sb.append(add > 0 ? "start" : "finish");
                if (tmp[i].x == tmp[i + add].x) {
                    if (tmp[i].y == tmp[i + add].y - 1) {
                        sb.append("D");
                    } else {
                        sb.append("U");
                    }
                } else {
                    if (tmp[i].x == tmp[i + add].x - 1) {
                        sb.append("R");
                    } else {
                        sb.append("L");
                    }
                }
            } else {
                if (tmp[i].x == tmp[i - 1].x && tmp[i].x == tmp[i + 1].x) {
                    int connected = 0;
                    int ti = i;
                    while (i + 2 < len && tmp[i].x == tmp[i + 1].x && tmp[i].x == tmp[i + 2].x) {
                        connected++;
                        i++;
                    }
                    if (connected < 2) {
                        tmp[ti].type = "1pipeDU";
                        sb.append("1pipeDU");
                    } else {
                        if (tmp[ti].y > tmp[i].y) {
                            tmp[ti].type = "1teleD";
                            sb.append("1teleU");
                        } else {
                            tmp[ti].type = "1teleU";
                            sb.append("1teleD");
                        }
                    }
                } else if (tmp[i].y == tmp[i - 1].y && tmp[i].y == tmp[i + 1].y) {
                    int connected = 0;
                    int ti = i;
                    while (i + 2 < len && tmp[i].y == tmp[i + 1].y && tmp[i].y == tmp[i + 2].y) {
                        connected++;
                        i++;
                    }
                    if (connected < 2) {
                        tmp[ti].type = "1pipeLR";
                        sb.append("1pipeLR");
                    } else {
                        if (tmp[ti].x > tmp[i].x) {
                            tmp[ti].type = "1teleR";
                            sb.append("1teleL");
                        } else {
                            tmp[ti].type = "1teleL";
                            sb.append("1teleR");
                        }
                    }
                } else {
                    sb.append("1turn");
                    if (tmp[i].x == tmp[i - 1].x || tmp[i].x == tmp[i + 1].x) {
                        if (tmp[i].y == tmp[i - 1].y - 1 || tmp[i].y == tmp[i + 1].y - 1) {
                            sb.append("D");
                        }
                        if (tmp[i].x == tmp[i - 1].x - 1 || tmp[i].x == tmp[i + 1].x - 1) {
                            sb.append("R");
                        } else {
                            sb.append("L");
                        }
                        if (!sb.toString().contains("D")) {
                            sb.append("U");
                        }
                    }
                }
            }
            tmp[i].type = sb.toString();
        }
        for (Tile t : tmp) {
            t.CheckConnections();
            tile[t.y][t.x] = t;
        }
    }

    private int[] goLine(int py, int px, int y, int x) {
        int dy = y - py;
        int dx = x - px;
        int diff = countAhead(y, x, dy, dx);
        diff = diff == 0 ? 0 : rand.nextInt(diff);
        while (diff > 0 && (!tile[y + diff * dy][x + diff * dx].type.equals("empty")
                || !tile[y + (diff-1) * dy][x + (diff-1) * dx].type.equals("empty")
                || valp.contains(tile[y + diff * dy][x + diff * dx])
                || valp.contains(tile[y + (diff-1) * dy][x + (diff-1) * dx]))) {
            diff--;
        }
        while (diff-- > 0) {
            tile[y][x].x = x;
            tile[y][x].y = y;
            valp.addTile(tile[y][x]);
            if (dy == 0) {
                x += dx;
            } else {
                y += dy;
            }
        }
        int[] ret = {y, x};
        return ret;
    }

    private int countAhead(int y, int x, int dy, int dx) {
        int diff = 0;
        if (dx == 0) {
            while (y + dy >= 0 && y + dy < h) {
                y += dy;
                diff++;
            }
        } else {
            while (x + dx >= 0 && x + dx < w) {
                x += dx;
                diff++;
            }
        }
        return diff;
    }

    //Chooses a random tile that is empty and has no neighbours facing its way.
    private int[] randomEmpty() {
        int y, x;
        do {
            y = rand.nextInt(h);
            x = rand.nextInt(w);
        } while (!(tile[y][x].type.equals("empty") && validSol(y, x)));
        int[] ret = {y, x};
        return ret;
    }

    private boolean validSol(int y, int x) {
        return ((y - 1 >= 0 && tile[y - 1][x].type.equals("empty")
                && (y - 2 >= 0 && tile[y - 2][x].type.equals("empty")
                || x - 1 >= 0 && tile[y - 1][x - 1].type.equals("empty")
                || x + 1 < w && tile[y - 1][x + 1].type.equals("empty")))
                || (y + 1 < h && tile[y + 1][x].type.equals("empty")
                && (y + 2 < h && tile[y + 2][x].type.equals("empty")
                || x - 1 >= 0 && tile[y + 1][x - 1].type.equals("empty")
                || x + 1 < w && tile[y + 1][x + 1].type.equals("empty")))
                || ((x + 1 < w && tile[y][x + 1].type.equals("empty"))
                && (y + 1 < h && tile[y + 1][x + 1].type.equals("empty")
                || y - 1 >= 0 && tile[y - 1][x + 1].type.equals("empty")
                || x + 2 < w && tile[y][x + 2].type.equals("empty")))
                || ((x - 1 >= 0 && tile[y][x - 1].type.equals("empty"))
                && (x - 2 >= 0 && tile[y][x - 2].type.equals("empty")
                || y - 1 >= 0 && tile[y - 1][x - 1].type.equals("empty")
                || y + 1 < h && tile[y + 1][x - 1].type.equals("empty"))));
    }

    public int[][] getSet() {
        if (CheckIfFinished(tile))
            return tilesToSet();
        else
            return new GeneratePath(set).getSet();
    }

    private int[][] tilesToSet() {
        set = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                set[i][j] = types.GetIndex(tile[i][j].type);
            }
        }
        return set;
    }

    private Tile[][] setToTiles() {
        tile = new Tile[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tile[i][j] = new Tile(types.type[set[i][j]]);
            }
        }
        return tile;
    }
}
