package a1;

/**
 * Represents the puzzle board.
 * This class is about:
     * 1.Stores tiles in 2D array
     * 2.Tracks blank position
     * 3.Validates and performs moves
     * 4.Checks if puzzle is solved
 */
public class Board {
    private final int rows, cols;
    private final Tile[][] grid;
    private int br, bc;  // blank row/col

    public Board(int rows, int cols){
        if (rows < 2 || cols < 2) throw new IllegalArgumentException("min 2x2");
        this.rows = rows; this.cols = cols;
        this.grid = new Tile[rows][cols];

        // Initialize in solved order
        int val = 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Tile(val++);
        grid[rows-1][cols-1] = new Tile(0);
        br = rows-1; bc = cols-1;
    }

    public int rows(){ return rows; }
    public int cols(){ return cols; }
    public int get(int r, int c){ return grid[r][c].value(); }

    // Check if a tile can slide , the tile must be adjacent to the blank
    public boolean canSlide(int number){
        if (number <= 0) return false; // blank or invalid
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c].value() == number)
                    return (r == br && Math.abs(c - bc) == 1) ||
                            (c == bc && Math.abs(r - br) == 1);
        return false;
    }

    // Slide the tile if possible and swap with blank
    public boolean slide(int number){
        if (!canSlide(number)) return false;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c].value() == number) {
                    grid[br][bc] = new Tile(number);
                    grid[r][c] = new Tile(0);
                    br = r; bc = c;
                    return true;
                }
        return false;
    }

    // Check if board is solved
    public boolean isSolved(){
        int k = 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (r == rows-1 && c == cols-1) return grid[r][c].value() == 0;
                if (grid[r][c].value() != k++) return false;
            }
        return true;
    }

//    // Render puzzle in simple text
//    public String render(){
//        StringBuilder sb = new StringBuilder();
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols; c++) {
//                int v = grid[r][c].value();
//                sb.append(String.format("%3s", v == 0 ? " " : Integer.toString(v)));
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }

    // Render puzzle in boxed style with grid lines
    public String renderBoxed() {
        int max = rows * cols - 1;
        int w = Math.max(2, Integer.toString(max).length());
        String hEdge = "+" + repeat("-", w + 2);

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) sb.append(hEdge);
            sb.append("+\n");
            for (int c = 0; c < cols; c++) {
                int v = grid[r][c].value();
                String cell = (v == 0) ? repeat(" ", w) : String.format("%" + w + "d", v);
                sb.append("| ").append(cell).append(" ");
            }
            sb.append("|\n");
        }
        for (int c = 0; c < cols; c++) sb.append(hEdge);
        sb.append("+\n");
        return sb.toString();
    }

    //move the blank if possible
    boolean moveBlank(int dr, int dc){
        int nr = br + dr, nc = bc + dc;
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) return false;
        int v = grid[nr][nc].value();
        grid[br][bc] = new Tile(v);
        grid[nr][nc] = new Tile(0);
        br = nr; bc = nc;
        return true;
    }

    // compatible replacement for String
    private static String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}