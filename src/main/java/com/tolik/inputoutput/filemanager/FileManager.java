package com.tolik.inputoutput.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    public static final String INCORRECT_PATH = "Your path is incorrect: ";

    public static void copy(String from, String to) throws IOException {
        checkSrcPathCorrectness(from);
        if (Objects.isNull(to)) {
            throw new RuntimeException(INCORRECT_PATH + to);
        }

        File fileFrom = new File(from);
        File fileTo = new File(to);

        if (fileFrom.isDirectory()) {
            copyFolder(to, fileTo, fileFrom);
        } else {
            copyFile(from, to);
        }
    }

    private static void copyFile(String from, String to) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(from);
             FileOutputStream fileOutputStream = new FileOutputStream(to)) {

            byte[] byteArray = new byte[255];
            int readBytesNumber;

            while ((readBytesNumber = fileInputStream.read(byteArray)) > 0) {
                fileOutputStream.write(byteArray, 0, readBytesNumber);
            }
        }
    }

    private static void copyFolder(String to, File fileTo, File fileFrom) throws IOException {
        fileTo.mkdirs();
        File[] innerFiles = fileFrom.listFiles();
        for (File innerFile : innerFiles) {
            copy(innerFile.getAbsolutePath(), to + File.separator + innerFile.getName());
        }
    }

    public static void move(String from, String to) throws IOException {
        new File(from).renameTo(new File(to));
    }

    public static boolean delete(String path) {
        checkSrcPathCorrectness(path);
        File file = new File(path);
        if (file.isFile()) {
            return file.delete();
        } else {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                delete(listFile.getAbsolutePath());
            }
            return file.delete();
        }
    }

    static void checkSrcPathCorrectness(String filePath) {
        if (Objects.isNull(filePath) || !new File(filePath).exists()) {
            throw new RuntimeException(INCORRECT_PATH + filePath);
        }
    }

    public static int calculateFoldersNumber(String path) {
        checkSrcPathCorrectness(path);
        File file = new File(path);
        int folderCount = 0;
        File[] listFiles = file.listFiles();
        for (File innerFile : listFiles) {
            if (innerFile.isDirectory()) {
                folderCount++;
                folderCount = folderCount + calculateFoldersNumber(innerFile.getAbsolutePath());
            }
        }
        return folderCount;
    }

    public static int calculateFilesNumber(String path) {
        checkSrcPathCorrectness(path);
        int fileCount = 0;
        File file = new File(path);
        if (file.isFile()) {
            System.out.println("File calculated: " + file.getAbsoluteFile());
            fileCount++;
        } else {
            File[] innerFilesArray = file.listFiles();
            if (innerFilesArray == null) {
                System.err.println("Cannot access directory: " + file.getAbsolutePath());
            } else {
                for (File innerFile : innerFilesArray) {
                    fileCount = fileCount + calculateFilesNumber(innerFile.getAbsolutePath());
                }
            }
        }
        return fileCount;
    }

}
