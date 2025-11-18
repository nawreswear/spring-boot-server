package com.springjwt.controllers;

import com.springjwt.models.Admin;
//import com.springjwt.models.ConfirmationToken;
import com.springjwt.models.User;
import com.springjwt.models.Vendeur;
import com.springjwt.payload.request.LoginRequest;
import com.springjwt.payload.request.SignupRequest;
import com.springjwt.payload.response.JwtResponse;
import com.springjwt.payload.response.MessageResponse;
import com.springjwt.repository.AdminRepository;
//import com.springjwt.repository.ConfirmationTokenRepository;
import com.springjwt.repository.UserRepository;
import com.springjwt.repository.VendeurRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    VendeurRepository vendeurRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AdminService adminService;

    @Autowired
    private VendeurService vendeurService;

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

    @GetMapping("/vendeurs")
    public ResponseEntity<?> getAllVendeurs() {
        List<Vendeur> vendeurs = vendeurService.getAll();
        return ResponseEntity.ok(vendeurs);
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
            User user = null;
            if (type.startsWith("admin")) {
                Admin admin = new Admin();
                admin.setNom(signUpRequest.getNom());
                admin.setPrenom(signUpRequest.getPrenom());
                admin.setEmail(signUpRequest.getEmail());
                admin.setTel(signUpRequest.getTel());
                admin.setType(signUpRequest.getType());
                admin.setCin(signUpRequest.getCin());
                admin.setPhoto(signUpRequest.getPhoto());
                admin.setPassword(encoder.encode(signUpRequest.getPassword()));
                newUser = admin;
                adminService.save(admin);

            } else  {
                Vendeur vendeur = new Vendeur();
                vendeur.setNom(signUpRequest.getNom());
                vendeur.setPrenom(signUpRequest.getPrenom());
                vendeur.setEmail(signUpRequest.getEmail());
                vendeur.setTel(signUpRequest.getTel());
                vendeur.setType("user");
                vendeur.setCin(signUpRequest.getCin());
                vendeur.setPhoto(signUpRequest.getPhoto());
                vendeur.setPassword(encoder.encode(signUpRequest.getPassword()));
                newUser = vendeur;
                vendeurService.save(vendeur);
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Créer une réponse contenant le token JWT et l'identifiant de l'utilisateur
            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getPassword(), userDetails.getEmail(), userDetails.getType());

            // Retourner la réponse avec le code de statut OK
            return ResponseEntity.ok(jwtResponse);
        } catch (DataIntegrityViolationException e) {
            // Gérer les erreurs de violation d'intégrité des données (par exemple, e-mail déjà utilisé)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: Data integrity violation."));
        }
    }
    @GetMapping("/photo/{nom}")
    public ResponseEntity<User> getUserByPhotoName(@PathVariable String nom) {
        User user = userService.findPhotoByNo(nom);
        return ResponseEntity.ok(user);
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
    public ResponseEntity<?> updateUserType(@PathVariable Long userId) {
        User updatedUser = userService.updateUserType(userId);
        return ResponseEntity.ok(updatedUser);
    }
    @PutMapping("/users/updatevendeurType/{userId}")
    public ResponseEntity<?> updateVendeurType(@PathVariable Long userId) {
        User updatedUser = userService.updateVendeurType(userId);
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
        } else if (signUpRequest.getType().toLowerCase().startsWith("vendeur")) {
            Vendeur vendeur = new Vendeur();
            vendeur.setId(userId);
            vendeur.setNom(signUpRequest.getNom());
            vendeur.setPrenom(signUpRequest.getPrenom());
            vendeur.setEmail(signUpRequest.getEmail());
            vendeur.setTel(signUpRequest.getTel());
            vendeur.setType(signUpRequest.getType());
            vendeur.setCin(signUpRequest.getCin());
            vendeur.setLongitude(signUpRequest.getLongitude());
            vendeur.setLatitude(signUpRequest.getLatitude());
            vendeur.setVille(signUpRequest.getVille());
            vendeur.setPays(signUpRequest.getPays());
            vendeur.setCodePostal(signUpRequest.getCodePostal());
            vendeur.setPhoto(signUpRequest.getPhoto());
            vendeur.setPassword(encoder.encode(signUpRequest.getPassword()));
            updatedUser = vendeur;
            vendeurService.update((Vendeur) updatedUser);
        } else {
            User user = new User();
            user.setId(userId);
            user.setNom(signUpRequest.getNom());
            user.setPrenom(signUpRequest.getPrenom());
            user.setEmail(signUpRequest.getEmail());
            user.setTel(signUpRequest.getTel());
            user.setType(("user"));
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

    @DeleteMapping("/deleteVendeur/{vendeurId}")
    public ResponseEntity<?> deleteVendeur(@PathVariable Long vendeurId) {
        if (!vendeurRepository.existsById(vendeurId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Vendeur not found with id " + vendeurId));
        }

        vendeurService.deletevendeur(vendeurId);
        return ResponseEntity.ok(new MessageResponse("Vendeur deleted successfully."));
    }


    @GetMapping("/getVendeurById/{vendeurId}")
    public ResponseEntity<?> getVendeurById(@PathVariable Long vendeurId) {
        Vendeur vendeur = vendeurService.getById(vendeurId);
        if (vendeur == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Vendeur not found with id " + vendeurId));
        }
        return ResponseEntity.ok(vendeur);
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
    @PostMapping("/addVendeur")
    public ResponseEntity<Vendeur> addVendeur(@RequestBody Vendeur vendeur) {
        Vendeur savedVendeur = vendeurService.save(vendeur);
        if (savedVendeur != null) {
            return new ResponseEntity<>(savedVendeur, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/vendeur-request")
    public ResponseEntity<String> sendVendeurRequestToAdmin(@RequestBody Vendeur vendeur) {
        try {
            // Process the vendeur request (e.g., save to database)
            vendeurService.processVendeurRequest(vendeur);
            return ResponseEntity.ok("Vendeur request received successfully.");
        } catch (Exception e) {
            // Handle any errors that may occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing vendeur request: " + e.getMessage());
        }
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


    @GetMapping("/admin/vendeur-requests")
    public ResponseEntity<List<Vendeur>> getAllVendeurRequests() {
        try {
            List<Vendeur> vendeurRequests = vendeurService.getAllVendeurRequests();
            return ResponseEntity.ok(vendeurRequests);
        } catch (Exception e) {
            // Handle any errors that may occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @PutMapping("/updateVendeur/{vendeurId}")
    public ResponseEntity<Vendeur> updateVendeur(@RequestBody Vendeur updatedVendeur) {
        Vendeur vendeur = vendeurService.update(updatedVendeur);
        if (vendeur != null) {
            return new ResponseEntity<>(vendeur, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}