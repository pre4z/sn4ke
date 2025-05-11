package org.example.sn4ke;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sn4ke.obj.*; // Import all from obj package
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnakeApplication extends Application {
    private Snake snake;
    private Food food;
    private final Text gameOverDisplay = new Text();
    private final Text restartPrompt = new Text();

    private int gameScore;

    private Pane gamePane = new Pane();
    private final List<Circle> snakeParts = new ArrayList<>();

    private final int tileSize = 20; // This is grid cell size
    private final int width = 500;   // Pixel width of game area
    private final int height = 500;  // Pixel height of game area

    // Calculate grid dimensions based on pane size and tile size
    private final int gridWidth = width / tileSize;
    private final int gridHeight = height / tileSize;

    private AnimationTimer gameLoopInstance;
    private Scene currentScene;

    @Override
    public void start(Stage stage) throws IOException {
        configureGameOverTexts();

        currentScene = new Scene(gamePane, width, height);
        currentScene.setFill(Color.BLACK);

        stage.setTitle("SN4KE");
        stage.setScene(currentScene);
        stage.show();

        resetAndStartGame();
    }

    private void configureGameOverTexts() {
        gameOverDisplay.setStyle("-fx-font-size: 32;");
        gameOverDisplay.setFill(Color.RED);

        restartPrompt.setStyle("-fx-font-size: 20;");
        restartPrompt.setFill(Color.GRAY);
    }

    public static void main(String[] args) {
        launch();
    }

    private void resetAndStartGame() {
        if (gameLoopInstance != null) {
            gameLoopInstance.stop();
        }

        gamePane.getChildren().clear(); // Clear everything from pane
        snakeParts.clear();
        gameScore = 0; // Reset score

        // Initialize Snake with grid dimensions and tileSize
        snake = new Snake(gridWidth, gridHeight, tileSize);

        // Initialize Food (it adds itself to gamePane)
        food = new Food(gamePane);

        // Add text nodes back (they were cleared)
        gamePane.getChildren().addAll(gameOverDisplay, restartPrompt);

        renderSnake(); // Initial render
        startGameLoop(currentScene);
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (snake == null) return;
            if (!gameOverDisplay.isVisible()) { // Game is active
                KeyCode code = event.getCode();
                if (code == KeyCode.UP || code == KeyCode.W) snake.setViewPos(SnakeEye.UP);
                else if (code == KeyCode.DOWN || code == KeyCode.S) snake.setViewPos(SnakeEye.DOWN);
                else if (code == KeyCode.LEFT || code == KeyCode.A) snake.setViewPos(SnakeEye.LEFT);
                else if (code == KeyCode.RIGHT || code == KeyCode.D) snake.setViewPos(SnakeEye.RIGHT);
            } else { // Game is over
                resetAndStartGame(); // Any key press restarts
            }
        });
    }

    private void renderSnake() {
        gamePane.getChildren().removeAll(snakeParts);
        snakeParts.clear();

        if (snake == null || snake.getLength() == null) return;

        for (int i = 0; i < snake.getLength().size(); i++) {
            SnakeSkin s = snake.getLength().get(i);
            double radius = (i == 0 ? tileSize / 1.2 : tileSize / 2.0); // Head slightly bigger
            Circle c = new Circle(radius, Color.BLUEVIOLET);
            // Center the circle in the tile
            c.setLayoutX(s.getX() * tileSize + tileSize / 2.0);
            c.setLayoutY(s.getY() * tileSize + tileSize / 2.0);
            snakeParts.add(c);
        }
        gamePane.getChildren().addAll(snakeParts);
        // Ensure food is drawn on top of snake parts if there's overlap
        if (food != null && food.getFoodObject() != null) {
            food.getFoodObject().toFront();
        }
    }

    private void startGameLoop(Scene scene) {
        gameOverDisplay.setVisible(false);
        restartPrompt.setVisible(false);
        setupControls(scene); // Ensure controls are set for the current scene

        // <editor-fold desc="Game Over Display Block + restart prompt: Set Text and Position">
        gameOverDisplay.setText("GAME OVER");
        gameOverDisplay.applyCss();
        gameOverDisplay.autosize();
        double textWidth = gameOverDisplay.getBoundsInLocal().getWidth();
        double textHeight = gameOverDisplay.getBoundsInLocal().getHeight();
        gameOverDisplay.setLayoutX((width - textWidth) / 2);
        gameOverDisplay.setLayoutY((height / 2.0) - textHeight);

        restartPrompt.setText("Press any key to restart");
        restartPrompt.applyCss();
        restartPrompt.autosize();
        double restartWidth = restartPrompt.getBoundsInLocal().getWidth();
        double restartHeight = restartPrompt.getBoundsInLocal().getHeight();
        restartPrompt.setLayoutX((width - restartWidth) / 2);
        restartPrompt.setLayoutY((height / 2.0) + restartHeight / 4);
        // </editor-fold>

        gameLoopInstance = new AnimationTimer() {
            private long lastUpdate = 0;
            // Target frame rate: e.g., 100_000_000 for 10 FPS (100ms delay), 200_000_000 for 5 FPS (200ms)
            private final long updateInterval = 100_000_000; // Trying for 10 FPS

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= updateInterval) { // Time-based update
                    if (snake == null) return; // Should not happen if resetAndStartGame is correct but is here in case

                    // Move snake and get result
                    MoveResult result = snake.move(food);

                    switch (result) {
                        case ATE_FOOD:
                            gameScore++;
                            // Growth and food relocation are handled by Snake and Food classes
                            System.out.println("Score: " + gameScore); // Print score for now
                            break;
                        case HIT_WALL:
                        case HIT_SELF:
                            this.stop(); // Stop this timer instance
                            gameOverDisplay.setVisible(true);
                            restartPrompt.setVisible(true);
                            return; // Exit handle method for this frame
                        case MOVED_OKAY:
                            // All good, proceed to render
                            break;
                    }

                    renderSnake();
                    lastUpdate = now;
                }
            }
        };
        gameLoopInstance.start();
    }
}