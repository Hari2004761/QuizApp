package com.hari.quizappdashboard.model;

public class QuizSummary {

    private final String subjectName;
    private final int total;
    private final int completed; // not really used, can be same as total

    public QuizSummary(String subjectName, int total, int completed) {
        this.subjectName = subjectName;
        this.total = total;
        this.completed = completed;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotal() {
        return total;
    }

    public int getCompleted() {
        return completed;
    }
}
