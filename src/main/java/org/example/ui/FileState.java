package org.example.ui;

import lombok.Getter;
import lombok.Setter;
import org.example.runners.MainTestRunner;
import org.example.runners.ScenarioRunner;
import org.example.runners.Test;

import java.io.File;

public class FileState {

    @Getter
    @Setter
    private static File inputFolder;

    @Getter
    @Setter
    private static File cFile;

    @Getter
    @Setter
    private static MainTestRunner mainTestRunner = null;

    @Getter
    @Setter
    private static ScenarioRunner scenarioRunner = null;

    @Getter
    @Setter
    private static Test test = null;
}
