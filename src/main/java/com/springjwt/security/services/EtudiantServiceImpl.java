package com.springjwt.security.services;

import com.springjwt.models.Etudiant;
import com.springjwt.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public Etudiant save(Etudiant e) {
        if (etudiantRepository != null) {
            return etudiantRepository.save(e);
        }
        return null;
    }

    @Override
    public void processEtudiantRequest(Etudiant etudiant) {
        // Validation logic
        if (!isValidEtudiant(etudiant)) {
            throw new IllegalArgumentException("Invalid student data");
        }
        etudiantRepository.save(etudiant);
    }

    @Override
    public List<Etudiant> getAllEtudiantRequests() {
        return etudiantRepository.findAll();
    }

    private boolean isValidEtudiant(Etudiant etudiant) {
        return etudiant != null
                && isValidString(etudiant.getNom())
                && isValidString(etudiant.getPrenom())
                && isValidString(etudiant.getEmail())
                && isValidString(etudiant.getPassword())
                && etudiant.getTel() != null
                && etudiant.getCodePostal() != null
                && isValidString(etudiant.getPays())
                && isValidString(etudiant.getVille())
                && etudiant.getCin() != null;
    }

    private boolean isValidString(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public Etudiant update(Etudiant updatedEtudiant) {
        if (etudiantRepository != null) {
            Etudiant existingEtudiant = etudiantRepository.findById(updatedEtudiant.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Etudiant not found with id: " + updatedEtudiant.getId()));

            existingEtudiant.setNom(updatedEtudiant.getNom());
            existingEtudiant.setPrenom(updatedEtudiant.getPrenom());
            existingEtudiant.setTel(updatedEtudiant.getTel());
            existingEtudiant.setEmail(updatedEtudiant.getEmail());
            existingEtudiant.setPassword(updatedEtudiant.getPassword());
            existingEtudiant.setCodePostal(updatedEtudiant.getCodePostal());
            existingEtudiant.setPays(updatedEtudiant.getPays());
            existingEtudiant.setVille(updatedEtudiant.getVille());
            existingEtudiant.setCin(updatedEtudiant.getCin());
            existingEtudiant.setLongitude(updatedEtudiant.getLongitude());
            existingEtudiant.setLatitude(updatedEtudiant.getLatitude());
            existingEtudiant.setPhoto(updatedEtudiant.getPhoto());
            existingEtudiant.setType(updatedEtudiant.getType());

            return etudiantRepository.save(existingEtudiant);
        }
        return null;
    }

    @Override
    public void deleteEtudiant(Long id) {
        if (etudiantRepository != null) {
            etudiantRepository.deleteById(id);
        }
    }

    @Override
    public List<Etudiant> getAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant getById(Long id) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findById(id);
        return etudiantOptional.orElse(null);
    }
}
