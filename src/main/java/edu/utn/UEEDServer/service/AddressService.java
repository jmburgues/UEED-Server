package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Brand;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.AddressRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressService {


    AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address add(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> getAll() {
        List<Address> list = addressRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Address list is empty");
        return list;
    }

    public Address getById(Integer addressId) {

        return addressRepository.findById(addressId).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND,"No address found under id: " + addressId));
    }

    public void delete(Integer addressId) {
        if(!addressRepository.existsById(addressId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No address found under id: " + addressId);
        addressRepository.deleteById(addressId);
    }

    public Address update(Address address) {
        this.getById(address.getAddressId());

        return addressRepository.save(address);
    }
}
