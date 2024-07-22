package com.library.booklend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RetourLivreDTO {
    private Long transactionId;
    private Date dateRetourReel;
}
