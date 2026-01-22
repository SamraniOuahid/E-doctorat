package com.example.demo.professeur.repository;

import com.example.demo.professeur.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByLaboratoire_Ced_Id(Long cedId);
    List<Commission> findByLaboratoire_Id(Long laboId);
    org.springframework.data.domain.Page<Commission> findByLaboratoire_Ced_Id(Long cedId, org.springframework.data.domain.Pageable pageable);
}
