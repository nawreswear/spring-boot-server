package com.springjwt.security.services;

import com.springjwt.models.*;
import com.springjwt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdministrateurRepository administrateurRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AgentAdministratifRepository agentAdministratifRepository;
    @Autowired
    private OperateurRepository operateurRepository;

    // --- Administrateur CRUD ---
    @Override
    public Administrateur save(Administrateur administrateur) { 
        return administrateurRepo.save(administrateur); 
    }
    
    @Override
    public List<Administrateur> getAll() { 
        return administrateurRepo.findAll(); 
    }
    
    @Override
    public void deleteAdmin(Integer id) { 
        administrateurRepo.deleteById(id); 
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Administrateur update(Administrateur administrateur) {

        Administrateur existing = administrateurRepo.findById(administrateur.getId())
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé"));

        existing.setNom(administrateur.getNom());
        existing.setPrenom(administrateur.getPrenom());
        existing.setEmail(administrateur.getEmail());
        existing.setTelephone(administrateur.getTelephone());
        existing.setVille(administrateur.getVille());
        existing.setPhoto(administrateur.getPhoto());
        existing.setType(administrateur.getType());

        // 🔐 ENCODAGE DU MOT DE PASSE
        if (administrateur.getMotdepasse() != null
                && !administrateur.getMotdepasse().isEmpty()
                && !passwordEncoder.matches(administrateur.getMotdepasse(), existing.getMotdepasse())) {

            existing.setMotdepasse(passwordEncoder.encode(administrateur.getMotdepasse()));
        }

        return administrateurRepo.save(existing);
    }


    @Override
    public Administrateur getById(Integer id) { 
        return administrateurRepo.findById(id).orElse(null); 
    }

    // --- User Management ---
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    
    // --- System Configuration ---
    @Override
    public void configureSystem() {
        // Implementation for system configuration
    }
    
    // --- Comment Management ---
    @Override
    public void manageComments() {
        // Implementation for comment management
    }
    
    // --- Responsibility Display ---
    @Override
    public String displayResponsibilities() {
        return "Administrateur responsibilities: User management, System configuration, Comment management";
    }
}
