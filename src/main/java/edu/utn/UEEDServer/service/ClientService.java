package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                orElseThrow(()->new IDnotFoundException("Client",clientId.toString()));
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