import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class Board extends JPanel {

    public final int NUM_IMAGES = 13;
    public final int CELL_SIZE = 30; // Increase the cell size

    public final int COVER_FOR_CELL = 10;
    public final int MARK_FOR_CELL = 10;
    public final int EMPTY_CELL = 0;
    public final int MINE_CELL = 9;
    public final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    public final int DRAW_MINE = 9;
    public final int DRAW_COVER = 10;
    public final int DRAW_MARK = 11;
    public final int DRAW_WRONG_MARK = 12;

    public int N_MINES = 0;
    private int N_ROWS = 0;    // Increase the number of rows
    private int N_COLS = 0;    // Increase the number of columns

    public int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    public int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    public int[] field;
    public boolean inGame;
    public int minesLeft;
    public Image[] img;
    public long startTime;
    public long gameTime;

    public int allCells;
    public final JLabel statusbar;
    private Minesweeper minesweeper;

    public int getN_ROWS() {
        return N_ROWS;
    }
    
    public void setN_ROWS(int N_ROWS) {
        this.N_ROWS = N_ROWS;
    }

    public int getN_COLS() {
        return N_COLS;
    }
    
    public void setN_COLS(int N_COLS) {
        this.N_COLS = N_COLS;
    }

    public Board(JLabel statusbar, Minesweeper minesweeper) {

        this.statusbar = statusbar;
        this.minesweeper = minesweeper;
    }

    public void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            String path = "/resources/" + i + ".png";  // Assuming resources are in the src/resources directory

            // Load the image using getResource()
            ImageIcon icon = new ImageIcon(getClass().getResource(path));

            // Check if the image is loaded successfully
            if (icon.getImage() != null) {
                // Scale the image based on CELL_SIZE
                Image originalImage = icon.getImage();
                Image scaledImage = originalImage.getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
                img[i] = scaledImage;
            } else {
                // Handle the case where the image couldn't be loaded
                System.err.println("Error loading image: " + path);
            }
        }

        addMouseListener(new MinesAdapter(this));
        newGame();
    }

    public void newGame() {
        //Reset initialization and creat a new game
        int cell;
        startTime = System.currentTimeMillis();

        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    public void find_empty_cells(int j) {
        //If the next box is also empty, click together
        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }

    public void showGameOverDialog() {
    
        gameTime = System.currentTimeMillis() - startTime;
        long gameTimeInMinutes = gameTime /  (1000 * 60);

        // Display a dialog with the elapsed time
        String message = "Elapsed Time: " + gameTimeInMinutes + " minutes";
    
        // Create an array of button labels
       String[] options = {"Play Again","Selection Difficulty","Exit Game"};

        // Show the option dialog
        int choice = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Handle the user's choice
        switch (choice) {
            case 0: // "Play Again" button
                newGame(); // Start a new game
                break;
            case 1: 
                minesweeper.showDifficultySelection();
                break;
            case 2: 
                System.exit(0); 
                break;
            default:
                break;
        }
    }

    public void setDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case BEGINNER:
                setDifficultyParameters(9, 9, 10);
                break;
            case INTERMEDIATE:
                setDifficultyParameters(16, 16, 40);
                break;
            case ADVANCED:
                setDifficultyParameters(25, 25, 65);
                break;
        }
    }

    public void setDifficultyParameters(int rows, int cols, int mines) {
        N_ROWS = rows;
        N_COLS = cols;
        N_MINES = mines;
        BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
        BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;
        initBoard();
        newGame();
    }

    public enum Difficulty {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {

                    inGame = false;
                }

                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            statusbar.setText("You Win!");
            showGameOverDialog();

        } else if (!inGame) {

            statusbar.setText("Game Over!");
        }
    }

}