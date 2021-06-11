package edu.utn.UEEDServer.service;

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

        List<Rate> list = rateRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Rate list is empty");
        return list;
    }

    public Rate getById(Integer rateId) {

        return rateRepository.findById(rateId).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No rates found under id: " + rateId));
    }

    public Rate add(Rate rate) {

        if(!rateRepository.existsById(rate.getId()))
        return  rateRepository.save(rate);
        else throw new ResponseStatusException(HttpStatus.CONFLICT);

    }

    public Rate update(Rate rate) {
        this.getById(rate.getId());

        return rateRepository.save(rate);
    }

    public void delete(Integer rateId) {
        if(!rateRepository.existsById(rateId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No rate found under id: " + rateId);
        rateRepository.deleteById(rateId);
    }
}
