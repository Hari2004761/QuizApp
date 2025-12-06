package com.hari.quizappdashboard.controller;

import com.hari.quizappdashboard.entity.QuizQuestionEntity;
import com.hari.quizappdashboard.entity.QuizResultEntity;
import com.hari.quizappdashboard.repository.QuizQuestionRepository;
import com.hari.quizappdashboard.repository.QuizResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class QuizApiController {

    private final QuizQuestionRepository questionRepo;
    private final QuizResultRepository resultRepo;

    public QuizApiController(QuizQuestionRepository questionRepo, QuizResultRepository resultRepo) {
        this.questionRepo = questionRepo;
        this.resultRepo = resultRepo;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam(name = "userEmail", required = false) String userEmail) {
        String normalizedEmail = normalizeEmail(userEmail);
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "userEmail is required"));
        }

        long totalQuestions = questionRepo.count();
        int totalQuizzes = (int) questionRepo.findAll().stream()
                .map(QuizQuestionEntity::getSubjectName)
                .distinct()
                .count();

        List<QuizResultEntity> results = resultRepo.findByUserEmailIgnoreCaseOrderByCompletedAtDesc(normalizedEmail);
        long totalAttempts = results.size();

        int bestScorePercent = results.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .max()
                .orElse(0);

        int avgScorePercent = results.isEmpty()
                ? 0
                : (int) Math.round(results.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .average()
                .orElse(0));

        int overallProgress = avgScorePercent;
        int progressDegrees = Math.max(0, Math.min(360, (int) Math.round(overallProgress * 3.6)));

        List<ResultSummary> recent = results.stream()
                .limit(5)
                .map(ResultSummary::from)
                .collect(Collectors.toList());

        List<ResultSummary> history = results.stream()
                .map(ResultSummary::from)
                .collect(Collectors.toList());

        SummaryResponse payload = new SummaryResponse(
                overallProgress,
                progressDegrees,
                bestScorePercent,
                avgScorePercent,
                totalAttempts,
                totalQuestions,
                totalQuizzes,
                recent,
                history
        );

        return ResponseEntity.ok(payload);
    }

    public record SummaryResponse(
            int overallProgress,
            int progressDegrees,
            int bestScorePercent,
            int avgScorePercent,
            long totalAttempts,
            long totalQuestions,
            int totalQuizzes,
            List<ResultSummary> recentResults,
            List<ResultSummary> history
    ) {}

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public record ResultSummary(
            String subjectName,
            int score,
            int total,
            int percentage,
            String completedAt
    ) {
        static ResultSummary from(QuizResultEntity entity) {
            return new ResultSummary(
                    entity.getSubjectName(),
                    entity.getScore(),
                    entity.getTotal(),
                    entity.getPercentage(),
                    formatDate(entity.getCompletedAt())
            );
        }

        private static String formatDate(LocalDateTime completedAt) {
            if (completedAt == null) return "";
            return completedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
