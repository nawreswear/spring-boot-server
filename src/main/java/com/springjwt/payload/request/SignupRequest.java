package com.springjwt.payload.request;

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

    private String photo;
    
    // Fields for specific user types
    private String userType;
    
    // Client specific fields
    private String adresse;
    private Integer matriculeFiscale;
    private String typeClient;
    
    // AgentAdministratif specific fields
    private String statut;
    private String chargeTravail;
    
    // Operateur specific fields
    private String typeOperation;

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SignupRequest(String nom, String prenom, String email, String motdepasse, String telephone, String ville, String photo) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.telephone = telephone;
        this.ville = ville;
        this.photo = photo;
    }
}
