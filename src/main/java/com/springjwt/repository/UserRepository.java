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
public interface UserRepository extends JpaRepository<User, Long>{

    Boolean existsByEmail(String email);

    User findByEmail(String email);
    User findByNom(String nom);
    boolean existsByType(String type);

    User findByEmailIgnoreCase(String email);
    default boolean existsAdmin() {
        return existsByType("admin");
    };
    @Query("SELECT u FROM User u WHERE u.nom = :nom")
    User findPhotobyuser(@Param("nom") String nom);

}
