package edu.utn.UEEDServer.controller;


import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reading")
public class ReadingController {

    ReadingService readingService;

    @Autowired
    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }


    @PostMapping
    public PostResponse add(@RequestBody Reading reading)
    {
         return readingService.add(reading);
    }

    @GetMapping("/{meterSerialNumber}")

    public List<Reading> getByMeterId(@PathVariable UUID meterSerialNumber)
    {
        return readingService.getByMeterId(meterSerialNumber);
    }

    @GetMapping("/{readingId}")
    public Reading getById(@PathVariable Integer readingId)
    {
    return readingService.getById(readingId);
    }

    @PutMapping("/{meterSerialNumber}/")
    public void addToMeter(@RequestBody Reading reading,@PathVariable UUID meterSerialNumber)
    {
        readingService.addToMeter(reading,meterSerialNumber);
    }

    @DeleteMapping("/{readingId}")
    public void delete(@PathVariable Integer readingId)
    {
        readingService.delete(readingId);
    }

    @GetMapping("/NotBilled/{meterSerialNumber}")
    public List<Reading> getNotBilledReadings(@PathVariable UUID meterSerialNumber) {

       return  readingService.getNotBilledReadings(meterSerialNumber);
    }

    @GetMapping
    public List<Reading>getAll()
    {
        return readingService.getAll();
    }
}

