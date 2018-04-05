import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Based on: https://github.com/hexadeciman/Snake/

class Game extends JFrame {

    private static final long serialVersionUID = -2542001418764869760L;
    private static int width = 20;
    private static int height = 20;
    private Snake snake;
    private boolean moveLock;

    public Game() {

        int len = 5;

        Thread t = new Thread(() -> {
            getContentPane().setLayout(new GridLayout(height, width, 0, 0));

            snake = new Snake(width, height, len, Direction.RIGHT);

            // Creates the array that'll contain the threads
            PixelType[][] grid = snake.pixels();

            // Start & pauses all threads, then adds every square of each thread to the panel
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    getContentPane().add(new SquarePanel(grid[i][j].color));
                }
            }
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                snake = new Snake(width, height, len, Direction.RIGHT);

                while (snake.move()) {
                    moveLock = false;
                    setTitle("Score: " + (snake.length - len));
                    // Creates the array that'll contain the threads
                    grid = snake.pixels();

                    // Start & pauses all threads, then adds every square of each thread to the panel
                    int c = 0;
                    for (int i = height - 1; i >= 0; i--) {
                        for (int j = 0; j < width; j++) {
                            SquarePanel sp = (SquarePanel) getContentPane().getComponent(c);
                            sp.ChangeColor(grid[i][j].color);
                            c++;
                        }
                    }
                    validate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (moveLock) return;
                t.resume();
                moveLock = true;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        snake.setDirection(Direction.LEFT);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        snake.setDirection(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        snake.setDirection(Direction.UP);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        snake.setDirection(Direction.DOWN);
                        break;
                    case KeyEvent.VK_P:
                        t.suspend();
                        snake.debug();
                        snake.setDirection(Direction.NONE);
                        moveLock = false;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        addKeyListener(kl);

        t.start();
    }
}


