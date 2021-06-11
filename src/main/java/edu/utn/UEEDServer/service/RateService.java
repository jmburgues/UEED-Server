package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RateService {

    RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<Rate> getAll() {
        return rateRepository.findAll();
    }

    public Rate getById(Integer rateId) {
        return rateRepository.findById(rateId).
                orElseThrow(()->new IDnotFoundException("Rate",rateId.toString()));
    }

    public Rate add(Rate rate) {
        if(this.rateRepository.findById(rate.getId()).isPresent())
            throw new IllegalArgumentException("Rate " + rate.getId() + " already exists.");

        return  rateRepository.save(rate);
    }

    public Rate update(Rate rate) {
        this.getById(rate.getId());

        return rateRepository.save(rate);
    }

    public void delete(Integer rateId) {
        getById(rateId);
        rateRepository.deleteById(rateId);
    }
}
