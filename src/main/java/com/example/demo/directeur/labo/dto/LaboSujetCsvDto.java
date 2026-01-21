package com.example.demo.directeur.labo.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class LaboSujetCsvDto {

    @CsvBindByName(column = "titre")
    private String titre;

    @CsvBindByName(column = "description")
    private String description;

    // Email of the professor who owns this subject
    @CsvBindByName(column = "email_professeur")
    private String emailProfesseur;

    // Optional: Email of a co-director
    @CsvBindByName(column = "email_co_directeur")
    private String emailCoDirecteur;

    // Comma separated keywords
    @CsvBindByName(column = "mots_cles")
    private String motsCles;
}
