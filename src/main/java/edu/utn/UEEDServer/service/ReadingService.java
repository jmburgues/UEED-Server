package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.projections.ConsumersDTO;
import edu.utn.UEEDServer.model.projections.ClientConsumption;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReadingService {

    private final MeterService meterService;
    private final ReadingRepository readingRepo;
    private final AddressService addressService;

    @Autowired
    public ReadingService(MeterService meterService, ReadingRepository readingRepo, AddressService addressService) {
        this.meterService = meterService;
        this.readingRepo = readingRepo;
        this.addressService = addressService;
    }

    public Reading add(Reading reading) {
        return readingRepo.save(reading);
    }

    public List<Reading>getAddressReadingsByDate(Integer addressId, Date from, Date to) {
        verifyDates(from,to);
        addressService.getById(addressId);
        return this.readingRepo.getAddressReadingsByDate(addressId,from,to);
    }

    public List<Reading>getClientReadingsByDate(Integer clientId, Date from, Date to, Integer page, Integer size) {
        verifyDates(from,to);
         return this.readingRepo.getClientReadingsByDate(clientId,from,to, page, size);
    }

    public ClientConsumption getClientConsumption(Integer clientId, Date from, Date to) {

        verifyDates(from,to);
        return this.readingRepo.getClientConsumption(clientId,from,to);
    }

    public List<ConsumersDTO> getTopConsumers(Date from, Date to) {

        verifyDates(from,to);
        return this.readingRepo.getTopConsumers(from,to);
    }

    public void verifyDates(Date from,Date to){
        if(from.after(to))
            throw new IllegalArgumentException("Date From (" + from + ") can not be after date To (" + to + ")");
    }
}