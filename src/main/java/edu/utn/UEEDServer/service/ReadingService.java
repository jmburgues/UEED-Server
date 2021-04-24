package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.MeterRepository;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Service
public class ReadingService {

    private MeterRepository meterRepo;
    private ReadingRepository readingRepo;

    @Autowired
    public ReadingService(ReadingRepository readingRepo, MeterRepository meterRepo){
        this.meterRepo = meterRepo;
        this.readingRepo = readingRepo;
    }

    public List<Reading> getAll(){
        List<Reading> readingList = this.readingRepo.findAll();
        if(!readingList.isEmpty())
            return readingList;
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public Reading getById(Integer readingId){
        return this.readingRepo.findById(readingId)
                .orElseThrow( () -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addToMeter(Reading newReading, UUID meterSerialNumber){
        Meter existentMeter = this.meterRepo.findById(meterSerialNumber)
                .orElseThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND);
        existentMeter.getReadings().add(newReading);
        meterRepo.save(existentMeter);
    }

    public void update(Reading existentReading){
        if(this.readingRepo.existsById(existentReading.getId()))
            this.readingRepo.save(existentReading);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public void delete(Integer readingId){
        if(this.readingRepo.existsById(readingId))
            this.readingRepo.deleteById(readingId);
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
}