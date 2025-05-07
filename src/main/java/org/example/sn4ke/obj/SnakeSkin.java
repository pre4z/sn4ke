package org.example.sn4ke.obj;

public class SnakeSkin {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * places snakeskin at the desired coordinates
     * @param x xPos
     * @param y yPos
     */
    public SnakeSkin(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
