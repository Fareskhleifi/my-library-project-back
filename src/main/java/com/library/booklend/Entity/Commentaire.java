package com.library.booklend.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commentaires")
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "date_commentaire", nullable = false)
    private Date dateCommentaire;

    @Column(name = "texte_commentaire", columnDefinition = "TEXT")
    private String texteCommentaire;

    @Column(name = "evaluation")
    private Integer evaluation; // Note sur 5, optionnel

    // Getters and setters (Omitted for brevity)
}
