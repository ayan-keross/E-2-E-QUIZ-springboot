package com.example.uon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uon.model.Question;
import com.example.uon.service.QuestionService;
import com.example.uon.utility.ApiResponse;



@RestController
@RequestMapping("api/question")

public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/all-questions")
    public ResponseEntity<ApiResponse<Question>> getAllQuestions (){
        return questionService.getAllQuestions();
    }
    
    // Endpoint to get questions by category
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Question>> getQuestionsByCategory(@PathVariable String category) {
        // Logic to filter questions by category
        return questionService.getQuestionsByCategory(category); // Return filtered questions
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Question>> createQuestion(@RequestBody Question question) {
        // Logic to create a new question
        return questionService.createQuestion(question); // Return the created question
    }   

    
}
