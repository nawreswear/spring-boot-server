package com.springjwt.repository;

import com.springjwt.models.Client;
import com.springjwt.models.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByType(TypeClient type);
}
