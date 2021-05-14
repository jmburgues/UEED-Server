package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Brand;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.BrandRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandService {

    private static final String BRAND_PATH = "brand";
    BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public PostResponse add(Brand brand)
    {
        Brand b = brandRepository.save(brand);
        return PostResponse.builder()
                .status(HttpStatus.CREATED).url(EntityURLBuilder.buildURL(BRAND_PATH,b.getId()))
                .build();
    }

    public List<Brand> getAll() {

        List<Brand> list = brandRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Brand list is empty");
        return list;
    }

    public Brand getById(Integer brandId) {

        return brandRepository.findById(brandId).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND,"No brand found under id: " + brandId));
    }

    public void delete(Integer brandId) {
        if(!brandRepository.existsById(brandId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No brand found under id: " + brandId);
        brandRepository.deleteById(brandId);
    }

    public PostResponse update(Brand brand) {

        this.getById(brand.getId());

        Brand saved = brandRepository.save(brand);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(BRAND_PATH,saved.getId().toString()))
                .build();
    }
}
