package com.example.demo.directeur.labo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDto {
    private Long id;
    private String nomComplet;
    private long subjectCount;
    private String grade;
    private String laboratoire;
    private Long laboId;

    // Manual Getters/Setters for safety if Lombok fails
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public long getSubjectCount() { return subjectCount; }
    public void setSubjectCount(long subjectCount) { this.subjectCount = subjectCount; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) { this.laboratoire = laboratoire; }
    public Long getLaboId() { return laboId; }
    public void setLaboId(Long laboId) { this.laboId = laboId; }
}
