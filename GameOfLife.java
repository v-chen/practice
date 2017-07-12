import java.util.Random;

/**
 * Implementation for Game of Life
 * Board will be randomly initialized per class instantiation
 * Board size and number of ticks to play are configurable through command line
 * Usage as runnable class: java GameOfLife <num of rows> <num of cols> <num of ticks to run>
 * Example: java GameOfLife 3 5 2, this will create a 3x5 board, initialize it, play 2 ticks and print results. 
 * 
 * This class is not thread-safe as in current implementation. 
 */
public class GameOfLife {

    // adjust according to requirements and constraints
    public static final int MAX_NUM_ROWS = 1000;
    public static final int MAX_NUM_COLS = 1000;

    private int[][] board;
    private final int m;
    private final int n;

    public GameOfLife(int numRows, int numCols) {
        if (numRows < 0 || numRows > MAX_NUM_ROWS) {
            throw new IllegalArgumentException(
                String.format("num of rows must between 0 and %d", MAX_NUM_ROWS));
        }

        if (numCols < 0 || numCols > MAX_NUM_COLS) {
            throw new IllegalArgumentException(
                String.format("num of cols must between 0 and %d", MAX_NUM_COLS));
        }

        m = numRows;
        n = numCols;
        board = new int[numRows][numCols];
        init();
    }

    protected void init() {

        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = r.nextInt(2);
            }
        }
    }

    public void nextTick() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int count = countLiveNeighbors(i, j);
                if (board[i][j] == 1 && (count == 2 || count == 3)) {
                    board[i][j] = 3; 
                } else if (board[i][j] == 0 && count == 3) {
                    board[i][j] = 2;
                }
            }
        }
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = board[i][j] >> 1;
            }
        }
    }

    private int countLiveNeighbors(int i, int j) {
        int count = 0;
        for (int r = Math.max(0, i - 1); r <= Math.min(m-1, i+1); r++) {
            for (int c = Math.max(0, j - 1); c <= Math.min(n-1, j+1); c++) {
                count += board[r][c] & 1;
            }
        }
        count -= board[i][j] & 1;
        return count;
    }

    public void print() {
        System.out.println("--------------------");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("usage: java GameOfLife <num of rows> <num of cols> <num of ticks to run>");
            return;
        }
       
        try {
            int numRows = Integer.valueOf(args[0]);
            int numCols = Integer.valueOf(args[1]);
            int numTicks = Integer.valueOf(args[2]);

            GameOfLife game = new GameOfLife(numRows, numCols);
            game.print();
            for (int i = 0; i < numTicks; i++) {
                game.nextTick();
                game.print();
            }
        } catch (RuntimeException e) {
            System.err.println("failed with: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
