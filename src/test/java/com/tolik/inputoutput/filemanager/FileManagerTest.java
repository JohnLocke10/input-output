package com.tolik.inputoutput.filemanager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.tolik.inputoutput.filemanager.FileManager.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    public static final String MAIN_TMP_FOLDER_PATH = "src/test/resources/FileManagerTest/tmp1/mainFolder";
    public static final String INNER_SECOND_FOLDER_PATH = MAIN_TMP_FOLDER_PATH + "/innerSecondDir";
    public final String EMPTY_FOLDER_PATH = MAIN_TMP_FOLDER_PATH + "/emptyDir";
    public final String FIRST_FILE_PATH = MAIN_TMP_FOLDER_PATH + "/firstFile.txt";
    public final String INNER_SECOND_FILE_PATH = MAIN_TMP_FOLDER_PATH + "/innerSecondDir/innerSecondFile.txt";
    static File mainTMPFolder = new File(MAIN_TMP_FOLDER_PATH);

    @BeforeEach
    public void setUpFilesTree() throws IOException {
        mainTMPFolder.mkdirs();
        File emptyDir = new File(EMPTY_FOLDER_PATH);
        emptyDir.mkdirs();
        File firstFile = new File(FIRST_FILE_PATH);
        if (!firstFile.exists()) {
            assertTrue(firstFile.createNewFile());
            try (FileOutputStream fileOutputStream = new FileOutputStream(firstFile)) {
                fileOutputStream.write(("Some text in " + firstFile.getName()).getBytes());
            }
        }
        File innerSecondDir = new File(INNER_SECOND_FOLDER_PATH);
        innerSecondDir.mkdir();
        File innerSecondFile =
                new File(INNER_SECOND_FILE_PATH);
        if (!innerSecondFile.exists()) {
            assertTrue(innerSecondFile.createNewFile());
            try (FileOutputStream fileOutputStream = new FileOutputStream(innerSecondFile)) {
                fileOutputStream.write(("Some text in " + innerSecondFile.getName()).getBytes());
            }
        }
    }

    @DisplayName("Check path correctness throws exception when path is null")
    @Test
    public void checkPathCorrectnessThrowsExceptionWhenPathIsNull() {
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            checkSrcPathCorrectness(null);
        });
        assertEquals("Your path is incorrect: " + null, actualException.getMessage());
    }

    @DisplayName("Check path correctness throws exception when file is not exist")
    @Test
    public void checkPathCorrectnessThrowsExceptionWhenFileIsNotExist() {
        String wrongPath = "C:NotExistingFile";
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            checkSrcPathCorrectness(wrongPath);
        });
        assertEquals("Your path is incorrect: " + wrongPath, actualException.getMessage());
    }

    @DisplayName("Test files count for not empty folder")
    @Test
    public void checkFilesCountForNotEmptyFolder() {
        assertEquals(2, calculateFilesNumber("src/test/resources/FileManagerTest/tmp1"));
    }

    @DisplayName("Test files count for empty folder")
    @Test
    public void checkFilesCountForEmptyFolder() {
        assertEquals(0, calculateFilesNumber(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Test folders count for not empty folder")
    @Test
    public void checkFoldersCountForNotEmptyFolder() {
        assertEquals(2, calculateFoldersNumber(MAIN_TMP_FOLDER_PATH));
    }

    @DisplayName("Test folders count for empty folder")
    @Test
    public void checkFoldersCountForEmptyFolder() {
        assertEquals(0, calculateFoldersNumber(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Check content of files in copied folders")
    @Test
    public void checkContentOfFilesInCopiedFolders() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/tmp4");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        copy(MAIN_TMP_FOLDER_PATH, destinationFolder.getPath());
        String expectedFileContentFirst = "Some text in " + new File(FIRST_FILE_PATH).getName();
        String expectedFileContentSecond = "Some text in " + new File(INNER_SECOND_FILE_PATH).getName();
        byte[] byteArrayFirst = new byte[expectedFileContentFirst.getBytes().length];
        byte[] byteArraySecond = new byte[expectedFileContentSecond.getBytes().length];
        try (FileInputStream fileInputStreamFirst = new FileInputStream(FIRST_FILE_PATH);
             FileInputStream fileInputStreamSecond = new FileInputStream(INNER_SECOND_FILE_PATH)) {
            fileInputStreamFirst.read(byteArrayFirst);
            fileInputStreamSecond.read(byteArraySecond);
        }
        assertEquals(expectedFileContentFirst, new String(byteArrayFirst));
        assertEquals(expectedFileContentSecond, new String(byteArraySecond));
    }

    @DisplayName("Check copy not empty folder")
    @Test
    public void checkCopyNotEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/tmp2");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        copy(MAIN_TMP_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(2, calculateFilesNumber(destinationFolder.getPath()));
        assertEquals(2, calculateFoldersNumber(destinationFolder.getPath()));
        assertEquals(2, calculateFilesNumber(MAIN_TMP_FOLDER_PATH));
        assertEquals(2, calculateFoldersNumber(MAIN_TMP_FOLDER_PATH));
    }

    @DisplayName("Check copy empty folder")
    @Test
    public void checkCopyEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/emptyFolder2");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        copy(EMPTY_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(0, calculateFilesNumber(destinationFolder.getPath()));
        assertEquals(0, calculateFoldersNumber(destinationFolder.getPath()));
        assertEquals(0, calculateFilesNumber(EMPTY_FOLDER_PATH));
        assertEquals(0, calculateFoldersNumber(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Check move not empty folder")
    @Test
    public void checkMoveNotEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/tmp3");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        move(MAIN_TMP_FOLDER_PATH, destinationFolder.getPath());
        assertTrue(destinationFolder.exists());
        assertEquals(2, calculateFilesNumber("src/test/resources/FileManagerTest/tmp3"));
        assertEquals(2, calculateFoldersNumber("src/test/resources/FileManagerTest/tmp3"));
        assertFalse(new File(MAIN_TMP_FOLDER_PATH).exists());
    }

    @DisplayName("Check move empty folder")
    @Test
    public void checkMoveEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/emptyFolder3");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        move(EMPTY_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(0, calculateFilesNumber(destinationFolder.getPath()));
        assertEquals(0, calculateFoldersNumber(destinationFolder.getPath()));
        assertTrue(destinationFolder.exists());
        assertFalse(new File(EMPTY_FOLDER_PATH).exists());
    }

    @DisplayName("Check deleting not empty folder")
    @Test
    public void checkDeletingNotEmptyFolder() {
        assertTrue(mainTMPFolder.exists());
        delete("src/test/resources/FileManagerTest");
        assertFalse(mainTMPFolder.exists());
    }

    @DisplayName("Check deleting empty folder")
    @Test
    public void checkDeletingEmptyFolder() {
        assertTrue(new File(EMPTY_FOLDER_PATH).exists());
        delete("src/test/resources/FileManagerTest");
        assertFalse(new File(EMPTY_FOLDER_PATH).exists());
    }

    @AfterAll
    public static void clearFileTrees() {
        delete("src/test/resources/FileManagerTest");
        assertFalse(new File("src/test/resources/FileManagerTest").exists());
    }

}
