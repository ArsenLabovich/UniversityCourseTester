package org.example;

import org.example.envpreparation.GCCTool;
import org.example.runners.MainTestRunner;

public class Main {
    public static void main(String[] args) {

        GCCTool.compileFile("main.c", "output.exe");
        MainTestRunner mainTestRunner = new MainTestRunner("/home/arsen/IdeaProjects/Prog2Tester/z3_testing");
        Thread thread = new Thread(mainTestRunner);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
        }
    }
}