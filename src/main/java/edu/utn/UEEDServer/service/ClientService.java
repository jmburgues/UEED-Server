package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getById(Integer clientId) {

        return clientRepository.findById(clientId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No client found under id: " + clientId));
    }

    public Client update(Client client) {

        this.getById(client.getId());

        return clientRepository.save(client);
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client getByUsername(String username) {
        return clientRepository.getByUsername(username);
    }
}