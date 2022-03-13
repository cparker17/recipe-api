package com.example.recipeapi.model;

import lombok.*;

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
public class Step implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private static final long serialVersionUID = -8201478179274108373L;

    @NotNull
    private int stepNumber;

    @NotNull
    private String description;
}
