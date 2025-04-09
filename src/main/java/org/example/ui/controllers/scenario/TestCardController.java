package org.example.ui.controllers.scenario;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.runners.Test;
import org.example.ui.FileState;
import org.example.ui.Navigation;
import org.example.ui.controllers.test.DetailedViewController;

import java.io.IOException;

public class TestCardController {

    @FXML
    private Text testNumberText;
    @FXML
    private Text exitCodeText;
    @FXML
    private Text statusText;
    @FXML
    private Circle statusCircle;
    @FXML
    private JFXButton detailsButton;

    public void setData(Test test) {
        testNumberText.setText("Test #" + test.getTestNumber());
        exitCodeText.setText("Exit Code: " + test.getExitCode());

        Boolean isPassed = test.getIsPassed();
        Boolean isPassedWithSpaces = test.getIsPassedWithSpaces();
        if (Boolean.TRUE.equals(isPassed)&&Boolean.TRUE.equals(isPassedWithSpaces)) {
            statusText.setText("Passed");
            statusCircle.setFill(Paint.valueOf("#32CD32")); // Lime green
        } else if (Boolean.FALSE.equals(isPassed)&&Boolean.FALSE.equals(isPassedWithSpaces)) {
            statusText.setText("Failed");
            statusCircle.setFill(Paint.valueOf("#FF5555")); // Red
        }else if (Boolean.TRUE.equals(isPassed)||Boolean.TRUE.equals(isPassedWithSpaces)) {
            statusText.setText("Spaces mismatching");
            statusCircle.setFill(Paint.valueOf("#FFA500")); // Orange
        }
        else {
            statusText.setText("Unknown");
            statusCircle.setFill(Paint.valueOf("#aaaaaa")); // Gray
        }

        detailsButton.setOnAction(e -> {
            FileState.setTest(test);
            try {
                Navigation.switchTo("detailed.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
}
