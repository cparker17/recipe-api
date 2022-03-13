package com.example.recipeapi.services;

import com.example.recipeapi.exceptions.NoSuchRecipeException;
import com.example.recipeapi.model.Recipe;
import com.example.recipeapi.repositories.RecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class RecipeService {
    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    CacheManager cacheManager;

    @Transactional
    public Recipe createNewRecipe(Recipe recipe) throws IllegalStateException {
        recipe.validate();
        recipe = recipeRepo.save(recipe);
        recipe.generateLocationURI();
        return recipe;
    }

    @Cacheable(value = "recipeCache", key = "#id", sync = true)
    public Recipe getRecipeById(Long id) throws NoSuchRecipeException {
        Optional<Recipe> recipeOptional = recipeRepo.findById(id);

        if (recipeOptional.isEmpty()) {
            throw new NoSuchRecipeException("No recipe with ID " + id + " could be found.");
        }

        Recipe recipe = recipeOptional.get();
        recipe.generateLocationURI();
        return recipe;
    }

    @Cacheable(value = "recipeListCache", key = "#name", sync = true)
    public ArrayList<Recipe> getRecipesByName(String name) throws NoSuchRecipeException {
        ArrayList<Recipe> matchingRecipes = recipeRepo.findByNameContaining(name);

        if (matchingRecipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }

        for (Recipe r : matchingRecipes) {
            r.generateLocationURI();
        }
        return matchingRecipes;
    }

    @Cacheable(value = "recipeListCache", key = "#name", sync = true)
    public ArrayList<Recipe> getAllRecipes() throws NoSuchRecipeException {
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepo.findAll());

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("There are no recipes yet :( feel free to add one though");
        }
        return recipes;
    }

    @Transactional
    @CacheEvict(value = "recipeCache", allEntries = true)
    public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException {
        try {
            Recipe recipe = getRecipeById(id);
            recipeRepo.deleteById(id);
            return recipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException(e.getMessage() + " Could not delete.");
        }
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe) throws NoSuchRecipeException {
        if (recipeRepo.findById(recipe.getId()).isEmpty()) {
            throw new NoSuchRecipeException("The recipe you passed in did not have an ID found in the database." +
                    " Double check that it is correct. Or maybe you meant to POST a recipe not PATCH one.");
        }
        recipe.validate();
        Recipe savedRecipe = recipeRepo.save(recipe);
        savedRecipe.generateLocationURI();
        return savedRecipe;
    }

    public Recipe getRecipeByName(String name) {
        return recipeRepo.findByName(name);
    }

    public List<Recipe> getRecipesByMinimumRAverageRating(Integer rating) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findAll();
        recipes.removeIf(recipe -> recipe.getAverageReviewRating() <= rating);
        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("There aren't any recipes above this average rating.");
        }
        return recipes;
    }

    public List<Recipe> getRecipesByRating(Integer rating) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findByAverageReviewRating(rating);
        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("There aren't any recipes with this rating");
        }
        return recipes;
    }

    public List<Recipe> getRecipesByUsername(String username) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findAllByUser_Username(username);
        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("There aren't any recipes submitted by this user.");
        }
        return recipes;
    }
}
