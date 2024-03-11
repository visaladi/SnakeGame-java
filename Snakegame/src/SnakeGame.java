import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int DELAY = 100;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    private static final int X[] = new int[GAME_UNITS];
    private static final int Y[] = new int[GAME_UNITS];
    private static int bodyParts = 6;
    private static int applesEaten = 0;
    private static int appleX;
    private static int appleY;
    private static int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static int dirIndex = 0;
    private static boolean running = false;
    private static boolean gameOver = false;
    private static Random random;

    public SnakeGame() {
        super("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && dirIndex != 2) {
                    dirIndex = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && dirIndex != 3) {
                    dirIndex = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && dirIndex != 0) {
                    dirIndex = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT && dirIndex != 1) {
                    dirIndex = 3;
                }
            }
        });

        random = new Random();
        startGame();
    }

    public void startGame() {
        new Apple();
        new Snake();
        running = true;
        gameLoop();
    }

    public void gameLoop() {
        while (running) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!gameOver) {
                checkApple();
                checkCollisions();
                moveSnake();
            }
            repaint();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", 250, 200);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Play Again", 230, 250);
        } else {
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(X[i] * UNIT_SIZE, Y[i] * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX * UNIT_SIZE, appleY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
    }

    public static class Apple {
        public Apple() {
            appleX= random.nextInt(32);
            appleY = random.nextInt(24);
            appleX *= UNIT_SIZE;
            appleY *= UNIT_SIZE;
        }
    }

    public static class Snake extends Thread {
        public Snake() {
            start();
        }

        public void run() {
            while (running) {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = bodyParts; i > 0; i--) {
                    X[i] = X[i - 1];
                    Y[i] = Y[i - 1];
                }
                X[0] += dirs[dirIndex][0] * UNIT_SIZE;
                Y[0] += dirs[dirIndex][1] * UNIT_SIZE;
            }
        }
    }

    public void checkApple() {
        if (X[0] == appleX && Y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            new Apple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (X[0] == X[i] && Y[0] == Y[i]) {
                gameOver = true;
            }
        }
        if (X[0] < 0 || X[0] > WIDTH / UNIT_SIZE || Y[0] < 0 || Y[0] > HEIGHT / UNIT_SIZE) {
            gameOver = true;
        }
    }

    public void moveSnake() {
        for (int i = bodyParts; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }
        X[0] += dirs[dirIndex][0] * UNIT_SIZE;
        Y[0] += dirs[dirIndex][1] * UNIT_SIZE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}