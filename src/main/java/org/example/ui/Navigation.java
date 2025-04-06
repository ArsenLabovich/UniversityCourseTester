package org.example.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.example.JavaFXApp;

import java.io.IOException;
import java.net.URL;

public class Navigation {
    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlLocation = Navigation.class.getResource("/fxml/" + fxml);
        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found: " + fxml);
        }
        return FXMLLoader.load(fxmlLocation);
    }

    public static void switchTo(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        JavaFXApp.getPrimaryStage().getScene().setRoot(root);
    }
}
