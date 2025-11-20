package com.springjwt.security.services;

import com.springjwt.models.Salle;
import com.springjwt.repository.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SalleServiceImpl implements SalleService {

    @Autowired
    private SalleRepository salleRepository;

    @Override
    public Salle saveSalle(Salle s) {
        if (salleRepository != null) {
            return salleRepository.save(s);
        }
        return null;
    }

    @Override
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    @Override
    public Salle updateSalle(Salle s) {
        if (salleRepository != null) {
            Salle existingSalle = salleRepository.findById(s.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Salle not found with id: " + s.getId()));

            existingSalle.setNom(s.getNom());
            existingSalle.setCapacite(s.getCapacite());
            existingSalle.setBatiment(s.getBatiment());
            return salleRepository.save(existingSalle);
        }
        return null;
    }

    @Override
    public void deleteSalle(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        // Remove all related courses

        // Delete salle
        salleRepository.delete(salle);
    }


    @Override
    public Salle getById(Long id) {
        Optional<Salle> salleOptional = salleRepository.findById(id);
        return salleOptional.orElse(null);
    }
}
