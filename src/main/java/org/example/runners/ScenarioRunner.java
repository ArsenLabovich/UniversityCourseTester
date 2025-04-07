package org.example.runners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ScenarioRunner implements Runnable {
    private final String pathToScenarioInputFolder;
    private final String pathToScenarioExpectedFolder;

    private Integer testsInScenario = null;

    private final Integer scenarioNumber;

    private final List<Test> tests = Collections.synchronizedList(new ArrayList<>());

    private Boolean isFinished = false;


    @Override
    public void run() {
        try (var paths = Files.list(Paths.get(pathToScenarioInputFolder))) {
            paths.filter(Files::isRegularFile).forEach(inputFile -> {
                String fileName = inputFile.getFileName().toString();
                Path expectedFile = Paths.get(pathToScenarioExpectedFolder, fileName);
                if (Files.exists(expectedFile)) {
                    Test test = new Test(inputFile.toString(), expectedFile.toString(), scenarioNumber, tests.size() + 1);
                    tests.add(test);
                    Thread thread = new Thread(test);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(e.getMessage());
                    }
                }
            });
            this.isFinished = true;
            this.testsInScenario = tests.size();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean isSuccessful() {
        for (Test test : tests) {
            if (test.getIsPassed() == null || !test.getIsPassed()) {
                return false;
            }
        }
        return true;
    }


}
