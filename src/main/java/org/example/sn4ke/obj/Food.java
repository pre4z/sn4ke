package org.example.sn4ke.obj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

public class Food {
    private final ImageView foodObject;
    private final int imageSize;
    private final Random random = new Random();
    private Timeline lifetimeTimer;

    // Add grid dimensions and tileSize to constructor
    private final int gridWidth;
    private final int gridHeight;
    private final int tileSize;

    // Current grid position of the food
    private int currentGridX;
    private int currentGridY;

    public Food(Pane gamePane, int gridWidth, int gridHeight, int tileSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.tileSize = tileSize;
        this.imageSize = tileSize; // Set image size to tile size so it fits

        foodObject = new ImageView();
        foodObject.setFitWidth(imageSize);
        foodObject.setFitHeight(imageSize);
        foodObject.setPreserveRatio(true); // Preserve ratio to prevent distortion if not square AKA THE APPLE STAYS AN APPLE

        // Load image
        try {
            Image image = new Image(getClass().getResource("/img/apple.png").toExternalForm());
            foodObject.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading food image!");
        }

        gamePane.getChildren().add(foodObject);
        relocate(); // Call relocate to set initial position
    }

    public void relocate() {
        if (lifetimeTimer != null) {
            lifetimeTimer.stop();
        }

        // Generate random grid coordinates (0 to gridWidth-1, 0 to gridHeight-1)
        currentGridX = random.nextInt(gridWidth);
        currentGridY = random.nextInt(gridHeight);

        // Set layout based on grid coordinates multiplied by tileSize
        // This places the food image exactly at the top-left corner of a grid cell
        foodObject.setLayoutX(currentGridX * tileSize);
        foodObject.setLayoutY(currentGridY * tileSize);

        // Reset and start the lifetime timer for the new food position
        lifetimeTimer = new Timeline(new KeyFrame(Duration.seconds(6), e -> relocate()));
        lifetimeTimer.setCycleCount(1);
        lifetimeTimer.play();
    }

    /**
     * Called when the snake eats the food.
     * Stops the current timer and relocates the food immediately.
     */
    public void wasEaten() {
        if (lifetimeTimer != null) {
            lifetimeTimer.stop();
        }
        relocate(); // Relocate immediately
    }

    public ImageView getFoodObject() {
        return foodObject;
    }

    // Return the food's grid coordinates directly for collision detection
    public int getGridX() {
        return currentGridX;
    }

    public int getGridY() {
        return currentGridY;
    }
}