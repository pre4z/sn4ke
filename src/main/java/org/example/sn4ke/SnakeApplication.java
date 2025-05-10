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
import org.example.sn4ke.obj.Food;
import org.example.sn4ke.obj.Snake;
import org.example.sn4ke.obj.SnakeEye;
import org.example.sn4ke.obj.SnakeSkin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnakeApplication extends Application {
    private Snake snake;
    private Food food;
    private final Text gameOverDisplay = new Text();
    private final Text restartPrompt = new Text();

    private Pane gamePane = new Pane();
    private final List<Circle> snakeParts = new ArrayList<>();

    private final int tileSize = 20;
    private final int width = 500;
    private final int height = 500;

    private AnimationTimer gameLoopInstance;
    private Scene currentScene;

    @Override
    public void start(Stage stage) throws IOException {
        configureGameOverTexts(); // Sets styles

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

        gamePane.getChildren().clear();
        snakeParts.clear();

        snake = new Snake(width / tileSize, height / tileSize);
        snake.setViewPos(SnakeEye.DOWN);

        // Add text nodes to the scene. startGameLoop handles content and positioning.
        gamePane.getChildren().addAll(gameOverDisplay, restartPrompt);

        renderSnake();
        startGameLoop(currentScene);
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (snake == null) return; // Prevent NullPointerException if snake isn't initialized
            if (!gameOverDisplay.isVisible()) {
                KeyCode code = event.getCode();
                if (code == KeyCode.UP || code == KeyCode.W) snake.setViewPos(SnakeEye.UP);
                else if (code == KeyCode.DOWN || code == KeyCode.S) snake.setViewPos(SnakeEye.DOWN);
                else if (code == KeyCode.LEFT || code == KeyCode.A) snake.setViewPos(SnakeEye.LEFT);
                else if (code == KeyCode.RIGHT || code == KeyCode.D) snake.setViewPos(SnakeEye.RIGHT);
            } else { // Game is over (gameOverDisplay is visible)
                // Any key press triggers a restart
                resetAndStartGame();
            }
        });
    }

    private void renderSnake() {
        gamePane.getChildren().removeAll(snakeParts); // Remove old snake visuals
        snakeParts.clear();

        if (snake == null || snake.getLength() == null) return;

        for (int i = 0; i < snake.getLength().size(); i++) {
            SnakeSkin s = snake.getLength().get(i);
            double radius = (i == 0 ? tileSize / 1.2 : tileSize / 2.0);
            Circle c = new Circle(radius, Color.BLUEVIOLET);
            c.setLayoutX(s.getX() * tileSize + tileSize / 2.0);
            c.setLayoutY(s.getY() * tileSize + tileSize / 2.0);
            snakeParts.add(c);
        }
        gamePane.getChildren().addAll(snakeParts);
    }

    private void startGameLoop(Scene scene) {
        gameOverDisplay.setVisible(false); // Ensure game over messages are hidden
        restartPrompt.setVisible(false);
        setupControls(scene);

        // <editor-fold desc="Game Over Display Block + restart prompt: Set Text and Position">

        // Set text content for gameOverDisplay
        gameOverDisplay.setText("GAME OVER");

        gameOverDisplay.applyCss();
        gameOverDisplay.autosize();
        double textWidth = gameOverDisplay.getBoundsInLocal().getWidth();
        double textHeight = gameOverDisplay.getBoundsInLocal().getHeight();
        gameOverDisplay.setLayoutX((width - textWidth) / 2);
        gameOverDisplay.setLayoutY((height / 2.0) - textHeight);

        // Set text content for restartPrompt
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

            @Override
            public void handle(long now) {
                if (gameOverDisplay.isVisible() && this != gameLoopInstance) {
                    this.stop();
                    return;
                }

                if (now - lastUpdate >= 200_000_000) {
                    if (snake == null) return;

                    boolean hitWall = snake.move();

                    if (hitWall) {
                        this.stop();
                        gamePane.getChildren().removeAll(snakeParts);
                        snakeParts.clear();
                        gameOverDisplay.setVisible(true);
                        restartPrompt.setVisible(true);
                        return;
                    }
                    renderSnake();
                    lastUpdate = now;
                }
            }
        };
        gameLoopInstance.start();
    }
}