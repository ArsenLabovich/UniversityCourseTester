package org.example;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.ui.Navigation;

import java.util.Objects;


public class JavaFXApp extends Application {

    @Getter
    private static final Double welcomeSceneWidth = 800.0;
    @Getter
    private static final Double welcomeSceneHeight = 600.0;

    @Getter
    private static final Double setupSceneWidth = 800.0;
    @Getter
    private static final Double setupSceneHeight = 600.0;


    @Getter
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = Navigation.loadFXML("welcome.fxml");
        Scene scene = new Scene(Objects.requireNonNull(root));
        stage.setScene(scene);
        stage.setTitle("B-PROG2 Testing Tool");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}