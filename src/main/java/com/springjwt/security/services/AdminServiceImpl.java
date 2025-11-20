package com.springjwt.security.services;

import com.springjwt.models.*;
import com.springjwt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private EnseignantRepository enseignantRepo;
    @Autowired
    private EtudiantRepository etudiantRepo;
    @Autowired
    private SalleRepository salleRepo;

    // --- Users ---
    @Override
    public Admin save(Admin admin) { return adminRepo.save(admin); }
    @Override
    public List<Admin> getAll() { return adminRepo.findAll(); }
    @Override
    public void deleteAdmin(Long id) { adminRepo.deleteById(id); }
    @Override
    public Admin update(Admin admin) { return adminRepo.save(admin); }
    @Override
    public Admin getById(Long id) { return adminRepo.findById(id).orElse(null); }

    @Override
    public Enseignant saveEnseignant(Enseignant e) { return enseignantRepo.save(e); }
    @Override
    public List<Enseignant> getAllEnseignants() { return enseignantRepo.findAll(); }
    @Override
    public void deleteEnseignant(Long id) { enseignantRepo.deleteById(id); }
    @Override
    public Enseignant updateEnseignant(Enseignant e) { return enseignantRepo.save(e); }

    @Override
    public Etudiant saveEtudiant(Etudiant e) { return etudiantRepo.save(e); }
    @Override
    public List<Etudiant> getAllEtudiants() { return etudiantRepo.findAll(); }
    @Override
    public void deleteEtudiant(Long id) { etudiantRepo.deleteById(id); }
    @Override
    public Etudiant updateEtudiant(Etudiant e) { return etudiantRepo.save(e); }


    // --- Salles ---
    @Override
    public Salle saveSalle(Salle s) { return salleRepo.save(s); }
    @Override
    public List<Salle> getAllSalles() { return salleRepo.findAll(); }
    @Override
    public void deleteSalle(Long id) { salleRepo.deleteById(id); }
    @Override
    public Salle updateSalle(Salle s) { return salleRepo.save(s); }
}
