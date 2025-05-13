package org.example.sn4ke;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sn4ke.obj.*;
import java.util.ArrayList;
import java.util.List;

public class SnakeApplication extends Application {
    private Snake snake;
    private Food food;
    private final Text gameOverDisplay = new Text();
    private final Text restartPrompt = new Text();
    private final Text scoreText = new Text();

    private int gameScore;
    private int highScore;

    private Pane gamePane = new Pane();
    private final List<Circle> snakeParts = new ArrayList<>();
    private Canvas boardCanvas;

    private final int tileSize = 40;
    private final int width = 600;
    private final int height = 600;

    private final int gridWidth = width / tileSize; // calculates the playing board width
    private final int gridHeight = height / tileSize; // ditto - height

    private AnimationTimer gameLoopInstance;
    private Scene currentScene;

    @Override
    public void start(Stage stage) {
        configureGameOverTexts();
        configureScoreText();

        boardCanvas = new Canvas(width, height);
        drawBoardBackground(boardCanvas.getGraphicsContext2D());
        gamePane.getChildren().add(boardCanvas);

        currentScene = new Scene(gamePane, width, height);
        stage.setTitle("SN4KE");
        stage.setScene(currentScene);
        stage.show();

        resetAndStartGame();
    }

    private void configureGameOverTexts() {
        gameOverDisplay.setStyle("-fx-font-size: 32; -fx-font-weight: bold;");
        gameOverDisplay.setFill(Color.RED);

        restartPrompt.setStyle("-fx-font-size: 20;");
        restartPrompt.setFill(Color.LIGHTGRAY);
    }

    private void configureScoreText() {
        scoreText.setFont(Font.font("Arial", 20));
        scoreText.setFill(Color.WHITE);
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(25);
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        scoreText.setText("Score: " + gameScore + "\nHighscore: " + highScore);
        scoreText.setStyle("-fx-font-weight: bold");
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(25);
    }

    public static void main(String[] args) {
        launch();
    }

    private void resetAndStartGame() {
        if (gameLoopInstance != null) {
            gameLoopInstance.stop();
        }

        gamePane.getChildren().removeAll(snakeParts);
        if (food != null && food.getFoodObject() != null) {
            gamePane.getChildren().remove(food.getFoodObject());
        }
        snakeParts.clear();
        gameScore = 0;

        updateScoreDisplay();

        snake = new Snake(gridWidth, gridHeight);
        food = new Food(gamePane, gridWidth, gridHeight, tileSize);

        if (!gamePane.getChildren().contains(gameOverDisplay)) {
            gamePane.getChildren().add(gameOverDisplay);
        }
        if (!gamePane.getChildren().contains(restartPrompt)) {
            gamePane.getChildren().add(restartPrompt);
        }
        if (!gamePane.getChildren().contains(scoreText)) {
            gamePane.getChildren().add(scoreText);
        }
        scoreText.toFront();

        renderSnake();
        startGameLoop(currentScene);
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (snake == null) return;
            if (!gameOverDisplay.isVisible()) {
                KeyCode code = event.getCode();
                if (code == KeyCode.UP || code == KeyCode.W) snake.setViewPos(SnakeEye.UP);
                else if (code == KeyCode.DOWN || code == KeyCode.S) snake.setViewPos(SnakeEye.DOWN);
                else if (code == KeyCode.LEFT || code == KeyCode.A) snake.setViewPos(SnakeEye.LEFT);
                else if (code == KeyCode.RIGHT || code == KeyCode.D) snake.setViewPos(SnakeEye.RIGHT);
            } else {
                resetAndStartGame();
            }
        });
    }

    private void renderSnake() {
        gamePane.getChildren().removeAll(snakeParts);
        snakeParts.clear();

        if (snake == null || snake.getLength() == null) return;

        for (int i = 0; i < snake.getLength().size(); i++) {
            SnakeSkin s = snake.getLength().get(i);
            double radius = tileSize / 2.0;
            Circle c = new Circle(radius, Color.web("#4A00E0"));
            if (i == 0) {
                c.setFill(Color.web("#8E2DE2")); // different colour for head
            }
            c.setLayoutX(s.getX() * tileSize + tileSize / 2.0);
            c.setLayoutY(s.getY() * tileSize + tileSize / 2.0);
            snakeParts.add(c);
        }
        gamePane.getChildren().addAll(snakeParts);

        if (food != null && food.getFoodObject() != null) {
            food.getFoodObject().toFront(); // setting this to front to make sure it displays properly
        }
        scoreText.toFront(); // setting this to front to make sure it displays properly
        gameOverDisplay.toFront(); // setting this to front to make sure it displays properly
        restartPrompt.toFront(); // setting this to front to make sure it displays properly
    }

    private void drawBoardBackground(GraphicsContext gc) {
        Color lightColor = Color.web("#AAD751");
        Color darkColor = Color.web("#A2D149");

        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                if ((row + col) % 2 == 0) {
                    gc.setFill(lightColor);
                } else {
                    gc.setFill(darkColor);
                }
                gc.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    private void startGameLoop(Scene scene) {
        gameOverDisplay.setVisible(false);
        restartPrompt.setVisible(false);
        setupControls(scene);

        gameOverDisplay.setText("GAME OVER");
        gameOverDisplay.applyCss(); gameOverDisplay.autosize();
        gameOverDisplay.setLayoutX((width - gameOverDisplay.getBoundsInLocal().getWidth()) / 2);
        gameOverDisplay.setLayoutY((height / 2.0) - gameOverDisplay.getBoundsInLocal().getHeight());

        restartPrompt.setText("Press any key to restart");
        restartPrompt.applyCss(); restartPrompt.autosize();
        restartPrompt.setLayoutX((width - restartPrompt.getBoundsInLocal().getWidth()) / 2);
        restartPrompt.setLayoutY((height / 2.0) + restartPrompt.getBoundsInLocal().getHeight() / 4);

        gameLoopInstance = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long updateInterval = 100_000_000; // 100ms for 10 FPS

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= updateInterval) {
                    if (snake == null) return;

                    MoveResult result = snake.move(food);

                    switch (result) {
                        case ATE_FOOD:
                            gameScore++;
                            if (gameScore > highScore) {
                                highScore = gameScore;
                            }
                            updateScoreDisplay();
                            break;
                        case HIT_WALL:
                        case HIT_SELF:
                            this.stop();
                            gameOverDisplay.setVisible(true);
                            restartPrompt.setVisible(true);
                            gameOverDisplay.toFront();
                            restartPrompt.toFront();
                            return;
                        case MOVED_OKAY:
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