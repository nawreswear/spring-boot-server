package com.springjwt.controllers;

import com.springjwt.models.*;
//import com.springjwt.models.ConfirmationToken;
import com.springjwt.payload.request.LoginRequest;
import com.springjwt.payload.request.SignupRequest;
import com.springjwt.payload.response.JwtResponse;
import com.springjwt.payload.response.MessageResponse;
import com.springjwt.repository.AdminRepository;
//import com.springjwt.repository.ConfirmationTokenRepository;
import com.springjwt.repository.UserRepository;
import com.springjwt.security.jwt.JwtUtils;
import com.springjwt.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    EnseignantService enseignantService;
    @Autowired

    SalleService salleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AdminService adminService;
    @Autowired
    EtudiantService etudiantService;

    @Autowired
    private UserDetailsServiceImpl userService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Votre logique d'authentification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Réponse contenant le jeton JWT et les détails de l'utilisateur
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getType()));
        } catch (AuthenticationException e) {
            // Gestion des erreurs d'authentification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/getUserById/{userId}")
    public User getUserById(@PathVariable Long userId) {

        return userService.getUserById(userId);
    }


    @PostMapping("/checkEmailUnique")
    public ResponseEntity<?> checkEmailUnique(@RequestBody String email) {
        try {
            // Vérifiez si l'e-mail est unique en appelant le service approprié
            boolean isUnique = userService.isEmailUnique(email);
            if (isUnique) {
                return ResponseEntity.ok("Email is unique.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }
    @GetMapping("/api/{nomuser}")
    public ResponseEntity<Object> findUserIdByNom(@PathVariable String nomuser) {
        try {
            Long userId = userService.findUserIdByNom(nomuser);
            return ResponseEntity.ok(userId);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec le nom : " + nomuser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la recherche de l'utilisateur.");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            User newUser;
            String type = signUpRequest.getType().toLowerCase();

            if (type.equals("admin")) {
                Admin admin = new Admin();
                admin.setNom(signUpRequest.getNom());
                admin.setPrenom(signUpRequest.getPrenom());
                admin.setEmail(signUpRequest.getEmail());
                admin.setTel(signUpRequest.getTel());
                admin.setType("admin");
                admin.setCin(signUpRequest.getCin());
                admin.setPhoto(signUpRequest.getPhoto());
                admin.setPassword(encoder.encode(signUpRequest.getPassword()));
                newUser = admin;
                adminService.save(admin);

            } else if (type.equals("enseignant")) {
                Enseignant enseignant = new Enseignant();
                enseignant.setNom(signUpRequest.getNom());
                enseignant.setPrenom(signUpRequest.getPrenom());
                enseignant.setEmail(signUpRequest.getEmail());
                enseignant.setTel(signUpRequest.getTel());
                enseignant.setType("enseignant");
                enseignant.setCin(signUpRequest.getCin());
                enseignant.setPhoto(signUpRequest.getPhoto());
                enseignant.setPassword(encoder.encode(signUpRequest.getPassword()));
                newUser = enseignant;
                enseignantService.save(enseignant);

            } else if (type.equals("etudiant")) {
                Etudiant etudiant = new Etudiant();
                etudiant.setNom(signUpRequest.getNom());
                etudiant.setPrenom(signUpRequest.getPrenom());
                etudiant.setEmail(signUpRequest.getEmail());
                etudiant.setTel(signUpRequest.getTel());
                etudiant.setType("etudiant");
                etudiant.setCin(signUpRequest.getCin());
                etudiant.setPhoto(signUpRequest.getPhoto());
                etudiant.setPassword(encoder.encode(signUpRequest.getPassword()));
                newUser = etudiant;
                etudiantService.save(etudiant);

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Type must be admin, enseignant, or etudiant!"));
            }

            // Authenticate and generate JWT
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getPassword(),
                    userDetails.getEmail(), userDetails.getType());

            return ResponseEntity.ok(jwtResponse);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Data integrity violation."));
        }
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/users/updateType/{userId}")
    public ResponseEntity<?> updateUserType(
            @PathVariable Long userId,
            @RequestParam String newType) {  // <-- get new type from request
        User updatedUser = userService.updateUserType(userId, newType);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody SignupRequest signUpRequest) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found with id " + userId));
        }
        User updatedUser;
        if (signUpRequest.getType().toLowerCase().startsWith("admin")) {
            Admin admin = new Admin();
            admin.setId(userId);
            admin.setNom(signUpRequest.getNom());
            admin.setPrenom(signUpRequest.getPrenom());
            admin.setEmail(signUpRequest.getEmail());
            admin.setTel(signUpRequest.getTel());
            admin.setType(signUpRequest.getType());
            admin.setCin(signUpRequest.getCin());
            admin.setLongitude(signUpRequest.getLongitude());
            admin.setLatitude(signUpRequest.getLatitude());
            admin.setVille(signUpRequest.getVille());
            admin.setPays(signUpRequest.getPays());
            admin.setCodePostal(signUpRequest.getCodePostal());
            admin.setPhoto(signUpRequest.getPhoto());
            admin.setPassword(encoder.encode(signUpRequest.getPassword()));
            updatedUser = admin;
            adminService.update((Admin) updatedUser);
        } else if (signUpRequest.getType().toLowerCase().startsWith("enseignant")) {
            Enseignant enseignant = new Enseignant();
            enseignant.setId(userId);
            enseignant.setNom(signUpRequest.getNom());
            enseignant.setPrenom(signUpRequest.getPrenom());
            enseignant.setEmail(signUpRequest.getEmail());
            enseignant.setTel(signUpRequest.getTel());
            enseignant.setType(signUpRequest.getType());
            enseignant.setCin(signUpRequest.getCin());
            enseignant.setLongitude(signUpRequest.getLongitude());
            enseignant.setLatitude(signUpRequest.getLatitude());
            enseignant.setVille(signUpRequest.getVille());
            enseignant.setPays(signUpRequest.getPays());
            enseignant.setCodePostal(signUpRequest.getCodePostal());
            enseignant.setPhoto(signUpRequest.getPhoto());
            enseignant.setPassword(encoder.encode(signUpRequest.getPassword()));
            updatedUser = enseignant;
            enseignantService.updateEnseignant((Enseignant) updatedUser);
        } else {
            User user = new User();
            user.setId(userId);
            user.setNom(signUpRequest.getNom());
            user.setPrenom(signUpRequest.getPrenom());
            user.setEmail(signUpRequest.getEmail());
            user.setTel(signUpRequest.getTel());
            user.setType(("etudiant"));
            user.setCin(signUpRequest.getCin());
            user.setLongitude(signUpRequest.getLongitude());
            user.setLatitude(signUpRequest.getLatitude());
            user.setVille(signUpRequest.getVille());
            user.setPays(signUpRequest.getPays());
            user.setCodePostal(signUpRequest.getCodePostal());
            user.setPhoto(signUpRequest.getPhoto());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            updatedUser = user;
            userService.update(updatedUser);
        }


        return ResponseEntity.ok(new MessageResponse("Utilisateur mis à jour avec succès !"));
    }

    @DeleteMapping("/deleteUser/{userId}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur : Utilisateur non trouvé avec l'identifiant " + userId));
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResponse("Utilisateur supprimé avec succès."));
    }


    @DeleteMapping("/deleteAdmin/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Admin not found with id " + adminId));
        }

        adminService.deleteAdmin(adminId);
        return ResponseEntity.ok(new MessageResponse("Admin deleted successfully."));
    }





    @GetMapping("/admins")
    public ResponseEntity<?> getAllAdmins() {
        List<Admin> admins = adminService.getAll();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admins/{adminId}")
    public ResponseEntity<?> getAdminById(@PathVariable Long adminId) {
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Admin not found with id " + adminId));
        }
        return ResponseEntity.ok(admin);
    }



    @GetMapping("/getUserIdByName/{nom}")
    public ResponseEntity<Map<String, Object>> getUserIdByName(@PathVariable String nom) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = userService.getUserIdByName(nom);
            if (userId != null) {
                response.put("userId", userId);
                return ResponseEntity.ok().body(response);
            } else {
                response.put("message", "Aucun utilisateur trouvé avec le nom donné");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("message", "Une erreur s'est produite lors de la récupération de l'ID de l'utilisateur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PutMapping("/admin/update")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.update(admin));
    }


    @PostMapping("/enseignant")
    public ResponseEntity<Enseignant> registerEnseignant(@RequestBody Enseignant e) {
        return ResponseEntity.ok(adminService.saveEnseignant(e));
    }

    @GetMapping("/enseignant")
    public ResponseEntity<List<Enseignant>> getAllEnseignants() {
        return ResponseEntity.ok(adminService.getAllEnseignants());
    }

    @PutMapping("/enseignant/update")
    public ResponseEntity<Enseignant> updateEnseignant(@RequestBody Enseignant e) {
        return ResponseEntity.ok(adminService.updateEnseignant(e));
    }



    @GetMapping("/etudiant")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAll());
    }

    @PutMapping("/etudiant/update")
    public ResponseEntity<Etudiant> updateEtudiant(@RequestBody Etudiant e) {
        return ResponseEntity.ok(etudiantService.update(e));
    }

    @DeleteMapping("/etudiant/delete/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/etudiant/save")
    public ResponseEntity<Etudiant> saveEtudiant(@RequestBody Etudiant e) {
        Etudiant savedEtudiant = etudiantService.save(e);
        return ResponseEntity.ok(savedEtudiant);
    }

    @GetMapping("/etudiant/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.getById(id);
        if (etudiant != null) {
            return ResponseEntity.ok(etudiant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

// ----------------- CLASSROOMS (Salles) ENDPOINTS -----------------
    @PostMapping("/salle")
    public ResponseEntity<Salle> saveSalle(@RequestBody Salle s) {
        return ResponseEntity.ok(salleService.saveSalle(s));
    }

    @GetMapping("/salle")
    public ResponseEntity<List<Salle>> getAllSalles() {
        return ResponseEntity.ok(salleService.getAllSalles());
    }

    @PutMapping("/salle/update")
    public ResponseEntity<Salle> updateSalle(@RequestBody Salle s) {
        return ResponseEntity.ok(salleService.updateSalle(s));
    }

    @DeleteMapping("/salle/delete/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.ok().build();
    }



}