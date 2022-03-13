package com.example.recipeapi.services;

import com.example.recipeapi.exceptions.NoSuchUserException;
import com.example.recipeapi.model.CustomUserDetails;
import com.example.recipeapi.model.Role;
import com.example.recipeapi.repositories.UserRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails optionalUser = userRepo.findByUsername(username);

        if (optionalUser == null) {
            throw new UsernameNotFoundException(username + " is not a valid username! Check for typos and try again.");
        }

        return optionalUser;
    }

    @Transactional(readOnly = true)
    public CustomUserDetails getUserByUserId(Long userId) throws EntityNotFoundException {
        CustomUserDetails user = userRepo.getById(userId);

        //call unproxy() to ensure all related entities are loaded—no lazy load exceptions.
        return (CustomUserDetails) Hibernate.unproxy(user);
    }

    @Transactional(readOnly = true)
    public CustomUserDetails getUser(String username) throws EntityNotFoundException  {
        return userRepo.findByUsername(username);
    }

    public CustomUserDetails createNewUser(CustomUserDetails customUserDetails) {
        customUserDetails.setId(null);
        customUserDetails.getAuthorities().forEach(a -> a.setId(null));

        //override or set user settings to correct values
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setCredentialsNonExpired(true);
        customUserDetails.setEnabled(true);
        customUserDetails.setAuthorities(new HashSet<Role>(Collections.singleton(new Role(Role.Roles.ROLE_USER))));
        
        checkPassword(customUserDetails.getPassword());
        customUserDetails.setPassword(encoder.encode(customUserDetails.getPassword()));
        try {
            return userRepo.save(customUserDetails);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password is too short. Must be longer than 6 characters");
        }
    }

    public CustomUserDetails updateUser(CustomUserDetails customUserDetails) throws NoSuchUserException {
        if(userRepo.findByUsername(customUserDetails.getUsername()) == null) {
            throw new NoSuchUserException("User with this username does not exist.");
        }
        return userRepo.save(customUserDetails);
    }
}
