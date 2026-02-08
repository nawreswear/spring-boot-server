package com.springjwt.security.services;

import com.springjwt.models.*;

import java.util.List;

public interface AdminService {
    Administrateur save(Administrateur a);
    void deleteAdmin(Integer userId);
    List<Administrateur> getAll();
    Administrateur getById(Integer adminId);
    Administrateur update(Administrateur updatedAdmin);
    
    // User management methods
    List<User> getAllUsers();
    User getUserById(Integer id);
    void deleteUser(Integer id);
    
    // System configuration methods
    void configureSystem();
    
    // Comment management methods
    void manageComments();
    
    // Responsibility display methods
    String displayResponsibilities();
}