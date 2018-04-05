import java.util.Arrays;
import java.util.Random;

public class Snake {

    private class Segment {
        int x;
        int y;
        Segment next;
        Segment prev;

        Segment(int x, int y, Segment next, Segment prev) {
            this.x = x;
            this.y = y;
            this.next = next;
            this.prev = prev;
        }

    }

    private final int width;
    private final int height;
    public int length;
    private Segment head;
    private Segment tail;
    private Direction heading;
    private static final Random foodRandom = new Random();
    private int foodx;
    private int foody;
    private int moveCount;

    public Snake(int width, int height, int length, Direction heading) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.heading = heading;
        this.head = new Segment(length, 0, null, null);
        Segment n = head;
        for (int i = 1; i < length; i++) {
            n.next = new Segment(length - i, 0, null, n);
            n = n.next;
        }
        this.tail = n;
        placeFood();
    }

    public static void main(String[] args) {
        Snake s = new Snake(20, 20, 5, Direction.RIGHT);
        s.debug();
    }

    private void placeFood() {
        /*
        First picks a square from the board left to right, bottom to top,
        excluding the last length squares.
          */
        int f = foodRandom.nextInt(width * height - length);

        f = adjustedF(f, head, null); // adjust f to not be in snake

        this.foodx = f % width;
        this.foody = f / width;
    }

    private int adjustedF(int f, Segment from, Segment to) {
        int c = 0;
        // first check all snake segments (efficient)
        for (Segment n = from; n != to; n = n.next) {
            if (n.y * width + n.x <= f) {
                c++;
            }
        }
        // then check all potential snake segments between f and f+c (slower)
        for (int i = f + 1; i <= f + c; i++) {
            int x = i % width;
            int y = i / width;
            if (isSnake(x, y)) c++;
        }
        return f + c;
    }

    // Checks if a coordinate is part of the snake
    private boolean isSnake(int x, int y) {
        for (Segment n = head; n != null; n = n.next) {
            if (x == n.x && y == n.y) return true;
        }
        return false;
    }

    /**
     * Moves the snake.
     *
     * @return false if this move kills the snake.
     */
    public boolean move() {
        int dx = 0;
        int dy = 0;
        switch (heading) {
            case UP:
                dx = 0;
                dy = 1;
                break;
            case DOWN:
                dx = 0;
                dy = -1;
                break;
            case LEFT:
                dx = -1;
                dy = 0;
                break;
            case RIGHT:
                dx = 1;
                dy = 0;
                break;
            case NONE:
                return true;
        }
        boolean isKill = false;
        if (head.x + dx >= width || head.y + dy >= height || head.x + dx < 0 || head.y + dy < 0) {
            isKill = true;
        }
        Segment n = tail;
        while (n != head) {
            n.x = n.prev.x;
            n.y = n.prev.y;
            if (n.x == head.x + dx && n.y == head.y + dy) {
                isKill = true;
            }
            n = n.prev;
        }
        head.x += dx;
        head.y += dy;
        if (head.x == foodx && head.y == foody) {
            length++;
            tail.next = new Segment(tail.x, tail.y, null, tail);
            placeFood();
            tail = tail.next;
        }
        if (!isKill) moveCount++;
        int c = 0;
        for (Segment a = head; a != null; a = a.next) {
            c++;
        }
        if (c != length) System.out.println("Error: " + c + ", " + length);
        return !isKill;
    }

    /**
     * Changes the direction the snake is heading
     *
     * @param direction the new direction
     */
    public void setDirection(Direction direction) {
        if (direction != this.heading.opposite()) {
            this.heading = direction;
        }
    }

    @Override
    public String toString() {
        char[][] c = new char[height][width];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                c[j][i] = '.';
            }
        }
        c[foody][foodx] = 'F';
        Segment n = head;
        while (n != null) {
            c[n.y][n.x] = 'S';
            n = n.next;
        }
        StringBuilder out = new StringBuilder();
        for (char[] chars : c) {
            out.append(new String(chars));
            out.append("\n");
        }
        return out.toString();
    }

    public int[][] info() {
        int[][] out = new int[length + 1][2];
        out[0][0] = foodx;
        out[0][1] = foody;
        int i = 1;
        for (Segment n = head; n != null; n = n.next) {
            out[i][0] = n.x;
            out[i][1] = n.y;
            i++;
        }
        return out;
    }

    /**
     * Returns an array of pixels containing the game information
     *
     * @return an array of pixels for the game
     */
    public PixelType[][] pixels() {
        PixelType[][] out = new PixelType[height][width];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                out[i][j] = PixelType.EMPTY;
            }
        }
        out[foody][foodx] = PixelType.FOOD;
        out[head.y][head.x] = PixelType.HEAD;
        Segment n = head.next;
        while (n != null) {
            out[n.y][n.x] = PixelType.SNAKE;
            n = n.next;
        }
        return out;
    }

    void debug() {
        int[][] c = new int[height][width];
        for (int i = 0; i < 10000000; i++) {
            placeFood();
            c[foody][foodx]++;
        }
        System.out.println(Arrays.deepToString(c));
    }
}
