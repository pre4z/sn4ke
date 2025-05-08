package org.example.sn4ke.obj;

import java.util.Random;

public class Food {
    private int x;
    private int y;
    private int boardWidth;
    private int boardHeight;
    private Random random = new Random();

    public Food(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        spawn();
    }

    public void spawn() {
        x = random.nextInt(boardWidth);
        y = random.nextInt(boardHeight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
