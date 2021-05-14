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

    private static final String ADDRESS_PATH = "address";
    AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public PostResponse add(Address address) {

        Address a = addressRepository.save(address);
        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(ADDRESS_PATH,a.getAddressId())).
                build();
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

    public PostResponse updateAddress(Address address) {
        this.getById(address.getAddressId());

        Address saved = addressRepository.save(address);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(ADDRESS_PATH,saved.getAddressId().toString()))
                .build();
    }
}
