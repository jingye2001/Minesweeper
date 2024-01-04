import javax.swing.JPanel;

// Define an abstract class that extends JPanel
public abstract class GameBoard extends JPanel {

    // Add abstract methods that each subclass should implement
    public abstract void initBoard();
    public abstract void newGame();
    public abstract void setDifficulty(Board.Difficulty difficulty);
    public abstract void showDifficultySelection();
}