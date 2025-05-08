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

/**
 * Main class
 */


public class SnakeApplication extends Application {

    private Snake snake = new Snake();
    private Food food;
    private final Text gameOverDisplay = new Text();
    private Pane gamePane = new Pane();

    private final List<Circle> snakeParts = new ArrayList<>();

    private final int tileSize = 20;


    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(gamePane, 500, 700);
        scene.setFill(Color.BLACK);

        renderSnake(); // initial draw
        snake.setViewPos(SnakeEye.DOWN); // default starting direction

        startGameLoop(scene);

        stage.setTitle("SN4KE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (!gameOverDisplay.isVisible()) {
                KeyCode code = event.getCode();
                if (code == KeyCode.UP || code == KeyCode.W) snake.setViewPos(SnakeEye.UP);
                else if (code == KeyCode.DOWN || code == KeyCode.S) snake.setViewPos(SnakeEye.DOWN);
                else if (code == KeyCode.LEFT || code == KeyCode.A) snake.setViewPos(SnakeEye.LEFT);
                else if (code == KeyCode.RIGHT || code == KeyCode.D) snake.setViewPos(SnakeEye.RIGHT);
            }
        });
    }

    private void renderSnake() {
        gamePane.getChildren().removeAll(snakeParts);
        snakeParts.clear();

        for (int i = 0; i < snake.getLength().size(); i++) {
            SnakeSkin s = snake.getLength().get(i);

            double radius = (i == 0  ? tileSize / 1.2 : tileSize / 2.0);

            Circle c = new Circle(radius, Color.BLUEVIOLET); // youssef vil gerne ha at den er pink?
            c.setLayoutX(s.getX() * tileSize + tileSize / 2.0);
            c.setLayoutY(s.getY() * tileSize + tileSize / 2.0);

            snakeParts.add(c);
        }

        gamePane.getChildren().addAll(snakeParts);
    }

    private void startGameLoop(Scene scene) {
        gameOverDisplay.setVisible(false);
        setupControls(scene); // dis doesnt work youssef fix nu

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 200_000_000) { // 200ms :O
                    snake.move();
                    renderSnake();
                    lastUpdate = now;
                }
            }
        };

        gameLoop.start();
    }

}