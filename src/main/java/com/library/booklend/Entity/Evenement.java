package com.library.booklend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "evenements")
@Data
public class Evenement  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom",nullable = false)
    private String nom;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "organisateur")
    private String organisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut",nullable = true)
    private Statut statut;

    public enum Statut {
        PROGRAMME,
        EN_COURS,
        TERMINE
    }
}
