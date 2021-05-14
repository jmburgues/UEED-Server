package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.MeterRepository;
import edu.utn.UEEDServer.repository.ReadingRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MeterService {

    private static final String METER_PATH="meter";
    private MeterRepository meterRepo;
    private ReadingRepository readingRepo;

    @Autowired
    public MeterService(MeterRepository meterRepo, ReadingRepository readingRepo) {
        this.meterRepo = meterRepo;
        this.readingRepo = readingRepo;
    }

    public List<Meter> getAll(){
        List<Meter> list = this.meterRepo.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Meter list is empty");
        return list;
    }

    public Meter getById(UUID serialNumber){
        return this.meterRepo.findById(serialNumber)
                .orElseThrow( () -> new HttpClientErrorException(HttpStatus.NOT_FOUND,"No meter found under serial number: " + serialNumber));
    }

    public PostResponse add(Meter newMeter){
        UUID serialNum = newMeter.getSerialNumber();

        if(serialNum == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Meter serial number cannot be null");

        if(this.meterRepo.existsById(serialNum))
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,"Meter serial number: " + serialNum + " already registered. No action performed.");

        Meter m = meterRepo.save(newMeter);

        return PostResponse.builder().
                status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(METER_PATH,m.getSerialNumber().toString()))
                .build();
    }

    public void delete(UUID serialNumber){
            if(this.meterRepo.existsById(serialNumber))
                this.meterRepo.deleteById(serialNumber);
            else
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public PostResponse update(Meter meter){
        this.getById(meter.getSerialNumber());

        Meter m = meterRepo.save(meter);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(METER_PATH,m.getSerialNumber().toString()))
                .build();
    }
}
