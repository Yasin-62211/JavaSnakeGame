
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS =
            (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    static final int DELAY = 100;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int foodX;
    int foodY;
    int score = 0;

    char direction = 'R';

    boolean running = false;

    Timer timer;

    public GamePanel() {

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        startGame();
    }

    // Start the game
    public void startGame() {

        bodyParts = 6;
        score = 0;
        direction = 'R';

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 150 - (i * UNIT_SIZE);
            y[i] = 150;
        }

        newFood();

        running = true;

        timer = new Timer(DELAY, this);
        timer.start();

        requestFocusInWindow();
    }

    // Generate food
    public void newFood() {

        boolean validPosition = false;

        while (!validPosition) {

            foodX = (int) (Math.random() *
                    (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;

            foodY = (int) (Math.random() *
                    (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            validPosition = true;

            // Make sure food does not appear inside snake
            for (int i = 0; i < bodyParts; i++) {

                if (foodX == x[i] && foodY == y[i]) {
                    validPosition = false;
                    break;
                }
            }
        }
    }

    // Draw game
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Food
        g.setColor(Color.RED);
        g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

        // Snake
        for (int i = 0; i < bodyParts; i++) {

            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(45, 180, 45));
            }

            g.fillRect(
                    x[i],
                    y[i],
                    UNIT_SIZE,
                    UNIT_SIZE
            );
        }

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString(
                "Score: " + score,
                10,
                25
        );

        // Game Over
        if (!running) {

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));

            String gameOver = "GAME OVER";

            FontMetrics metrics = getFontMetrics(g.getFont());

            g.drawString(
                    gameOver,
                    (SCREEN_WIDTH - metrics.stringWidth(gameOver)) / 2,
                    SCREEN_HEIGHT / 2
            );

            // Final score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));

            String finalScore = "Final Score: " + score;

            metrics = getFontMetrics(g.getFont());

            g.drawString(
                    finalScore,
                    (SCREEN_WIDTH - metrics.stringWidth(finalScore)) / 2,
                    SCREEN_HEIGHT / 2 + 45
            );

            // Restart
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            String restart = "Press R to Restart";

            metrics = getFontMetrics(g.getFont());

            g.drawString(
                    restart,
                    (SCREEN_WIDTH - metrics.stringWidth(restart)) / 2,
                    SCREEN_HEIGHT / 2 + 85
            );
        }
    }

    // Move snake
    public void move() {

        for (int i = bodyParts - 1; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] -= UNIT_SIZE;
                break;

            case 'D':
                y[0] += UNIT_SIZE;
                break;

            case 'L':
                x[0] -= UNIT_SIZE;
                break;

            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    // Check food
    public void checkFood() {

        if (x[0] == foodX && y[0] == foodY) {

            bodyParts++;
            score++;

            newFood();
        }
    }

    // Check collisions
    public void checkCollisions() {

        // Check body collision
        for (int i = 1; i < bodyParts; i++) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;
            }
        }

        // Check walls
        if (x[0] < 0 ||
                x[0] >= SCREEN_WIDTH ||
                y[0] < 0 ||
                y[0] >= SCREEN_HEIGHT) {

            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    // Restart game
    public void restartGame() {

        if (timer != null) {
            timer.stop();
        }

        startGame();
        repaint();
    }

    // Game loop
    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkFood();
            checkCollisions();
        }

        repaint();
    }

    // Keyboard controls
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        // Restart
        if (key == KeyEvent.VK_R && !running) {

            restartGame();
            return;
        }

        // Left
        if (key == KeyEvent.VK_LEFT &&
                direction != 'R') {

            direction = 'L';
        }

        // Right
        else if (key == KeyEvent.VK_RIGHT &&
                direction != 'L') {

            direction = 'R';
        }

        // Up
        else if (key == KeyEvent.VK_UP &&
                direction != 'D') {

            direction = 'U';
        }

        // Down
        else if (key == KeyEvent.VK_DOWN &&
                direction != 'U') {

            direction = 'D';
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}