package com.example.recipeapi.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private static final long serialVersionUID = 6098697371161055563L;

    @NotNull
    private String name;

    @NotNull
    private String amount;

    @NotNull
    private String state;
}
