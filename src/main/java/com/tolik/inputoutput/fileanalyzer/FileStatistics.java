package com.tolik.inputoutput.fileanalyzer;

import java.util.List;

public class FileStatistics {

    private int worldCount;

    private List<String> sentencesWithWorld;

    public List<String> getSentencesWithWorld() {
        return sentencesWithWorld;
    }

    public void setSentencesWithWorld(List<String> sentencesWithWorld) {
        this.sentencesWithWorld = sentencesWithWorld;
    }

    public int getWorldCount() {
        return worldCount;
    }

    public void setWorldCount(int worldCount) {
        this.worldCount = worldCount;
    }
}
