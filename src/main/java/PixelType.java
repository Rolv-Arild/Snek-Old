import java.awt.*;

public enum PixelType {
    SNAKE(Color.GREEN), FOOD(Color.RED), EMPTY(Color.BLACK), HEAD(new Color(0, 200, 0));

    Color color;

    PixelType(Color color) {
        this.color = color;
    }
}