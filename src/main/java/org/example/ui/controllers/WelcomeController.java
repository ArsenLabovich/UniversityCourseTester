package org.example.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.example.JavaFXApp;
import org.example.ui.Navigation;

import java.io.IOException;

public class WelcomeController {
    @FXML
    private Button startButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        anchorPane.setPrefHeight(JavaFXApp.getWelcomeSceneHeight());
        anchorPane.setPrefWidth(JavaFXApp.getWelcomeSceneWidth());
        startButton.setOnAction(event -> {
            try {
                Navigation.switchTo("setup.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
