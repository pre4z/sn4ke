package org.example.sn4ke.obj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

public class Food {
    private final ImageView imageView;
    private final double imageWidth = 30;
    private final double imageHeight = 35;
    private final Random random = new Random();
    private final Pane gamePane;
    private Timeline lifetimeTimer;

    public Food(Pane gamePane) {
        this.gamePane = gamePane;
        imageView = new ImageView();
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(false);

        // Load image
        Image image = new Image(getClass().getResource("/img/apple.png").toExternalForm());
        imageView.setImage(image);

        gamePane.getChildren().add(imageView);
        relocate();
    }

    public void relocate() {
        // Cancel any running timer
        if (lifetimeTimer != null) {
            lifetimeTimer.stop();
        }

        // Place the apple at a new random location
        double maxX = gamePane.getWidth() - imageWidth;
        double maxY = gamePane.getHeight() - imageHeight;
        imageView.setLayoutX(random.nextDouble() * maxX);
        imageView.setLayoutY(random.nextDouble() * maxY);

        // auto respawn
        lifetimeTimer = new Timeline(new KeyFrame(Duration.seconds(6), e -> relocate()));
        lifetimeTimer.setCycleCount(1);
        lifetimeTimer.play();
    }


    public ImageView getAppleImage() {
        return imageView;
    }
}
