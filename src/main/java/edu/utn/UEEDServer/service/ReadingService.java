package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReadingService {

    private static final String READING_PATH = "reading";

    private MeterService meterService;
    private ReadingRepository readingRepo;
    private AddressService addressService;

    @Autowired
    public ReadingService(MeterService meterService, ReadingRepository readingRepo, AddressService addressService) {
        this.meterService = meterService;
        this.readingRepo = readingRepo;
        this.addressService = addressService;
    }

    public Reading add(Reading reading) {
        return readingRepo.save(reading);
    }

    public List<Reading> getNotBilledReadings(String serialNumber) {
        this.meterService.getById(serialNumber);
        return readingRepo.getNotBilledReadings(serialNumber);
    }

    public List<Reading>getAddressReadingsByDate(Integer addressId, LocalDateTime from, LocalDateTime to) {
        Meter meter = this.meterService.getByAddressId(addressId);
        return this.readingRepo.getReadingsByMeterAndDate(meter.getSerialNumber(),from,to);
    }

    public List<Reading>getClientReadingsByDate(Integer clientId, LocalDateTime from, LocalDateTime to) {
        return this.readingRepo.getClientReadingsByDate(clientId,from,to);
    }

    public Map<String, Float> getClientConsumption(Integer clientId, LocalDateTime from, LocalDateTime to) {
        return this.readingRepo.getClientConsumption(clientId,from,to);
    }

    public Map<String, Float> getConsumption(String serialNumber, LocalDateTime from, LocalDateTime to) {
        this.meterService.getById(serialNumber);
        return this.readingRepo.getConsumption(serialNumber,from,to);
    }

    public List<Client> getTopConsumers(LocalDateTime from, LocalDateTime to) {
        return this.readingRepo.getTopConsumers(from,to);
    }
}