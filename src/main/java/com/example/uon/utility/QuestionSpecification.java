package com.example.uon.utility;

// ProductSpecifications.java
import org.springframework.data.jpa.domain.Specification;

import com.example.uon.model.Question;


public class QuestionSpecification {

    public static Specification<Question> hasQuestionTitleLike(String questionTitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("questionTitle")), "%" + questionTitle.toLowerCase() + "%");
    }

    public static Specification<Question> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Question> hasDifficultyLevel(String difficultyLevel) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("difficultyLevel")), difficultyLevel.toLowerCase());
    }


}
