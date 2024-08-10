package com.tolik.inputoutput.fileanalyzer;

import java.util.ArrayList;

public class FileStatistics {

    private int worldCount;

    private ArrayList<String> sentencesWithWorld;

    public ArrayList<String> getSentencesWithWorld() {
        return sentencesWithWorld;
    }

    public void setSentencesWithWorld(ArrayList<String> sentencesWithWorld) {
        this.sentencesWithWorld = sentencesWithWorld;
    }

    public int getWorldCount() {
        return worldCount;
    }

    public void setWorldCount(int worldCount) {
        this.worldCount = worldCount;
    }
}
