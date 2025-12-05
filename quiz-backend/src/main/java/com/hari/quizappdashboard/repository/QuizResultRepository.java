package com.hari.quizappdashboard.repository;

import com.hari.quizappdashboard.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {

    List<QuizResultEntity> findTop5ByOrderByCompletedAtDesc();
}
