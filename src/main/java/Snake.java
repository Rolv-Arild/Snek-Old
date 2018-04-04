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
    private int length;
    private Segment head;
    private Segment tail;
    private Direction heading;
    private static final Random foodRandom = new Random();
    private int foodx;
    private int foody;

    public Snake(int width, int height, int length, Direction heading) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.heading = heading;
        this.head = new Segment(length, 0, null, null);
        Segment n = head;
        for (int i = 0; i < length; i++) {
            n.next = new Segment(length - i, 0, null, n);
            n = n.next;
        }
        this.tail = n;
        placeFood();
    }

    private void placeFood() {
        this.foodx = foodRandom.nextInt(width);
        this.foody = foodRandom.nextInt(height);
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
        return !isKill;
    }

    public void setDirection(Direction direction) {
        if (direction != this.heading.opposite()) {
            this.heading = direction;
        }
    }

    public PixelType[][] pixels() {
        PixelType[][] out = new PixelType[height][width];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[i].length; j++) {
                out[j][i] = PixelType.EMPTY;
            }
        }
        out[foody][foodx] = PixelType.FOOD;
        Segment n = head;
        while (n != null) {
            out[n.y][n.x] = PixelType.SNAKE;
            n = n.next;
        }
        return out;
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

    public static void main(String[] args) throws InterruptedException {
        Snake s = new Snake(10, 10, 5, Direction.RIGHT);
        s.pixels();
        do {
            System.out.print("\r\r\r\r\r\r\r\r\r\r");
            System.out.print(s);
            Thread.sleep(500);
        } while (s.move());
    }
}
