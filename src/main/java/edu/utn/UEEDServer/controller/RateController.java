package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rate")
public class RateController {

    RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }
}
