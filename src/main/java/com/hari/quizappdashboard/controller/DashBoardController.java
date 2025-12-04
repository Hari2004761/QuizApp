package com.hari.quizappdashboard.controller;

import com.hari.quizappdashboard.entity.QuizQuestionEntity;
import com.hari.quizappdashboard.entity.QuizResultEntity;
import com.hari.quizappdashboard.model.QuestionResult;
import com.hari.quizappdashboard.model.QuizQuestion;
import com.hari.quizappdashboard.model.QuizSummary;
import com.hari.quizappdashboard.repository.QuizQuestionRepository;
import com.hari.quizappdashboard.repository.QuizResultRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashBoardController {

    private final QuizQuestionRepository questionRepo;
    private final QuizResultRepository resultRepo;

    public DashBoardController(QuizQuestionRepository questionRepo,
                               QuizResultRepository resultRepo) {
        this.questionRepo = questionRepo;
        this.resultRepo = resultRepo;
    }

    private Map<String, List<QuizQuestion>> buildQuizBank() {
        List<QuizQuestionEntity> entities = questionRepo.findAll();

        Map<String, List<QuizQuestion>> quizBank = new LinkedHashMap<>();

        for (QuizQuestionEntity e : entities) {
            QuizQuestion q = new QuizQuestion(
                    (int) (long) e.getId(),
                    e.getQuestionText(),
                    List.of(e.getOption1(), e.getOption2(), e.getOption3(), e.getOption4()),
                    e.getCorrectIndex()
            );

            quizBank.computeIfAbsent(e.getSubjectName(), k -> new ArrayList<>())
                    .add(q);
        }

        for (List<QuizQuestion> list : quizBank.values()) {
            list.sort(Comparator.comparingInt(QuizQuestion::getId));
        }

        return quizBank;
    }

    private Map<String, String> buildCategoryMap() {
        List<QuizQuestionEntity> entities = questionRepo.findAll();
        Map<String, String> categories = new HashMap<>();
        for (QuizQuestionEntity e : entities) {
            categories.putIfAbsent(e.getSubjectName(), e.getCategory());
        }
        return categories;
    }

    // ===== DASHBOARD =====

    @GetMapping("/")
    public String dashboard(@RequestParam(name = "category", required = false) String category,
                            Model model) {

        Map<String, List<QuizQuestion>> quizBank = buildQuizBank();
        Map<String, String> quizCategories = buildCategoryMap();

        Set<String> categories = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        categories.addAll(quizCategories.values());

        List<QuizSummary> quizSummaries = new ArrayList<>();
        for (Map.Entry<String, List<QuizQuestion>> entry : quizBank.entrySet()) {
            String name = entry.getKey();
            String cat = quizCategories.getOrDefault(name, "Other");

            if (category != null && !category.isBlank() && !"All".equalsIgnoreCase(category)) {
                if (!cat.equalsIgnoreCase(category)) continue;
            }

            int count = entry.getValue().size();
            quizSummaries.add(new QuizSummary(name, count, count));
        }

        long totalQuestions = questionRepo.count();
        int totalQuizzes = quizBank.size();

        List<QuizResultEntity> allResults = resultRepo.findAll();
        long totalAttempts = allResults.size();

        int bestScorePercent = allResults.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .max()
                .orElse(0);

        int avgScorePercent = allResults.isEmpty()
                ? 0
                : (int) Math.round(allResults.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .average()
                .orElse(0));

        int overallProgress = avgScorePercent;

        List<QuizResultEntity> recentResults = resultRepo.findTop5ByOrderByCompletedAtDesc();

        String shortName = "Hari";
        String fullName = "Hari Narayanan";

        model.addAttribute("userName", shortName);
        model.addAttribute("fullName", fullName);
        model.addAttribute("quizSummaries", quizSummaries);
        model.addAttribute("overallProgress", overallProgress);

        model.addAttribute("categories", categories);
        model.addAttribute("activeCategory",
                (category == null || category.isBlank()) ? "All" : category);

        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("bestScorePercent", bestScorePercent);
        model.addAttribute("avgScorePercent", avgScorePercent);
        model.addAttribute("recentResults", recentResults);

        return "dashboard";
    }

    // ===== PROFILE =====

    @GetMapping("/profile")
    public String profile(Model model) {

        List<QuizResultEntity> allResults = resultRepo.findAll();

        long totalQuestions = questionRepo.count();
        int totalQuizzes = (int) buildQuizBank().size();
        long totalAttempts = allResults.size();

        int avgScorePercent = allResults.isEmpty()
                ? 0
                : (int) Math.round(allResults.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .average()
                .orElse(0));

        model.addAttribute("fullName", "Hari Narayanan");
        model.addAttribute("username", "hari_n");
        model.addAttribute("email", "hari@example.com");
        model.addAttribute("country", "Hungary");
        model.addAttribute("studyGoal", "Improve programming, economics, and history knowledge.");
        model.addAttribute("bio", "2nd-year Computer Science student who likes building full-stack apps and learning by doing quizzes.");

        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("avgScorePercent", avgScorePercent);

        return "profile";
    }

    // ===== QUIZ PAGES =====

    @GetMapping("/quiz/{subject}")
    public String showQuiz(@PathVariable String subject, Model model) {

        Map<String, List<QuizQuestion>> quizBank = buildQuizBank();
        List<QuizQuestion> questions = quizBank.getOrDefault(subject, List.of());

        int totalSeconds = questions.size() * 30;

        model.addAttribute("subjectName", subject);
        model.addAttribute("questions", questions);
        model.addAttribute("totalTimeSeconds", totalSeconds);

        return "quiz";
    }

    @PostMapping("/quiz/{subject}")
    public String submitQuiz(@PathVariable String subject,
                             @RequestParam Map<String, String> params,
                             Model model) {

        Map<String, List<QuizQuestion>> quizBank = buildQuizBank();
        List<QuizQuestion> questions = quizBank.getOrDefault(subject, List.of());

        int score = 0;
        List<QuestionResult> questionResults = new ArrayList<>();

        for (QuizQuestion q : questions) {
            String key = "q" + q.getId();
            String value = params.get(key);

            Integer chosenIndex = null;
            if (value != null && !value.isBlank()) {
                chosenIndex = Integer.parseInt(value);
            }

            QuestionResult qr = new QuestionResult(q, chosenIndex);
            questionResults.add(qr);

            if (qr.isCorrect()) {
                score++;
            }
        }

        resultRepo.save(new QuizResultEntity(subject, score, questions.size(), LocalDateTime.now()));

        model.addAttribute("subjectName", subject);
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("questionResults", questionResults);

        return "quiz-result";
    }

    // ===== ADD NEW QUESTION =====

    @GetMapping("/quiz/new")
    public String showCreateQuizForm(Model model) {
        List<QuizQuestionEntity> entities = questionRepo.findAll();
        Set<String> subjects = entities.stream()
                .map(QuizQuestionEntity::getSubjectName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> categories = entities.stream()
                .map(QuizQuestionEntity::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute("existingSubjects", subjects);
        model.addAttribute("existingCategories", categories);

        return "add-quiz";
    }

    @PostMapping("/quiz/new")
    public String createQuiz(@RequestParam String subject,
                             @RequestParam String category,
                             @RequestParam String question,
                             @RequestParam String option1,
                             @RequestParam String option2,
                             @RequestParam String option3,
                             @RequestParam String option4,
                             @RequestParam int correctOption) {

        int correctIndex = Math.max(0, Math.min(3, correctOption - 1));

        QuizQuestionEntity entity = new QuizQuestionEntity(
                subject,
                category.isBlank() ? "Other" : category,
                question,
                option1, option2, option3, option4,
                correctIndex
        );

        questionRepo.save(entity);

        return "redirect:/quiz/" + subject;
    }
}
