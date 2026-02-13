package com.springjwt.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
public class AgentAdministratif extends User {
    
    @NotBlank
    @Size(max = 50)
    private String statut;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "charge_travail")
    private String chargeTravail;
    
    public AgentAdministratif() {
        super();
        this.setType(TypeUtilisateur.AGENT_ADMINISTRATIF);
    }
    
    public AgentAdministratif(Integer id, String statut, String chargeTravail) {
        super(id);
        this.setType(TypeUtilisateur.AGENT_ADMINISTRATIF);
        this.statut = statut;
        this.chargeTravail = chargeTravail;
    }
}
