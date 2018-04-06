package lern;

import org.tensorflow.Tensor;
import snek.Direction;
import snek.PixelType;
import snek.Snake;

import java.nio.IntBuffer;

public class LernSnek {
    public static void main(String[] args) {
        Snake s = new Snake(20, 20, 5, Direction.RIGHT);
        PixelType[][] snakeData = s.pixels();
        IntBuffer ib = IntBuffer.allocate(snakeData.length * snakeData[0].length);
        for (PixelType[] pts : snakeData) {
            for (PixelType pt : pts) {
                ib.put(pt.code);
            }
        }
        ib.position(0);
        try (Tensor t = Tensor.create(new long[]{snakeData.length, snakeData[0].length}, ib)) {
            System.out.println(t);
        }
    }
}
