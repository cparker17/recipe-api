package com.example.recipeapi.services;

import com.example.recipeapi.exceptions.NoSuchRecipeException;
import com.example.recipeapi.exceptions.NoSuchReviewException;
import com.example.recipeapi.model.Recipe;
import com.example.recipeapi.model.Review;
import com.example.recipeapi.repositories.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;

    @Cacheable(value = "reviewCache", key = "#id", sync = true)
    public Review getReviewById(Long id) throws NoSuchReviewException {
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()) {
            throw new NoSuchReviewException("The review with ID " + id + " could not be found.");
        }
        return review.get();
    }

    @Cacheable(value = "reviewCache", key = "#recipeId", sync = true)
    public ArrayList<Review> getReviewByRecipeId(Long recipeId) throws NoSuchRecipeException, NoSuchReviewException {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        ArrayList<Review> reviews = new ArrayList<>(recipe.getReviews());

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("There are no reviews for this recipe.");
        }
        return reviews;
    }

    @Cacheable(value = "reviewListCache", key = "#username", sync = true)
    public ArrayList<Review> getReviewByUsername(String username) throws NoSuchReviewException {
        ArrayList<Review> reviews = reviewRepo.findByUser_Username(username);

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("No reviews could be found for username " + username);
        }

        return reviews;
    }

    public Recipe postNewReview(Review review, Long recipeId) throws NoSuchRecipeException {
        try {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            recipe.addReview(review);
            recipeService.updateRecipe(recipe);
            return recipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException("The recipe you are trying to review does not exist.");
        }
    }

    @CacheEvict(value = "reviewCache", allEntries = true)
    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);

        if (null == review) {
            throw new NoSuchReviewException("The review you are trying to delete does not exist.");
        }
        reviewRepo.deleteById(id);
        return review;
    }

    @CachePut(value="reviewCache", key="#reviewToUpdate.id")
    public Review updateReviewById(Review reviewToUpdate) throws NoSuchReviewException {
        try {
            getReviewById(reviewToUpdate.getId());
        } catch (NoSuchReviewException e) {
            throw new NoSuchReviewException("The review you are trying to update does not exist. Maybe you meant to " +
                    "create one? If not, please double check the ID you passed in.");
        }
        reviewRepo.save(reviewToUpdate);
        return reviewToUpdate;
    }
}
