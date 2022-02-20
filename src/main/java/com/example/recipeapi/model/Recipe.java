package com.example.recipeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "You must give a name for your recipe.")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "You must include how long the recipe takes to make.")
    @Column(nullable = false)
    private Integer minutesToMake;

    @NotNull(message = "You must include a difficulty rating.")
    @Column(nullable = false)
    private Integer difficultyRating;

    @Column
    private Integer averageReviewRating;

    @ManyToOne(optional = false)
    @JoinColumn
    //@JsonIgnore
    private CustomUserDetails user;

    @NotNull(message = "You must include at least one ingredient.")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Collection<Ingredient> ingredients = new ArrayList<>();

    @NotNull(message = "You must include at least one step.")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Collection<Step> steps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Collection<Review> reviews;

    @Transient
    @JsonIgnore
    private URI locationURI;

    public void setDifficultyRating(int difficultyRating) {
        if (difficultyRating < 0 || difficultyRating > 10) {
            throw new IllegalStateException("Difficulty rating must be between 0 and 10.");
        }
        this.difficultyRating = difficultyRating;
    }

    public void validate() throws IllegalStateException {
        if (ingredients.size() == 0) {
            throw new IllegalStateException("You have to have at least one ingredient for you recipe!");
        } else if (steps.size() == 0) {
            throw new IllegalStateException("You have to include at least one step for your recipe!");
        }
    }

    public void generateLocationURI() {
        try {
            locationURI = new URI(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/recipes/")
                            .path(String.valueOf(id))
                            .toUriString());
        } catch (URISyntaxException e) {
            //Exception should stop here.
        }
    }

    public void setAverageReviewRating() {
        int sum = 0;
        int count = 0;

        if (reviews != null) {
            for (Review review : reviews) {
                sum += review.getRating();
                count++;
            }
            if (count == 0) {
                averageReviewRating = 0;
            } else {
                averageReviewRating = sum/count;
            }
        }
    }

    public Integer getAverageReviewRating() {
        setAverageReviewRating();
        return averageReviewRating;
    }

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    public String getAuthor() {
        return user.getUsername();
    }
}
