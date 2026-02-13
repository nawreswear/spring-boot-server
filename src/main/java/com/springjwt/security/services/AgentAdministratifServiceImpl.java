package com.springjwt.security.services;

import com.springjwt.models.AgentAdministratif;
import com.springjwt.repository.AgentAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AgentAdministratifServiceImpl implements AgentAdministratifService {

    @Autowired
    private AgentAdministratifRepository agentAdministratifRepository;

    // --- CRUD Operations ---
    @Override
    public AgentAdministratif save(AgentAdministratif agent) {
        return agentAdministratifRepository.save(agent);
    }

    @Override
    public AgentAdministratif getById(Integer id) {
        return agentAdministratifRepository.findById(id).orElse(null);
    }

    @Override
    public List<AgentAdministratif> getAll() {
        return agentAdministratifRepository.findAll();
    }

    @Override
    public void deleteAgent(Integer id) {
        agentAdministratifRepository.deleteById(id);
    }

    @Override
    public AgentAdministratif update(AgentAdministratif agent) {
        return agentAdministratifRepository.save(agent);
    }

    // --- AgentAdministratif Specific Methods ---
    @Override
    public boolean validerDocument(Integer documentId) {
        // Logic to validate a document
        // This would typically update the document status in the database
        return true; // Return true if validation successful
    }

    @Override
    public boolean refuserDocument(Integer documentId, String motif) {
        // Logic to refuse a document with a reason
        // This would typically update the document status and store the refusal reason
        return true; // Return true if refusal processed successfully
    }

    @Override
    public String consulterProcessusDossier(Integer dossierId) {
        // Logic to check the process status of a dossier
        return "Processus du dossier " + dossierId + ": En cours de traitement par l'agent administratif";
    }

    @Override
    public List<AgentAdministratif> findByStatut(String statut) {
        return agentAdministratifRepository.findByStatut(statut);
    }

    @Override
    public List<AgentAdministratif> findByChargeTravail(String chargeTravail) {
        return agentAdministratifRepository.findByChargeTravail(chargeTravail);
    }
}
