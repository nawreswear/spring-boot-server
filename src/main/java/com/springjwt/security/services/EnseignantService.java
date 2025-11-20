package com.springjwt.security.services;


import com.springjwt.models.Enseignant;

import java.util.List;

public interface EnseignantService {
    Enseignant save(Enseignant e);        // <-- Added save method
    List<Enseignant> getAll();                   // Read all
    Enseignant getById(Long id);                 // Read one
    Enseignant updateEnseignant(Enseignant e);   // Update
    void deleteEnseignant(Long id);

}
