package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MeterService {

    private MeterRepository meterRepo;

    @Autowired
    public MeterService(MeterRepository meterRepo) {
        this.meterRepo = meterRepo;
    }

    public List<Meter> getAll(){
        List<Meter> list = this.meterRepo.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Meter list is empty");
        return list;
    }

    public Meter getById(String serialNumber){
        return this.meterRepo.findById(serialNumber)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No meter found under serial number: " + serialNumber));
    }

    public Meter add(Meter newMeter){
        String serialNum = newMeter.getSerialNumber();

        if(serialNum == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Meter serial number cannot be null");

        if(this.meterRepo.existsById(serialNum))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Meter serial number: " + serialNum + " already registered. No action performed.");

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Meter serial number: " + serialNumber + " does not exists.");
    }

    public Meter getByAddressId(Integer addressId) {
        return this.meterRepo.findByAddressId(addressId);
    }
}
