package com.hari.quizappdashboard.config;

import com.hari.quizappdashboard.entity.QuizQuestionEntity;
import com.hari.quizappdashboard.repository.QuizQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final QuizQuestionRepository questionRepo;

    public DataInitializer(QuizQuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public void run(String... args) {

        // Seed questions if they are missing. This runs idempotently on each startup.

        // Software Development (Programming)
        List<QuizQuestionEntity> devQuestions = List.of(
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "Which language is mainly used in Spring Boot?",
                        "Python", "Java", "C", "Rust", 1),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "What does MVC stand for?",
                        "Model View Controller", "Main View Client",
                        "Model Version Control", "Multi View Code", 0),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "Which annotation starts a Spring Boot application?",
                        "@EnableSpring", "@SpringBoot", "@SpringBootApplication", "@SpringApp", 2),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "Which dependency is commonly used for building web apps in Spring Boot?",
                        "spring-boot-starter-web", "spring-boot-starter-mail",
                        "spring-boot-starter-test", "spring-boot-starter-aop", 0),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "What HTTP method is idempotent and often used for fetching resources?",
                        "POST", "GET", "PATCH", "CONNECT", 1),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "Which principle does the 'O' in SOLID represent?",
                        "Object Pooling", "Open-Closed Principle", "Overriding", "Optional Chaining", 1),
                new QuizQuestionEntity(
                        "Software Development", "Programming",
                        "What does CI/CD primarily automate?",
                        "Customer interviews", "Code formatting", "Build, test, and deployment pipelines", "API rate limiting", 2)
        );

        // Economics (Business)
        List<QuizQuestionEntity> econQuestions = List.of(
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "What is 'demand'?",
                        "Willingness to sell", "Willingness to buy",
                        "Cost of production", "Government tax", 1),
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "What is GDP?",
                        "Gross Domestic Product", "Global Development Plan",
                        "Government Domestic Policy", "General Demand Price", 0),
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "What happens to price when supply decreases and demand stays the same?",
                        "Price falls", "Price rises", "Price stays the same", "Price becomes zero", 1),
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "Inflation primarily measures:",
                        "Growth of technology", "Increase in general price levels", "Changes in unemployment", "Rise of stock markets", 1),
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "A market with a single seller is called:",
                        "Perfect competition", "Monopoly", "Oligopoly", "Duopoly", 1),
                new QuizQuestionEntity(
                        "Economics", "Business",
                        "Which policy does a central bank use to control money supply?",
                        "Fiscal policy", "Monetary policy", "Trade policy", "Immigration policy", 1)
        );

        // History (Humanities)
        List<QuizQuestionEntity> historyQuestions = List.of(
                new QuizQuestionEntity(
                        "History", "Humanities",
                        "In which year did World War II end?",
                        "1942", "1945", "1939", "1950", 1),
                new QuizQuestionEntity(
                        "History", "Humanities",
                        "The pyramids are located in:",
                        "Greece", "India", "Egypt", "Mexico", 2),
                new QuizQuestionEntity(
                        "History", "Humanities",
                        "Which empire was ruled by Julius Caesar?",
                        "Greek Empire", "Roman Empire",
                        "Ottoman Empire", "British Empire", 1),
                new QuizQuestionEntity(
                        "History", "Humanities",
                        "The Renaissance began in which country?",
                        "France", "Italy", "England", "Spain", 1),
                new QuizQuestionEntity(
                        "History", "Humanities",
                        "The Cold War was primarily a tension between:",
                        "USA and USSR", "France and Germany", "China and Japan", "UK and India", 0)
        );

        devQuestions.forEach(this::saveIfMissing);
        econQuestions.forEach(this::saveIfMissing);
        historyQuestions.forEach(this::saveIfMissing);
    }

    private void saveIfMissing(QuizQuestionEntity entity) {
        if (questionRepo.existsByQuestionText(entity.getQuestionText())) {
            return;
        }
        questionRepo.save(entity);
    }
}
