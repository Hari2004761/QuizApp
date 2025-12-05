package com.hari.quizappdashboard.model;

import java.util.List;

public class QuizQuestion {
    private int id;
    private String text;
    private List<String> options;
    private int correctIndex;

    public QuizQuestion(int id, String text, List<String> options, int correctIndex) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
