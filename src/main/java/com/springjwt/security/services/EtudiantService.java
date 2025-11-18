package com.springjwt.security.services;

import com.springjwt.models.Vendeur;

import java.util.List;


public interface VendeurService {

    Vendeur save(Vendeur v);
    List<Vendeur> getAll();
    Vendeur update(Vendeur updatedVendeur);
    void deletevendeur(Long id);
    Vendeur getById(Long id);
    void processVendeurRequest(Vendeur vendeur);
    List<Vendeur> getAllVendeurRequests();
}