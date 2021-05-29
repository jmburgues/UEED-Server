package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AddressService {


    AddressRepository addressRepository;
    ClientService clientService;

    @Autowired
    public AddressService(AddressRepository addressRepository, ClientService clientService) {
        this.addressRepository = addressRepository;
        this.clientService = clientService;
    }

    public Address add(Integer clientId, Address address) {
        Client client = clientService.getById(clientId);
        Address newAddress = addressRepository.save(address);
        client.getAddresses().add(newAddress);
        clientService.update(client);
        return newAddress;
    }

    public List<Address> getAll() {
        List<Address> list = addressRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Address list is empty");
        return list;
    }

    public Address getById(Integer addressId) {

        return addressRepository.findById(addressId).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"No address found under id: " + addressId));
    }

    public void delete(Integer addressId) {
        if(!addressRepository.existsById(addressId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No address found under id: " + addressId);
        addressRepository.deleteById(addressId);
    }

    public Client update(Integer clientId, Address newAddress) {
        Client client = clientService.getById(clientId);
        Address old = getById(newAddress.getAddressId());
        List<Address> addresses = client.getAddresses();
        addresses.remove(old);
        addresses.add(newAddress);
        return clientService.update(client);
    }
}
