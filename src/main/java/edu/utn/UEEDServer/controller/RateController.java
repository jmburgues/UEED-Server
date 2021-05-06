package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rate")
public class RateController {

    RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }


    @PostMapping
    public PostResponse add (@RequestBody Rate rate)
    {
        return rateService.add(rate);
    }

    @GetMapping
    public List<Rate>getAll()
    {
        return rateService.getAll();
    }

    @GetMapping("/{id}")
    public Rate getById(@PathVariable Integer id)
    {
            return rateService.getById(id);
    }

}
