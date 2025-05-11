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
    private final double imageWidth = 30;
    private final double imageHeight = 35;
    private final Random random = new Random();
    private final Pane gamePane;
    private Timeline lifetimeTimer;

    public Food(Pane gamePane) {
        this.gamePane = gamePane;
        foodObject = new ImageView();
        foodObject.setFitWidth(imageWidth);
        foodObject.setFitHeight(imageHeight);
        foodObject.setPreserveRatio(false);

        // Load image
        try {
            Image image = new Image(getClass().getResource("/img/apple.png").toExternalForm());
            foodObject.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading food image!");
        }


        gamePane.getChildren().add(foodObject);
        relocate();
    }

    public void relocate() {
        if (lifetimeTimer != null) {
            lifetimeTimer.stop();
        }

        double FmaxX = gamePane.getWidth() - imageWidth;
        double FmaxY = gamePane.getHeight() - imageHeight;

        // Ensure max coordinates are not negative if pane is smaller than food
        // Minor bug handling to avoid it
        FmaxX = Math.max(0, FmaxX);
        FmaxY = Math.max(0, FmaxY);

        foodObject.setLayoutX(random.nextDouble() * FmaxX);
        foodObject.setLayoutY(random.nextDouble() * FmaxY);

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

    // Renamed from getAppleImage to getFoodObject for readability
    public ImageView getFoodObject() {
        return foodObject;
    }

    public double getX() {
        return foodObject.getLayoutX();
    }

    public double getY() {
        return foodObject.getLayoutY();
    }

    // Returns the actual displayed width
    public double getWidth() {
        return imageWidth; // As set by setFitWidth
    }

    // Returns the actual displayed height
    public double getHeight() {
        return imageHeight; // As set by setFitHeight
    }
}