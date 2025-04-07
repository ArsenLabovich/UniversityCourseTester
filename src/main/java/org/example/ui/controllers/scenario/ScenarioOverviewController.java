package org.example.ui.controllers.scenario;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.runners.ScenarioRunner;
import org.example.runners.Test;
import org.example.ui.FileState;
import org.example.ui.Navigation;
import org.example.ui.controllers.overview.ScenarioCardController;

import java.io.IOException;

public class ScenarioOverviewController {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox testsList;
    @FXML
    public JFXButton backToOverViewButton;

    public void initialize() {
        if (FileState.getScenarioRunner() == null) {
            throw new IllegalStateException("ScenarioRunner is not set");
        }
        ScenarioRunner scenarioRunner = FileState.getScenarioRunner();
        scenarioRunner.getTests().forEach(this::addTestCard);
        backToOverViewButton.setOnAction(event -> {
            try {
                Navigation.switchTo("overview.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void addTestCard(Test test) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/test_card.fxml"));
            HBox card = loader.load();
            TestCardController testCardController = loader.getController();
            testCardController.setData(test);
            testsList.getChildren().add(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

