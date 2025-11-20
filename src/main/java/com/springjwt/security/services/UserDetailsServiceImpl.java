package com.springjwt.security.services;

import com.springjwt.models.*;
import com.springjwt.repository.AdminRepository;
import com.springjwt.repository.EtudiantRepository;
import com.springjwt.repository.EnseignantRepository;
import com.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return UserDetailsImpl.build(user.getType(), user);
    }

    // ------------------- GET & LIST -------------------
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ------------------- SAVE -------------------
    public User save(User user) {
        return userRepository.save(user);
    }

    // ------------------- DELETE -------------------
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);

        switch (user.getType().toLowerCase()) {
            case "admin":
                if (adminRepository.existsById(userId)) {
                    adminRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Admin not found with id: " + userId);
                }
                break;

            case "enseignant":
                if (enseignantRepository.existsById(userId)) {
                    enseignantRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Enseignant not found with id: " + userId);
                }
                break;

            case "etudiant":
                if (etudiantRepository.existsById(userId)) {
                    etudiantRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Etudiant not found with id: " + userId);
                }
                break;

            default:
                if (userRepository.existsById(userId)) {
                    userRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("User not found with id: " + userId);
                }
                break;
        }
    }

    // ------------------- UPDATE -------------------
    @Transactional
    public void update(User updatedUser) {
        userRepository.findById(updatedUser.getId()).ifPresent(existingUser -> {
            existingUser.setType(updatedUser.getType());
            existingUser.setNom(updatedUser.getNom());
            existingUser.setPrenom(updatedUser.getPrenom());
            existingUser.setTel(updatedUser.getTel());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setCodePostal(updatedUser.getCodePostal());
            existingUser.setPays(updatedUser.getPays());
            existingUser.setVille(updatedUser.getVille());
            existingUser.setCin(updatedUser.getCin());
            existingUser.setLongitude(updatedUser.getLongitude());
            existingUser.setLatitude(updatedUser.getLatitude());

            if (updatedUser.getPhoto() != null && !updatedUser.getPhoto().isEmpty()) {
                existingUser.setPhoto(updatedUser.getPhoto());
            }

            userRepository.save(existingUser);
        });
    }

    // ------------------- EMAIL & NAME UTILS -------------------
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public Long getUserIdByName(String nom) {
        User user = userRepository.findByNom(nom);
        if (user == null) throw new UsernameNotFoundException("User not found with name: " + nom);
        return user.getId();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ------------------- TYPE UPDATE -------------------
    @Transactional
    public User updateUserType(Long userId, String newType) {
        User user = getUserById(userId);
        user.setType(newType.toLowerCase());
        return userRepository.save(user);
    }
    public Long findUserIdByNom(String nom) {
        User user = userRepository.findByNom(nom); // Find user by name
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + nom);
        }
        return user.getId(); // Return the ID of the user
    }

}
