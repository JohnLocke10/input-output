package com.tolik.inputoutput.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    public static final String INCORRECT_PATH = "Your path is incorrect: ";
    private static int count = 0;

    public static int countFiles(String path) {
        int result = calculateFilesNumber(path);
        resetCount();
        return result;
    }

    public static int countFolders(String path) {
        int result = calculateFoldersNumber(path);
        resetCount();
        return result;
    }

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
        copy(from, to);
        delete(from);
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

    private static int calculateFoldersNumber(String path) {
        checkSrcPathCorrectness(path);
        File file = new File(path);
        if (file.isDirectory()) {
            File[] innerFiles = file.listFiles();
            for (File innerFile : innerFiles) {
                calculateFoldersNumber(innerFile.getAbsolutePath());
            }
            count++;
        }
        return count - 1;
    }

    private static int calculateFilesNumber(String path) {
        checkSrcPathCorrectness(path);
        File file = new File(path);
        if (file.isFile()) {
            System.out.println("File calculated: " + file.getAbsoluteFile());
            count++;
        } else {
            File[] innerFilesArray = file.listFiles();
            if (innerFilesArray == null) {
                System.err.println("Cannot access directory: " + file.getAbsolutePath());
            } else {
                for (File innerFile : innerFilesArray) {
                    calculateFilesNumber(innerFile.getAbsolutePath());
                }
            }
        }
        return count;
    }

    private static void resetCount() {
        count = 0;
    }
}
