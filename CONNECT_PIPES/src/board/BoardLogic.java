package board;

public class BoardLogic {
    public static int[][] ScanBoard(Tile[][] tile) {
        int[][] tmp = new int[8][2];
        int h = tile.length;
        int w = tile[0].length;
        int cur = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (tile[i][j].type.contains("start")) {
                    tmp[cur][0] = j;
                    tmp[cur][1] = i;
                    cur++;
                }
            }
        }
        int[][] tmp2 = new int[cur][2];
        for (int i = 0; i < cur; i++) {
            tmp2[i][0] = tmp[i][0];
            tmp2[i][1] = tmp[i][1];
        }
        return tmp2;
    }

    public static boolean CheckIfFinished(Tile[][] tile) {
        for (int i = 0; i < tile.length; i++)
            for (int j = 0; j < tile[0].length; j++) {
                tile[i][j].x = j;
                tile[i][j].y = i;
            }
        int[][] start = ScanBoard(tile);
        if (start == null) {
            System.out.println("Error. This level doesn't have a start!");
            return false;
        }
        for (int[] point : start) {
            int prex = -1, prey = -1;
            int x = point[0], y = point[1];
            int error = 0;
            while (true) {
                error++;
                boolean failed = true;
                if (error > 100) {
                    System.out.println("Zapobiegam nieskończonej pętli.");
                    return false;
                }
                if (tile[y][x].type.contains("finish")) {
                    break;
                } else {
                    for (Tile[] r : tile) {
                        for (Tile t : r) {
                            if (t.x == x || t.y == y) {
                                if (tile[y][x].IsConnectedTo(t) && (prey != t.y || prex != t.x)) {
                                    prex = x;
                                    prey = y;
                                    x = t.x;
                                    y = t.y;
                                    failed = false;
                                }
                            }
                        }
                    }
                    if (failed) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
