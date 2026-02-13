package com.springjwt.security.services;

import com.springjwt.models.Operateur;
import com.springjwt.models.TypeOperation;
import com.springjwt.repository.OperateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class OperateurServiceImpl implements OperateurService {

    @Autowired
    private OperateurRepository operateurRepository;

    // --- CRUD Operations ---
    @Override
    public Operateur save(Operateur operateur) {
        return operateurRepository.save(operateur);
    }

    @Override
    public Operateur getById(Integer id) {
        return operateurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Operateur> getAll() {
        return operateurRepository.findAll();
    }

    @Override
    public void deleteOperateur(Integer id) {
        operateurRepository.deleteById(id);
    }

    @Override
    public Operateur update(Operateur operateur) {
        return operateurRepository.save(operateur);
    }

    // --- Operateur Specific Methods ---
    @Override
    public String traiterDossier(Integer dossierId, Integer operateurId) {
        Operateur operateur = getById(operateurId);
        if (operateur != null) {
            // Logic to process a dossier based on operator type
            String operationType = operateur.getTypeOperation().name();
            return "Dossier " + dossierId + " traité par l'opérateur " + operateurId + 
                   " (Type: " + operationType + ")";
        }
        return "Opérateur non trouvé";
    }

    @Override
    public boolean ajouterDocument(Integer dossierId, String document, Integer operateurId) {
        Operateur operateur = getById(operateurId);
        if (operateur != null) {
            // Logic to add a document to a dossier
            // This would typically save the document and link it to the dossier
            return true;
        }
        return false;
    }

    @Override
    public List<Operateur> findByTypeOperation(TypeOperation typeOperation) {
        return operateurRepository.findByTypeOperation(typeOperation);
    }
}
