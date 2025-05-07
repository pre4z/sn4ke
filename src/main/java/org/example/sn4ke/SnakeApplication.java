package org.example.sn4ke;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.sn4ke.obj.Snake;

import java.io.IOException;

/**
 * Main class
 */


public class SnakeApplication extends Application {

    private Snake snake = new Snake();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnakeApplication.class.getResource("hello-view.fxml"));


        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        scene.setFill(Color.BLACK);

        stage.setTitle("SN4KE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void startSnake() {

    }
}