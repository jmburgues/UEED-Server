package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.AddressRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

        return addressRepository.findAll();
    }

    public Address getById(Integer id) {

        return addressRepository.findById(id).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));

    }
}
