package com.springjwt.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
public class Client extends User {
    
    @Column(name = "matricule_fiscale")
    private Integer matriculeFiscale;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_client")
    private TypeClient typeClient;
    
    public Client() {
        super();
        this.setType(TypeUtilisateur.CLIENT);
    }
    
    public Client(Integer id, Integer matriculeFiscale, TypeClient typeClient) {
        super(id);
        this.setType(TypeUtilisateur.CLIENT);
        this.matriculeFiscale = matriculeFiscale;
        this.typeClient = typeClient;
    }


}
