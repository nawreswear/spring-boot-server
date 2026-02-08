package com.springjwt.security.services;

import com.springjwt.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    User save(User user);
    boolean isEmailUnique(String email);
    String getUserType(String email);
    List<User> getAllUsers();
    void deleteUser(Integer userId);
    void update(User user);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    User updateUserType(Integer userId, String newType);
    Integer getUserIdByName(String nom);
    Integer findUserIdByNom(String nomuser);
}
