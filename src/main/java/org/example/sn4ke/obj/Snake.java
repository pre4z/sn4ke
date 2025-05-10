package org.example.sn4ke.obj;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakeEye viewPos;
    private final List<SnakeSkin> length = new ArrayList<>();
    private long lastDirectionChangeTime = 0;

    private int width;
    private int height;

    public Snake(int width, int height) {
        this.length.add(new SnakeSkin(5, 5));
        this.viewPos = SnakeEye.RIGHT; // start direction
        this.width = width;
        this.height = height;
    }

    /**
     * Turns the snake
     * Returns true if the snake collides with a wall or itself
     */
    public boolean move() {
        int oldHeadX = length.get(0).getX();
        int oldHeadY = length.get(0).getY();

        int newHeadX = oldHeadX;
        int newHeadY = oldHeadY;

        switch (viewPos) {
            case UP:    newHeadY--; break;
            case DOWN:  newHeadY++; break;
            case LEFT:  newHeadX--; break;
            case RIGHT: newHeadX++; break;
        }

        // Check for wall collision
        if (newHeadX < 0 || newHeadX >= width || newHeadY < 0 || newHeadY >= height) {
            return true; // Hit wall
        }

        // Check for self-collision
        // Iterate through all segments to check if the new head position overlaps with any existing segment
        for (SnakeSkin snakeSkin : length) {
            if (newHeadX == snakeSkin.getX() && newHeadY == snakeSkin.getY()) {
                return true; // Hit self
            }
        }

        // Move snake
        length.add(0, new SnakeSkin(newHeadX, newHeadY));
        length.remove(length.size() - 1);

        return false; // No collision
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



