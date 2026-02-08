package com.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springjwt.models.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    Boolean existsByEmail(String email);

    User findByEmail(String email);
    User findByNom(String nom);

    User findByEmailIgnoreCase(String email);
    
    @Query("SELECT u FROM User u WHERE u.nom = :nom")
    User findPhotobyuser(@Param("nom") String nom);

}
