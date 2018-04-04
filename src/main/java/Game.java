import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// From: https://github.com/hexadeciman/Snake/

class Game extends JFrame {
    private static final long serialVersionUID = -2542001418764869760L;
    private static int width = 20;
    private static int height = 20;
    private Snake snake;

    public Game() {

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        snake.setDirection(Direction.LEFT);
                        break;
                    case KeyEvent.VK_D:
                        snake.setDirection(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_W:
                        snake.setDirection(Direction.UP);
                        break;
                    case KeyEvent.VK_S:
                        snake.setDirection(Direction.DOWN);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        addKeyListener(kl);

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                snake = new Snake(width, height, 5, Direction.RIGHT);
                getContentPane().setLayout(new GridLayout(height, width, 0, 0));

                // Creates the array that'll contain the threads
                PixelType[][] grid = snake.pixels();

                // Start & pauses all threads, then adds every square of each thread to the panel
                for (int i = height - 1; i >= 0; i--) {
                    for (int j = 0; j < width; j++) {
                        getContentPane().add(new SquarePanel(grid[i][j].color));
                    }
                }

                while (snake.move()) {

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
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}


