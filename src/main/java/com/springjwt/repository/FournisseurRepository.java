package com.springjwt.repository;

import com.springjwt.models.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    Optional<Fournisseur> findByNomEntreprise(String nomEntreprise);
    boolean existsByNomEntreprise(String nomEntreprise);
}
