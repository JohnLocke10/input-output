package com.tolik.inputoutput.filemanager;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static com.tolik.inputoutput.filemanager.FileManager.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    public static final String MAIN_TMP_FOLDER_PATH = "src/test/resources/FileManagerTest/tmp1/mainFolder";
    public final String EMPTY_FOLDER_PATH = "src/test/resources/FileManagerTest/tmp1/mainFolder/emptyDir";
    static File mainTMPFolder = new File(MAIN_TMP_FOLDER_PATH);

    @BeforeEach
    public void setUpFilesTree() throws IOException {
        mainTMPFolder.mkdirs();
        File emptyDir = new File(EMPTY_FOLDER_PATH);
        emptyDir.mkdirs();
        File firstFile = new File("src/test/resources/FileManagerTest/tmp1/mainFolder/firstFile.txt");
        if (!firstFile.exists()) {
            assertTrue(firstFile.createNewFile());
        }
        File innerSecondDir = new File("src/test/resources/FileManagerTest/tmp1/mainFolder/innerSecondDir");
        innerSecondDir.mkdir();
        File innerSecondFile =
                new File("src/test/resources/FileManagerTest/tmp1/mainFolder/innerSecondDir/innerSecondFile.txt");
        if (!innerSecondFile.exists()) {
            assertTrue(innerSecondFile.createNewFile());
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
        assertEquals(2, countFiles("src/test/resources/FileManagerTest/tmp1"));
    }

    @DisplayName("Test files count for empty folder")
    @Test
    public void checkFilesCountForEmptyFolder() {
        assertEquals(0, countFiles(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Test folders count for not empty folder")
    @Test
    public void checkFoldersCountForNotEmptyFolder() {
        assertEquals(3, countFolders("src/test/resources/FileManagerTest/tmp1"));
    }

    @DisplayName("Test folders count for empty folder")
    @Test
    public void checkFoldersCountForEmptyFolder() {
        assertEquals(0, countFolders(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Check copy not empty folder")
    @Test
    public void checkCopyNotEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/tmp2");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        copy(MAIN_TMP_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(2, countFiles(destinationFolder.getPath()));
        assertEquals(2, countFolders(destinationFolder.getPath()));
        assertEquals(2, countFiles(MAIN_TMP_FOLDER_PATH));
        assertEquals(2, countFolders(MAIN_TMP_FOLDER_PATH));
    }

    @DisplayName("Check copy empty folder")
    @Test
    public void checkCopyEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/emptyFolder2");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        copy(EMPTY_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(0, countFiles(destinationFolder.getPath()));
        assertEquals(0, countFolders(destinationFolder.getPath()));
        assertEquals(0, countFiles(EMPTY_FOLDER_PATH));
        assertEquals(0, countFolders(EMPTY_FOLDER_PATH));
    }

    @DisplayName("Check move not empty folder")
    @Test
    public void checkMoveNotEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/tmp3");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        move(MAIN_TMP_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(2, countFiles("src/test/resources/FileManagerTest/tmp3"));
        assertEquals(2, countFolders("src/test/resources/FileManagerTest/tmp3"));
        assertFalse(new File(MAIN_TMP_FOLDER_PATH).exists());
    }

    @DisplayName("Check move empty folder")
    @Test
    public void checkMoveEmptyFolder() throws IOException {
        File destinationFolder = new File("src/test/resources/FileManagerTest/emptyFolder3");
        assertFalse(destinationFolder.exists());
        destinationFolder.mkdir();
        move(EMPTY_FOLDER_PATH, destinationFolder.getPath());
        assertEquals(0, countFiles(destinationFolder.getPath()));
        assertEquals(0, countFolders(destinationFolder.getPath()));
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
