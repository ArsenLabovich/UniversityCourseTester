package org.example.envpreparation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class GCCTool {

    public static boolean isGCCInPath() {
        return Objects.requireNonNull(runCommand("which gcc")).contains("/gcc") || Objects.requireNonNull(runCommand("where gcc")).contains("\\gcc");
    }

    public static boolean isGCCVersionValid() {
        String versionOutput = runCommand("gcc --version");
        return isVersionValid(Objects.requireNonNull(versionOutput));
    }

    public static boolean compileFile(String filePath, String outputFileName) {
        return runCommand("gcc " + filePath + " -o " + outputFileName) != null;
    }

    private static String runCommand(String command) {
        try {
            Process process = startProcess(command);
            String output = getProcessOutput(process);
            int exitCode = process.waitFor();
            return isCommandSuccessful(exitCode) ? output : null;
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static Process startProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        return processBuilder.start();
    }

    private static String getProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
        }
        return output.toString();
    }

    private static boolean isCommandSuccessful(int exitCode) {
        return exitCode == 0;
    }

    private static boolean isVersionValid(String versionOutput) {
        String[] lines = versionOutput.split(System.lineSeparator());
        if (lines.length > 0) {
            String[] parts = lines[0].split(" ");
            for (String part : parts) {
                if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                    return compareVersions(part) >= 0;
                }
            }
        }
        return false;
    }

    private static int compareVersions(String version1) {
        String[] v1 = version1.split("\\.");
        String[] v2 = "14.2.0".split("\\.");
        for (int i = 0; i < Math.max(v1.length, v2.length); i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 != num2) {
                return num1 - num2;
            }
        }
        return 0;
    }
}