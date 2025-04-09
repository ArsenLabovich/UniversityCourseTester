package org.example.ui.controllers.test;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.example.runners.Test;
import org.example.ui.FileState;
import org.example.ui.Navigation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DetailedViewController {

    @FXML
    private WebView webView;
    @FXML
    private Text titleText;
    @FXML
    private JFXButton backButton;

    public void setData(String title, String leftText, String rightText) {
        titleText.setText(title);
        String htmlContent = generateMergelyHTML(leftText, rightText);
        webView.getEngine().loadContent(htmlContent);

    }

    @FXML
    private void initialize() {
        Test test = FileState.getTest();
        setData("Scenario #" + test.getScenarioNumber() + " - Test #" + test.getTestNumber(), test.getProgramOutput(), test.getExpectedOutput());
        backButton.setOnAction(e -> {
            try {
                Navigation.switchTo("scenario_overview.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private String generateMergelyHTML(String leftText, String rightText) {
        try {
            InputStream input = getClass().getResourceAsStream("/html/mergely-template.html");
            if (input == null) throw new FileNotFoundException("mergely-template.html not found");

            String html = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            html = html.replace("__LHS__", escapeJS(leftText));
            html = html.replace("__RHS__", escapeJS(rightText));
            return html;
        } catch (IOException e) {
            e.printStackTrace();
            return "<html><body><h2>Error loading comparison view</h2></body></html>";
        }
    }

    private String escapeJS(String str) {
        return str.replace("\\", "\\\\").replace("`", "\\`").replace("$", "\\$").replace("\n", "\\n").replace("\r", "");
    }
}
