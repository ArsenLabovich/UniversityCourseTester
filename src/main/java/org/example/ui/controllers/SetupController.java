package org.example.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.JavaFXApp;
import org.example.envpreparation.GCCTool;

import java.io.File;

public class SetupController {

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public JFXTextField cFileField;

    @FXML
    public JFXButton cFileButton;

    @FXML
    public Circle fileStatusCircle;
    @FXML
    public Text fileStatusLabel;


    @FXML
    private JFXTextField inputFolderField;

    @FXML
    private JFXTextField expectedFolderField;

    @FXML
    private JFXButton inputBrowseButton;

    @FXML
    private JFXButton expectedBrowseButton;

    @FXML
    private JFXButton startTestingButton;

    @FXML
    private Circle statusCircle;

    @FXML
    private Text statusLabel;

    @Setter
    private Stage primaryStage;

    private File inputFolder;
    private File expectedFolder;

    private File cFile;

    @FXML
    public void initialize() {

        anchorPane.setPrefHeight(JavaFXApp.getSetupSceneHeight());
        anchorPane.setPrefWidth(JavaFXApp.getSetupSceneWidth());


        String gccStatus;
        if (GCCTool.isGCCInPath()) {
            gccStatus = GCCTool.isGCCVersionValid() ? "OK" : "BAD_VERSION";
        } else {
            gccStatus = "NOT_FOUND";
        }
        updateCompilerStatus(gccStatus);
        updateCFileStatus("NOT_SELECTED");

        inputBrowseButton.setOnAction(e -> {
            File folder = chooseDirectory();
            if (isFolder(folder)) {
                inputFolderField.setText(folder.getAbsolutePath());
                inputFolder = folder;
            } else {
                showError("Please select a valid folder.");
            }


        });

        expectedBrowseButton.setOnAction(e -> {
            File folder = chooseDirectory();
            if (isFolder(folder)) {
                expectedFolderField.setText(folder.getAbsolutePath());
                expectedFolder = folder;
            } else {
                showError("Please select a valid folder.");
            }
        });

        cFileButton.setOnAction(e -> {
            File file = chooseFile();
            if (isCFile(file)) {
                cFileField.setText(file.getAbsolutePath());
                cFile = file;
                if (GCCTool.compileFile(cFile.getAbsolutePath(), "output.exe")) {
                    updateCFileStatus("OK");
                } else {
                    updateCFileStatus("IS_NOT_COMPILABLE");
                }
            } else {
                showError("Please select a valid C file.");
            }
        });
        startTestingButton.setOnAction(e -> startTesting());

        // Register shutdown hook to delete the file
        Runtime.getRuntime().addShutdownHook(new Thread(this::deleteOutputFile));
    }

    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Folder");
        return chooser.showDialog(primaryStage);
    }

    private File chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select C File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("C Files", "*.c"));
        return chooser.showOpenDialog(primaryStage);
    }

    private void startTesting() {
        // TO DO: Implement the logic to start testing
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Input");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void updateCompilerStatus(String status) {
        statusCircle.getStyleClass().clear();
        switch (status) {
            case "OK":
                statusCircle.getStyleClass().add("circle-green");
                statusLabel.setText("Compiler OK");
                statusLabel.setStyle("-fx-fill: #66ff66;");
                break;
            case "BAD_VERSION":
                statusCircle.getStyleClass().add("circle-yellow");
                statusLabel.setText("Bad Version");
                statusLabel.setStyle("-fx-fill: #ffe066;");
                break;
            case "NOT_FOUND":
            default:
                statusCircle.getStyleClass().add("circle-red");
                statusLabel.setText("Not Found");
                statusLabel.setStyle("-fx-fill: #ff4d4d;");
                break;
        }
    }

    public void updateCFileStatus(String status) {
        fileStatusCircle.getStyleClass().clear();
        switch (status) {
            case "OK":
                fileStatusCircle.getStyleClass().add("circle-green");
                fileStatusLabel.setText("C File OK");
                fileStatusLabel.setStyle("-fx-fill: #66ff66;");
                break;
            case "NOT_SELECTED":
                fileStatusCircle.getStyleClass().add("circle-yellow");
                fileStatusLabel.setText("C File is not selected");
                fileStatusLabel.setStyle("-fx-fill: #ffe066;");
                break;
            case "IS_NOT_COMPILABLE":
            default:
                fileStatusCircle.getStyleClass().add("circle-red");
                fileStatusLabel.setText("C File is not compilable");
                fileStatusLabel.setStyle("-fx-fill: #ff4d4d;");
                break;
        }
    }

    private boolean isFolder(File file) {
        if (file == null) {
            return false;
        }
        return file.exists() && file.isDirectory();
    }

    private boolean isCFile(File file) {
        if (file == null) {
            return false;
        }
        return file.exists() && file.isFile() && file.getName().endsWith(".c");
    }

    private void deleteOutputFile() {
        File outputFile = new File("output.exe");
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

}
