package com.springjwt.security.services;

import com.springjwt.models.Fournisseur;
import com.springjwt.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurServiceImpl implements FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Fournisseur update(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public void deleteFournisseur(Integer fournisseurId) {
        fournisseurRepository.deleteById(fournisseurId);
    }

    @Override
    public Fournisseur getById(Integer fournisseurId) {
        return fournisseurRepository.findById(fournisseurId).orElse(null);
    }

    @Override
    public List<Fournisseur> getAll() {
        return fournisseurRepository.findAll();
    }

    @Override
    public Optional<Fournisseur> findByNomEntreprise(String nomEntreprise) {
        return fournisseurRepository.findByNomEntreprise(nomEntreprise);
    }
}
