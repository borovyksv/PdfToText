package com.borovyksv.mongo;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class zipTest {
    public static void main(String[] args) throws IOException {

        File d = new File("myDir/hi");

        d.mkdirs();

        File f = new File(d, "hello.txt");

        f.createNewFile();



        String fileName = "_result.zip";

        String source = "C:\\Users\\user-pc\\AppData\\Local\\Temp\\2017-Savana-Owners-Manual_v2.pdf4645824077500820385_parsed";
        zip(source, fileName);
    }

    private static ZipFile zip(String source, String fileName) throws IOException {
        Path tempDirectory = Files.createTempDirectory("");
        System.out.println(tempDirectory);

        String resultFile = tempDirectory.toString()+ File.separatorChar+fileName;

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(resultFile);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setIncludeRootFolder(false);
            zipFile.addFolder(source, zipParameters);

        } catch (ZipException e) {
            e.printStackTrace();
        }

        return zipFile;
    }
}
