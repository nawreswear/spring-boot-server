package com.springjwt.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Administrateur extends User {
    
    public Administrateur() {
        super();
        this.setType(TypeUtilisateur.ADMINISTRATEUR);
    }
    
    public Administrateur(Integer id) {
        super(id);
        this.setType(TypeUtilisateur.ADMINISTRATEUR);
    }
}
