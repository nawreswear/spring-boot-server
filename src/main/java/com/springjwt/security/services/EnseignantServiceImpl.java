package com.springjwt.security.services;


import com.springjwt.models.Enseignant;
import com.springjwt.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class EnseignantServiceImpl implements EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepo;

    @Override
    public Enseignant save(Enseignant e) {
        if (enseignantRepo != null) {
            return enseignantRepo.save(e);
        }
        return null;
    }




    @Override
    public List<Enseignant> getAll() {
        return enseignantRepo.findAll();
    }

    @Override
    public Enseignant getById(Long id) {
        return enseignantRepo.findById(id).orElse(null);
    }

    @Override
    public Enseignant updateEnseignant(Enseignant updatedEnseignant) {
        if (enseignantRepo != null) {
            Enseignant existing = enseignantRepo.findById(updatedEnseignant.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Enseignant not found with id: " + updatedEnseignant.getId()));

            existing.setNom(updatedEnseignant.getNom());
            existing.setPrenom(updatedEnseignant.getPrenom());
            existing.setEmail(updatedEnseignant.getEmail());
            existing.setPassword(updatedEnseignant.getPassword());
            existing.setTel(updatedEnseignant.getTel());
            existing.setCin(updatedEnseignant.getCin());
            existing.setType(updatedEnseignant.getType());
            existing.setPhoto(updatedEnseignant.getPhoto());

            return enseignantRepo.save(existing);
        }
        return null;
    }

    @Override
    public void deleteEnseignant(Long id) {
        if (enseignantRepo != null) {
            enseignantRepo.deleteById(id);
        }
    }


}
