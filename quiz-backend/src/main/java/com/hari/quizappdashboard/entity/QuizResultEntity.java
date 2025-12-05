package com.hari.quizappdashboard.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_result")
public class QuizResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private int score;
    private int total;
    private LocalDateTime completedAt;

    public QuizResultEntity() {
    }

    public QuizResultEntity(String subjectName, int score, int total, LocalDateTime completedAt) {
        this.subjectName = subjectName;
        this.score = score;
        this.total = total;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
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
