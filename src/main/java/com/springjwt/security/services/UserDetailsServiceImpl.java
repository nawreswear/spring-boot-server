package com.springjwt.security.services;

import com.springjwt.models.*;
import com.springjwt.repository.AdministrateurRepository;
import com.springjwt.repository.ClientRepository;
import com.springjwt.repository.AgentAdministratifRepository;
import com.springjwt.repository.OperateurRepository;
import com.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AgentAdministratifRepository agentAdministratifRepository;

    @Autowired
    private OperateurRepository operateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return UserDetailsImpl.build(user);
    }

    // ------------------- GET & LIST -------------------
    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ------------------- SAVE -------------------
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    // ------------------- DELETE -------------------
    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserById(userId);

        switch (user.getType().toLowerCase()) {
            case "administrateur":
                if (administrateurRepository.existsById(userId)) {
                    administrateurRepository.deleteById(userId);
                } else {
                    throw new UsernameNotFoundException("Administrateur not found with id: " + userId);
                }
                break;

            case "client":
                if (clientRepository.existsById(userId)) {
                    clientRepository.deleteById(userId);
                } else {
                    throw new UsernameNotFoundException("Client not found with id: " + userId);
                }
                break;

            case "agentadministratif":
                if (agentAdministratifRepository.existsById(userId)) {
                    agentAdministratifRepository.deleteById(userId);
                } else {
                    throw new UsernameNotFoundException("Agent Administratif not found with id: " + userId);
                }
                break;

            case "operateur":
                if (operateurRepository.existsById(userId)) {
                    operateurRepository.deleteById(userId);
                } else {
                    throw new UsernameNotFoundException("Operateur not found with id: " + userId);
                }
                break;

            default:
                if (userRepository.existsById(userId)) {
                    userRepository.deleteById(userId);
                } else {
                    throw new UsernameNotFoundException("User not found with id: " + userId);
                }
                break;
        }
    }

    // ------------------- UPDATE -------------------
    @Override
    @Transactional
    public void update(User updatedUser) {
        userRepository.findById(updatedUser.getId()).ifPresent(existingUser -> {
            existingUser.setNom(updatedUser.getNom());
            existingUser.setPrenom(updatedUser.getPrenom());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setTelephone(updatedUser.getTelephone());
            existingUser.setVille(updatedUser.getVille());
            existingUser.setMotdepasse(updatedUser.getMotdepasse());
            existingUser.setPhoto(updatedUser.getPhoto());

            userRepository.save(existingUser);
        });
    }

    // ------------------- EMAIL & NAME UTILS -------------------
    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }

    @Override
    public Integer getUserIdByName(String nom) {
        User user = userRepository.findByNom(nom);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + nom);
        }
        return user.getId();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUserType(Integer userId, String newType) {
        User user = getUserById(userId);
        user.setType(newType.toLowerCase());
        return userRepository.save(user);
    }

    @Override
    public Integer findUserIdByNom(String nomuser) {
        User user = userRepository.findByNom(nomuser);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + nomuser);
        }
        return user.getId();
    }

    @Override
    public String getUserType(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getType() : null;
    }
}
