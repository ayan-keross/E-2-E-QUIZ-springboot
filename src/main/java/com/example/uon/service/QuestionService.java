package com.example.uon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.uon.dao.QuestionDao;
import com.example.uon.model.Question;
import com.example.uon.utility.ApiResponse;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    // Method to get all questions
    // This method will fetch all questions from the database and return them in a response entity
    public ResponseEntity<ApiResponse<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.OK.value(), questions, "Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Failed to fetch questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Method to get questions by category
    // This method will filter questions based on the provided category and return the filtered list
    public ResponseEntity<ApiResponse<Question>> getQuestionsByCategory(String category) {
        // Logic to filter questions by category
        if (category == null || category.isEmpty()) {
                ApiResponse<Question> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, "Category cannot be null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        try {
            List<Question> questions = questionDao.findByCategory(category);
            if (questions.isEmpty()) {
                ApiResponse<Question> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "No questions found for the specified category");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.OK.value(), questions, "Questions fetched successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing request");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
    }
    
    // Method to create a new question
    // This method will save the question to the database and return the created question
    public ResponseEntity<ApiResponse<Question>> createQuestion(Question question) {
        // Validate the question object if necessary
        if (question == null || question.getQuestionTitle() == null || question.getRightAnswer() == null) {
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, "Invalid question data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            Question createdQuestion = questionDao.save(question);
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.CREATED.value(), List.of(createdQuestion), "Question created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<Question> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Failed to create question");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

}
