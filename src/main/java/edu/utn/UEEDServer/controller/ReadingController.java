package edu.utn.UEEDServer.controller;


import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reading")
public class ReadingController {

    ReadingService readingService;

    @Autowired
    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @GetMapping
    public List<Reading>getAll()
    {
        return readingService.getAll();
    }

    @GetMapping("/{readingId}")
    public Reading getById(@PathVariable Integer readingId)
    {
        return readingService.getById(readingId);
    }

    @GetMapping("/meter/{meterSerialNumber}")
    public List<Reading> getByMeterId(@PathVariable UUID meterSerialNumber,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDateTime from,
                                      @RequestParam(required = false) @DateTimeFormat(pattern ="yyyy-MM-dd")LocalDateTime to){
        if(from != null && to != null){
            return readingService.getByDate(meterSerialNumber,from,to);
        }
        else{
            return readingService.getByMeterId(meterSerialNumber);
        }
    }

    @GetMapping("/meter/{meterSerialNumber}/consumption")
    public Map<String,Float> getConsumption(@PathVariable UUID meterSerialNumber,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime to){
        return this.readingService.getConsumption(meterSerialNumber,from,to);
    }

    @GetMapping("/NotBilled/{meterSerialNumber}")
    public List<Reading> getNotBilledReadings(@PathVariable UUID meterSerialNumber) {

        return  readingService.getNotBilledReadings(meterSerialNumber);
    }

    @PostMapping
    public PostResponse add(@RequestBody Reading reading)
    {
        return readingService.add(reading);
    }

    @PutMapping("/meter/{meterSerialNumber}/")
    public void addToMeter(@RequestBody Reading reading,@PathVariable UUID meterSerialNumber)
    {
        readingService.addToMeter(reading,meterSerialNumber);
    }

    @DeleteMapping("/{readingId}")
    public void delete(@PathVariable Integer readingId)
    {
        readingService.delete(readingId);
    }
}

