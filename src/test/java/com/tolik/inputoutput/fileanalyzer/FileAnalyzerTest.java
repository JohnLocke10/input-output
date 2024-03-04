package com.tolik.inputoutput.fileanalyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.tolik.inputoutput.fileanalyzer.FileAnalyzer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileAnalyzerTest {
    @DisplayName("check number of read bytes is correct")
    @Test
    public void checkNumberOfReadBytesIsCorrect() {
        byte[] byteArray = new byte[20];
        byte[] inputBytes = "C:/filePath word".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        int numberOfReadBytes = getNumberOfReadBytes(byteArray, inputStream, 20);
        assertEquals(16, numberOfReadBytes);
    }

    @DisplayName("Space position is defined correctly when space exist")
    @Test
    public void SpacePositionIsDefinedCorrectlyWhenSpacePresent() {
        byte[] spaceInTteMiddle = "C:/filePath2 word".getBytes();
        assertEquals(12, defineSpasePosition(spaceInTteMiddle));
        byte[] spaceAtTheBeginning = " C:/filePath2 word".getBytes();
        assertEquals(0, defineSpasePosition(spaceAtTheBeginning));
    }

    @DisplayName("Check return negative number when no space present")
    @Test
    public void checkReturnNegativeNumberWhenNoSpacePresent() {
        byte[] spaceInTteMiddle = "C:/filePathAndWord".getBytes();
        assertEquals(-1, defineSpasePosition(spaceInTteMiddle));
    }

    @DisplayName("Check validation message is empty for positive case")
    @Test
    public void checkValidationMessageIsEmptyForPositiveCase() {
        assertEquals("",
                validateAndGetExceptionMessage(15, 20));
    }

    @DisplayName("Check validation message when spase position is negative")
    @Test
    public void checkValidationMessageWhenSpasePositionIsNegative() {
        assertEquals("There is no spase in typed text",
                validateAndGetExceptionMessage(-1, 20));
    }

    @DisplayName("Check validation message when number of read bytes is less than three")
    @Test
    public void checkValidationMessageWhenNumberOfReadBytesIsLessThanThree() {
        assertEquals("Typed char sequence is too short",
                validateAndGetExceptionMessage(1, 2));
    }

    @DisplayName("Check validation message when input size limit is reached")
    @Test
    public void checkValidationMessageWhenInputSizeLimitIsReached() {
        assertEquals("Typed char sequence is too long",
                validateAndGetExceptionMessage(1, 202));
    }

    @DisplayName("check when sentence contains dot put it to list")
    @Test
    public void checkWhenSentenceContainsDotPutItToList() throws IOException {
        byte[] textWithDot = "Test sentence1   which contains dot. And some text".getBytes();
        InputStream inputStreamWithDot = new ByteArrayInputStream(textWithDot);
        List<String> actualSentences = getArrayOfSentences(inputStreamWithDot);
        assertEquals(1, actualSentences.size());
        assertEquals("Test sentence1   which contains dot.", actualSentences.get(0));
    }

    @DisplayName("check when sentence contains question mark put it to list")
    @Test
    public void checkWhenSentenceContainsQuestionMarkPutItToList() throws IOException {
        String testedString = "Some text" +
                System.lineSeparator() +
                "Is this sentence2 with question mark? " + System.lineSeparator();
        byte[] textWithQuestionMark = testedString.getBytes();
        InputStream inputStreamWithQuestionMark = new ByteArrayInputStream(textWithQuestionMark);
        List<String> actualSentences = getArrayOfSentences(inputStreamWithQuestionMark);
        assertEquals(1, actualSentences.size());
        assertEquals("Some text" + System.lineSeparator() +
                "Is this sentence2 with question mark?", actualSentences.get(0));
    }

    @DisplayName("check when sentence contains exclamation mark put it to list")
    @Test
    public void checkWhenSentenceContainsExclamationMarkPutItToList() throws IOException {
        byte[] textWithExclamationMark = "Test sentence3   which contains exclamation mark! And some text".getBytes();
        InputStream inputStreamExclamationMark = new ByteArrayInputStream(textWithExclamationMark);
        List<String> actualSentences = getArrayOfSentences(inputStreamExclamationMark);
        assertEquals(1, actualSentences.size());
        assertEquals("Test sentence3   which contains exclamation mark!", actualSentences.get(0));
    }

    @DisplayName("check when sentence contains several sentences put all them to list")
    @Test
    public void checkWhenSentenceContainsContainsSeveralSentencesPutAllThemToList() throws IOException {
        String testedStrings = "First sentence with dot." + System.lineSeparator() + System.lineSeparator() +
                "Is this sentence2 with question mark?" + System.lineSeparator() + System.lineSeparator() +
                "Third sentence!";
        byte[] textWithThreeSentences = testedStrings.getBytes();
        InputStream inputStreamWithThreeSentences = new ByteArrayInputStream(textWithThreeSentences);
        List<String> actualSentences = getArrayOfSentences(inputStreamWithThreeSentences);
        assertEquals(3, actualSentences.size());
        assertEquals("First sentence with dot.",
                actualSentences.get(0));
        assertEquals("Is this sentence2 with question mark?",
                actualSentences.get(1));
        assertEquals("Third sentence!",
                actualSentences.get(2));
    }

}
