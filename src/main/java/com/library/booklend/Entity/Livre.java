package com.library.booklend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "livres")
@Data
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "auteur", nullable = false)
    private String auteur;

    @Column(name = "isbn", length = 20, unique = true)
    private String isbn;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "categorie", length = 50)
    private String categorie;

    @Column(name = "disponibilite", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean disponibilite;

    @Column(name = "prix_par_jour", nullable = false)
    private double prixParJour;

    public boolean getDisponibilite() {
        return this.disponibilite;
    }

}
