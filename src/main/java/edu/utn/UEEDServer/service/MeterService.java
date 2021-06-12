package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
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

    public List<Meter> getAll(Integer page, Integer size){
        return this.meterRepo.findAllPagable(page,size);
    }

    public Meter getById(String serialNumber){
        return this.meterRepo.findById(serialNumber)
                    .orElseThrow(()-> new IDnotFoundException("Meter",serialNumber));
    }

    public Meter add(Meter newMeter){
        String serialNum = newMeter.getSerialNumber();

        if(this.meterRepo.existsById(serialNum))
            throw new IllegalArgumentException("Meter id " + serialNum + " already exists.");

        return meterRepo.save(newMeter);
    }

    public Meter update(Meter meter){

        return meterRepo.save(meter);
    }

    public void delete(String serialNumber) {
        getById(serialNumber);
        this.meterRepo.deleteById(serialNumber);
    }

    public Meter getByAddressId(Integer addressId) {
        return this.meterRepo.findByAddressId(addressId);
    }
}
