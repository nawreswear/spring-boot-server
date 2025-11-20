package com.springjwt.repository;

import com.springjwt.models.Enseignant;
import com.springjwt.models.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalleRepository extends JpaRepository<Salle, Long> {
}
