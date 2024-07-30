package com.library.booklend.Entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "categorie")
@Data
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
