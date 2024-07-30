package com.library.booklend.dto;

import lombok.Data;

@Data
public class LivreDTO {
    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private String genre;
    private String description;
    private String categorie;
    private Integer categorieId;
    private boolean disponibilite;
    private double prixParJour;
}
