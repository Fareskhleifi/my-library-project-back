package com.library.booklend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionDTO {
    private Long id;
    private Date dateEmprunt;
    private Date dateRetour;
    private double prixTotal;
    private boolean retourne;
    private Long livre_id;
}