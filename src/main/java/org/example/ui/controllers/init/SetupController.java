package org.example.ui.controllers.init;

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
import org.example.ui.FileState;
import org.example.ui.Navigation;

import java.io.File;
import java.io.IOException;

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
    private JFXButton inputBrowseButton;

    @FXML
    private JFXButton startTestingButton;

    @FXML
    private Circle statusCircle;

    @FXML
    private Text statusLabel;

    @Setter
    private Stage primaryStage;


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
            if (!isFolder(folder)) {
                showError("Please select a valid folder.");
            } else if (folder.getAbsolutePath().contains("stdin") || folder.getAbsolutePath().contains("stdout")) {
                showError("Please select a folder that does not contain 'stdin' or 'stdout' in its name.");
            } else if (!doStdinAndStdoutExist(folder)) {
                showError("Please select a valid folder with 'stdin' and 'stdout' directories.");
            } else {
                inputFolderField.setText(folder.getAbsolutePath());
                FileState.setInputFolder(folder);
            }
            updateStartTestingButton();
        });


        cFileButton.setOnAction(e -> {
            File file = chooseFile();
            if (isCFile(file)) {
                cFileField.setText(file.getAbsolutePath());
                FileState.setCFile(file);
                if (GCCTool.compileFile(FileState.getCFile().getAbsolutePath(), "output.exe")) {
                    updateCFileStatus("OK");
                } else {
                    updateCFileStatus("IS_NOT_COMPILABLE");
                }
            } else {
                showError("Please select a valid C file.");
            }
            updateStartTestingButton();

        });
        startTestingButton.setOnAction(e -> startTesting());

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
        try {
            Navigation.switchTo("overview.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private boolean doStdinAndStdoutExist(File folder) {
        if (folder == null || !folder.isDirectory()) {
            return false;
        }
        File stdinDir = new File(folder, "stdin");
        File stdoutDir = new File(folder, "stdout");
        return stdinDir.exists() && stdinDir.isDirectory() && stdoutDir.exists() && stdoutDir.isDirectory();
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

    private void updateStartTestingButton() {
        startTestingButton.setDisable(!(statusLabel.getText().equals("Compiler OK") && fileStatusLabel.getText().equals("C File OK") && FileState.getInputFolder() != null));

    }
}
