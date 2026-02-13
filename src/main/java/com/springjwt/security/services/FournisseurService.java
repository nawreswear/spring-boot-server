package com.springjwt.security.services;

import com.springjwt.models.Fournisseur;
import java.util.List;
import java.util.Optional;

public interface FournisseurService {
    Fournisseur save(Fournisseur fournisseur);
    Fournisseur update(Fournisseur fournisseur);
    void deleteFournisseur(Integer fournisseurId);
    Fournisseur getById(Integer fournisseurId);
    List<Fournisseur> getAll();
    Optional<Fournisseur> findByNomEntreprise(String nomEntreprise);
}
