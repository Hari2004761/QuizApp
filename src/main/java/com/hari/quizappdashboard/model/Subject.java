package com.hari.quizappdashboard.model;

public class Subject {
    private String name;
    private int notesDone;
    private int notesTotal;
    private String teacher;

    public Subject(String name, int notesDone, int notesTotal, String teacher) {
        this.name = name;
        this.notesDone = notesDone;
        this.notesTotal = notesTotal;
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public int getNotesDone() {
        return notesDone;
    }

    public int getNotesTotal() {
        return notesTotal;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getNotesPercentage() {
        if (notesTotal == 0) return 0;
        return (int) Math.round((notesDone * 100.0) / notesTotal);
    }
}
