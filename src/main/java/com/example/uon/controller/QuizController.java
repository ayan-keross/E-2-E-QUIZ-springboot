package com.example.uon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uon.model.QuestionWrapper;
import com.example.uon.model.Quiz;
import com.example.uon.model.Response;
import com.example.uon.service.QuizService;
import com.example.uon.utility.ApiResponse;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/quiz")
// This controller will handle quiz-related endpoints
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Endpoint to get a welcome message for the quiz service
    @GetMapping("/welcome")
    public String getQuizWelcome() {
        return quizService.getQuizWelcome();
    }

    // @PostMapping("create")
    // public ResponseEntity<ApiResponse<Quiz>> createQuiz(@RequestParam String category, @RequestParam int numQuestions, @RequestParam String title) {
    //     // This method will handle the creation of a quiz based on the category and number of questions
    //     return quizService.createQuiz(category, numQuestions, title);

    // }

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam Map<String, String> allParams) {
        // This method will handle the creation of a quiz based on the category and number of questions
        System.out.println("Params: " + allParams);

        return quizService.createNewQuizDynamic(allParams);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        // This method will handle fetching a quiz by its ID
        // You can implement the logic to retrieve the quiz from the database
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<ApiResponse<Integer>> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> answers) {
        // This method will handle submitting a quiz with the provided answers
        // You can implement the logic to process the submitted answers
        // For now, we will return a placeholder response
        return quizService.calculateResult(id, answers);
    }
    
    

}
