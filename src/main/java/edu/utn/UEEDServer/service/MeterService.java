package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterService {

    private final MeterRepository meterRepo;
    private final AddressService addressService;
    private final ModelService modelService;

    @Autowired
        public MeterService(MeterRepository meterRepo, AddressService addressService, ModelService modelService) {
        this.meterRepo = meterRepo;
        this.addressService = addressService;
        this.modelService = modelService;
    }


    public List<Meter> getAll(Integer page, Integer size){
        return this.meterRepo.findAllPagable(page,size);
    }

    public Meter getById(String serialNumber){
        return this.meterRepo.findById(serialNumber)
                    .orElseThrow(()-> new IDnotFoundException("Meter",serialNumber));
    }

    public Meter add(Meter newMeter, Integer modelId, Integer addressId){
        String serialNum = newMeter.getSerialNumber();

        if(this.meterRepo.existsById(serialNum))
            throw new IllegalArgumentException("Meter id " + serialNum + " already exists.");

        newMeter.setAddress(addressService.getById(addressId));
        newMeter.setModel(modelService.getById(modelId));

        return meterRepo.save(newMeter);
    }

    public Boolean update(Meter meter){
        Boolean updated = meterRepo.findById(meter.getSerialNumber()).isPresent();
        meterRepo.save(meter);
        return updated;
    }

    public void delete(String serialNumber) {
        getById(serialNumber);
        this.meterRepo.deleteById(serialNumber);
    }

    public Meter getByAddressId(Integer addressId) {
        return this.meterRepo.findByAddressId(addressId);
    }
}
