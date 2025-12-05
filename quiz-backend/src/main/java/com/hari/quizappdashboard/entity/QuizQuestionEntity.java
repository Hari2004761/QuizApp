package com.hari.quizappdashboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_question")
public class QuizQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private String category;

    @Column(length = 1000)
    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private int correctIndex; // 0-3

    public QuizQuestionEntity() {
    }

    public QuizQuestionEntity(String subjectName, String category, String questionText,
                              String option1, String option2, String option3, String option4,
                              int correctIndex) {
        this.subjectName = subjectName;
        this.category = category;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctIndex = correctIndex;
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

    public String getQuestionText() {
        return questionText;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
