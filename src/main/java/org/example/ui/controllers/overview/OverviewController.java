package org.example.ui.controllers.overview;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.runners.MainTestRunner;
import org.example.runners.ScenarioRunner;
import org.example.ui.FileState;

import java.io.IOException;

public class OverviewController {

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public JFXButton exitButton;

    @FXML
    private VBox scenarioList;

    @FXML
    public void initialize() {
        if (FileState.getMainTestRunner() == null) {
            MainTestRunner mainTestRunner = new MainTestRunner(FileState.getInputFolder().getAbsolutePath());
            Thread thread = new Thread(mainTestRunner);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(e.getMessage());
            }
            FileState.setMainTestRunner(mainTestRunner);
        }
        FileState.getMainTestRunner().getScenarioRunners().forEach(this::addScenarioCard);
        exitButton.setOnAction(event -> Platform.exit());
    }

    public void addScenarioCard(ScenarioRunner scenarioRunner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scenario_card.fxml"));
            HBox card = loader.load();

            ScenarioCardController controller = loader.getController();
            controller.setData(scenarioRunner);

            scenarioList.getChildren().add(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
