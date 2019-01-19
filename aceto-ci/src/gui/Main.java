package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    double posX,posY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("home_layout.fxml.fxml"));
            primaryStage.setTitle("TCG");
            primaryStage.setScene(new Scene(root));
            primaryStage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(event -> {
                posX = event.getSceneX();
                posY = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - posX);
                primaryStage.setY(event.getScreenY() - posY);
            });

            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}