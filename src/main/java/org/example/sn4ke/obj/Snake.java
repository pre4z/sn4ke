package org.example.sn4ke.obj;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakeEye viewPos;
    private final List<SnakeSkin> length = new ArrayList<>();
    private long lastDirectionChangeTime = 0;

    public Snake() {
        this.length.add(new SnakeSkin(5, 5));
        this.viewPos = SnakeEye.RIGHT; // start direction
    }

    public void move() {
        SnakeSkin head = length.get(0);
        int x = head.getX();
        int y = head.getY();

        switch (viewPos) {
            case UP:    y--; break;
            case DOWN:  y++; break;
            case LEFT:  x--; break;
            case RIGHT: x++; break;
        }

        // Add new head and remove tail
        length.add(0, new SnakeSkin(x, y));
        length.remove(length.size() - 1);
    }

    public void eat() {
        SnakeSkin tail = length.get(length.size() - 1);
        length.add(new SnakeSkin(tail.getX(), tail.getY()));
    }

    public SnakeEye getViewPos() {
        return viewPos;
    }

    public void setViewPos(SnakeEye newDirection) {
        long currentTime = System.currentTimeMillis();

        if (this.viewPos == null || currentTime - lastDirectionChangeTime >= 150) {
            if (!isOppositeDirection(this.viewPos, newDirection)) {
                this.viewPos = newDirection;
                lastDirectionChangeTime = currentTime;
            }
        }
    }


    private boolean isOppositeDirection(SnakeEye dir1, SnakeEye dir2) {
        return (dir1 == SnakeEye.UP && dir2 == SnakeEye.DOWN) ||
                (dir1 == SnakeEye.DOWN && dir2 == SnakeEye.UP) ||
                (dir1 == SnakeEye.LEFT && dir2 == SnakeEye.RIGHT) ||
                (dir1 == SnakeEye.RIGHT && dir2 == SnakeEye.LEFT);
    }

    public List<SnakeSkin> getLength() {
        return length;
    }

    }



