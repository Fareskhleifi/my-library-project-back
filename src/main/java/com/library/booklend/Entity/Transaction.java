package com.library.booklend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livre_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Livre livre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Utilisateur utilisateur;

    @Column(name = "date_emprunt", nullable = false)
    private Date dateEmprunt;

    @Column(name = "date_retour")
    private Date dateRetour;

    @Column(name = "date_retour_reel")
    private Date dateRetourReel;

    @Column(name = "prix_total", nullable = false)
    private double prixTotal;

    @Column(name = "retourne", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean retourne;

}
