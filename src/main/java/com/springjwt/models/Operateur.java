package com.springjwt.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Operateur extends User {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_operation")
    private TypeOperation typeOperation;
    
    public Operateur() {
        super();
        this.setType(TypeUtilisateur.OPERATEUR);
    }
    
    public Operateur(Integer id, TypeOperation typeOperation) {
        super(id);
        this.setType(TypeUtilisateur.OPERATEUR);
        this.typeOperation = typeOperation;
    }
}
