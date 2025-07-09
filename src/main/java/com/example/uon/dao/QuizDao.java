package com.example.uon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uon.model.Quiz;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {
    // This class will handle database operations related to quizzes
    // You can define custom query methods here if needed

    
    
}
