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
                int[][] info2 = snake.info();

                while (snake.move()) {
                    moveLock = false;
                    setTitle("Score: " + (snake.length - len));
                    // Creates the array that'll contain the threads
                    int[][] info1 = snake.info();

                    updateBoard(info1, info2);

                    validate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    info2 = snake.info();
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

    private void updateBoard(int[][] info1, int[][] info2) {
        for (int i = 0; i < info2.length; i++) {
            if (info1[i][0] != info2[i][0] || info1[i][1] != info2[i][1]) {
                int x = info1[i][0];
                int y = info1[i][1];
                SquarePanel sp2 = (SquarePanel) getContentPane().getComponent((height - info2[i][1] - 1) * width + info2[i][0]);
                sp2.ChangeColor(PixelType.EMPTY.color); // reset the old color
                SquarePanel sp1 = (SquarePanel) getContentPane().getComponent((height - y - 1) * width + x);
                changeColor(i, sp1);
                i++;
            }
        }
        if (info1.length != info2.length) {
            int x = info1[info1.length - 1][0];
            int y = info1[info1.length - 1][0];
            SquarePanel sp1 = (SquarePanel) getContentPane().getComponent((height - y - 1) * width + x);
            changeColor(info1.length - 1, sp1);
        }
    }

    private void changeColor(int i, SquarePanel sp1) {
        switch (i) {
            case 0:  // food
                sp1.ChangeColor(PixelType.FOOD.color);
                break;
            case 1:  // head
                sp1.ChangeColor(PixelType.HEAD.color);
                break;
            default: // body
                sp1.ChangeColor(PixelType.SNAKE.color);
                break;
        }
    }
}


