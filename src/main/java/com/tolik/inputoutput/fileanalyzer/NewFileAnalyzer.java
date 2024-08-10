package com.tolik.inputoutput.fileanalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewFileAnalyzer {
    private final String pathToFile;
    private final String wordToFind;

    public static final String FILE_NOT_FOUND = "System can't find file by path: %s";

    public NewFileAnalyzer(String pathToFile, String wordToFind) {
        this.pathToFile = pathToFile;
        this.wordToFind = wordToFind;
    }

    public FileStatistics start() {

        FileStatistics fileStatistics = new FileStatistics();

        try (FileInputStream fileInputStream = new FileInputStream(pathToFile)) {

            ArrayList<String> sentences = getArrayOfSentences(fileInputStream);

            fileStatistics.setWorldCount(getNumberOfDefinedWord(sentences, wordToFind));
            fileStatistics.setSentencesWithWorld(collectSentencesWithWord(sentences, wordToFind));

        } catch (FileNotFoundException exception) {
            throw new RuntimeException(String.format(FILE_NOT_FOUND, pathToFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileStatistics;
    }

    private static ArrayList<String> collectSentencesWithWord(ArrayList<String> sentences, String wordToFind) {
        ArrayList<String> sentencesWithWord = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.contains(wordToFind)) {
                sentencesWithWord.add(sentence);
            }
        }
        return sentencesWithWord;
    }

    static int getNumberOfDefinedWord(ArrayList<String> sentences, String wordToFind) {
        int count = 0;

        for (String sentence : sentences) {
            int index = sentence.indexOf(wordToFind);
            while (index != -1) {
                count++;
                index = sentence.indexOf(wordToFind, ++index);
            }
        }
        return count;
    }

    static ArrayList<String> getArrayOfSentences(InputStream inputStream) throws IOException {
        int charCode;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> sentences = new ArrayList<>();

        boolean skipLineSeparator = false;
        while ((charCode = inputStream.read()) != -1) {
            char charFromCode = (char) charCode;
            if (isItValidCharToAdd(charFromCode, skipLineSeparator)) {
                stringBuilder.append(charFromCode);
                skipLineSeparator = false;
            }
            if (charFromCode == '?' || charFromCode == '!' || charFromCode == '.') {
                sentences.add(stringBuilder.toString());
                skipLineSeparator = true;
                stringBuilder.setLength(0);
            }
        }
        return sentences;
    }

    private static boolean isItValidCharToAdd(char charFromCode, boolean skipLineSeparator) {
        return !((System.lineSeparator().contains(String.valueOf(charFromCode))) && skipLineSeparator);
    }

}
