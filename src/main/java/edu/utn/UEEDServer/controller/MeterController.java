package edu.utn.UEEDServer.controller;



import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meter")
public class MeterController {


    private MeterService meterService;

    @Autowired
    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping
    public List<Meter> getAll(){
        return this.meterService.getAll();
    }

    @GetMapping("/{serialNumber}")
    public Meter getById(@PathVariable UUID serialNumber){
        return this.meterService.getById(serialNumber);
    }

    @PostMapping
    public PostResponse add(@RequestBody Meter newMeter){
        return meterService.add(newMeter);
    }

    @PostMapping("/update/{serialNumber}")
    public PostResponse updateMeter(@RequestBody Meter meter,@PathVariable UUID serialNumber)
    {
        return meterService.updateMeter(meter,serialNumber);
    }







}
