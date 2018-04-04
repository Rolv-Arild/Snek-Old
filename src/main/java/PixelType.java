import java.awt.*;

public enum PixelType {
    SNAKE(Color.GREEN), FOOD(Color.RED), EMPTY(Color.BLACK);

    Color color;

    PixelType(Color color) {
        this.color = color;
    }
}