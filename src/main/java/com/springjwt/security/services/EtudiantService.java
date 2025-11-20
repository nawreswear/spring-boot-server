package com.springjwt.security.services;

import com.springjwt.models.Etudiant;
import java.util.List;

public interface EtudiantService {
    Etudiant save(Etudiant e);
    List<Etudiant> getAll();
    Etudiant update(Etudiant updatedEtudiant);
    void deleteEtudiant(Long id);
    Etudiant getById(Long id);
    void processEtudiantRequest(Etudiant etudiant);
    List<Etudiant> getAllEtudiantRequests();
}
