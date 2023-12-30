import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Minesweeper extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Board board;
    private JLabel statusbar;
    
    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.NORTH);

        // Create a panel for difficulty selection
        JPanel difficultyPanel = createDifficultyPanel();

        // Add the difficulty panel to the card panel
        cardPanel.add(difficultyPanel, "difficulty");

        // Add the card panel to the main frame
        add(cardPanel, BorderLayout.CENTER);

        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createDifficultyPanel() {
        JPanel difficultyPanel = new JPanel();
        JButton beginnerButton = new JButton("Beginner");
        JButton intermediateButton = new JButton("Intermediate");
        JButton advancedButton = new JButton("Advanced");

        beginnerButton.addActionListener(e -> setDifficulty(Board.Difficulty.BEGINNER));
        intermediateButton.addActionListener(e -> setDifficulty(Board.Difficulty.INTERMEDIATE));
        advancedButton.addActionListener(e -> setDifficulty(Board.Difficulty.ADVANCED));

        difficultyPanel.add(beginnerButton);
        difficultyPanel.add(intermediateButton);
        difficultyPanel.add(advancedButton);

        return difficultyPanel;
    }

    private void setDifficulty(Board.Difficulty difficulty) {
        if (board != null) {
            cardPanel.remove(board);
        }
    
        // Create a new Board with the selected difficulty
        board = new Board(statusbar, this);
        board.setDifficulty(difficulty);
    
        // Add the Board to the card panel
        cardPanel.add(board, "board");
    
        // Switch to the Board view
        cardLayout.show(cardPanel, "board");
    
        // Re-pack the frame to adjust to the size of the new board
        pack();
        // Update the frame so that it can display the board correctly
        setLocationRelativeTo(null);
    }    

    public void showDifficultySelection() {
        cardLayout.show(cardPanel, "difficulty");
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
