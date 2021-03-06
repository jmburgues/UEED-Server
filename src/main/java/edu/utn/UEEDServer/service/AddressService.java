package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
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
        RateService rateService;

    @Autowired
    public AddressService(AddressRepository addressRepository, ClientService clientService, RateService rateService) {
        this.addressRepository = addressRepository;
        this.clientService = clientService;
        this.rateService = rateService;
    }

    public Address add(Integer clientId, Integer rateId, Address address) {
        if(addressRepository.getByStreeetAndNumber(address.getStreet(),address.getNumber()).isPresent())
            throw new IllegalArgumentException("Address (street and number combination) already exists.");

        Rate rate = rateService.getById(rateId);
        Client client = clientService.getById(clientId);
        address.setRate(rate);
        address.setClient(client);
        return addressRepository.save(address);
    }

    public List<Address> getAll(Integer page, Integer size) {
        return addressRepository.findAllPageable(page,size);
    }

    public Address getById(Integer addressId) {

        return addressRepository.findById(addressId).
                orElseThrow(()->new IDnotFoundException("Address",addressId.toString()));
    }

    public void delete(Integer addressId) {
        if(!addressRepository.existsById(addressId))
            throw new IDnotFoundException("Address",addressId.toString());
        addressRepository.deleteById(addressId);
    }

    public Boolean update(Address newAddress) {
        Boolean updated = addressRepository.findById(newAddress.getAddressId()).isPresent();
        addressRepository.save(newAddress);
        return updated;
    }
}
