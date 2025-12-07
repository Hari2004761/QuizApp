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
import jakarta.servlet.http.HttpSession;

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

    private static class UserContext {
        private final String email;
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String country;

        UserContext(String email, String firstName, String lastName, String username, String country) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.country = country;
        }

        String fullNameOrFallback() {
            if (firstName != null && !firstName.isBlank()) {
                if (lastName != null && !lastName.isBlank()) {
                    return firstName + " " + lastName;
                }
                return firstName;
            }
            if (username != null && !username.isBlank()) return username;
            return email;
        }

        String displayName() {
            if (firstName != null && !firstName.isBlank()) return firstName;
            if (username != null && !username.isBlank()) return username;
            return email;
        }

        String countryOrUnknown() {
            return (country == null || country.isBlank()) ? "Unknown" : country;
        }

        String initials() {
            StringBuilder sb = new StringBuilder();
            if (firstName != null && !firstName.isBlank()) {
                sb.append(Character.toUpperCase(firstName.trim().charAt(0)));
            }
            if (lastName != null && !lastName.isBlank()) {
                sb.append(Character.toUpperCase(lastName.trim().charAt(0)));
            }
            if (sb.length() == 0 && username != null && !username.isBlank()) {
                sb.append(Character.toUpperCase(username.trim().charAt(0)));
            }
            if (sb.length() == 0 && email != null && !email.isBlank()) {
                sb.append(Character.toUpperCase(email.trim().charAt(0)));
            }
            return sb.length() == 0 ? "U" : sb.toString();
        }
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

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v.trim();
        }
        return null;
    }

    private UserContext resolveUserContext(HttpSession session,
                                           String userEmailParam,
                                           String firstNameParam,
                                           String lastNameParam,
                                           String usernameParam,
                                           String countryParam) {
        String email = normalizeEmail(firstNonBlank(userEmailParam, (String) session.getAttribute("userEmail"), "anonymous"));
        if (email == null) email = "anonymous";
        String first = firstNonBlank(firstNameParam, (String) session.getAttribute("firstName"));
        String last = firstNonBlank(lastNameParam, (String) session.getAttribute("lastName"));
        String username = firstNonBlank(usernameParam, (String) session.getAttribute("username"));
        String country = firstNonBlank(countryParam, (String) session.getAttribute("country"));

        session.setAttribute("userEmail", email);
        if (first != null) session.setAttribute("firstName", first);
        if (last != null) session.setAttribute("lastName", last);
        if (username != null) session.setAttribute("username", username);
        if (country != null) session.setAttribute("country", country);

        return new UserContext(email, first, last, username, country);
    }

    private String resolveUserEmail(HttpSession session, String userEmailParam) {
        String email = normalizeEmail(firstNonBlank(userEmailParam, (String) session.getAttribute("userEmail"), "anonymous"));
        if (email == null) email = "anonymous";
        session.setAttribute("userEmail", email);
        return email;
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        String cleaned = email.split(",")[0].trim();
        if (cleaned.isBlank()) return null;
        return cleaned.toLowerCase();
    }

    private List<QuizResultEntity> findResultsForUser(String email) {
        String normalized = normalizeEmail(email);
        if (normalized == null) return List.of();

        Comparator<QuizResultEntity> byCompletedAtDesc = Comparator
                .comparing(QuizResultEntity::getCompletedAt, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed();

        return resultRepo.findAll().stream()
                .filter(r -> normalized.equals(normalizeEmail(r.getUserEmail())))
                .sorted(byCompletedAtDesc)
                .collect(Collectors.toList());
    }

    // ===== DASHBOARD =====

    @GetMapping("/")
    public String dashboard(@RequestParam(name = "category", required = false) String category,
                            @RequestParam(name = "userEmail", required = false) String userEmailParam,
                            @RequestParam(name = "firstName", required = false) String firstNameParam,
                            @RequestParam(name = "lastName", required = false) String lastNameParam,
                            @RequestParam(name = "username", required = false) String usernameParam,
                            @RequestParam(name = "country", required = false) String countryParam,
                            HttpSession session,
                            Model model) {

        UserContext userCtx = resolveUserContext(session, userEmailParam, firstNameParam, lastNameParam, usernameParam, countryParam);
        String userEmail = userCtx.email;

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

        List<QuizResultEntity> allResults = findResultsForUser(userEmail);
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
        int progressDegrees = Math.max(0, Math.min(360, (int) Math.round(overallProgress * 3.6)));

        List<QuizResultEntity> recentResults = allResults;
        List<QuizResultEntity> history = allResults;

        model.addAttribute("userName", userCtx.displayName());
        model.addAttribute("fullName", userCtx.fullNameOrFallback());
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("quizSummaries", quizSummaries);
        model.addAttribute("overallProgress", overallProgress);
        model.addAttribute("progressDegrees", progressDegrees);
        model.addAttribute("userInitials", userCtx.initials());

        model.addAttribute("categories", categories);
        model.addAttribute("activeCategory",
                (category == null || category.isBlank()) ? "All" : category);

        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("bestScorePercent", bestScorePercent);
        model.addAttribute("avgScorePercent", avgScorePercent);
        model.addAttribute("recentResults", recentResults);
        model.addAttribute("history", history);

        return "dashboard";
    }

    // ===== PROFILE =====

    @GetMapping("/profile")
    public String profile(@RequestParam(name = "userEmail", required = false) String userEmailParam,
                          @RequestParam(name = "firstName", required = false) String firstNameParam,
                          @RequestParam(name = "lastName", required = false) String lastNameParam,
                          @RequestParam(name = "username", required = false) String usernameParam,
                          @RequestParam(name = "country", required = false) String countryParam,
                          HttpSession session,
                          Model model) {

        UserContext userCtx = resolveUserContext(session, userEmailParam, firstNameParam, lastNameParam, usernameParam, countryParam);
        String userEmail = userCtx.email;

        List<QuizResultEntity> allResults = findResultsForUser(userEmail);

        long totalQuestions = questionRepo.count();
        int totalQuizzes = (int) buildQuizBank().size();
        long totalAttempts = allResults.size();

        int avgScorePercent = allResults.isEmpty()
                ? 0
                : (int) Math.round(allResults.stream()
                .mapToInt(QuizResultEntity::getPercentage)
                .average()
                .orElse(0));

        model.addAttribute("fullName", userCtx.fullNameOrFallback());
        model.addAttribute("username", firstNonBlank(userCtx.username, userEmail));
        model.addAttribute("email", userEmail);
        model.addAttribute("country", userCtx.countryOrUnknown());
        model.addAttribute("firstName", userCtx.firstName);
        model.addAttribute("lastName", userCtx.lastName);
        model.addAttribute("studyGoal", "Keep learning with personalized quizzes.");
        model.addAttribute("bio", "Welcome back! Your quiz history and progress are tied to your account.");

        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("avgScorePercent", avgScorePercent);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userInitials", userCtx.initials());

        return "profile";
    }

    // ===== QUIZ PAGES =====

    @GetMapping("/quiz/{subject}")
    public String showQuiz(@PathVariable String subject,
                           @RequestParam(name = "userEmail", required = false) String userEmailParam,
                           @RequestParam(name = "firstName", required = false) String firstNameParam,
                           @RequestParam(name = "lastName", required = false) String lastNameParam,
                           @RequestParam(name = "username", required = false) String usernameParam,
                           @RequestParam(name = "country", required = false) String countryParam,
                           HttpSession session,
                           Model model) {

        UserContext userCtx = resolveUserContext(session, userEmailParam, firstNameParam, lastNameParam, usernameParam, countryParam);
        String userEmail = userCtx.email;

        Map<String, List<QuizQuestion>> quizBank = buildQuizBank();
        List<QuizQuestion> questions = quizBank.getOrDefault(subject, List.of());

        int totalSeconds = questions.size() * 30;

        model.addAttribute("subjectName", subject);
        model.addAttribute("questions", questions);
        model.addAttribute("totalTimeSeconds", totalSeconds);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("fullName", userCtx.fullNameOrFallback());

        return "quiz";
    }

    @PostMapping("/quiz/{subject}")
    public String submitQuiz(@PathVariable String subject,
                             @RequestParam Map<String, String> params,
                             @RequestParam(name = "userEmail", required = false) String userEmailParam,
                             @RequestParam(name = "firstName", required = false) String firstNameParam,
                             @RequestParam(name = "lastName", required = false) String lastNameParam,
                             @RequestParam(name = "username", required = false) String usernameParam,
                             @RequestParam(name = "country", required = false) String countryParam,
                             HttpSession session,
                             Model model) {

        UserContext userCtx = resolveUserContext(session, userEmailParam, firstNameParam, lastNameParam, usernameParam, countryParam);
        String userEmail = userCtx.email;

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

        resultRepo.save(new QuizResultEntity(subject, userEmail, score, questions.size(), LocalDateTime.now()));

        model.addAttribute("subjectName", subject);
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("questionResults", questionResults);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("fullName", userCtx.fullNameOrFallback());

        return "quiz-result";
    }

    // ===== ADD NEW QUESTION =====

    @GetMapping("/quiz/new")
    public String showCreateQuizForm(@RequestParam(name = "userEmail", required = false) String userEmailParam,
                                     HttpSession session,
                                     Model model) {
        String userEmail = resolveUserEmail(session, userEmailParam);

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
        model.addAttribute("userEmail", userEmail);

        return "add-quiz";
    }

    @PostMapping("/quiz/new")
    public String createQuiz(@RequestParam String subject,
                             @RequestParam String category,
                             @RequestParam List<String> questionTexts,
                             @RequestParam List<String> option1s,
                             @RequestParam List<String> option2s,
                             @RequestParam List<String> option3s,
                             @RequestParam List<String> option4s,
                             @RequestParam List<Integer> correctOptions,
                             @RequestParam(name = "userEmail", required = false) String userEmailParam,
                             HttpSession session) {

        String userEmail = resolveUserEmail(session, userEmailParam);

        String safeCategory = category == null || category.isBlank() ? "Other" : category.trim();
        String trimmedSubject = subject == null ? "" : subject.trim();
        int count = Math.min(questionTexts.size(),
                Math.min(Math.min(option1s.size(), option2s.size()),
                        Math.min(option3s.size(), Math.min(option4s.size(), correctOptions.size()))));

        for (int i = 0; i < count; i++) {
            String question = questionTexts.get(i);
            if (question == null || question.isBlank()) continue;

            int correctIndex = Math.max(0, Math.min(3, correctOptions.get(i) - 1));

            QuizQuestionEntity entity = new QuizQuestionEntity(
                    trimmedSubject,
                    safeCategory,
                    question.trim(),
                    option1s.get(i).trim(),
                    option2s.get(i).trim(),
                    option3s.get(i).trim(),
                    option4s.get(i).trim(),
                    correctIndex
            );

            questionRepo.save(entity);
        }

        return "redirect:/quiz/" + trimmedSubject + "?userEmail=" + userEmail;
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session) {
        session.invalidate();
        return "redirect:http://localhost:3000/auth";
    }
}
