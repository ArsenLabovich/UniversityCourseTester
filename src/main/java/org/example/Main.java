package org.example;

import org.example.envpreparation.GCCTool;
import org.example.runners.Test;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        System.out.println(GCCTool.isGCCInPath());
        System.out.println(GCCTool.isGCCVersionValid());
        System.out.println(GCCTool.compileFile("main.c", "output.exe"));
/*        try {
            ProcessBuilder pb = new ProcessBuilder("gcc", "main.c", "-o", "output.exe");
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Compilation failed.");
                return;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return;
        }
        Test test = new Test("z3_testing/stdin/scenar_1/example_1.txt", "z3_testing/stdout/scenar_1/example_1.txt",1, 1);
        Thread testThread = new Thread(test);
        testThread.start();
        try {
            testThread.join();
            System.out.println(test);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }*/
    }
}