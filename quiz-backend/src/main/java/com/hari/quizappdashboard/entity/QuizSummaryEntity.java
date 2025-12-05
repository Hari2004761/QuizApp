package com.hari.quizappdashboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_summary")
public class QuizSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private String category;
    private int totalQuestions;

    public QuizSummaryEntity() {
    }

    public QuizSummaryEntity(String subjectName, String category, int totalQuestions) {
        this.subjectName = subjectName;
        this.category = category;
        this.totalQuestions = totalQuestions;
    }

    public Long getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
