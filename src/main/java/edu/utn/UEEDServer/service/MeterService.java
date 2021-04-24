package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.MeterRepository;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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
        List<Meter> meterList = this.meterRepo.findAll();
        if(!meterList.isEmpty())
            return meterList;
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public Meter getById(UUID serialNumber){
        return this.meterRepo.findById(serialNumber)
                .orElseThrow( () -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void add(Meter newMeter){
        UUID serialNum = newMeter.getSerialNumber();
        if(serialNum != null && this.meterRepo.existsById(serialNum)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Meter nº " + serialNum + " already exists.");
        }
        else{
            this.meterRepo.save(newMeter);
        }
    }

    public void update(Meter existentMeter){
        if(this.meterRepo.existsById(existentMeter.getSerialNumber()))
            this.meterRepo.save(existentMeter);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public void delete(UUID serialNumber){
            if(this.meterRepo.existsById(serialNumber))
                this.meterRepo.deleteById(serialNumber);
            else
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}
