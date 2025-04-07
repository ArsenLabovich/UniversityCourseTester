package org.example.runners;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class MainTestRunner implements Runnable {

    private final String pathToStdinStdoutDirectory;

    private final Integer numberOfScenarios = null;
    private final Integer numberOfTests = null;
    private final Integer numberOfPassedTests = null;
    private final Integer numberOfFailedTests = null;
    private Boolean isFinished = false;

    private final List<ScenarioRunner> scenarioRunners = Collections.synchronizedList(new ArrayList<>());

    public MainTestRunner(String pathToStdinStdoutDirectory) {
        this.pathToStdinStdoutDirectory = pathToStdinStdoutDirectory;
    }

    @Override
    public void run() {
        try (var paths = Files.list(Paths.get(pathToStdinStdoutDirectory, "stdin"))) {
            paths.filter(Files::isDirectory).forEach(scenarioFolder -> {
                String scenarioName = scenarioFolder.getFileName().toString();
                Path expectedFolder = Paths.get(pathToStdinStdoutDirectory, "stdout", scenarioName);
                if (Files.exists(expectedFolder) && Files.isDirectory(expectedFolder)) {
                    ScenarioRunner scenarioRunner = new ScenarioRunner(scenarioFolder.toString(), expectedFolder.toString(), scenarioRunners.size() + 1);
                    scenarioRunners.add(scenarioRunner);
                    new Thread(scenarioRunner).start();
                }
            });
            scenarioRunners.forEach(scenarioRunner -> {
                synchronized (scenarioRunner) {
                    try {
                        while (!scenarioRunner.getIsFinished()) {
                            scenarioRunner.wait(10);
                        }
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
            });
            this.isFinished = true;


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
