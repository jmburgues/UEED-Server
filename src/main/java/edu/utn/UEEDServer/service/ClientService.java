package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.repository.ClientRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final static String CLIENT_PATH = "client";
    ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public PostResponse add(Client client) {
        Client c = clientRepository.save(client);
        return PostResponse.builder().
                status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(CLIENT_PATH,c.getId()))
                .build();
    }

    public List<Client> getAll() {

        List<Client> list = clientRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Client list is empty");
        return list;
    }

    public Client getById(Integer clientId) {

        return clientRepository.findById(clientId).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND, "No client found under id: " + clientId));
    }

    public void delete(Integer clientId) {
        if(!clientRepository.existsById(clientId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No client found under id: " + clientId);
        clientRepository.deleteById(clientId);
    }

    public PostResponse update(Client client) {

        this.getById(client.getId());

        Client saved = clientRepository.save(client);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(CLIENT_PATH,saved.getId().toString()))
                .build();
    }
}
