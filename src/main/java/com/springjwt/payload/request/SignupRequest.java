package com.springjwt.payload.request;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Size(max = 20)
    private String type;

    @NotBlank
    @Size(max = 20)
    private String nom;

    @NotBlank
    @Size(max = 20)
    private String prenom;

    @NotNull(message = "Tel cannot be null")
    private Integer tel;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    private String verificationCode;
    @NotBlank
    @Size(max = 120)
    private String password;

    @Min(value = 1000, message = "Postal code must be at least 1000")
    @Max(value = 9999, message = "Postal code must be at most 9999")
    private Integer codePostal;

    @Size(max = 20)
    private String pays;

    @Size(max = 20)
    private String ville;

    @Digits(integer = 8, fraction = 0, message = "CIN must contain exactly 8 digits")
    @Min(value = 10000000, message = "CIN must contain exactly 8 digits")
    @Max(value = 99999999, message = "CIN must contain exactly 8 digits")
    private String cin;

    private String photo;

    private double longitude;
    private double latitude;
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void getPhoto(String photo) {
    }

    public SignupRequest(Long id, String type, String nom, String prenom, Integer tel, String email, String password, String photo) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }
}
