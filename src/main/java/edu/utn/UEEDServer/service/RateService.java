package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.repository.RateRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RateService {

    private static final String RATE_PATH="rate";
    RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public PostResponse add(Rate rate) {

        Rate r = rateRepository.save(rate);

        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(RATE_PATH,r.getId())).
                build();
    }

    public List<Rate> getAll() {

        List<Rate> list = rateRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Rate list is empty");
        return list;
    }

    public Rate getById(Integer rateId) {

        return rateRepository.findById(rateId).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No rates found under id: " + rateId));
    }

    public PostResponse updateRate(Rate rate) {


        this.getById(rate.getId());

        Rate saved = rateRepository.save(rate);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(RATE_PATH,saved.getId().toString()))
                .build();
    }
}
