package gui;

import snek.Direction;
import snek.PixelType;
import snek.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Based on: https://github.com/hexadeciman/Snake/

class Game extends JFrame {

    private static int width = 20; // board width in pixels
    private static int height = 20; // board height in pixels
    private Snake snake;
    private final int len = 5; // initial length of the snake
    private boolean moveLock; // locks the movement after a key press so the player can't turn on the spot
    private final Thread t = new Thread(() -> {
        getContentPane().setLayout(new GridLayout(height, width, 0, 0));

        int highscore = 0;
        snake = new Snake(width, height, len, Direction.RIGHT);

        // Creates the array that'll contain the threads
        PixelType[][] grid = snake.pixels();


        // Start & pauses all threads, then adds every square of each thread to the panel
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                getContentPane().add(new SquarePanel(grid[i][j].color));
            }
        }
        System.out.println("Starting...");
        while (isEnabled()) {
            sleep(1000);

            if (snake.length - len > highscore) highscore = snake.length - len;
            snake = new Snake(width, height, len, Direction.RIGHT);

            while (snake.move()) {
                moveLock = false;
                setTitle("Score: " + (snake.length - len) + ", Highscore: " + highscore);

                updateBoard();

                sleep(100);
            }
        }
    });

    public Game() {
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

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateBoard() {
        PixelType[][] grid = snake.pixels();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                SquarePanel sp = (SquarePanel) getContentPane().getComponent((height - i - 1) * width + j);
                if (!sp.getBackground().equals(grid[i][j].color)) {
                    sp.changeColor(grid[i][j].color);
                }
            }
        }

        validate();
    }
}


