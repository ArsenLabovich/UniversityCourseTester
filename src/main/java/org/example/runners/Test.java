package org.example.runners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.strtools.StringComparator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@RequiredArgsConstructor
public class Test implements Runnable {

    private Boolean isFinished = false;

    private Boolean isPassed = null;
    private Boolean isPassedWithSpaces = null;

    private final String pathToInput;
    private final String pathToExpected;

    private final Integer scenarioNumber;
    private final Integer testNumber;

    private String programOutput = null;
    private String expectedOutput = null;

    private Integer exitCode;

    @Override
    public void run() {
        try {
            executeProgram();
            finishTest();
        } catch (Exception e) {
            e.printStackTrace();
            setErrorFailed();
        }
    }

    private void executeProgram() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("./output.exe");
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectInput(new File(pathToInput));
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append(System.lineSeparator());
            }
            this.programOutput = outputBuilder.toString().trim();
            this.exitCode = process.waitFor();
        } finally {
            process.destroy();
        }
    }

    private void setErrorFailed() {
        isPassed = false;
        isPassedWithSpaces = false;
        exitCode = -1;
        programOutput = null;
    }

    private void finishTest() throws IOException {
        this.expectedOutput = new String(Files.readAllBytes(Paths.get(pathToExpected))).trim();
        this.isPassed = StringComparator.isEquivalent(this.expectedOutput, this.programOutput);
        this.isPassedWithSpaces = StringComparator.isEquivalentByDiff(this.expectedOutput, this.programOutput);
        this.isFinished = true;
    }

    public String toString() {
        return "Test{" + "\nisFinished=" + isFinished + "\nisPassed=" + isPassed + "\npathToInput='" + pathToInput + '\'' + "\npathToExpected='" + pathToExpected + '\'' + "\nscenarioNumber=" + scenarioNumber + "\ntestNumber=" + testNumber + "\nprogramOutput='" + programOutput + '\'' + "\nexpectedOutput='" + expectedOutput + '\'' + "\nexitCode=" + exitCode + "\n}";
    }

}
