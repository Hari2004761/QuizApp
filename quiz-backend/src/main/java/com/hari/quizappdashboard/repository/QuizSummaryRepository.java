package com.hari.quizappdashboard.repository;

import com.hari.quizappdashboard.entity.QuizSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizSummaryRepository extends JpaRepository<QuizSummaryEntity, Long> {

    // All distinct categories
    @Query("SELECT DISTINCT q.category FROM QuizSummaryEntity q")
    List<String> findAllCategories();

    // All summaries in one category
    List<QuizSummaryEntity> findByCategory(String category);

    // One summary by subject
    QuizSummaryEntity findBySubjectName(String subjectName);
}
