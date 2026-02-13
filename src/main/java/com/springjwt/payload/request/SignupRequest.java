package com.springjwt.payload.request;

import com.springjwt.models.TypeUtilisateur;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(max = 50)
    private String nom;

    @NotBlank
    @Size(max = 50)
    private String prenom;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(max = 255)
    private String motdepasse;

    @NotBlank
    @Size(max = 20)
    private String telephone;

    @NotBlank
    @Size(max = 50)
    private String ville;

    @NotBlank
    @Size(max = 50)
    private String pays;

    @NotBlank
    @Size(max = 255)
    private String adresse;

    private String photo;
    
    // Fields for specific user types
    private TypeUtilisateur userType;
    
    // Client specific fields
    private Integer matriculeFiscale;
    private String typeClient;
    
    // Fournisseur specific fields
    private String nomEntreprise;
    private String registreCommerce;
    
    // AgentAdministratif specific fields
    private String statut;
    private String chargeTravail;
    
    // Operateur specific fields
    private String typeOperation;

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SignupRequest(String nom, String prenom, String email, String motdepasse, String telephone, String ville, String pays, String adresse, String photo, TypeUtilisateur userType) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.telephone = telephone;
        this.ville = ville;
        this.pays = pays;
        this.adresse = adresse;
        this.photo = photo;
        this.userType = userType;
    }
}
