package com.library.booklend.dto;

import lombok.Data;

@Data
public class EmpruntRequest {
    private Long livreId;
    private int nombreJours;
    private String token; // Assuming the token is a String
}
