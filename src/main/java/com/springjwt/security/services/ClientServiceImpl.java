package com.springjwt.security.services;

import com.springjwt.models.Client;
import com.springjwt.models.TypeClient;
import com.springjwt.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // --- CRUD Operations ---
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getById(Integer id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }
   @Override
    public Client update(Client client) {

        Client existing = clientRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setMotdepasse(client.getMotdepasse());
        existing.setTelephone(client.getTelephone());
        existing.setVille(client.getVille());
        existing.setPhoto(client.getPhoto());
        existing.setType(client.getType());

        // champs spécifiques client
        existing.setAdresse(client.getAdresse());
        existing.setMatriculeFiscale(client.getMatriculeFiscale());
        existing.setTypeClient(client.getTypeClient());

        return clientRepository.save(existing);
    }

    // --- Client Specific Methods ---
    @Override
    public Client deposerDocument(Integer clientId, String document) {
        Client client = getById(clientId);
        if (client != null) {
            // Logic to handle document deposition
            // This would typically involve saving the document and updating client status
        }
        return client;
    }

    @Override
    public String consulterStatutDossier(Integer clientId) {
        Client client = getById(clientId);
        if (client != null) {
            // Logic to check dossier status
            return "Dossier status for client " + clientId + ": En cours de traitement";
        }
        return "Client not found";
    }

    @Override
    public String communiquerAvecChatbot(Integer clientId, String message) {
        Client client = getById(clientId);
        if (client != null) {
            // Logic to handle chatbot communication
            return "Chatbot response for client " + clientId + ": Message received - " + message;
        }
        return "Client not found";
    }

    @Override
    public List<Client> findByType(TypeClient type) {
        return clientRepository.findByType(type);
    }
}
