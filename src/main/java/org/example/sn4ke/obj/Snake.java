package org.example.sn4ke.obj;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakeEye viewPos;
    private final List<SnakeSkin> length = new ArrayList<>();
    private long lastDirectionChangeTime = 0;
    private final long directionChangeCooldown = 100; // ms, adjust as needed

    private final int gameGridWidth;  // Width of the game area in grid cells
    private final int gameGridHeight; // Height of the game area in grid cells
    private final int tileSize;       // Size of each grid cell in pixels

    public Snake(int gameGridWidth, int gameGridHeight, int tileSize) {
        this.gameGridWidth = gameGridWidth;
        this.gameGridHeight = gameGridHeight;
        this.tileSize = tileSize;

        // Initial snake position (e.g., center) and length
        int startX = gameGridWidth / 2;
        int startY = gameGridHeight / 2;
        this.length.add(new SnakeSkin(startX, startY)); // Head
        this.length.add(new SnakeSkin(startX - 1, startY)); // Second segment
        this.viewPos = SnakeEye.RIGHT;
    }

    /**
     * Moves the snake, checks for collisions (wall, self, food), and handles growth.
     * @param food The food object to check against.
     * @return MoveResult indicating the outcome.
     */
    public MoveResult move(Food food) {
        if (length.isEmpty()) {
            return MoveResult.HIT_SELF; // Should not happen
        }

        SnakeSkin currentHead = length.get(0);
        int newHeadX = currentHead.getX();
        int newHeadY = currentHead.getY();

        switch (viewPos) {
            case UP:    newHeadY--; break;
            case DOWN:  newHeadY++; break;
            case LEFT:  newHeadX--; break;
            case RIGHT: newHeadX++; break;
        }

        // 1. Check for wall collision
        if (newHeadX < 0 || newHeadX >= gameGridWidth || newHeadY < 0 || newHeadY >= gameGridHeight) {
            return MoveResult.HIT_WALL;
        }

        // 2. Check for food collision
        // Determine the grid cell the food's center occupies
        double foodCenterX = food.getX() + food.getWidth() / 2.0;
        double foodCenterY = food.getY() + food.getHeight() / 2.0;

        int foodGridX = (int) (foodCenterX / tileSize);
        int foodGridY = (int) (foodCenterY / tileSize);

        boolean ateFood = (newHeadX == foodGridX && newHeadY == foodGridY);

        if (ateFood) {
            length.add(0, new SnakeSkin(newHeadX, newHeadY)); // Add new head
            food.wasEaten(); // Tell food it's eaten
            // Snake grows because we don't remove the tail
            return MoveResult.ATE_FOOD;
        }

        // 3. Check for self-collision (if not eating food)
        for (SnakeSkin segment : length) {
            if (newHeadX == segment.getX() && newHeadY == segment.getY()) {
                return MoveResult.HIT_SELF;
            }
        }

        // 4. Move snake normally (add new head, remove tail)
        length.add(0, new SnakeSkin(newHeadX, newHeadY));
        length.remove(length.size() - 1);

        return MoveResult.MOVED_OKAY;
    }

    public void setViewPos(SnakeEye newDirection) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDirectionChangeTime < directionChangeCooldown) { // Use defined cooldown
            return;
        }
        // Prevent immediate 180-degree turns
        if (!isOppositeDirection(this.viewPos, newDirection)) {
            this.viewPos = newDirection;
            lastDirectionChangeTime = currentTime;
        }
    }

    private boolean isOppositeDirection(SnakeEye dir1, SnakeEye dir2) {
        if (dir1 == null || dir2 == null) return false;
        return (dir1 == SnakeEye.UP && dir2 == SnakeEye.DOWN) ||
                (dir1 == SnakeEye.DOWN && dir2 == SnakeEye.UP) ||
                (dir1 == SnakeEye.LEFT && dir2 == SnakeEye.RIGHT) ||
                (dir1 == SnakeEye.RIGHT && dir2 == SnakeEye.LEFT);
    }

    public List<SnakeSkin> getLength() {
        return length;
    }
}