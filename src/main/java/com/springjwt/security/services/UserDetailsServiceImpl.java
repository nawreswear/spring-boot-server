package com.springjwt.security.services;
import com.springjwt.models.User;
import com.springjwt.models.Vendeur;
import com.springjwt.repository.AdminRepository;
import com.springjwt.repository.UserRepository;
import com.springjwt.repository.VendeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendeurRepository vendeurRepository;

    @Autowired
    private VendeurService  vendeurSerive;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'adresse email : " + username);
        }
        return UserDetailsImpl.build(user.getType(), user);
    }

    public User findPhotoByNo(String nom) {
        return userRepository.findPhotobyuser(nom); // Assurez-vous que cette méthode retourne une chaîne de caractères
    }
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
      /*  @Transactional
        public void deleteUser(Long userId) {
            // Supprimer l'utilisateur
            userRepository.deleteById(userId);
        }*/
    @Transactional
    public void deleteUser(Long userId) {
    User   User= getUserById(userId);
        switch (User.getType().toLowerCase()) {
            case "admin":
                if (adminRepository.existsById(userId)) {
                    adminRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Aucun admin avec l'id " + userId + " n'existe!");
                }
                break;
            case "vendeur":
                if (vendeurRepository.existsById(userId)) {
                    vendeurRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Aucun vendeur avec l'id " + userId + " n'existe!");
                }
                break;
            default:
                if (userRepository.existsById(userId)) {
                    userRepository.deleteById(userId);
                } else {
                    throw new EntityNotFoundException("Aucun utilisateur avec l'id " + userId + " n'existe!");
                }
                break;
        }
    }
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

            // Update the photo only if a new photo is provided
            if (updatedUser.getPhoto() != null && updatedUser.getPhoto().length() > 0) {
                existingUser.setPhoto(updatedUser.getPhoto());
            }

            // Save the updated user
            userRepository.save(existingUser);
        });
    }
    public Long findUserIdByNom(String nom) {
        User user = userRepository.findByNom(nom);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom : " + nom);
        }
        return user.getId();
    }
    public boolean isEmailUnique(String email) {
        // Vérifiez si l'e-mail existe déjà dans la base de données
        User user = userRepository.findByEmail(email);

        return user == null; // Si l'utilisateur est null, l'e-mail est unique
    }
    public Long getUserIdByName(String nom) {
        User user = userRepository.findByNom(nom);
        return user.getId();
    }
    @Transactional
    public User updateUserType(Long userId) {
        // Vérifiez si userId est null
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Recherchez l'utilisateur par son ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Mettez à jour le type de l'utilisateur
        user.setType("vendeur");

        // Enregistrez les modifications de l'utilisateur dans la base de données
        return userRepository.save(user);
    }

    @Transactional
    public User updateVendeurType(Long userId) {
        // Vérifiez si userId est null
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Recherchez l'utilisateur par son ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Vendeur not found with id: " + userId));

        // Mettez à jour le type de l'utilisateur
        user.setType("user");

        // Enregistrez les modifications de l'utilisateur dans la base de données
        return userRepository.save(user);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}