package com.example.recipeapi.controller;

import com.example.recipeapi.exceptions.NoSuchUserException;
import com.example.recipeapi.model.CustomUserDetails;
import com.example.recipeapi.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    CustomUserDetailsService userDetailsService;

    @GetMapping("/user")
    public CustomUserDetails getUser(@CurrentSecurityContext Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }

    @PostMapping("/user")
    public ResponseEntity<?> createNewUser(@RequestBody CustomUserDetails customUserDetails) {
        try {
            return ResponseEntity.ok(userDetailsService.createNewUser(customUserDetails));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PatchMapping("/user")
    @PreAuthorize("hasPermission(#customUserDetails, 'CustomerUserDetails', 'edit')")
    public ResponseEntity<?> updateUser(@RequestBody CustomUserDetails customUserDetails) {
        try {
            return ResponseEntity.ok(userDetailsService.updateUser(customUserDetails));
        } catch (NoSuchUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
