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
        Integer n = getN(scenarioNumber, testNumber);
        ProcessBuilder processBuilder = null;
        if(n == null) {
            processBuilder = new ProcessBuilder("./output.exe");
        }else{
            processBuilder = new ProcessBuilder("./output.exe", n.toString());
        }
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

    private Integer getN(int scenario, int testNumber) {
        return switch (scenario) {
            case 1, 3, 5, 6 -> null; // В этих сценариях N не задаётся
            case 2 -> 0; // В сценарии 2 всегда N=0
            case 4 -> switch (testNumber) {
                case 1, 7 -> 10;
                case 2 -> 5;
                case 3 -> 14;
                case 4, 8 -> 12;
                case 5 -> 11;
                case 6 -> 16;
                case 9 -> 190;
                default -> null; // неизвестный тест
            };
            default -> null; // неизвестный сценарий
        };
    }

}
