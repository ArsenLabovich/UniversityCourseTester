package org.example.ui.controllers.overview;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.example.runners.ScenarioRunner;
import org.example.ui.FileState;
import org.example.ui.Navigation;

import java.io.IOException;

public class ScenarioCardController {
    @FXML
    public Text scenarioNameText;
    @FXML
    public Text scenarioTestCountText;
    @FXML
    public Circle statusCircle;
    @FXML
    public Text statusText;
    @FXML
    public JFXButton detailsButton;

    public void setData(ScenarioRunner scenarioRunner) {
        scenarioNameText.setText("Scenario #" + scenarioRunner.getScenarioNumber());
        scenarioTestCountText.setText("Tests: " + scenarioRunner.getTestsInScenario());

        boolean successful = scenarioRunner.isSuccessful();
        if (successful) {
            statusCircle.setFill(Color.GREEN);
            statusText.setText("Passed");
        } else {
            statusCircle.setFill(Color.RED);
            statusText.setText("Failed");
        }

        detailsButton.setOnAction(e -> {
            FileState.setScenarioRunner(scenarioRunner);
            try {
                Navigation.switchTo("scenario_overview.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

