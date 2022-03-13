package com.example.recipeapi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_meta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMeta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final long serialVersionUID = 50165290078679576L;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
}

