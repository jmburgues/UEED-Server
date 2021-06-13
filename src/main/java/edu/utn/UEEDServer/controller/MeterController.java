package edu.utn.UEEDServer.controller;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.service.MeterService;
import edu.utn.UEEDServer.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Meter> addReading(@RequestBody ReadingDTO incoming){
        System.out.println("INCOMING READING: " + incoming.toString());
        Meter existent = meterService.getById(incoming.getMeterSerialNumber());
        if(existent.getPassword().equals(incoming.getPassword())){
            Reading added = readingService.add(conversionService.convert(incoming, Reading.class));
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials.");
        }

        return ResponseEntity.ok(Meter.builder().build());
    }
}
