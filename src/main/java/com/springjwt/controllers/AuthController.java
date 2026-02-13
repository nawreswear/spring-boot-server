package com.springjwt.controllers;

import com.springjwt.models.*;
import com.springjwt.payload.request.LoginRequest;
import com.springjwt.payload.request.SignupRequest;
import com.springjwt.payload.response.JwtResponse;
import com.springjwt.payload.response.MessageResponse;
import com.springjwt.repository.AdministrateurRepository;
import com.springjwt.repository.FournisseurRepository;
import com.springjwt.repository.UserRepository;
import com.springjwt.security.jwt.JwtUtils;
import com.springjwt.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    AdministrateurRepository administrateurRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    AgentAdministratifService agentAdministratifService;

    @Autowired
    OperateurService operateurService;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserDetailsServiceImpl userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Votre logique d'authentification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMotdepasse()));
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            User newUser;
            TypeUtilisateur userType = signUpRequest.getUserType();

            if (userType == TypeUtilisateur.ADMINISTRATEUR) {
                Administrateur administrateur = new Administrateur();
                administrateur.setNom(signUpRequest.getNom());
                administrateur.setPrenom(signUpRequest.getPrenom());
                administrateur.setEmail(signUpRequest.getEmail());
                administrateur.setTelephone(signUpRequest.getTelephone());
                administrateur.setVille(signUpRequest.getVille());
                administrateur.setPays(signUpRequest.getPays());
                administrateur.setAdresse(signUpRequest.getAdresse());
                administrateur.setMotdepasse(encoder.encode(signUpRequest.getMotdepasse()));
                administrateur.setPhoto(signUpRequest.getPhoto());
                administrateur.setType(TypeUtilisateur.ADMINISTRATEUR);
                newUser = administrateur;
                adminService.save(administrateur);

            } else if (userType == TypeUtilisateur.CLIENT) {
                Client client = new Client();
                client.setNom(signUpRequest.getNom());
                client.setPrenom(signUpRequest.getPrenom());
                client.setEmail(signUpRequest.getEmail());
                client.setTelephone(signUpRequest.getTelephone());
                client.setVille(signUpRequest.getVille());
                client.setPays(signUpRequest.getPays());
                client.setAdresse(signUpRequest.getAdresse());
                client.setMotdepasse(encoder.encode(signUpRequest.getMotdepasse()));
                client.setPhoto(signUpRequest.getPhoto());
                client.setType(TypeUtilisateur.CLIENT);
                newUser = client;
                if (signUpRequest.getMatriculeFiscale() != null) {
                    client.setMatriculeFiscale(signUpRequest.getMatriculeFiscale());
                }
                if (signUpRequest.getTypeClient() != null) {
                    client.setTypeClient(TypeClient.valueOf(signUpRequest.getTypeClient().toUpperCase()));
                }
                clientService.save(client);

            } else if (userType == TypeUtilisateur.AGENT_ADMINISTRATIF) {
                AgentAdministratif agent = new AgentAdministratif();
                agent.setNom(signUpRequest.getNom());
                agent.setPrenom(signUpRequest.getPrenom());
                agent.setEmail(signUpRequest.getEmail());
                agent.setTelephone(signUpRequest.getTelephone());
                agent.setVille(signUpRequest.getVille());
                agent.setPays(signUpRequest.getPays());
                agent.setAdresse(signUpRequest.getAdresse());
                agent.setMotdepasse(encoder.encode(signUpRequest.getMotdepasse()));
                agent.setPhoto(signUpRequest.getPhoto());
                agent.setType(TypeUtilisateur.AGENT_ADMINISTRATIF);
                newUser = agent;
                if (signUpRequest.getStatut() != null) {
                    agent.setStatut(signUpRequest.getStatut());
                }
                if (signUpRequest.getChargeTravail() != null) {
                    agent.setChargeTravail(signUpRequest.getChargeTravail());
                }
                agentAdministratifService.save(agent);

            } else if (userType == TypeUtilisateur.OPERATEUR) {
                Operateur operateur = new Operateur();
                operateur.setNom(signUpRequest.getNom());
                operateur.setPrenom(signUpRequest.getPrenom());
                operateur.setEmail(signUpRequest.getEmail());
                operateur.setTelephone(signUpRequest.getTelephone());
                operateur.setVille(signUpRequest.getVille());
                operateur.setPays(signUpRequest.getPays());
                operateur.setAdresse(signUpRequest.getAdresse());
                operateur.setMotdepasse(encoder.encode(signUpRequest.getMotdepasse()));
                operateur.setPhoto(signUpRequest.getPhoto());
                operateur.setType(TypeUtilisateur.OPERATEUR);
                newUser = operateur;
                if (signUpRequest.getTypeOperation() != null) {
                    operateur.setTypeOperation(TypeOperation.valueOf(signUpRequest.getTypeOperation().toUpperCase()));
                }
                operateurService.save(operateur);

            } else if (userType == TypeUtilisateur.FOURNISSEUR) {
                Fournisseur fournisseur = new Fournisseur();
                fournisseur.setNom(signUpRequest.getNom());
                fournisseur.setPrenom(signUpRequest.getPrenom());
                fournisseur.setEmail(signUpRequest.getEmail());
                fournisseur.setTelephone(signUpRequest.getTelephone());
                fournisseur.setVille(signUpRequest.getVille());
                fournisseur.setPays(signUpRequest.getPays());
                fournisseur.setAdresse(signUpRequest.getAdresse());
                fournisseur.setMotdepasse(encoder.encode(signUpRequest.getMotdepasse()));
                fournisseur.setPhoto(signUpRequest.getPhoto());
                fournisseur.setType(TypeUtilisateur.FOURNISSEUR);
                newUser = fournisseur;
                if (signUpRequest.getNomEntreprise() != null) {
                    fournisseur.setNomEntreprise(signUpRequest.getNomEntreprise());
                }
                if (signUpRequest.getRegistreCommerce() != null) {
                    fournisseur.setRegistreCommerce(signUpRequest.getRegistreCommerce());
                }
                fournisseurService.save(fournisseur);

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Type must be ADMINISTRATEUR, CLIENT, AGENT_ADMINISTRATIF, OPERATEUR, or FOURNISSEUR!"));
            }

            // Authenticate and generate JWT
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getMotdepasse()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), userDetails.getType());

            return ResponseEntity.ok(jwtResponse);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Data integrity violation."));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
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

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur : Utilisateur non trouvé avec l'identifiant " + userId));
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResponse("Utilisateur supprimé avec succès."));
    }

    @PostMapping("/checkEmailUnique")
    public ResponseEntity<?> checkEmailUnique(@RequestBody String email) {
        try {
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
            Integer userId = userService.findUserIdByNom(nomuser);
            return ResponseEntity.ok(userId);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec le nom : " + nomuser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la recherche de l'utilisateur.");
        }
    }

    @GetMapping("/getUserIdByName/{nom}")
    public ResponseEntity<Map<String, Object>> getUserIdByName(@PathVariable String nom) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = userService.getUserIdByName(nom);
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

    // ==================== ADMINISTRATEUR ENDPOINTS ====================

    @GetMapping("/administrateurs")
    public ResponseEntity<?> getAllAdministrateurs() {
        List<Administrateur> administrateurs = adminService.getAll();
        return ResponseEntity.ok(administrateurs);
    }


    @GetMapping("/administrateurs/{adminId}")
    public ResponseEntity<?> getAdministrateurById(@PathVariable Integer adminId) {
        Administrateur administrateur = adminService.getById(adminId);
        if (administrateur == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Administrateur not found with id " + adminId));
        }
        return ResponseEntity.ok(administrateur);
    }

   /* @PostMapping("/administrateurs")
    public ResponseEntity<Administrateur> createAdministrateur(@RequestBody Administrateur administrateur) {
        return ResponseEntity.ok(adminService.save(administrateur));
    }*/
   @PostMapping("/administrateurs")
   public ResponseEntity<Administrateur> createAdministrateur(@RequestBody Administrateur administrateur) {
       administrateur.setMotdepasse(
               encoder.encode(administrateur.getMotdepasse())
       );
       return ResponseEntity.ok(adminService.save(administrateur));
   }

    @DeleteMapping("/administrateurs/{adminId}")
    public ResponseEntity<?> deleteAdministrateur(@PathVariable Integer adminId) {

        Administrateur admin = adminService.getById(adminId);
        if (admin == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Administrateur introuvable avec l'id : " + adminId));
        }

        adminService.deleteAdmin(adminId);
        return ResponseEntity.ok(
                new MessageResponse("Administrateur supprimé avec succès.")
        );
    }

    @PutMapping("/administrateurs/{adminId}")
    public ResponseEntity<Administrateur> updateAdministrateur(
            @PathVariable Integer adminId,
            @RequestBody Administrateur administrateur) {

        administrateur.setId(adminId);
        Administrateur updatedAdmin = adminService.update(administrateur);
        return ResponseEntity.ok(updatedAdmin);
    }



    @PostMapping("/administrateurs/{adminId}/configure-system")
    public ResponseEntity<?> configureSystem(@PathVariable Integer adminId) {
        adminService.configureSystem();
        return ResponseEntity.ok(new MessageResponse("System configured successfully."));
    }

    @PostMapping("/administrateurs/{adminId}/manage-comments")
    public ResponseEntity<?> manageComments(@PathVariable Integer adminId) {
        adminService.manageComments();
        return ResponseEntity.ok(new MessageResponse("Comments managed successfully."));
    }

    // ==================== CLIENT ENDPOINTS ====================

    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients() {
        List<Client> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<?> getClientById(@PathVariable Integer clientId) {
        Client client = clientService.getById(clientId);
        if (client == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Client not found with id " + clientId));
        }
        return ResponseEntity.ok(client);
    }

    /*@PostMapping("/clients")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.save(client));
    }*/
    @PostMapping("/clients")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        client.setMotdepasse(
                encoder.encode(client.getMotdepasse())
        );
        return ResponseEntity.ok(clientService.save(client));
    }


    @PutMapping("/clients/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer clientId, @RequestBody Client client) {
        client.setId(clientId);
        return ResponseEntity.ok(clientService.update(client));
    }

    @DeleteMapping("/clients/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Integer clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok(new MessageResponse("Client deleted successfully."));
    }

    // Client specific methods
    @PostMapping("/clients/{clientId}/deposer-document")
    public ResponseEntity<?> deposerDocument(@PathVariable Integer clientId, @RequestBody Map<String, String> request) {
        String document = request.get("document");
        Client updatedClient = clientService.deposerDocument(clientId, document);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/clients/{clientId}/statut-dossier")
    public ResponseEntity<String> consulterStatutDossier(@PathVariable Integer clientId) {
        String status = clientService.consulterStatutDossier(clientId);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/clients/{clientId}/chatbot")
    public ResponseEntity<String> communiquerAvecChatbot(@PathVariable Integer clientId, @RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = clientService.communiquerAvecChatbot(clientId, message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clients/type/{type}")
    public ResponseEntity<?> getClientsByType(@PathVariable String type) {
        try {
            TypeClient clientType = TypeClient.valueOf(type.toUpperCase());
            List<Client> clients = clientService.findByType(clientType);
            return ResponseEntity.ok(clients);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid client type. Must be PME, INDUSTRIEL, or COMMERCIANT"));
        }
    }

    // ==================== AGENT ADMINISTRATIF ENDPOINTS ====================

    @GetMapping("/agents-administratifs")
    public ResponseEntity<?> getAllAgentsAdministratifs() {
        List<AgentAdministratif> agents = agentAdministratifService.getAll();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/agents-administratifs/{agentId}")
    public ResponseEntity<?> getAgentAdministratifById(@PathVariable Integer agentId) {
        AgentAdministratif agent = agentAdministratifService.getById(agentId);
        if (agent == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Agent Administratif not found with id " + agentId));
        }
        return ResponseEntity.ok(agent);
    }

   /* @PostMapping("/agents-administratifs")
    public ResponseEntity<AgentAdministratif> createAgentAdministratif(@RequestBody AgentAdministratif agent) {
        return ResponseEntity.ok(agentAdministratifService.save(agent));
    }*/
   @PostMapping("/agents-administratifs")
   public ResponseEntity<AgentAdministratif> createAgentAdministratif(@RequestBody AgentAdministratif agent) {
       agent.setMotdepasse(
               encoder.encode(agent.getMotdepasse())
       );
       return ResponseEntity.ok(agentAdministratifService.save(agent));
   }


    @PutMapping("/agents-administratifs/{agentId}")
    public ResponseEntity<AgentAdministratif> updateAgentAdministratif(@PathVariable Integer agentId, @RequestBody AgentAdministratif agent) {
        agent.setId(agentId);
        return ResponseEntity.ok(agentAdministratifService.update(agent));
    }

    @DeleteMapping("/agents-administratifs/{agentId}")
    public ResponseEntity<?> deleteAgentAdministratif(@PathVariable Integer agentId) {
        agentAdministratifService.deleteAgent(agentId);
        return ResponseEntity.ok(new MessageResponse("Agent Administratif deleted successfully."));
    }

    // Agent Administratif specific methods
    @PostMapping("/agents-administratifs/{agentId}/valider-document")
    public ResponseEntity<?> validerDocument(@PathVariable Integer agentId, @RequestBody Map<String, Integer> request) {
        Integer documentId = request.get("documentId");
        boolean result = agentAdministratifService.validerDocument(documentId);
        return ResponseEntity.ok(new MessageResponse("Document validation result: " + result));
    }

    @PostMapping("/agents-administratifs/{agentId}/refuser-document")
    public ResponseEntity<?> refuserDocument(@PathVariable Integer agentId, @RequestBody Map<String, Object> request) {
        Integer documentId = (Integer) request.get("documentId");
        String motif = (String) request.get("motif");
        boolean result = agentAdministratifService.refuserDocument(documentId, motif);
        return ResponseEntity.ok(new MessageResponse("Document refusal result: " + result));
    }

    @GetMapping("/agents-administratifs/{agentId}/processus-dossier/{dossierId}")
    public ResponseEntity<String> consulterProcessusDossier(@PathVariable Integer agentId, @PathVariable Integer dossierId) {
        String process = agentAdministratifService.consulterProcessusDossier(dossierId);
        return ResponseEntity.ok(process);
    }

    @GetMapping("/agents-administratifs/statut/{statut}")
    public ResponseEntity<?> getAgentsByStatut(@PathVariable String statut) {
        List<AgentAdministratif> agents = agentAdministratifService.findByStatut(statut);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/agents-administratifs/charge-travail/{chargeTravail}")
    public ResponseEntity<?> getAgentsByChargeTravail(@PathVariable String chargeTravail) {
        List<AgentAdministratif> agents = agentAdministratifService.findByChargeTravail(chargeTravail);
        return ResponseEntity.ok(agents);
    }

    // ==================== OPERATEUR ENDPOINTS ====================

    @GetMapping("/operateurs")
    public ResponseEntity<?> getAllOperateurs() {
        List<Operateur> operateurs = operateurService.getAll();
        return ResponseEntity.ok(operateurs);
    }

    @GetMapping("/operateurs/{operateurId}")
    public ResponseEntity<?> getOperateurById(@PathVariable Integer operateurId) {
        Operateur operateur = operateurService.getById(operateurId);
        if (operateur == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Operateur not found with id " + operateurId));
        }
        return ResponseEntity.ok(operateur);
    }


    @PostMapping("/operateurs")
    public ResponseEntity<Operateur> createOperateur(@RequestBody Operateur operateur) {
        operateur.setMotdepasse(
                encoder.encode(operateur.getMotdepasse())
        );
        return ResponseEntity.ok(operateurService.save(operateur));
    }


    @PutMapping("/operateurs/{operateurId}")
    public ResponseEntity<Operateur> updateOperateur(@PathVariable Integer operateurId, @RequestBody Operateur operateur) {
        operateur.setId(operateurId);
        return ResponseEntity.ok(operateurService.update(operateur));
    }

    @DeleteMapping("/operateurs/{operateurId}")
    public ResponseEntity<?> deleteOperateur(@PathVariable Integer operateurId) {
        operateurService.deleteOperateur(operateurId);
        return ResponseEntity.ok(new MessageResponse("Operateur deleted successfully."));
    }

    // Operateur specific methods
    @PostMapping("/operateurs/{operateurId}/traiter-dossier")
    public ResponseEntity<String> traiterDossier(@PathVariable Integer operateurId, @RequestBody Map<String, Integer> request) {
        Integer dossierId = request.get("dossierId");
        String result = operateurService.traiterDossier(dossierId, operateurId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/operateurs/{operateurId}/ajouter-document")
    public ResponseEntity<?> ajouterDocument(@PathVariable Integer operateurId, @RequestBody Map<String, Object> request) {
        Integer dossierId = (Integer) request.get("dossierId");
        String document = (String) request.get("document");
        boolean result = operateurService.ajouterDocument(dossierId, document, operateurId);
        return ResponseEntity.ok(new MessageResponse("Document addition result: " + result));
    }

    @GetMapping("/operateurs/type-operation/{typeOperation}")
    public ResponseEntity<?> getOperateursByTypeOperation(@PathVariable String typeOperation) {
        try {
            TypeOperation type = TypeOperation.valueOf(typeOperation.toUpperCase());
            List<Operateur> operateurs = operateurService.findByTypeOperation(type);
            return ResponseEntity.ok(operateurs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid operation type. Must be IMPORT, EXPORT, or MIXTE"));
        }
    }

    // ==================== FOURNISSEUR ENDPOINTS ====================

    @GetMapping("/fournisseurs")
    public ResponseEntity<?> getAllFournisseurs() {
        List<Fournisseur> fournisseurs = fournisseurService.getAll();
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/fournisseurs/{fournisseurId}")
    public ResponseEntity<?> getFournisseurById(@PathVariable Integer fournisseurId) {
        Fournisseur fournisseur = fournisseurService.getById(fournisseurId);
        if (fournisseur == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Fournisseur not found with id " + fournisseurId));
        }
        return ResponseEntity.ok(fournisseur);
    }

    @PostMapping("/fournisseurs")
    public ResponseEntity<Fournisseur> createFournisseur(@RequestBody Fournisseur fournisseur) {
        fournisseur.setMotdepasse(
                encoder.encode(fournisseur.getMotdepasse())
        );
        return ResponseEntity.ok(fournisseurService.save(fournisseur));
    }

    @PutMapping("/fournisseurs/{fournisseurId}")
    public ResponseEntity<Fournisseur> updateFournisseur(@PathVariable Integer fournisseurId, @RequestBody Fournisseur fournisseur) {
        fournisseur.setId(fournisseurId);
        return ResponseEntity.ok(fournisseurService.update(fournisseur));
    }

    @DeleteMapping("/fournisseurs/{fournisseurId}")
    public ResponseEntity<?> deleteFournisseur(@PathVariable Integer fournisseurId) {
        fournisseurService.deleteFournisseur(fournisseurId);
        return ResponseEntity.ok(new MessageResponse("Fournisseur deleted successfully."));
    }

    @GetMapping("/fournisseurs/entreprise/{nomEntreprise}")
    public ResponseEntity<?> getFournisseurByNomEntreprise(@PathVariable String nomEntreprise) {
        return fournisseurService.findByNomEntreprise(nomEntreprise)
                .map(fournisseur -> ResponseEntity.ok(fournisseur))
                .orElse(ResponseEntity.notFound().build());
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
}