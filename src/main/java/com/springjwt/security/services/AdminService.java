package com.springjwt.security.services;

import com.springjwt.models.*;

import java.util.List;

public interface AdminService {
    Admin save(Admin a);
    void deleteAdmin(Long userId);
    List<Admin> getAll();
    Admin getById(Long adminId);

    Admin update(Admin updatedAdmin);

    Enseignant saveEnseignant(Enseignant e);
    List<Enseignant> getAllEnseignants();
    void deleteEnseignant(Long id);
    Enseignant updateEnseignant(Enseignant e);

    Etudiant saveEtudiant(Etudiant e);
    List<Etudiant> getAllEtudiants();
    void deleteEtudiant(Long id);
    Etudiant updateEtudiant(Etudiant e);

    // Courses


    // Classrooms (Salles)
    Salle saveSalle(Salle s);
    List<Salle> getAllSalles();
    void deleteSalle(Long id);
    Salle updateSalle(Salle s);
}