package com.springjwt.security.services;

import com.springjwt.models.Client;
import com.springjwt.models.TypeClient;

import java.util.List;

public interface ClientService {
    Client save(Client client);
    Client getById(Integer id);
    List<Client> getAll();
    void deleteClient(Integer id);
    Client update(Client client);
    
    // Client specific methods
    Client deposerDocument(Integer clientId, String document);
    String consulterStatutDossier(Integer clientId);
    String communiquerAvecChatbot(Integer clientId, String message);
    
    // Find by type
    List<Client> findByType(TypeClient type);
}
