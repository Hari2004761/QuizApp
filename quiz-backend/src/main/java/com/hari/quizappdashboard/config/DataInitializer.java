package com.hari.quizappdashboard.config;

import com.hari.quizappdashboard.entity.QuizQuestionEntity;
import com.hari.quizappdashboard.repository.QuizQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final QuizQuestionRepository questionRepo;

    public DataInitializer(QuizQuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public void run(String... args) {

        if (questionRepo.count() > 0) {
            return; // already initialized
        }

        // Software Development (Programming)
        questionRepo.save(new QuizQuestionEntity(
                "Software Development", "Programming",
                "Which language is mainly used in Spring Boot?",
                "Python", "Java", "C", "Rust", 1));

        questionRepo.save(new QuizQuestionEntity(
                "Software Development", "Programming",
                "What does MVC stand for?",
                "Model View Controller", "Main View Client",
                "Model Version Control", "Multi View Code", 0));

        questionRepo.save(new QuizQuestionEntity(
                "Software Development", "Programming",
                "Which annotation starts a Spring Boot application?",
                "@EnableSpring", "@SpringBoot", "@SpringBootApplication", "@SpringApp", 2));

        questionRepo.save(new QuizQuestionEntity(
                "Software Development", "Programming",
                "Which dependency is commonly used for building web apps in Spring Boot?",
                "spring-boot-starter-web", "spring-boot-starter-mail",
                "spring-boot-starter-test", "spring-boot-starter-aop", 0));

        // Economics (Business)
        questionRepo.save(new QuizQuestionEntity(
                "Economics", "Business",
                "What is 'demand'?",
                "Willingness to sell", "Willingness to buy",
                "Cost of production", "Government tax", 1));

        questionRepo.save(new QuizQuestionEntity(
                "Economics", "Business",
                "What is GDP?",
                "Gross Domestic Product", "Global Development Plan",
                "Government Domestic Policy", "General Demand Price", 0));

        questionRepo.save(new QuizQuestionEntity(
                "Economics", "Business",
                "What happens to price when supply decreases and demand stays the same?",
                "Price falls", "Price rises", "Price stays the same", "Price becomes zero", 1));

        // History (Humanities)
        questionRepo.save(new QuizQuestionEntity(
                "History", "Humanities",
                "In which year did World War II end?",
                "1942", "1945", "1939", "1950", 1));

        questionRepo.save(new QuizQuestionEntity(
                "History", "Humanities",
                "The pyramids are located in:",
                "Greece", "India", "Egypt", "Mexico", 2));

        questionRepo.save(new QuizQuestionEntity(
                "History", "Humanities",
                "Which empire was ruled by Julius Caesar?",
                "Greek Empire", "Roman Empire",
                "Ottoman Empire", "British Empire", 1));
    }
}
