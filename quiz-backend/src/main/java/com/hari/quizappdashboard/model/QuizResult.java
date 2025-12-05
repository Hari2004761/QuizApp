package com.hari.quizappdashboard.model;

import java.time.LocalDateTime;

public class QuizResult {
    private final String subjectName;
    private final int score;
    private final int total;
    private final LocalDateTime completedAt;

    public QuizResult(String subjectName, int score, int total, LocalDateTime completedAt) {
        this.subjectName = subjectName;
        this.score = score;
        this.total = total;
        this.completedAt = completedAt;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public int getPercentage() {
        if (total == 0) return 0;
        return (int) Math.round(score * 100.0 / total);
    }
}
