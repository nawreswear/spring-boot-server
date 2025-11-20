package com.springjwt.repository;

import com.springjwt.models.Enseignant;
import com.springjwt.models.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}
