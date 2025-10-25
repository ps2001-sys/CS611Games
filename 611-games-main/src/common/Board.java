package common;

/**
 * Board class holds the tiles for various games.
 * It's kinda like the playground where your game happens.
 *
 * Feel free to extend and tweak to fit your own game style.
 *
 * Created by Zhuojun & Priyanshu, 2025-10-25
 */
public abstract class Board {

    protected final int rows;
    protected final int cols;
    protected Tile[][] grid;


    public Board(int rows, int cols, boolean autoInit) {
    if (rows < 1 || cols < 1) {
        throw new IllegalArgumentException("Board must have at least one row AND one column!");
    }
    this.rows = rows;
    this.cols = cols;
    grid = new Tile[rows][cols];

    if (autoInit) {
        initializeBoard(); // if autoInit == true, then initialize board
    }
    }
    public Board(int rows, int cols) {
        this(rows, cols, true); // 默认行为：自动初始化（不影响A1和A3）
    }


    // Each game should fill the board how it wants here
    protected abstract void initializeBoard();

    public boolean isValidPosition(int row, int col) {
        boolean rowOk = row >= 0 && row < rows;
        boolean colOk = col >= 0 && col < cols;
        return rowOk && colOk;
    }

    public Tile getTile(int row, int col) {
        if(!isValidPosition(row, col)) {
            throw new IllegalArgumentException("No tile there! Position (" + row + "," + col + ") is invalid.");
        }
        return grid[row][col];
    }

    public void setTile(int row, int col, Tile tile) {
        if(!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Can't set tile: (" + row + "," + col + ") out of bounds!");
        }
        grid[row][col] = tile;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    // Your turn! Show us the board on screen (or console).
    public abstract String render();

    // When is the game done? Define 'game over' here.
    public abstract boolean isGameOver();

    // Reset board to starting config.
    public void reset() {
        initializeBoard();
    }

    // Just a friendly heads-up about the board size
    public void printBoardInfo() {
        System.out.println("Board size is " + rows + " rows by " + cols + " columns. Play on!");
    }
}
