package com.example.recipeapi.repositories;

import com.example.recipeapi.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

    ArrayList<Recipe> findByNameContaining(String name);
    Recipe findByName(String name);
    ArrayList<Recipe> findByAverageReviewRating(int rating);
    ArrayList<Recipe> findAllByUser_Username(String username);
}
