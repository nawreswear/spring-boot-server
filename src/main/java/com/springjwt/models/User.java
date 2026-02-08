package com.springjwt.models;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "utilisateur")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 50)
    private String nom;

    @NotBlank
    @Size(max = 50)
    private String prenom;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true)
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
    
    @Column(name = "type")
    private String type;

    public User(Integer id) {
        this.id = id;
    }
}
