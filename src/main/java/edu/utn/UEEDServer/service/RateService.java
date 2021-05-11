package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.repository.RateRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

        return rateRepository.findAll();
    }

    public Rate getById(Integer id) {

        return rateRepository.findById(id).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void delete(Integer id) {

        if(rateRepository.existsById(id))
            rateRepository.deleteById(id);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public PostResponse updateRate(Rate rate) {

        if(rateRepository.existsById(rate.getId()))
            return add(rate);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}
