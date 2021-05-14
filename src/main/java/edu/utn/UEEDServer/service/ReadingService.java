package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.MeterRepository;
import edu.utn.UEEDServer.repository.ReadingRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReadingService {

    private static final String READING_PATH = "reading";

    private MeterRepository meterRepo;
    private ReadingRepository readingRepo;

    @Autowired
    public ReadingService(ReadingRepository readingRepo, MeterRepository meterRepo){
        this.meterRepo = meterRepo;
        this.readingRepo = readingRepo;
    }

    public List<Reading> getAll() {
        List<Reading> list = readingRepo.findAll();
        if (list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Readings list is empty");
        return list;
    }

    public Reading getById(Integer readingId){
        return this.readingRepo.findById(readingId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No readings found under id: " + readingId));
    }

    public List<Reading> getByMeterId(String serialNumber) {

        Meter meter = this.meterRepo.getOne(serialNumber);

        List<Reading> list = meter.getReadings();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No readings found under meter id: " + serialNumber);
        return list;
    }

    public PostResponse add(Reading reading) {
        Reading r = readingRepo.save(reading);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(READING_PATH,r.getReadingId()))
                .build();
    }

    public List<Reading> getNotBilledReadings(String serialNumber) {
        this.meterRepo.getOne(serialNumber);
        return readingRepo.getNotBilledReadings(serialNumber);
    }

    public Map<String, Float> getConsumption(String serialNumber, LocalDateTime from, LocalDateTime to) {
        this.meterRepo.getOne(serialNumber);
        return this.readingRepo.getConsuption(serialNumber,from,to);
    }

    public List<Reading> getByDate(String serialNumber, LocalDateTime from, LocalDateTime to) {
        this.meterRepo.getOne(serialNumber);
        return this.readingRepo.getByDate(serialNumber,from,to);
    }

    public Map<Integer, Float> getTopConsumers(LocalDateTime from, LocalDateTime to) {
        return this.readingRepo.getTopConsumers(from,to);
    }
}