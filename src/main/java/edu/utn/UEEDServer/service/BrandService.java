package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Brand;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.BrandRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

        return brandRepository.findAll();
    }


    public Brand getById(Integer id) {

        return brandRepository.findById(id).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}
