package com.springjwt.security.services;

import com.springjwt.models.Admin;
import com.springjwt.models.User;

import java.util.List;

public interface AdminService {
    Admin save(Admin a);
    void deleteAdmin(Long userId);
    List<Admin> getAll();
    Admin getById(Long adminId);

    Admin update(Admin updatedAdmin);
}