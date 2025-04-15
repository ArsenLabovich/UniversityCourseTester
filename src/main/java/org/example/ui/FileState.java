package org.example.ui;

import lombok.AccessLevel;
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
    private static File projectFolder;

    public static void setProjectFolder(File folder) {
        projectFolder = folder;
        if (folder != null) {
            File includeFolder = new File(folder, "include");
            File srcFolder = new File(folder, "src");
            File z4File = new File(srcFolder, "z4.c");
            File dataCFile = new File(srcFolder, "data.c");
            if (z4File.exists() && z4File.isFile()) {
                setCFile(z4File);
            } else {
                setCFile(null);
            }
            if (dataCFile.exists() && dataCFile.isFile()) {
                setDataCFile(dataCFile);
            } else {
                setDataCFile(null);
            }
            if (includeFolder.exists() && includeFolder.isDirectory()) {
                setIncludeFolder(includeFolder);
            } else {
                setIncludeFolder(null);
            }
            if (srcFolder.exists() && srcFolder.isDirectory()) {
                setSrcFolder(srcFolder);
            } else {
                setSrcFolder(null);
            }
        } else {
            setCFile(null);
        }
    }

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static File cFile;


    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static File includeFolder;



    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static File srcFolder;

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static File dataCFile;

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static File z4CFile;


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
