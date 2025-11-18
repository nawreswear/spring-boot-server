package com.springjwt.security.services;

import com.springjwt.models.Admin;
import com.springjwt.models.Vendeur;
import com.springjwt.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin save(Admin a) {
        return adminRepository.save(a);
    }

    @Override
    public Admin update(Admin updatedAdmin) {
        if (adminRepository != null) {
            Admin existingVendeur = adminRepository.findById(updatedAdmin.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Vendeur not found with id: " + updatedAdmin.getId()));
            existingVendeur.setType(updatedAdmin.getType());
            existingVendeur.setNom(updatedAdmin.getNom());
            existingVendeur.setPrenom(updatedAdmin.getPrenom());
            existingVendeur.setTel(updatedAdmin.getTel());
            existingVendeur.setEmail(updatedAdmin.getEmail());
            existingVendeur.setPassword(updatedAdmin.getPassword());
            existingVendeur.setCodePostal(updatedAdmin.getCodePostal());
            existingVendeur.setPays(updatedAdmin.getPays());
            existingVendeur.setVille(updatedAdmin.getVille());
            existingVendeur.setCin(updatedAdmin.getCin());
            existingVendeur.setLongitude(updatedAdmin.getLongitude());
            existingVendeur.setLatitude(updatedAdmin.getLatitude());
            existingVendeur.setPhoto(updatedAdmin.getPhoto());
            return adminRepository.save(existingVendeur);
        }
        return null;
    }
    @Override
    public void deleteAdmin(Long userId) {
        adminRepository.deleteById(userId);
    }
    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getById(Long adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        return adminOptional.orElse(null);
    }

}
