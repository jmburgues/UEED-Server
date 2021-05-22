package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.repository.MeterRepository;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MeterService {

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

    public Meter getById(String serialNumber){
        return this.meterRepo.findById(serialNumber)
                .orElseThrow( () -> new HttpClientErrorException(HttpStatus.NOT_FOUND,"No meter found under serial number: " + serialNumber));
    }

    public Meter add(Meter newMeter){
        String serialNum = newMeter.getSerialNumber();

        if(serialNum == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Meter serial number cannot be null");

        if(this.meterRepo.existsById(serialNum))
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,"Meter serial number: " + serialNum + " already registered. No action performed.");

        return meterRepo.save(newMeter);
    }

    public Meter update(Meter meter){
        this.getById(meter.getSerialNumber());

        return meterRepo.save(meter);
    }

    public void delete(String serialNumber){
        if(this.meterRepo.existsById(serialNumber))
            this.meterRepo.deleteById(serialNumber);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}
