package com.example.demo.professeur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeur_commission_professeurs")
public class CommissionProfesseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commission_id", nullable = false)
    private Long commissionId;

    @Column(name = "professeur_id", nullable = false)
    private Long professeurId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Long commissionId) {
        this.commissionId = commissionId;
    }

    public Long getProfesseurId() {
        return professeurId;
    }

    public void setProfesseurId(Long professeurId) {
        this.professeurId = professeurId;
    }
}
