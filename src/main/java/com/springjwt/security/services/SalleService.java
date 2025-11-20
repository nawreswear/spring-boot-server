package com.springjwt.security.services;


import com.springjwt.models.Salle;
import java.util.List;

public interface SalleService {

    Salle saveSalle(Salle s);

    List<Salle> getAllSalles();

    Salle updateSalle(Salle s);

    void deleteSalle(Long id);

    Salle getById(Long id);
}
