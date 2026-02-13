package com.springjwt.security.services;

import com.springjwt.models.Operateur;
import com.springjwt.models.TypeOperation;

import java.util.List;

public interface OperateurService {
    Operateur save(Operateur operateur);
    Operateur getById(Integer id);
    List<Operateur> getAll();
    void deleteOperateur(Integer id);
    Operateur update(Operateur operateur);
    
    // Operateur specific methods
    String traiterDossier(Integer dossierId, Integer operateurId);
    boolean ajouterDocument(Integer dossierId, String document, Integer operateurId);
    
    // Find by type operation
    List<Operateur> findByTypeOperation(TypeOperation typeOperation);
}
