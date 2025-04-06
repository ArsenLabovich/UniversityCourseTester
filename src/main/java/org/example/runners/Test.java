package org.example.runners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@RequiredArgsConstructor
public class Test implements Runnable {

    private Boolean isFinished = false;

    private Boolean isPassed = null;

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
            System.err.println(e.getMessage());
            setErrorFailed();
        }
    }

    private void executeProgram() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("./output.exe");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        BufferedReader inputReader = new BufferedReader(new FileReader(pathToInput));
        StringBuilder outputBuilder = new StringBuilder();
        String line;
        while ((line = inputReader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
            writer.flush();
            while (reader.ready()) {
                outputBuilder.append(reader.readLine()).append(System.lineSeparator());
            }
        }

        while ((line = reader.readLine()) != null) {
            outputBuilder.append(line).append(System.lineSeparator());
        }
        reader.close();
        inputReader.close();
        writer.close();
        this.programOutput = outputBuilder.toString().trim();
        this.exitCode = process.waitFor();
    }

    private void setErrorFailed() {
        isPassed = false;
        exitCode = -1;
        programOutput = null;
    }

    private void finishTest() throws IOException {
        this.expectedOutput = new String(Files.readAllBytes(Paths.get(pathToExpected))).trim();
        this.isPassed = this.programOutput.equals(this.expectedOutput);
        this.isFinished = true;
    }

    public String toString() {
        return "Test{" +
                "\nisFinished=" + isFinished +
                "\nisPassed=" + isPassed +
                "\npathToInput='" + pathToInput + '\'' +
                "\npathToExpected='" + pathToExpected + '\'' +
                "\nscenarioNumber=" + scenarioNumber +
                "\ntestNumber=" + testNumber +
                "\nprogramOutput='" + programOutput + '\'' +
                "\nexpectedOutput='" + expectedOutput + '\'' +
                "\nexitCode=" + exitCode +
                "\n}";
    }

}
