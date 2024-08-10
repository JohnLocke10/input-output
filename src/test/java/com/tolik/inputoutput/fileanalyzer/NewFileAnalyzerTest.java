package com.tolik.inputoutput.fileanalyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.tolik.inputoutput.fileanalyzer.NewFileAnalyzer.getArrayOfSentences;
import static com.tolik.inputoutput.fileanalyzer.NewFileAnalyzer.getNumberOfDefinedWord;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewFileAnalyzerTest {

    @DisplayName("check when sentence contains dot put it to list")
    @Test
    public void checkWhenSentenceContainsDotPutItToList() throws IOException {
        byte[] textWithDot = "Test sentence1   which contains dot. And some text".getBytes();
        InputStream inputStreamWithDot = new ByteArrayInputStream(textWithDot);
        List<String> actualSentences = getArrayOfSentences(inputStreamWithDot);
        assertEquals(1, actualSentences.size());
        assertEquals("Test sentence1   which contains dot.", actualSentences.getFirst());
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
                "Is this sentence2 with question mark?", actualSentences.getFirst());
    }

    @DisplayName("check when sentence contains exclamation mark put it to list")
    @Test
    public void checkWhenSentenceContainsExclamationMarkPutItToList() throws IOException {
        byte[] textWithExclamationMark = "Test sentence3   which contains exclamation mark! And some text".getBytes();
        InputStream inputStreamExclamationMark = new ByteArrayInputStream(textWithExclamationMark);
        List<String> actualSentences = getArrayOfSentences(inputStreamExclamationMark);
        assertEquals(1, actualSentences.size());
        assertEquals("Test sentence3   which contains exclamation mark!", actualSentences.getFirst());
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

    @DisplayName("Check count defined word for sentences with word")
    @Test
    public void checkCountDefinedWordForSentencesWithWord() {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("ourWord is present in sentence, repeat,ourWord is present.");
        sentences.add("in this sentence ourWord is present!");
        assertEquals(3, getNumberOfDefinedWord(sentences, "ourWord"));
    }

    @DisplayName("Check count defined word for sentences without word")
    @Test
    public void checkCountDefinedWordForSentencesWithoutWord() {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("Nothing is present in sentence, repeat, nothing is present.");
        sentences.add("in this sentence nothing is present!");
        assertEquals(0, getNumberOfDefinedWord(sentences, "ourWord"));
    }

    @DisplayName("Check count defined word for empty sentences")
    @Test
    public void checkCountDefinedWordForEmptySentences() {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("");
        sentences.add("");
        assertEquals(0, getNumberOfDefinedWord(sentences, "ourWord"));
    }

}
