package com.example.recipeapi;

import com.example.recipeapi.controller.RecipeController;
import com.example.recipeapi.model.*;
import com.example.recipeapi.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

import static com.example.recipeapi.model.Role.Roles.ROLE_USER;

@SpringBootApplication
public class RecipeApiApplication {

//	@Autowired
//	CustomUserDetailsService customUserDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(RecipeApiApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadInitialData() {
//		return (args) -> {
//			Role role = new Role();
//			role.setRole(ROLE_USER);
//
//			CustomUserDetails user = CustomUserDetails.builder()
//						.username("user")
//						.password("password")
//						.authorities(List.of(role))
//					.userMeta(UserMeta.builder().name("user").email("email").build())
//						.build();
//			customUserDetailsService.createNewUser(user);
//			};
//	}
}
