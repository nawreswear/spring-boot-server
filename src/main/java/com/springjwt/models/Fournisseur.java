package com.springjwt.models;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
public class Fournisseur extends User {

    @NotBlank
    @Size(max = 100)
    private String nomEntreprise;

    @Column(name = "registre_commerce")
    private String registreCommerce;

    public Fournisseur() {
        super();
        this.setType(TypeUtilisateur.FOURNISSEUR);
    }

    public Fournisseur(Integer id, String nomEntreprise, String registreCommerce) {
        super(id);
        this.setType(TypeUtilisateur.FOURNISSEUR);
        this.nomEntreprise = nomEntreprise;
        this.registreCommerce = registreCommerce;
    }
}
