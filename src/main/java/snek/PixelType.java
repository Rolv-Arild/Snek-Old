package snek;

import java.awt.*;

public enum PixelType {
    EMPTY(Color.BLACK, 0), FOOD(Color.RED, 1), HEAD(new Color(0, 200, 0), 2), SNAKE(Color.GREEN, 3);

    public Color color;
    public int code;

    PixelType(Color color, int code) {
        this.color = color;
        this.code = code;
    }
}