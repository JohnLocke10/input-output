package com.tolik.inputoutput.fileanalyzer;

import java.io.*;
import java.util.ArrayList;

public class FileAnalyzer {
    public static final int INPUT_SIZE_INCLUDING_LINE_BREAK = 202;
    public static final String INITIAL_CONSOLE_MESSAGE =
            "Please write the name of file and the word to found separated by ONE space...";
    public static final String NO_SPACE_IN_MESSAGE = "There is no spase in typed text";
    public static final String TOO_SHORT_MESSAGE = "Typed char sequence is too short";
    public static final String TOO_LONG_MESSAGE = "Typed char sequence is too long";
    public static final String INPUT_SIZE_RESTRICTION_MESSAGE =
            "Allowed number of characters is in range from 3 till " + (INPUT_SIZE_INCLUDING_LINE_BREAK - 2);
    public static final String DATA_READING_EXCEPTION = "Data reading is unsuccessful";
    public static final String FILE_NOT_FOUND = "System can't find file by path: %s";

    public static void main(String[] args) {
        byte[] consoleInputByteArray = new byte[INPUT_SIZE_INCLUDING_LINE_BREAK + 1];  //+1 for line break;
        int numberOfReadBytes;
        int spacePosition;

        System.out.println(INITIAL_CONSOLE_MESSAGE);
        System.out.println(INPUT_SIZE_RESTRICTION_MESSAGE);

        numberOfReadBytes = readContent(consoleInputByteArray, System.in, INPUT_SIZE_INCLUDING_LINE_BREAK);
        spacePosition = defineSpasePosition(consoleInputByteArray);
        validateTypedText(spacePosition, numberOfReadBytes);

        String filePath = new String(consoleInputByteArray, 0, spacePosition);
        String wordToFind = getWordToFind(consoleInputByteArray, spacePosition, numberOfReadBytes);

        analyzeContentAndPrintResults(filePath, wordToFind);
    }

    static String getWordToFind(byte[] consoleInputByteArray, int spacePosition, int numberOfReadBytes) {
        return new String(consoleInputByteArray, spacePosition + 1, numberOfReadBytes - spacePosition - 2);
    }

    static void analyzeContentAndPrintResults(String filePath, String wordToFind) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {

            ArrayList<String> sentences = getArrayOfSentences(fileInputStream);
            printWordNumber(wordToFind, sentences);
            printSentencesWithWord(sentences, wordToFind);

        } catch (FileNotFoundException exception) {
            throw new RuntimeException(String.format(FILE_NOT_FOUND, filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printWordNumber(String wordToFind, ArrayList<String> sentences) {
        System.out.println("Number of word " + wordToFind + " : " + getNumberOfDefinedWord(sentences, wordToFind));
    }

    private static void printSentencesWithWord(ArrayList<String> sentences, String wordToFind) {
        for (String sentence : sentences) {
            if (sentence.contains(wordToFind)) {
                System.out.println(sentence);
            }
        }
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
        ArrayList<String> sentences = new ArrayList();

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

    static int readContent(byte[] inputByteArray, InputStream inputStream, int length) {
        int numberOfReadBytes;
        try {
            numberOfReadBytes = inputStream.read(inputByteArray, 0, length);
            System.in.close();
        } catch (IOException e) {
            throw new RuntimeException(DATA_READING_EXCEPTION);
        }
        return numberOfReadBytes;
    }

    static int defineSpasePosition(byte[] consoleInputByteArray) {
        for (int i = 0; i < consoleInputByteArray.length; i++) {
            if (consoleInputByteArray[i] == ' ') {
                return i;
            }
        }
        return -1;
    }

    static void validateTypedText(int spacePosition, int numberOfReadBytes) {
        if (spacePosition == -1) {
            throw new RuntimeException(NO_SPACE_IN_MESSAGE);
        } else if (numberOfReadBytes < 3) {
            throw new RuntimeException(TOO_SHORT_MESSAGE);
        } else if (numberOfReadBytes == INPUT_SIZE_INCLUDING_LINE_BREAK) {
            throw new RuntimeException(TOO_LONG_MESSAGE);
        }
    }
}
