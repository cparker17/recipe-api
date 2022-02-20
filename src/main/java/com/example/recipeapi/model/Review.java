package com.example.recipeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private CustomUserDetails user;

    @NotNull(message = "You must include a rating with your review.")
    private Integer rating;

    @NotNull(message = "Please leave a description for your review.")
    private String description;

    public void setRating(Integer rating) {
        if (rating == null) {
            throw new IllegalStateException("A rating must be included with a review.");
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalStateException("Rating must be between 0 and 10.");
        }
        this.rating = rating;
    }

//    public String getAuthor() {
//        return user.getUsername();
//    }
}
