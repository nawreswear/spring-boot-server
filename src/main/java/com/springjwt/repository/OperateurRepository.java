package com.springjwt.repository;

import com.springjwt.models.Operateur;
import com.springjwt.models.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Integer> {
    List<Operateur> findByTypeOperation(TypeOperation typeOperation);
}
