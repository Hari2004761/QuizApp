package com.hari.quizappdashboard.repository;

import com.hari.quizappdashboard.entity.QuizQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, Long> {

    List<QuizQuestionEntity> findBySubjectName(String subjectName);
}
