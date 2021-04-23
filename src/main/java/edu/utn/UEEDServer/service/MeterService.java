package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Service
public class MeterService {

    @Autowired
    MeterRepository meterRepo;

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
        this.meterRepo.fid
                (newMeter.getSerialNumber());


    }
}
