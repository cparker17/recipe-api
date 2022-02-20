package com.example.recipeapi;

import com.example.recipeapi.model.*;
import com.example.recipeapi.repositories.RecipeRepo;
import com.example.recipeapi.repositories.UserRepo;
import org.apache.catalina.util.CustomObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@SpringBootApplication
@Profile("test")
public class RecipeMainTest implements CommandLineRunner {

    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("STARTING WITH TEST DATABASE SETUP");
        if (recipeRepo.findAll().isEmpty()) {

            Ingredient ingredient = Ingredient.builder().name("flour").state("dry").amount("2 cups").build();
            Step step1 = Step.builder().description("put flour in bowl").stepNumber(1).build();
            Step step2 = Step.builder().description("eat it?").stepNumber(2).build();

            UserMeta userMeta1 = UserMeta.builder().name("name1").email("name1@email.com").build();
            UserMeta userMeta2 = UserMeta.builder().name("name2").email("name2@email.com").build();

            CustomUserDetails user1 = CustomUserDetails.builder().username("username1")
                    .password("password1").userMeta(userMeta1).build();
            CustomUserDetails user2 = CustomUserDetails.builder().username("username2")
                    .password("password2").userMeta(userMeta2).build();

            userRepo.saveAll(Set.of(user1, user2));

            Review review = Review.builder().description("tasted pretty bad").rating(2).user(user1).build();

            Recipe recipe1 = Recipe.builder()
                    .name("test recipe")
                    .difficultyRating(10)
                    .minutesToMake(2)
                    .ingredients(Set.of(ingredient))
                    .steps(Set.of(step1, step2))
                    .user(user1)
                    .reviews(Set.of(review))
                    .build();

            recipeRepo.save(recipe1);

            ingredient.setId(null);
            Recipe recipe2 = Recipe.builder()
                    .steps(Set.of(Step.builder().description("test").build()))
                    .ingredients(Set.of(Ingredient.builder().name("test ing").amount("1").state("dry").build()))
                    .name("another test recipe")
                    .difficultyRating(10)
                    .user(user2)
                    .minutesToMake(2)
                    .build();
            recipeRepo.save(recipe2);

            Recipe recipe3 = Recipe.builder()
                    .steps(Set.of(Step.builder().description("test 2").build()))
                    .ingredients(Set.of(Ingredient.builder().name("test ing 2").amount("2").state("wet").build()))
                    .name("another another test recipe potato")
                    .difficultyRating(5)
                    .user(user1)
                    .minutesToMake(2)
                    .build();
            recipeRepo.save(recipe3);

            System.out.println("FINISHED TEST DATABASE SETUP");
        }
    }
}
