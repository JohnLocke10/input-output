package com.tolik.inputoutput.fileanalyzer;

public class FileAnalyzerStarter {
    public static void main(String[] args) {
        String pathToFile = args[0];
        String worldToFind = args[1];

        FileStatistics fileStatistics = new NewFileAnalyzer(pathToFile, worldToFind).start();
        System.out.println("TOTAL COUNT OF " + worldToFind + " = " + fileStatistics.getWorldCount());
        System.out.println("SENTENCES WHICH CONTAINS THE WORLD:");

        for (String sentence : fileStatistics.getSentencesWithWorld()) {
            System.out.println(sentence);
        }
    }
}
