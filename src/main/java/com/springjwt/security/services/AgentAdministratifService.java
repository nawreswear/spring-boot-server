package com.springjwt.security.services;

import com.springjwt.models.AgentAdministratif;

import java.util.List;

public interface AgentAdministratifService {
    AgentAdministratif save(AgentAdministratif agent);
    AgentAdministratif getById(Integer id);
    List<AgentAdministratif> getAll();
    void deleteAgent(Integer id);
    AgentAdministratif update(AgentAdministratif agent);
    
    // AgentAdministratif specific methods
    boolean validerDocument(Integer documentId);
    boolean refuserDocument(Integer documentId, String motif);
    String consulterProcessusDossier(Integer dossierId);
    
    // Find by statut
    List<AgentAdministratif> findByStatut(String statut);
    
    // Find by charge de travail
    List<AgentAdministratif> findByChargeTravail(String chargeTravail);
}
