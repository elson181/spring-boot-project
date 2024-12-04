package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Bi-directional relationship with Book
    @ManyToMany(mappedBy = "authors")
    @JsonBackReference
    private Set<Books> books = new HashSet<>();

    // Constructors, getters, and setters
    public Author() {}

    public Author(String name) {
        this.name = name;
    }

}
