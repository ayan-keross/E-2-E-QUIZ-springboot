package com.example.uon.dao;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uon.model.Question;


@Repository
public interface QuestionDao extends JpaRepository<Question, Integer>{
        
    // Custom query method to find questions by category
    ArrayList<Question> findByCategory(String category);
    // Custom query method to find questions by category and difficulty
    //ArrayList<Question> findByCategoryAndDifficulty(String category, String difficulty);
    // @Query(value = "SELECT * FROM question q WHERE q.category =:category ORDER BY RAND() LIMIT :numQuestions", nativeQuery = true)
    // List<Question> findRandomQuestionsByCategory(@Param("category") String category, @Param("numQuestions") int numQuestions);

    ArrayList<Question> findByCategoryAndDifficultyLevel(String category, String difficulty);
    
}
