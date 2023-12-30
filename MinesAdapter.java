import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesAdapter extends MouseAdapter{

    private final Board board;
    
    public MinesAdapter(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int cCol = x / board.CELL_SIZE;
        int cRow = y / board.CELL_SIZE;
        
        int numRows = board.getN_ROWS();
        int numCols = board.getN_COLS();

        boolean doRepaint = false;

        if (!board.inGame) {
            board.newGame();
            board.repaint();
        }

        if ((x < numCols * board.CELL_SIZE) && (y < numRows * board.CELL_SIZE)) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                handleRightClick(cCol, cRow);
            } else {
                handleLeftClick(cCol, cRow);
            }

            if (doRepaint) {
                board.repaint();
            }
        }
    }

    private void handleRightClick(int cCol, int cRow) {
        int numCols = board.getN_COLS();

        if (board.field[(cRow * numCols) + cCol] > board.MINE_CELL) {
            board.repaint();

            if (board.field[(cRow * numCols) + cCol] <= board.COVERED_MINE_CELL) {
                if (board.minesLeft > 0) {
                    board.field[(cRow * numCols) + cCol] += board.MARK_FOR_CELL;
                    board.minesLeft--;
                    String msg = Integer.toString(board.minesLeft);
                    board.statusbar.setText(msg);
                } else {
                    board.statusbar.setText("No marks left");
                }
            } else {
                board.field[(cRow * numCols) + cCol] -= board.MARK_FOR_CELL;
                board.minesLeft++;
                String msg = Integer.toString(board.minesLeft);
                board.statusbar.setText(msg);
            }
        }
    }

    private void handleLeftClick(int cCol, int cRow) {
        int numCols = board.getN_COLS();

        if (board.field[(cRow * numCols) + cCol] > board.COVERED_MINE_CELL) {
            return;
        }

        boolean doRepaint = false;

        if ((board.field[(cRow * numCols) + cCol] > board.MINE_CELL)
                && (board.field[(cRow * numCols) + cCol] < board.MARKED_MINE_CELL)) {
            board.field[(cRow * numCols) + cCol] -= board.COVER_FOR_CELL;
            doRepaint = true;

            if (board.field[(cRow * numCols) + cCol] == board.MINE_CELL) {
                board.inGame = false;
                board.repaint();
                board.showGameOverDialog();
            }

            if (board.field[(cRow * numCols) + cCol] == board.EMPTY_CELL) {
                board.find_empty_cells((cRow * numCols) + cCol);
            }
        }

        if (doRepaint) {
            board.repaint();
        }
    }
}
