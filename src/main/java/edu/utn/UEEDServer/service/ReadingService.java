package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;
import edu.utn.UEEDServer.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReadingService {

    private MeterService meterService;
    private ReadingRepository readingRepo;

    @Autowired
    public ReadingService(MeterService meterService, ReadingRepository readingRepo) {
        this.meterService = meterService;
        this.readingRepo = readingRepo;
    }

    public Reading add(Reading reading) {
        return readingRepo.save(reading);
    }

    public List<Reading>getAddressReadingsByDate(Integer addressId, Date from, Date to) {
        Meter meter = this.meterService.getByAddressId(addressId);
        return this.readingRepo.getReadingsByMeterAndDate(meter.getSerialNumber(),from,to);
    }

    public List<Reading>getClientReadingsByDate(Integer clientId, Date from, Date to) {
        return this.readingRepo.getClientReadingsByDate(clientId,from,to);
    }

    public Map<String, Float> getClientConsumption(Integer clientId, Date from, Date to) {
        return this.readingRepo.getClientConsumption(clientId,from,to);
    }

    public List<ConsumersDTO> getTopConsumers(Date from, Date to) {
        return this.readingRepo.getTopConsumers(from,to);
    }
}