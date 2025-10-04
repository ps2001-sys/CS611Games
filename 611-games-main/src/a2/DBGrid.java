package a2;

import engine.TextUI;

/**
 * Board state + rules execution
 * It stores who drew each edge and who owns each box, and it enforces the “complete-a-box ⇒ you get the box” rule when a move is applied.
 */
public class DBGrid {
    // all these never change after construction
    private final int rows, cols;
    private final int[][] H;     // free/p1/p2 for H edges  : 0/1/2
    private final int[][] V;     // free/p1/p2 for H edges  : 0/1/2
    private final int[][] owner; // free/p1/p2 for box      : 0/1/2

    //initialize
    public DBGrid(int rows, int cols) {
        if (rows < 1 || cols < 1) throw new IllegalArgumentException("min 1x1");
        this.rows = rows; this.cols = cols;
        this.H = new int[rows + 1][cols];
        this.V = new int[rows][cols + 1];
        this.owner = new int[rows][cols];
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    // Bounds helpers: All index checks go through these helpers, avoiding off-by-one bugs
    private boolean inH(int r, int c) { return 0 <= r && r <= rows && 0 <= c && c < cols; }
    private boolean inV(int r, int c) { return 0 <= r && r < rows && 0 <= c && c <= cols; }

    // check whether the edge was picked by any player
    public boolean isEdgeFree(DBMove m) {
        if (m.dir == 'H') return inH(m.r, m.c) && H[m.r][m.c] == 0;
        if (m.dir == 'V') return inV(m.r, m.c) && V[m.r][m.c] == 0;
        return false;
    }

    // A legal move for p1 or p2
    // If a side can be selected, mark the ownership of the side. If it becomes a box, mark the ownership of the box
    public boolean applyEdge(DBMove m, int player) {
        boolean made = false;
        if (m.dir == 'H' && isEdgeFree(m)) {
            H[m.r][m.c] = player;
            if (m.r > 0 && isBoxComplete(m.r - 1, m.c)) {//box is finished, mark the ownership of the box
                owner[m.r - 1][m.c] = player;
                made = true;
            }
            if (m.r < rows && isBoxComplete(m.r, m.c))   { // same
                owner[m.r][m.c]  = player;
                made = true;
            }
        } else if (m.dir == 'V' && isEdgeFree(m)) {
            V[m.r][m.c] = player;
            if (m.c > 0 && isBoxComplete(m.r, m.c - 1)) {// same
                owner[m.r][m.c - 1] = player;
                made = true;
            }
            if (m.c < cols && isBoxComplete(m.r, m.c))   {// same
                owner[m.r][m.c] = player;
                made = true;
            }
        }
        return made;
    }

    // Check whether all four sides of this box have been enclosed
    private boolean isBoxComplete(int r, int c) {
        return H[r][c] > 0 && H[r + 1][c] > 0 && V[r][c] > 0 && V[r][c + 1] > 0;
    }

    // score1/score2 = number of boxes owned by p1/p2
    public int score(int player) {
        int s = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (owner[r][c] == player) s++;
        return s;
    }

    // check whether if all boxes are already claimed.
    // using this: owner(all) > 0    --> END
    public boolean isFull() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (owner[r][c] == 0) return false;
        return true;
    }

    // print the board , no color
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append("+").append(H[r][c] > 0 ? "---" : "   ");
            }
            sb.append("+\n");
            if (r < rows) {
                for (int c = 0; c <= cols; c++) {
                    sb.append(V[r][c] > 0 ? "|" : " ");
                    if (c < cols) {
                        int o = owner[r][c];
                        sb.append(o == 0 ? "   " : " " + o + " ");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // Colored print: red for p1, blue for p2
    public String renderColored(TextUI ui) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                int who = H[r][c];
                if (who == 0) sb.append("   ");
                else sb.append(colorEdge(ui, who, "---"));
            }
            sb.append("+\n");
            if (r < rows) {
                for (int c = 0; c <= cols; c++) {
                    int who = V[r][c];
                    sb.append(who==0 ? " " : colorEdge(ui, who, "|"));
                    if (c < cols) {
                        int own = owner[r][c];
                        if (own == 0) sb.append("   ");
                        else sb.append(" ").append(colorBox(ui, own, Integer.toString(own))).append(" ");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // map player with color
    private String colorEdge(TextUI ui, int who, String s){
        return (who==1) ? ui.red(s) : ui.blue(s);
    }
    private String colorBox(TextUI ui, int who, String s){
        return (who==1) ? ui.red(s) : ui.blue(s);
    }
}
