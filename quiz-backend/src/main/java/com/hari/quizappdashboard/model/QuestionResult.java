package com.hari.quizappdashboard.model;

public class QuestionResult {

    private final QuizQuestion question;
    private final Integer chosenIndex; // null if not answered

    public QuestionResult(QuizQuestion question, Integer chosenIndex) {
        this.question = question;
        this.chosenIndex = chosenIndex;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public Integer getChosenIndex() {
        return chosenIndex;
    }

    public boolean isCorrect() {
        return chosenIndex != null && chosenIndex == question.getCorrectIndex();
    }

    public boolean getCorrect() {
        return isCorrect();
    }
}
