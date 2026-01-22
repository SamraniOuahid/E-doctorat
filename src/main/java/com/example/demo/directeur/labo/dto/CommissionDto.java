package com.example.demo.directeur.labo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommissionDto {
    private Long id;
    private String dateCommission; // String for easy JSON date handling
    private String lieu;
    private String heure;
    private Long laboId;
    
    // IDs for shuttle selection
    private List<Long> sujetIds;
    private List<Long> membreIds; // UserAccount IDs
}
