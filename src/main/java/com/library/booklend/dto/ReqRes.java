package com.library.booklend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.library.booklend.Entity.Utilisateur;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;

    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String password;
    private String telephone;
    private String adresse;
    private String role;
    private String accountStatus;

    private Utilisateur utilisateur;

}
