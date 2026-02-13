package com.springjwt.repository;

import com.springjwt.models.AgentAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentAdministratifRepository extends JpaRepository<AgentAdministratif, Integer> {
    List<AgentAdministratif> findByStatut(String statut);
    List<AgentAdministratif> findByChargeTravail(String chargeTravail);
}
