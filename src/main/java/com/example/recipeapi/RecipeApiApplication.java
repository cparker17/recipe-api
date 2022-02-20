package com.example.recipeapi;

import com.example.recipeapi.controller.RecipeController;
import com.example.recipeapi.model.Ingredient;
import com.example.recipeapi.model.Recipe;
import com.example.recipeapi.model.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RecipeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeApiApplication.class, args);
	}
}
