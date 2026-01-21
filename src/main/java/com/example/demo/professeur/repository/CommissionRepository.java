package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByLaboratoire_Ced_Id(Long cedId);
}
