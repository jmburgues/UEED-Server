package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.service.MeterService;
import edu.utn.UEEDServer.service.ReadingService;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeterController {

    MeterService meterService;
    ReadingService readingService;
    ConversionService conversionService;

    @Autowired
    public MeterController(MeterService meterService, ConversionService conversionService, ReadingService readingService) {
        this.meterService = meterService;
        this.conversionService = conversionService;
        this.readingService = readingService;
    }

    @PostMapping()
    public PostResponse addReading(@RequestBody ReadingDTO incoming){
        System.out.println("INCOMING !!! " + incoming.toString());
        Meter existent = meterService.getById(incoming.getMeterSerialNumber());
        System.out.printf("MATCH !!! meter: " + existent.toString());
        if(existent.getPassword().equals(incoming.getPassword())){
            System.out.println("ADDING reading" + conversionService.convert(incoming,Reading.class).toString());
            Reading added = readingService.add(conversionService.convert(incoming, Reading.class));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials.");
        }

        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public List<Meter> getAll(){
        return meterService.getAll();
    }
}
