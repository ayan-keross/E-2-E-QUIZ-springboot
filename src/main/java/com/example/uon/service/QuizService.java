package com.example.uon.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.uon.dao.QuestionDao;
import com.example.uon.dao.QuizDao;
import com.example.uon.model.Question;
import com.example.uon.model.QuestionWrapper;
import com.example.uon.model.Quiz;
import com.example.uon.model.Response;
import com.example.uon.utility.ApiResponse;
import com.example.uon.utility.QuestionSpecification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao; // Assuming you have a QuizDao for database operations
    @Autowired
    QuestionDao questionDao; // Assuming you have a QuestionDao for fetching questions
    // Add your quiz-related business logic here

    @PersistenceContext
    private EntityManager entityManager;

    public String getQuizWelcome() {
        return "Welcome to the Quiz Service!";
    }

    // public ResponseEntity<String> createNewQuizDynamic(Map<String, String> allParams) {
    //     String query = questionDao.findByCategoryAndDifficultyLevel(allParams.get("category"),
    //             allParams.get("difficultyLevel")).toString();
    //     return ResponseEntity.ok(query);
    // }

    // public ResponseEntity<ApiResponse<Quiz>> createQuizDynamic(Map<String, String> allParams) {
    //     // String sqlQuery = "SELECT * FROM question q WHERE ";

    //     // for (Map.Entry<String, String> entry : allParams.entrySet()) {
    //     // String key = entry.getKey();
    //     // String value = entry.getValue();
    //     // sqlQuery += "q." + key + " = :" + value + " AND ";
    //     // }
    //     // // Remove the last " AND "
    //     // sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 5);
    //     // System.out.println("SQL Query: " + sqlQuery);

    //     // List<Question> questions = entityManager.createQuery(sqlQuery,
    //     // Question.class).getResultList();

    //     StringBuilder jpql = new StringBuilder("SELECT * FROM Question q WHERE 1=1");
    //     for (String key : allParams.keySet()) {
    //         jpql.append(" AND q.").append(key).append(" = :").append(key);
    //     }
    //     TypedQuery<Question> query = entityManager.createQuery(jpql.toString(), Question.class);
    //     for (Map.Entry<String, String> entry : allParams.entrySet()) {
    //         query.setParameter(entry.getKey(), entry.getValue());
    //     }

    //     List<Question> questions = query.getResultList();

    //     Quiz quiz = new Quiz();
    //     quiz.setTitle(allParams.get("title"));
    //     quiz.setTitle("GKQuiz2");
    //     quiz.setQuestions(questions);

    //     ApiResponse<Quiz> response = new ApiResponse<>(HttpStatus.CREATED.value(), List.of(quiz),
    //             "Question created successfully");
    //     return ResponseEntity.status(HttpStatus.CREATED).body(response);
    //     // return createQuiz(category, numQuestions, title);
    // }

    public List<Question> findQuestions(String questionTitle, String category, String difficultyLevel) {
        Specification<Question> spec = Specification.where(null);

        if (questionTitle != null && !questionTitle.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasQuestionTitleLike(questionTitle));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasCategory(category));
        }

        if (difficultyLevel != null && !difficultyLevel.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasDifficultyLevel(difficultyLevel));
        }
        return questionDao.findAll(spec);
    }

    public ResponseEntity<ApiResponse<Quiz>> createQuiz(String category, int numQuestions, String title) {
        List<Question> all = questionDao.findByCategory(category);
        Collections.shuffle(all);
        List<Question> questions = all.stream().limit(numQuestions).toList();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);

        // Save the quiz to the database
        try {
            Quiz createdQuiz = quizDao.save(quiz);
            ApiResponse<Quiz> response = new ApiResponse<>(HttpStatus.CREATED.value(), List.of(createdQuiz),
                    "Question created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Quiz> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
                    "Failed to create question");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse<QuestionWrapper>> getQuizQuestions(int id) {
        Quiz quiz = quizDao.findById(id).orElse(null);
        if (quiz == null) {
            ApiResponse<QuestionWrapper> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null,
                    "Quiz not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<QuestionWrapper> wrappers = quiz.getQuestions().stream()
                .map(q -> new QuestionWrapper(
                        q.getId(),
                        q.getQuestionTitle(),
                        q.getOption1(),
                        q.getOption2(),
                        q.getOption3(),
                        q.getOption4(),
                        q.getDifficultyLevel(),
                        q.getCategory()))
                .toList();

        ApiResponse<QuestionWrapper> response = new ApiResponse<>(HttpStatus.OK.value(), wrappers,
                "Quiz questions retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<Integer>> calculateResult(int id, List<Response> answers) {
        Quiz quiz = quizDao.findById(id).orElse(null);
        if (quiz == null) {
            ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "Quiz not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        int score = 0;
        for (Response answer : answers) {
            Question question = questionDao.findById(answer.getQuestionId()).orElse(null);
            if (question != null && question.getRightAnswer().equals(answer.getAnswer())) {
                score++;
            }
        }

        ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK.value(), List.of(score),
                "Quiz submitted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> createNewQuizDynamic(String questionTitle, String category, String difficultyLevel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNewQuizDynamic'");
    }
}
