public enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE;

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case NONE:
                return NONE;
        }
        throw new IllegalStateException();
    }
}
